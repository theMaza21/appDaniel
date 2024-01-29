package com.daniel.appdaniel.models;

public class SliderItem {
    String imagenUrl;
    Long timestamp;
    public  SliderItem()
    {}
    public SliderItem(String imagenUrl, Long timestamp) {
        this.imagenUrl = imagenUrl;
        this.timestamp = timestamp;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
