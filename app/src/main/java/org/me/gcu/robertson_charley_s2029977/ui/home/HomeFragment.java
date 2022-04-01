package org.me.gcu.robertson_charley_s2029977.ui.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;



import org.me.gcu.robertson_charley_s2029977.R;
import org.me.gcu.robertson_charley_s2029977.models.ListAdapter;
import org.me.gcu.robertson_charley_s2029977.models.TrafficItem;
import org.me.gcu.robertson_charley_s2029977.parsers.ItemParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {


    private List<TrafficItem> trafficItemList = new ArrayList<>();
    private String roadworksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    ItemParser parser = new ItemParser();
    private ListView listView;
    private TextView clickableTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.home_fragment, container, false);
        listView = view.findViewById(R.id.listview);

        Log.i("Home Frag", "Starting Progress");
        startProgress();
        return view;
    }

    public void startProgress() {
        ExecutorService executerService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        Log.i("Home Frag", "startProgress: executing");
        executerService.execute(() -> {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;

            try {
                aurl = new URL("https://trafficscotland.org/rss/feeds/roadworks.aspx");
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                trafficItemList = parser.parseItems(yc.getInputStream());
                Log.i("Home Frag", "List:" + trafficItemList.toString());
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.post(() -> {
                Log.i("Home Frag", "startProgress: in handler");
                ListAdapter listAdapter = new ListAdapter(this.getContext(), trafficItemList);
                listView.setAdapter(listAdapter);
            });
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}

