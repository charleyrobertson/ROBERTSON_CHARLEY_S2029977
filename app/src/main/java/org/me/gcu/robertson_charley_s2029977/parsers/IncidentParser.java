package org.me.gcu.robertson_charley_s2029977.parsers;

import android.util.Log;

import org.me.gcu.robertson_charley_s2029977.models.Incidents;
import org.me.gcu.robertson_charley_s2029977.models.PlannedRoadworks;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class IncidentParser
{

    private Incidents incident;
    private List<Incidents> incidentsList = new ArrayList<>();

    public List<Incidents> parseIncidents(InputStream dataToParse) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(dataToParse, null);
            int eventType = parser.getEventType();
            boolean insideItemTag = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equalsIgnoreCase("item")) {
                        incident = new Incidents();
                        insideItemTag = true;

                    } else if (parser.getName().equalsIgnoreCase("title")) {
                        if (insideItemTag) {
                            incident.setTitle(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("description")) {
                        if (insideItemTag) {
                            incident.setDescription(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("link")) {
                        if (insideItemTag) {
                            incident.setLink(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("point")) {
                        if (insideItemTag) {
                            incident.setLocation(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("pubDate")) {
                        if (insideItemTag) {
                            incident.setPubDate(parser.nextText());
                        }
                    }

                } else if (eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")) {
                    insideItemTag = false;
                    incidentsList.add(incident);
                }
                eventType = parser.next();
            }
            Log.e("Incidents", ": " + incident.toString());
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return incidentsList;
    }//End of getincidents
}//End of class
