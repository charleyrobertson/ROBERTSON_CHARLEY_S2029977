package org.me.gcu.robertson_charley_s2029977.parsers;

import android.util.Log;

import org.me.gcu.robertson_charley_s2029977.models.PlannedRoadworks;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PlannedRoadworkParser {
    private PlannedRoadworks plannedRoadwork;
    private List<PlannedRoadworks> pRoadworksList = new ArrayList<>();

    public List<PlannedRoadworks> parsePlannedRoadworks(InputStream dataToParse) {
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
                        plannedRoadwork = new PlannedRoadworks();
                        insideItemTag = true;

                    } else if (parser.getName().equalsIgnoreCase("title")) {
                        if (insideItemTag) {
                            plannedRoadwork.setTitle(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("description")) {
                        if (insideItemTag) {
                            plannedRoadwork.setDescription(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("link")) {
                        if (insideItemTag) {
                            plannedRoadwork.setLink(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("point")) {
                        if (insideItemTag) {
                            plannedRoadwork.setLocation(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("pubDate")) {
                        if (insideItemTag) {
                            plannedRoadwork.setPubDate(parser.nextText());
                        }
                    }

                } else if (eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")) {
                    insideItemTag = false;
                    pRoadworksList.add(plannedRoadwork);
                }
                eventType = parser.next();
            }
            //Log.e("Planned Roadwork", ": " + plannedRoadwork.toString());
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return pRoadworksList;
    }//End of getplannedroadkworks
}//End of class
