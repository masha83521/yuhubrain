package com.masha.yuhubrain.data;

public class GameAwnser {
    private String value;
    private String imageLink;

    public String getImageLink() {
        return imageLink;
    }

    public String getValue() {
        return value;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public GameAwnser(){}
    public GameAwnser(String value, String imageLink){
        this.value = value;
        this.imageLink = imageLink;
    }
}
