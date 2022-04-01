package org.me.gcu.robertson_charley_s2029977.models;

public class TrafficItem
{
    //Initialise Variables
    private String title;
    private String description;
    private String link;
    private String location;
    private String pubDate;

    //Constructors
    public TrafficItem()
    {

    }

    public TrafficItem(String title, String description, String link, String location, String pubDate)
    {
        this.title = title;
        this.description = description;
        this.link = link;
        this.location = location;
        this.pubDate = pubDate;
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

    public String getPubDate()
    {
        return pubDate;
    }

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

    public void setPubDate(String pubDate)
    {
        this.pubDate = pubDate;
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
                '}';
    }


}
enum  TrafficListItem
{
    PlannedRoadworks,
    Roadworks,
    Incidents
}