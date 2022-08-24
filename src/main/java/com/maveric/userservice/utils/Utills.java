package com.maveric.userservice.utils;

import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public interface Utills {

    public static Date convertDateUtilToSql(java.util.Date date){
        LocalDateTime conv= LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDate convDate=conv.toLocalDate();
        return java.sql.Date.valueOf(convDate);
    }

    public  static  Date getCurrentDate(){
        java.util.Date currentDate = new java.util.Date();
        return convertDateUtilToSql(currentDate);
    }

    public  static java.util.Date convertSqlToUtilDate(Date date){
        return  new java.sql.Date(date.getTime());
    }
}
