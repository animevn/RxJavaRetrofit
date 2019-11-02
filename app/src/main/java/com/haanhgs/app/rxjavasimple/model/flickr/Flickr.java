package com.haanhgs.app.rxjavasimple.model.flickr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Flickr {

    @SerializedName("photos")
    @Expose
    private Photos photos;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }
}
