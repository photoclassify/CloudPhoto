package com.photo.photo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GetNowTime
{
        public static Integer getDate()            //获取年月日+三位数随机数，用于图片命名
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");

        Random rand = new Random ();
        Integer random = rand.nextInt(900) + 100;

        Integer date = Integer.valueOf (formatter.format(currentTime) + random) ;

        return date;
        }
}
