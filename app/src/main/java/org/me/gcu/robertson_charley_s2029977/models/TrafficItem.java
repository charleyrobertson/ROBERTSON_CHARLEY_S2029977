package org.me.gcu.robertson_charley_s2029977.models;

import java.util.Date;

public class TrafficItem
{
    //Initialise Variables
    private String title;
    private String description;
    private String link;
    private String location;
    private Date pubDate;
    private Date startDate;
    private Date endDate;
    private TrafficItemType type;

    //Constructors
    public TrafficItem()
    {

    }

    public TrafficItem(String title, String description, String link, String location, Date pubDate, Date startDate, Date endDate, TrafficItemType type)
    {
        this.title = title;
        this.description = description;
        this.link = link;
        this.location = location;
        this.pubDate = pubDate;
        this.startDate = startDate;
        this.endDate = endDate;;
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

    public String getLocation()
    {
        return location;
    }

    public Date getPubDate() { return pubDate; }

    public Date getStartDate() { return startDate; }

    public Date getEndDate() { return endDate; }

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

    public void setLocation(String location)
    {
        this.location = location;
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
                ", location='" + location + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", type=" + type +
                '}';
    }
}
