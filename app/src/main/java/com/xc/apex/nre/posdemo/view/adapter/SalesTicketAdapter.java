package com.xc.apex.nre.posdemo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xc.apex.nre.posdemo.R;
import com.xc.apex.nre.posdemo.model.OrderItemBean;

import java.util.ArrayList;
import java.util.List;

public class SalesTicketAdapter extends RecyclerView.Adapter<SalesTicketAdapter.SalesTicketViewHolder> {

    List<OrderItemBean> data = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    public SalesTicketAdapter(@NonNull List<OrderItemBean> data) {
        this.data = data;
    }

    public void setData(List<OrderItemBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SalesTicketAdapter.SalesTicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SalesTicketAdapter.SalesTicketViewHolder(inflater.inflate(R.layout.adapter_sales_ticket_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesTicketAdapter.SalesTicketViewHolder holder, int position) {
        if (data != null && data.size() > 0 && data.size() > position) {
            if (data.get(position) != null) {
                holder.tvName.setText(data.get(position).getName());
                holder.tvNum.setText(String.valueOf(data.get(position).getNum()));
                holder.tvTotalPrice.setText("$" + data.get(position).getTotal());
            }
        }
        int pos = position;
        holder.ivReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abnormalClear(pos);
                if (itemClickListener != null) {
                    if (data.size() > pos) {
                        itemClickListener.onReduceClick(view, pos, data.get(pos).getUnitPrice());
                    }
                }
            }
        });
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abnormalClear(pos);
                if (itemClickListener != null) {
                    if (data.size() > pos) {
                        itemClickListener.onAddClick(view, pos, data.get(pos).getUnitPrice());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class SalesTicketViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private ImageView ivReduce;
        private ImageView ivAdd;
        private TextView tvNum;
        private TextView tvTotalPrice;

        public SalesTicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivReduce = itemView.findViewById(R.id.iv_reduce);
            ivAdd = itemView.findViewById(R.id.iv_add);
            tvNum = itemView.findViewById(R.id.tv_num);
            tvTotalPrice = itemView.findViewById(R.id.tv_price);
        }
    }

    public interface OnItemClickListener {
        void onReduceClick(View view, int position, String unp);

        void onAddClick(View view, int position, String unp);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    private void abnormalClear(int position) {
        if (data.size() <= position) {
            data.clear();
            notifyDataSetChanged();
        }
    }
}
