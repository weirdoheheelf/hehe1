package com.example.cinema.vo;

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

    public PlacingRateVO(){
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
