package com.xc.apex.nre.posdemo.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.xc.apex.nre.posdemo.R;
import com.xc.apex.nre.posdemo.databinding.FragmentChargeBinding;
import com.xc.apex.nre.posdemo.model.ClearOrderEvent;
import com.xc.apex.nre.posdemo.model.OrderBean;
import com.xc.apex.nre.posdemo.model.OrderItemBean;
import com.xc.apex.nre.posdemo.model.PayCompleted;
import com.xc.apex.nre.posdemo.usb.SendCommand;
import com.xc.apex.nre.posdemo.usb.UsbCommunicateManager;
import com.xc.apex.nre.posdemo.utils.ToastUtil;
import com.xc.apex.nre.posdemo.view.MainTabActivity;
import com.xc.apex.nre.posdemo.view.adapter.ChargeOrderAdapter;
import com.xc.apex.nre.posdemo.view.base.BaseFragment;
import com.xc.apex.nre.posdemo.view.widget.CustomPayWay;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ChargeFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "ChargeFragment";

    private FragmentChargeBinding binding;
    private List<OrderItemBean> orderItemList = new ArrayList<>();
    private ChargeOrderAdapter adapter;
    private String ticketTotalPrice = "0.0";

    private static final int PAY_CASH = 0;
    private static final int PAY_CARD = 1;
    private static final int PAY_QRCODE = 2;
    private int curPayWay = PAY_CASH;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_charge, container, false);
        binding.setPresenter(this);

        EventBus.getDefault().register(this);

        if (getArguments() != null) {
            orderItemList = getArguments().getParcelableArrayList("order_data_key");
            ticketTotalPrice = getArguments().getString("order_total_val_key");
            Log.e(TAG, "[CHARGE] size = " + orderItemList.size() + " , ticketTotalPrice = " + ticketTotalPrice);

            if (orderItemList.size() > 0) {
                adapter = new ChargeOrderAdapter(getActivity(), orderItemList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                binding.rvOrder.setLayoutManager(layoutManager);
                binding.rvOrder.setAdapter(adapter);
            }

            binding.tvTotalVal.setText("$" + ticketTotalPrice);
            binding.tvPayTotalVal.setText("$" + ticketTotalPrice);
        }

        refreshPayTabState(binding.cashPay, PAY_CASH);
        refreshPayPage(true);

        Glide.with(this)
                .asGif()
                .load(R.drawable.gif_card)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(binding.ivOthersPay);

        return binding.getRoot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayCompleted(PayCompleted event) {
        refreshPayPage(false);
        payCompleted(event.isSuccess());
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_back) {
            ((MainTabActivity) getActivity()).hideChargeFragment();
        } else if (viewId == R.id.cash_pay) {
            changePayWay(binding.cashPay, PAY_CASH);
        } else if (viewId == R.id.card_pay) {
            changePayWay(binding.cardPay, PAY_CARD);
        } else if (viewId == R.id.qr_pay) {
            changePayWay(binding.qrPay, PAY_QRCODE);
        } else if (viewId == R.id.btn_charge) {
            if (!binding.etCashReceived.getText().toString().trim().isEmpty()) {
                sendCommandCashPaySuccess();

                EventBus.getDefault().post(new ClearOrderEvent(true));
                ((MainTabActivity) getActivity()).hideChargeFragment();
            } else {
                ToastUtil.showToast(getActivity(), getString(R.string.txt_cash_received_empty));
            }
        } else if (viewId == R.id.btn_new_sale) {
            EventBus.getDefault().post(new ClearOrderEvent(true));
            ((MainTabActivity) getActivity()).hideChargeFragment();
        } else if (viewId == R.id.btn_retry) {
            refreshPayPage(true);
            refreshPayTabState(binding.cashPay, PAY_CASH);
            curPayWay = PAY_CASH;
            sendCommand2Charge();
        }
    }

    private void changePayWay(CustomPayWay view, int payWay) {
        if (curPayWay != payWay) {
            refreshPayTabState(view, payWay);
            sendCommand2ChangePayWay(payWay);
            curPayWay = payWay;
        }
    }

    private void refreshPayTabState(CustomPayWay view, int payWay) {
        // 支付方式按键
        view.setPayWaySelected();
        // 支切换付方式布局
        binding.layoutReceived.setVisibility(payWay == PAY_CASH ? View.VISIBLE : View.GONE);
        binding.layoutOthersPay.setVisibility(payWay != PAY_CASH ? View.VISIBLE : View.GONE);
        binding.tvOthersPay.setText(payWay == PAY_CARD ? getString(R.string.txt_card_pay_tip) : getString(R.string.txt_qrcode_pay_tip));
    }

    private void refreshPayPage(boolean isShowPayWay) {
        binding.layoutPayWay.setVisibility(isShowPayWay ? View.VISIBLE : View.GONE);
        binding.layoutPayResult.setVisibility(isShowPayWay ? View.GONE : View.VISIBLE);
    }

    private void payCompleted(boolean isSuccess) {
        int payResultImg = isSuccess ? R.drawable.gif_pay_success : R.drawable.gif_pay_failed;
        Glide.with(this)
                .asGif()
                .load(payResultImg)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(binding.ivPayResult);
        binding.tvPayResult.setText(isSuccess ? getString(R.string.txt_pay_success) : getString(R.string.txt_pay_failed));
        binding.tvPayVal.setText("$" + ticketTotalPrice);
        binding.btnNewSale.setVisibility(isSuccess ? View.VISIBLE : View.GONE);
        binding.btnRetry.setVisibility(isSuccess ? View.GONE : View.VISIBLE);
    }

    private void sendCommand2ChangePayWay(int payWay) {
        String command = SendCommand.PAY_BY_CASH;
        if (payWay == PAY_QRCODE) {
            command = SendCommand.PAY_BY_QRCODE;
        } else if (payWay == PAY_CARD) {
            command = SendCommand.PAY_BY_CARD;
        }
        UsbCommunicateManager.getInstance().sendCommandToHost(command);
    }

    private void sendCommand2Charge() {
        Gson gson = new Gson();
        OrderBean orderBean = new OrderBean(SendCommand.START_CHARGE, ticketTotalPrice, orderItemList);
        String orderBeanStr = gson.toJson(orderBean);
        UsbCommunicateManager.getInstance().sendCommandToHost(orderBeanStr);
    }

    private void sendCommandCashPaySuccess() {
        UsbCommunicateManager.getInstance().sendCommandToHost(SendCommand.CASH_CHARGE_SUCCESS);
    }
}

