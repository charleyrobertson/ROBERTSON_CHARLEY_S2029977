package org.me.gcu.robertson_charley_s2029977.helpers;

import org.me.gcu.robertson_charley_s2029977.models.TrafficItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Name: Charley Robertson - Student ID: S2029977
public class ItemParser
{
    private TrafficItem item;
    private List<TrafficItem> list = new ArrayList<>();
    DateLogic dl = new DateLogic();

    public List<TrafficItem> parseItems(InputStream dataToParse) {
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
                        item = new TrafficItem();
                        insideItemTag = true;

                    } else if (parser.getName().equalsIgnoreCase("title")) {
                        if (insideItemTag) {
                            item.setTitle(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("description")) {
                        if (insideItemTag) {
                            String desc = parser.nextText().replaceAll("<br />", "\\\n");
                            item.setDescription(desc);

                                if(desc.substring(0, 5).equalsIgnoreCase("start"))
                                {
                                    item.setStartDate(dl.parseStartDate(desc));
                                    item.setEndDate(dl.parseEndDate(desc));
                                    item.setDuration(dl.calculateDuration(dl.parseStartDate(desc), dl.parseEndDate(desc)));
                                }
                                else
                                {
                                    item.setStartDate(new Date());
                                    item.setEndDate(new Date());
                                    item.setDuration(1);
                                }
                            }
                    } else if (parser.getName().equalsIgnoreCase("link")) {
                        if (insideItemTag) {
                            item.setLink(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("point")) {
                        if (insideItemTag) {
                            item.setLocation(parser.nextText());
                        }

                    } else if (parser.getName().equalsIgnoreCase("pubDate")) {
                        if (insideItemTag) {
                            Date tempPubDate = dl.parseDate(parser.nextText());

                            item.setPubDate(tempPubDate);
                            //Log.i("Item Parser", "Pub Date: " + temp2 );
                        }
                    }

                } else if (eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")) {
                    insideItemTag = false;
                    list.add(item);
                    //Log.i("LOOK", "parseItems: " + item);
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return list;
    } //End of parseItems


}
