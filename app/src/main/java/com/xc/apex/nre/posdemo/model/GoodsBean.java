package com.xc.apex.nre.posdemo.model;

public class GoodsBean {

    private int id;
    private String name;
    private String unitPrice;
    private int srcImg;

    public GoodsBean(int id, String name, String unitPrice, int srcImg) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.srcImg = srcImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(int srcImg) {
        this.srcImg = srcImg;
    }
}
