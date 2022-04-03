package org.me.gcu.robertson_charley_s2029977.ui.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.me.gcu.robertson_charley_s2029977.R;
import org.me.gcu.robertson_charley_s2029977.helpers.ItemParser;
import org.me.gcu.robertson_charley_s2029977.models.ListAdapter;
import org.me.gcu.robertson_charley_s2029977.models.TrafficItem;
import org.me.gcu.robertson_charley_s2029977.models.TrafficItemType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Name: Charley Robertson - Student ID: S2029977
public class SearchFragment extends Fragment implements View.OnClickListener {

    private List<TrafficItem> trafficItemList = new ArrayList<>();
    private List<TrafficItem> searchedTrafficList = new ArrayList<>();
    private TrafficItem item;

    ItemParser parser = new ItemParser();

    private ListView listView;
    private Button searchBtn;
    private Button clearBtn;
    private EditText searchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        listView = view.findViewById(R.id.listview);
        searchBar = view.findViewById(R.id.search_bar);

        searchBtn = view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(viewS -> { onClick(viewS); });

        clearBtn = view.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(viewC -> { onClick(viewC); });

        Log.i("Search Frag", "Starting Progress");
        startProgress();

        return view;
    }

    public void startProgress() {
        ExecutorService executerService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        Log.i("Search Frag", "startProgress: executing");
        String urls[] = {"https://trafficscotland.org/rss/feeds/roadworks.aspx", "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx", "https://trafficscotland.org/rss/feeds/currentincidents.aspx"};
        executerService.execute(() -> {
            for (String url : urls)
            {
                URL aurl;
                URLConnection yc;
                BufferedReader in = null;

                try
                {
                    aurl = new URL(url);
                    yc = aurl.openConnection();
                    in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                    List<TrafficItem> tempItem = parser.parseItems(yc.getInputStream());

                    if(url == "https://trafficscotland.org/rss/feeds/roadworks.aspx")
                    {
                        for(TrafficItem item : tempItem)
                        {
                            item.setType(TrafficItemType.Roadworks);
                            trafficItemList.addAll(tempItem);
                        }
                    }
                    else if (url == "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx")
                    {
                        for(TrafficItem item : tempItem)
                        {
                            item.setType(TrafficItemType.PlannedRoadworks);
                            trafficItemList.addAll(tempItem);
                        }
                    }
                    else if (url == "https://trafficscotland.org/rss/feeds/currentincidents.aspx")
                    {
                        for(TrafficItem item : tempItem)
                        {
                            item.setType(TrafficItemType.Incidents);
                            trafficItemList.addAll(tempItem);
                        }
                    }
                    tempItem.clear();
                    in.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                handler.post(() -> {
                    Log.i("Search Frag", "startProgress: in handler");

                    Collections.shuffle(trafficItemList);
                    ListAdapter listAdapter = new ListAdapter(this.getContext(), trafficItemList);
                    listView.setAdapter(listAdapter);
                });
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            //Start of searchBtm
            case R.id.searchBtn:
                if (searchBar.getText().toString().length() == 0)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage("Please enter a road before attempting to search!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {  public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } });
                    alertDialog.show();
                }
                else
                {
                    String searchTerm = searchBar.getText().toString();
                    searchedTrafficList.clear();

                    for(TrafficItem item : trafficItemList)
                    {
                        if(item.getTitle().contains(searchTerm))
                        {
                            searchedTrafficList.add(item);
                        }
                    }
                }

                if(searchedTrafficList.isEmpty())
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                    alertDialog.setTitle("Information");
                    alertDialog.setMessage("Please search for a different road. There are no scheduled roadworks or incidents on this road!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } });
                    alertDialog.show();
                }
                else
                {
                    ListAdapter listAdapter = new ListAdapter(this.getContext(), searchedTrafficList);
                    listView.setAdapter(listAdapter);
                }

            break;
            //End of searchBtm

            //Start of clearBtm
            case R.id.clearBtn:
                searchBar.getText().clear();
                ListAdapter listAdapter = new ListAdapter(this.getContext(), trafficItemList);
                listView.setAdapter(listAdapter);
            break;
            //End of clearBtn
        }
    }
}