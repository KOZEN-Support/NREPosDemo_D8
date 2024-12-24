package com.xc.apex.nre.posdemo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xc.apex.nre.posdemo.R;
import com.xc.apex.nre.posdemo.model.GoodsBean;

import java.util.ArrayList;
import java.util.List;

public class SalesGoodsAdapter extends RecyclerView.Adapter<SalesGoodsAdapter.SalesGoodsViewHolder> {

    private Context context;
    List<GoodsBean> data = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    public SalesGoodsAdapter(Context context, @NonNull List<GoodsBean> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(ArrayList<GoodsBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SalesGoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SalesGoodsViewHolder(inflater.inflate(R.layout.adapter_sales_good_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesGoodsViewHolder holder, int position) {
        if (data != null && data.size() > 0 && data.size() > position) {
            if (data.get(position) != null) {
                String name = data.get(position).getName();
                if (data.get(position).getId() == 3) {
                    name = "Bacon\nCheeseburger";
                } else if (data.get(position).getId() == 9) {
                    name = "Spaghetti and\nMeatballs";
                } else if (data.get(position).getId() == 11) {
                    name = "Chicago-Style\nHot Dog";
                } else if (data.get(position).getId() == 2) {
                    name = "Double\nCheeseburger";
                }

                holder.name.setText(name);
                holder.unitPrice.setText("$" + data.get(position).getUnitPrice());
                Glide.with(context)
                        .load(data.get(position).getSrcImg())
                        .into(holder.goodImg);
            }
            int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(view, pos);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class SalesGoodsViewHolder extends RecyclerView.ViewHolder {
        private ImageView goodImg;
        private TextView name;
        private TextView unitPrice;

        public SalesGoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            goodImg = itemView.findViewById(R.id.srcImg);
            name = itemView.findViewById(R.id.name);
            unitPrice = itemView.findViewById(R.id.tv_unit_price);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }
}