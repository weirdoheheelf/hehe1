package com.example.cinema.blImpl.sales;

import com.example.cinema.po.Coupon;

public interface CouponServiceForBl {
    /**
     * 根据id查找优惠劵
     * @param id
     * @return
     */
    Coupon getCouponById(int id);
}
