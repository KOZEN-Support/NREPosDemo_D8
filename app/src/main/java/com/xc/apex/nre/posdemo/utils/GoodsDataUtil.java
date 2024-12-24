package com.xc.apex.nre.posdemo.utils;

import com.xc.apex.nre.posdemo.R;
import com.xc.apex.nre.posdemo.model.GoodsBean;

import java.util.ArrayList;
import java.util.List;

public class GoodsDataUtil {

    private ArrayList<GoodsBean> goodsListData;
    private static volatile GoodsDataUtil instance;

    public static GoodsDataUtil getInstance() {
        if (instance == null) {
            synchronized (GoodsDataUtil.class) {
                if (instance == null) {
                    instance = new GoodsDataUtil();
                }
            }
        }
        return instance;
    }

    private GoodsDataUtil() {
        goodsListData = new ArrayList<>();
        goodsListData.add(new GoodsBean(1, "Cheeseburger", "5.99", R.drawable.ic_cheeseburger));
        goodsListData.add(new GoodsBean(2, "Double Cheeseburger", "7.99", R.drawable.ic_double_cheeseburger));
        goodsListData.add(new GoodsBean(3, "Bacon Cheeseburger", "8.99", R.drawable.ic_baconcheeseburger));
        goodsListData.add(new GoodsBean(4, "Tuna Sandwich", "6.99", R.drawable.ic_tuna_sandwich));
        goodsListData.add(new GoodsBean(5, "BLT Sandwich", "4.99", R.drawable.ic_blt_sandwich));
        goodsListData.add(new GoodsBean(6, "Club Sandwich", "5.19", R.drawable.ic_club_sandwich));
        goodsListData.add(new GoodsBean(7, "Caesar Salad", "3.99", R.drawable.ic_caesar_salad));
        goodsListData.add(new GoodsBean(8, "Fried Chicken", "8.99", R.drawable.ic_fried_chicken));
        goodsListData.add(new GoodsBean(9, "Spaghetti and Meatballs", "8.99", R.drawable.ic_spaghetti_and_meatballs));
        goodsListData.add(new GoodsBean(10, "Steak", "10.99", R.drawable.ic_steak));
        goodsListData.add(new GoodsBean(11, "Chicago-Style Hot Dog", "3.99", R.drawable.ic_chicago_style_hotdog));
        goodsListData.add(new GoodsBean(12, "Coca-Cola", "1.99", R.drawable.ic_coca_cola));
        goodsListData.add(new GoodsBean(13, "Orange Juice", "1.99", R.drawable.ic_orange_juice));
        goodsListData.add(new GoodsBean(14, "Iced Tea", "2.99", R.drawable.ic_iced_tea));
        goodsListData.add(new GoodsBean(15, "Iced Coffee", "3.99", R.drawable.ic_iced_coffee));
    }

    public List<GoodsBean> getGoodsListData() {
        return goodsListData;
    }
}
