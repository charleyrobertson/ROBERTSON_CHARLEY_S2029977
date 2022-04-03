package org.me.gcu.robertson_charley_s2029977.parsers;

import android.util.Log;

import org.me.gcu.robertson_charley_s2029977.models.TrafficItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemParser
{
    private TrafficItem item;
    private List<TrafficItem> list = new ArrayList<>();
    private String OLD_DATE_FORMAT = "EEE, dd MMM yyyy hh:mm:ss zzz";
    private String NEW_DATE_FORMAT = "dd/MM/yyyy";
    SimpleDateFormat formatDateOld =  new SimpleDateFormat(OLD_DATE_FORMAT, Locale.ENGLISH);
    SimpleDateFormat formatDateNew =  new SimpleDateFormat(NEW_DATE_FORMAT, Locale.ENGLISH);

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
                            item.setDescription(parser.nextText().replaceAll("<br />", "\\\n"));
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
                            String temp = parser.nextText();
                            Date dateTemp = formatDateOld.parse(temp);

                            String temp2 = formatDateNew.format(dateTemp);
                            dateTemp = formatDateNew.parse(temp2);

                            item.setPubDate(dateTemp);
                            //Log.i("Item Parser", "Pub Date: " + temp2 );
                        }
                    }

                } else if (eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")) {
                    insideItemTag = false;
                    list.add(item);
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException | ParseException e) {
            e.printStackTrace();
        }

        return list;
    } //End of parseItems


}
