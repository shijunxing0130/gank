package com.gank.android.app.entity;

/**
 * Created by shijunxing on 2017/9/7.
 */

public class IconEntity {
    private int image;
    private String name;

    public IconEntity(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
