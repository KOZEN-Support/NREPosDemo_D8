package com.xc.apex.nre.posdemo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xc.apex.nre.posdemo.R;
import com.xc.apex.nre.posdemo.model.OrderItemBean;

import java.util.ArrayList;
import java.util.List;

public class ChargeOrderAdapter extends RecyclerView.Adapter<ChargeOrderAdapter.ChargeOrderViewHolder> {

    private Context context;
    List<OrderItemBean> data = new ArrayList<>();

    public ChargeOrderAdapter(Context context, @NonNull List<OrderItemBean> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(List<OrderItemBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChargeOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ChargeOrderViewHolder(inflater.inflate(R.layout.adapter_charge_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChargeOrderViewHolder holder, int position) {
        if (data != null && data.size() > 0) {
            if (data.get(position) != null) {
                holder.tvItem.setText(data.get(position).getName());
                holder.tvAmount.setText(String.valueOf(data.get(position).getNum()));
                holder.tvSubTotal.setText("$" + data.get(position).getTotal());
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ChargeOrderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItem;
        private TextView tvAmount;
        private TextView tvSubTotal;

        public ChargeOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_order_title_item);
            tvAmount = itemView.findViewById(R.id.tv_order_title_amount);
            tvSubTotal = itemView.findViewById(R.id.tv_order_title_subtotal);
        }
    }
}