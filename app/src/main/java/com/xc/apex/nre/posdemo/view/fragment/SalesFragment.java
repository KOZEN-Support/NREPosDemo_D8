package com.xc.apex.nre.posdemo.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.xc.apex.nre.posdemo.R;
import com.xc.apex.nre.posdemo.databinding.FragmentSalesBinding;
import com.xc.apex.nre.posdemo.model.ClearOrderEvent;
import com.xc.apex.nre.posdemo.model.GoodsBean;
import com.xc.apex.nre.posdemo.model.OrderBean;
import com.xc.apex.nre.posdemo.model.OrderItemBean;
import com.xc.apex.nre.posdemo.model.PayCompleted;
import com.xc.apex.nre.posdemo.usb.SendCommand;
import com.xc.apex.nre.posdemo.usb.UsbCommunicateManager;
import com.xc.apex.nre.posdemo.utils.GoodsDataUtil;
import com.xc.apex.nre.posdemo.utils.ToastUtil;
import com.xc.apex.nre.posdemo.view.MainTabActivity;
import com.xc.apex.nre.posdemo.view.adapter.GridSpacingItemDecoration;
import com.xc.apex.nre.posdemo.view.adapter.SalesGoodsAdapter;
import com.xc.apex.nre.posdemo.view.adapter.SalesTicketAdapter;
import com.xc.apex.nre.posdemo.view.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SalesFragment extends BaseFragment implements SalesGoodsAdapter.OnItemClickListener,
        SalesTicketAdapter.OnItemClickListener, View.OnClickListener {

    private FragmentSalesBinding binding;

    private List<GoodsBean> goodsDataList;
    private SalesGoodsAdapter salesGoodsAdapter;

    private List<OrderItemBean> orderItemList = new ArrayList<>();
    private SalesTicketAdapter salesTicketAdapter;

    private String ticketTotalPrice = "0.0";
    private boolean hasOrderNow = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sales, container, false);
        binding.setPresenter(this);

        EventBus.getDefault().register(this);

        // 商品列表
        goodsDataList = GoodsDataUtil.getInstance().getGoodsListData();
        salesGoodsAdapter = new SalesGoodsAdapter(getContext(), goodsDataList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        binding.rvGoods.setLayoutManager(gridLayoutManager);
        binding.rvGoods.setAdapter(salesGoodsAdapter);
        int spacing = 12;
        boolean includeEdge = true;
        binding.rvGoods.addItemDecoration(new GridSpacingItemDecoration(gridLayoutManager.getSpanCount(), spacing, includeEdge));
        salesGoodsAdapter.setOnItemClickListener(this::onItemClick);

        // 订单列表
        salesTicketAdapter = new SalesTicketAdapter(orderItemList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvTicket.setLayoutManager(layoutManager);
        binding.rvTicket.setAdapter(salesTicketAdapter);
        salesTicketAdapter.setOnItemClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        addOrderItem(position);
    }

    @Override
    public void onReduceClick(View view, int position, String unp) {
        subtractOrderItemSize(position, unp);
    }

    @Override
    public void onAddClick(View view, int position, String unp) {
        addOrderItemSize(position, unp);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_ticket_clear) {
            if (hasOrderNow) {
                showClearDialog();
            }
        } else if (viewId == R.id.tv_ticket_charge) {
            if (orderItemList.size() > 0) {
                sendCommand2Charge();

                if (getActivity() != null) {
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("order_data_key", (ArrayList<? extends Parcelable>) orderItemList);
                    args.putString("order_total_val_key", ticketTotalPrice);
                    ((MainTabActivity) getActivity()).showChargeFragment(args);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClearOrderEvent(ClearOrderEvent event) {
        if (event.isClear()) {
            clearAllOrder(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayCompleted(PayCompleted event) {
        if (event.isSuccess()) {
            clearAllOrder(false);
            ToastUtil.showToast(getActivity(), getString(R.string.txt_pay_success));
        }
    }

    private void addOrderItem(int position) {
        refreshOrderStateAndSendCommand(true);

        String goodsName = goodsDataList.get(position).getName();
        String unitPrice = goodsDataList.get(position).getUnitPrice();
        for (int i = 0; i < orderItemList.size(); i++) {
            if (orderItemList.get(i).getName().equalsIgnoreCase(goodsName)) {
                addOrderItemSize(i, orderItemList.get(i).getUnitPrice());
                return;
            }
        }

        // 新增一条商品数据
        orderItemList.add(new OrderItemBean(goodsName, 1, unitPrice, unitPrice));
        salesTicketAdapter.setData(orderItemList);
        // 更新订单总价
        addTickTotalVal(unitPrice);
    }

    private void addOrderItemSize(int position, String unitPrice) {
        OrderItemBean item = orderItemList.get(position);
        int size = item.getNum();
        if (size >= 10) {
            return;
        }
        // 商品数量
        item.setNum(size + 1);
        // 商品总值
        item.setTotal(getAddPrice(unitPrice, item.getTotal()));
        // 刷新列表
        salesTicketAdapter.setData(orderItemList);
        // 更新订单总价
        addTickTotalVal(unitPrice);
    }

    private void addTickTotalVal(String unitP) {
        ticketTotalPrice = getAddPrice(unitP, ticketTotalPrice);
        binding.tvTicketTotal.setText("$" + ticketTotalPrice);
    }

    private String getAddPrice(String unitP, String targetP) {
        BigDecimal unitPb = new BigDecimal(unitP);
        BigDecimal targetPb = new BigDecimal(targetP);
        return targetPb.add(unitPb).toString();
    }

    private void subtractOrderItemSize(int position, String unitPrice) {
        if (orderItemList.size() <= position) {
            return;
        }

        int size = orderItemList.get(position).getNum();
        if (size == 1) {
            // 刷新列表
            orderItemList.remove(position);
            salesTicketAdapter.setData(orderItemList);
            // 刷新订单总价
            subtractTickTotalVal(unitPrice);
            // 刷新按键状态
            if (orderItemList.size() == 0) {
                refreshOrderStateAndSendCommand(false);
            }
        } else {
            // 商品数量
            orderItemList.get(position).setNum(size - 1);
            // 商品总值
            orderItemList.get(position).setTotal(getSubtractPrice(unitPrice, orderItemList.get(position).getTotal()));
            salesTicketAdapter.setData(orderItemList);
            // 刷新订单总价
            subtractTickTotalVal(unitPrice);
        }
    }

    private void subtractTickTotalVal(String unitP) {
        ticketTotalPrice = getSubtractPrice(unitP, ticketTotalPrice);
        binding.tvTicketTotal.setText("$" + ticketTotalPrice);
    }

    private String getSubtractPrice(String unitP, String targetP) {
        BigDecimal unitPb = new BigDecimal(unitP);
        BigDecimal targetPb = new BigDecimal(targetP);
        return targetPb.subtract(unitPb).toString();
    }

    private void refreshOrderStateAndSendCommand(boolean hasOrder) {
        if (hasOrderNow != hasOrder) {
            binding.tvTicketTotal.setTextColor(hasOrder ? getResources().getColor(R.color.black) : getResources().getColor(R.color.dark_98));
            binding.ivTicketClear.setImageResource(hasOrder ? R.drawable.ic_clear_clickable : R.drawable.ic_clear_cannotclick);
            binding.tvTicketCharge.setTextColor(hasOrder ? getResources().getColor(R.color.yellow)
                    : getResources().getColor(R.color.white));
            binding.tvTicketCharge.setBackground(hasOrder ? getResources().getDrawable(R.drawable.bg_sales_charge_btn_clickable)
                    : getResources().getDrawable(R.drawable.bg_sales_charge_btn_cannotclick));
            binding.ivEmpty.setVisibility(hasOrder ? View.GONE : View.VISIBLE);
            binding.rvTicket.setVisibility(hasOrder ? View.VISIBLE : View.GONE);
            hasOrderNow = hasOrder;
            // 根据订单状态发送指令
            sendCommandBaseOrderState(hasOrder);
        }
    }

    private void refreshOrderState(boolean hasOrder) {
        if (hasOrderNow != hasOrder) {
            binding.tvTicketTotal.setTextColor(hasOrder ? getResources().getColor(R.color.black) : getResources().getColor(R.color.dark_98));
            binding.ivTicketClear.setImageResource(hasOrder ? R.drawable.ic_clear_clickable : R.drawable.ic_clear_cannotclick);
            binding.tvTicketCharge.setTextColor(hasOrder ? getResources().getColor(R.color.yellow)
                    : getResources().getColor(R.color.white));
            binding.tvTicketCharge.setBackground(hasOrder ? getResources().getDrawable(R.drawable.bg_sales_charge_btn_clickable)
                    : getResources().getDrawable(R.drawable.bg_sales_charge_btn_cannotclick));
            binding.ivEmpty.setVisibility(hasOrder ? View.GONE : View.VISIBLE);
            binding.rvTicket.setVisibility(hasOrder ? View.VISIBLE : View.GONE);
            hasOrderNow = hasOrder;
        }
    }

    private void sendCommandBaseOrderState(boolean hasOrder) {
        if (hasOrder) {
            sendCommandStartOrder();
        } else {
            sendCommandStopOrder();
        }
    }

    private void sendCommandStartOrder() {
        UsbCommunicateManager.getInstance().sendCommandToHost(SendCommand.START_ORDERING);
    }

    private void sendCommandStopOrder() {
        UsbCommunicateManager.getInstance().sendCommandToHost(SendCommand.CANCEL_CHARGE);
    }

    private void sendCommand2Charge() {
        Gson gson = new Gson();
        OrderBean orderBean = new OrderBean(SendCommand.START_CHARGE, ticketTotalPrice, orderItemList);
        String orderBeanStr = gson.toJson(orderBean);
        UsbCommunicateManager.getInstance().sendCommandToHost(orderBeanStr);
    }

    private void showClearDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.dialog_clear_ticket, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        builder.setView(customView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        TextView cancel = customView.findViewById(R.id.cancel);
        TextView sure = customView.findViewById(R.id.sure);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllOrder(true);
                alertDialog.dismiss();
            }
        });
    }

    private void clearAllOrder(boolean isNotifyCustomer) {
        orderItemList.clear();
        salesTicketAdapter.setData(orderItemList);
        if (isNotifyCustomer) {
            refreshOrderStateAndSendCommand(false);
        } else {
            refreshOrderState(false);
        }
        binding.tvTicketTotal.setText(getString(R.string.txt_def_total_value));
        ticketTotalPrice = "0.0";
    }
}
