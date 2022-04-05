package org.me.gcu.robertson_charley_s2029977.models;

import java.util.Date;

//Name: Charley Robertson - Student ID: S2029977
public class TrafficItem
{
    //Initialise Variables
    private String title;
    private String description;
    private String link;
    private Date pubDate;
    private Date startDate;
    private Date endDate;
    private long duration;
    private double lat;
    private double lon;
    private TrafficItemType type;

    //Constructors
    public TrafficItem()
    {

    }

    public TrafficItem(String title, String description, String link, Date pubDate, Date startDate, Date endDate,double lat, double lon, long duration, TrafficItemType type)
    {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.lat = lat;
        this.lon = lon;
        this.type = type;
    }

    //Getters
    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getLink()
    {
        return link;
    }

    public Date getPubDate() { return pubDate; }

    public Date getStartDate() { return startDate; }

    public Date getEndDate() { return endDate; }

    public long getDuration() { return duration; }

    public double getLat() { return lat; }

    public double getLon() { return lon; }

    public TrafficItemType getType() { return type; }
    //Setters

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setLink(String link)
    {
        this.link = link;
    }


    public void setPubDate(Date pubDate)
    {
        this.pubDate = pubDate;
    }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public void setDuration(long duration) { this.duration = duration; }

    public void setLat(double lat) { this.lat = lat; }

    public void setLon(double lon) { this.lon = lon; }

    public void setType(TrafficItemType type)
    {
        this.type = type;
    }
    //To string method


    @Override
    public String toString() {
        return "TrafficItem{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", duration='" + duration + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", type=" + type +
                '}';
    }
}
