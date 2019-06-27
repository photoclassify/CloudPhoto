package com.photo.photo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GetNowTime
{
    public static Integer getDate()
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");

        Random rand = new Random ();
        Integer random = rand.nextInt(900) + 100;

        Integer date = Integer.valueOf (formatter.format(currentTime) + random) ;

        return date;
        }
}
