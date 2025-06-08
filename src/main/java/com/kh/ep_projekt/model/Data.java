package com.kh.ep_projekt.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Data {

    private Integer mal_id;
    private String title;
    private String synopsis;

    public Data() {}

    public Data(String title) {
        this.title = title;
    }

    public Integer getMal_id() {
        return mal_id;
    }

    public void setMal_id(Integer mal_id) {
        this.mal_id = mal_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}