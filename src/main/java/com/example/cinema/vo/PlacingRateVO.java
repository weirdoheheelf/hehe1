package com.example.cinema.vo;

import com.example.cinema.po.PlacingRate;

import java.util.Date;
import java.util.List;

public class PlacingRateVO {
    /**
     * 电影id
     */
    private Integer movieId;
    /**
     * 电影上座率
     */
    private String placingRate;
    /**
     * 电影名字
     */
    private String name;

    public PlacingRateVO(PlacingRate placingRate){
        this.movieId=placingRate.getId();
        this.placingRate=placingRate.getPlacingRate();
        this.name=placingRate.getName();
    }

    public Integer getId(){ return movieId;}

    public void setId(Integer id){ this.movieId=movieId;}

    public String getPlacingRate() {
        return placingRate;
    }

    public void setPlacingRate(String placingRateByDate) {
        this.placingRate = placingRateByDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
