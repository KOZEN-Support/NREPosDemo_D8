package com.xc.apex.nre.posdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * 与副屏数据bean保持一致
 */
public class OrderItemBean implements Parcelable {
    String name; // 商品名称
    int num; // 商品数量
    String unitPrice; // 商品单价
    String total; // 商品的总价

    public OrderItemBean(String name, int num, String unitPrice, String total) {
        this.name = name;
        this.num = num;
        this.unitPrice = unitPrice;
        this.total = total;
    }

    protected OrderItemBean(Parcel in) {
        name = in.readString();
        num = in.readInt();
        unitPrice = in.readString();
        total = in.readString();
    }

    public static final Creator<OrderItemBean> CREATOR = new Creator<OrderItemBean>() {
        @Override
        public OrderItemBean createFromParcel(Parcel in) {
            return new OrderItemBean(in);
        }

        @Override
        public OrderItemBean[] newArray(int size) {
            return new OrderItemBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "name='" + name + '\'' +
                ", num='" + num + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", total='" + total + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(num);
        parcel.writeString(unitPrice);
        parcel.writeString(total);
    }
}
