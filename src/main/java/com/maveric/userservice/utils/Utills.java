package com.maveric.userservice.utils;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public interface Utills {

    public static Date convertDateUtilToSql(java.util.Date date){
        LocalDateTime convert= LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDate convertDate=convert.toLocalDate();
        return java.sql.Date.valueOf(convertDate);
    }

    public  static  Date getCurrentDate(){
        java.util.Date currentDate = new java.util.Date();
        return convertDateUtilToSql(currentDate);
    }

    public  static java.util.Date convertSqlToUtilDate(Date date){
        return  new java.sql.Date(date.getTime());
    }



    public static String cryptWithMD5(String pass){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            messageDigest.reset();
            byte[] digested = messageDigest.digest(passBytes);
            StringBuffer stringBuffer = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                stringBuffer.append(Integer.toHexString(0xff & digested[i]));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException ex) {
        }
        return "";
    }
}
