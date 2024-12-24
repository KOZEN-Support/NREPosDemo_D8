package com.xc.apex.nre.posdemo.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xc.apex.nre.posdemo.R;
import com.xc.apex.nre.posdemo.databinding.FragmentItemsBinding;
import com.xc.apex.nre.posdemo.utils.GoodsDataUtil;
import com.xc.apex.nre.posdemo.view.adapter.ItemsGoodsAdapter;
import com.xc.apex.nre.posdemo.view.base.BaseFragment;

public class ItemsFragment extends BaseFragment {

    private FragmentItemsBinding binding;
    private ItemsGoodsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_items, container, false);

        adapter = new ItemsGoodsAdapter(getActivity(), GoodsDataUtil.getInstance().getGoodsListData());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvGoods.setLayoutManager(layoutManager);
        binding.rvGoods.setAdapter(adapter);

        return binding.getRoot();
    }
}
