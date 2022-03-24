package org.me.gcu.robertson_charley_s2029977.parsers;

import android.util.Log;

import org.me.gcu.robertson_charley_s2029977.models.Roadworks;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RoadworkParser
{
    private Roadworks roadwork;
    private List<Roadworks> roadworksList = new ArrayList<>();

    public List<Roadworks> parseRoadworks(InputStream dataToParse) {
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
                        roadwork = new Roadworks();
                        insideItemTag = true;

                    } else if (parser.getName().equalsIgnoreCase("title")) {
                        if (insideItemTag) {
                            roadwork.setTitle(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("description")) {
                        if (insideItemTag) {
                            roadwork.setDescription(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("link")) {
                        if (insideItemTag) {
                            roadwork.setLink(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("point")) {
                        if (insideItemTag) {
                            roadwork.setLocation(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("pubDate")) {
                        if (insideItemTag) {
                            roadwork.setPubDate(parser.nextText());
                        }
                    }

                } else if (eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")) {
                    insideItemTag = false;
                    roadworksList.add(roadwork);
                }
                eventType = parser.next();
            }
            Log.e("Roadwork", ": " + roadworksList.toString());
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return roadworksList;
    } //End of parseRoadworks

    } //End of RoadworkParser