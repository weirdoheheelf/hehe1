package com.example.cinema.blImpl.sales;

import com.example.cinema.bl.promotion.CouponService;
import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import com.example.cinema.data.promotion.ActivityMapper;
import com.example.cinema.data.promotion.CouponMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liying on 2019/4/16.
 */
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    ScheduleServiceForBl scheduleService;
    @Autowired
    HallServiceForBl hallService;
    @Autowired
    CouponServiceForBl couponServiceForBl;
    @Autowired
    ActivityServiceForBl activityServiceForBl;

    @Override
    @Transactional
    public ResponseVO addTicket(TicketForm ticketForm) {
        return null;
    }

    @Override
    @Transactional
    public ResponseVO completeTicket(List<Integer> id, int couponId) {
        try{
            Coupon coupon =couponServiceForBl.getCouponById(couponId);
            List<Coupon> coupons=new ArrayList<>();
            coupons.add(coupon);
            TicketWithCouponVO ticketWithCouponVO=new TicketWithCouponVO();
            List<TicketVO> ticketVOList=new ArrayList<>();
            double total=0;
            int movieId=0;
            for(Integer a:id){
                Ticket ticket=ticketMapper.selectTicketById(a);
                TicketWithScheduleVO ticketWithScheduleVO=ticket.getWithScheduleVO();
                if (ticketWithScheduleVO.getState().equals("未完成")){
                    ScheduleItem scheduleItem=ticketWithScheduleVO.getSchedule();
                    movieId=scheduleItem.getMovieId();
                    double ticketPrice=scheduleItem.getFare();
                    if (coupon!=null) {
                        double targetAmount = coupon.getTargetAmount();
                        Timestamp couponStartTime = coupon.getStartTime();
                        Timestamp couponEndTime = coupon.getEndTime();
                        Timestamp ticketTime = ticketWithScheduleVO.getTime();
                        if (ticketPrice >= targetAmount && ticketTime.after(couponStartTime) && ticketTime.before(couponEndTime)) {
                            total += ticketPrice - coupon.getDiscountAmount();
                        } else {
                            total += ticketPrice;
                        }
                    }
                    else {
                        total+=ticketPrice;
                    }
                    ticketMapper.updateTicketState(a,1);
                    ticketVOList.add(ticket.getVO());
                }
            }
            List<Activity> activities=activityServiceForBl.getActivitiesByMovie(movieId);
            ticketWithCouponVO.setCoupons(coupons);
            ticketWithCouponVO.setTotal(total);
            ticketWithCouponVO.setTicketVOList(ticketVOList);
            ticketWithCouponVO.setActivities(activities);
            return ResponseVO.buildSuccess(ticketWithCouponVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getBySchedule(int scheduleId) {
        try {
            List<Ticket> tickets = ticketMapper.selectTicketsBySchedule(scheduleId);
            ScheduleItem schedule=scheduleService.getScheduleItemById(scheduleId);
            Hall hall=hallService.getHallById(schedule.getHallId());
            int[][] seats=new int[hall.getRow()][hall.getColumn()];
            tickets.stream().forEach(ticket -> {
                seats[ticket.getRowIndex()][ticket.getColumnIndex()]=1;
            });
            ScheduleWithSeatVO scheduleWithSeatVO=new ScheduleWithSeatVO();
            scheduleWithSeatVO.setScheduleItem(schedule);
            scheduleWithSeatVO.setSeats(seats);
            return ResponseVO.buildSuccess(scheduleWithSeatVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTicketByUser(int userId) {
        return null;

    }

    @Override
    @Transactional
    public ResponseVO completeByVIPCard(List<Integer> id, int couponId) {
        return null;
    }

    @Override
    public ResponseVO cancelTicket(List<Integer> id) {
        try{
            for (Integer a:id){
                Ticket ticket=ticketMapper.selectTicketById(a);
                if (ticket!=null){
                    ticketMapper.deleteTicket(a);
                }
            }
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }



}
