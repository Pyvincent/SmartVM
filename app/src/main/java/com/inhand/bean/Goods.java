package com.inhand.bean;

import android.graphics.Bitmap;

/**
 * Created by vincent on 17-6-1.
 */

public class Goods {
    private Bitmap goodsBitmap;
    private String goodsName;

    public Goods(Bitmap goodsBitmap, String goodsName) {
        this.goodsBitmap = goodsBitmap;
        this.goodsName = goodsName;
    }

    public Bitmap getGoodsBitmap() {
        return goodsBitmap;
    }

    public void setGoodsBitmap(Bitmap goodsBitmap) {
        this.goodsBitmap = goodsBitmap;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
