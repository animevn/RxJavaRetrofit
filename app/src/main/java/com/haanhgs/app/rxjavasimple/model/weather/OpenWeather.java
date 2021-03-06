package com.haanhgs.app.rxjavasimple.model.weather;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenWeather {

    @SerializedName("list")
    @Expose
    private List<ListHour> list = null;

    @SerializedName("city")
    @Expose
    private City city;

    public List<ListHour> getList() {
        return list;
    }

    public void setList(List<ListHour> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }



//    @SerializedName("cod")
//    @Expose
//    private String cod;

//    @SerializedName("message")
//    @Expose
//    private Double message;

//    @SerializedName("cnt")
//    @Expose
//    private Integer cnt;



//    public String getCod() {
//        return cod;
//    }
//
//    public void setCod(String cod) {
//        this.cod = cod;
//    }
//
//    public Double getMessage() {
//        return message;
//    }
//
//    public void setMessage(Double message) {
//        this.message = message;
//    }
//
//    public Integer getCnt() {
//        return cnt;
//    }
//
//    public void setCnt(Integer cnt) {
//        this.cnt = cnt;
//    }


}
