package com.kh.ep_projekt.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Data {

    private String title;
    public Data() {

    }
    public Data(String animeTitel) {
        this.title = animeTitel;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
