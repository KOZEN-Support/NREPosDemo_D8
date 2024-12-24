package com.xc.apex.nre.posdemo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xc.apex.nre.posdemo.R;
import com.xc.apex.nre.posdemo.model.GoodsBean;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemsGoodsAdapter extends RecyclerView.Adapter<ItemsGoodsAdapter.ItemsGoodsViewHolder> {

    private Context context;
    List<GoodsBean> data = new ArrayList<>();

    public ItemsGoodsAdapter(Context context, @NonNull List<GoodsBean> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(ArrayList<GoodsBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemsGoodsAdapter.ItemsGoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemsGoodsAdapter.ItemsGoodsViewHolder(inflater.inflate(R.layout.adapter_items_goods_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsGoodsAdapter.ItemsGoodsViewHolder holder, int position) {
        if (data != null && data.size() > 0 && data.size() > position) {
            if (data.get(position) != null) {
                holder.name.setText(data.get(position).getName());
                Glide.with(context)
                        .load(data.get(position).getSrcImg())
                        .into(holder.imgView);
                holder.unitPrice.setText("$" + data.get(position).getUnitPrice());
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ItemsGoodsViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private CircleImageView imgView;
        private TextView unitPrice;

        public ItemsGoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            imgView = itemView.findViewById(R.id.iv_img);
            unitPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}