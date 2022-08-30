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



    public static String cryptWithMD5(String pass){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
           // Logger.getLogger(CryptWithMD5.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;


    }
}
