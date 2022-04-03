package org.me.gcu.robertson_charley_s2029977.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//Name: Charley Robertson - Student ID: S2029977
public class DateLogic
{
    private String OLD_DATE_FORMAT = "EEE, dd MMM yyyy hh:mm:ss zzz";
    private String NEW_DATE_FORMAT = "dd/MM/yyyy";
    SimpleDateFormat formatDateOld =  new SimpleDateFormat(OLD_DATE_FORMAT, Locale.ENGLISH);
    SimpleDateFormat formatDateNew =  new SimpleDateFormat(NEW_DATE_FORMAT, Locale.ENGLISH);
    SimpleDateFormat startEndDate = new SimpleDateFormat("dd MMMM yyyy",  Locale.ENGLISH);

    public Date parseDate(String tempIn)
    {
        String temp = tempIn;
        Date dateTemp = new Date();
        try {
            dateTemp = formatDateOld.parse(temp);
            String temp2 = formatDateNew.format(dateTemp);
            dateTemp = formatDateNew.parse(temp2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTemp;
    }

    public Date parseStartDate(String description)
    {
        String startDateString;
        Date startDate = new Date();

        try {
            int startOfDate = description.indexOf(",");
            int endOfDate = description.indexOf("-");

            startDateString = description.substring(startOfDate + 2, endOfDate -1);

            startDate = startEndDate.parse(startDateString);

            String startDateTemp = formatDateNew.format(startDate);
            startDate = formatDateNew.parse(startDateTemp);

            //Log.i("DateLogic", "Start Date: " + startDateTemp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return startDate;
    }

    public Date parseEndDate(String description)
    {
        String endDateString;
        Date endDate = new Date();

        try {
            int startOfDate = description.indexOf(",", description.indexOf(",") +1);
            int endOfDate = description.indexOf("-", description.indexOf("-") +1);

            endDateString = description.substring(startOfDate + 2, endOfDate -1);

            endDate = startEndDate.parse(endDateString);

            String endDateTemp = formatDateNew.format(endDate);
            endDate = formatDateNew.parse(endDateTemp);
            //Log.i("DateLogic", "parseEndDate: " + endDateTemp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return endDate;
    }

    public long calculateDuration(Date startDateIn, Date endDateIn)
    {
        Date startDate  = startDateIn;
        Date endDate = endDateIn;
        long duration = 0;

        duration = endDate.getTime() - startDate.getTime();

        duration = duration / (1000 * 60 * 60 * 24);

        return duration;
    }

}
