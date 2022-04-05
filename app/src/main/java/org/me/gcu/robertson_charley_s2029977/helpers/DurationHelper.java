package org.me.gcu.robertson_charley_s2029977.helpers;

//Name: Charley Robertson - Student ID: S2029977
public class DurationHelper
{
    public int findColor(long duration)
    {
        int color = 0;

        if(duration < 7)
        {
            color = 0xFF00FF00;
        }
        if(duration >= 7 && duration <= 14)
        {
            color =  0xffff9966;
        }

        if(duration >= 15)
        {
            color = 0xffff0000;
        }

        return color;
    }

    public String findDaysWeeks(long duration)
    {
        String daysOrWeeks = "";

        if(duration <= 1)
        {
            daysOrWeeks = "day";
        }
        if(duration < 7 && duration > 1)
        {
            daysOrWeeks = "days";
        }
        if(duration >=7 && duration < 14)
        {
            daysOrWeeks = "week";
        }
        if(duration >= 14)
        {
            daysOrWeeks= "weeks";
        }

        return daysOrWeeks;
    }

    public String totalDuration(long duration)
    {
        String finalDuration = "";

        long weeks;
        long days;

        if(duration >= 7)
        {
            weeks = duration / 7;
            days = duration % 7;

            if(days > 0)
            {
                finalDuration = weeks + " " + findDaysWeeks(duration) + " " + days + " " + findDaysWeeks(days);
            }
            else
            {
                finalDuration = weeks + " " + findDaysWeeks(duration);
            }
        }
        else
        {
            days = duration;

            finalDuration = days + " " + findDaysWeeks(days);
        }
        return finalDuration;
    }

}
