package org.me.gcu.robertson_charley_s2029977.ui.planner;


import org.me.gcu.robertson_charley_s2029977.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.gcu.robertson_charley_s2029977.helpers.ItemParser;
import org.me.gcu.robertson_charley_s2029977.models.ListAdapter;
import org.me.gcu.robertson_charley_s2029977.models.TrafficItem;
import org.me.gcu.robertson_charley_s2029977.models.TrafficItemType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Name: Charley Robertson - Student ID: S2029977
public class PlannerFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private final List<TrafficItem> trafficItemList = new ArrayList<>();
    private final List<TrafficItem> searchedTrafficList = new ArrayList<>();
    private final List<TrafficItem> mapMarkers = new ArrayList<>();
    private TrafficItem item;

    ItemParser parser = new ItemParser();

    MapView mapFragment;
    GoogleMap gMap;

    EditText dateChooser;
    EditText searchBar;
    Button searchBtn;
    DatePickerDialog dPicker;
    Button clearBtn;

    SimpleDateFormat formatDateNew =  new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.planner_fragment, container, false);

        try
        {
            MapsInitializer.initialize(this.getActivity());
             mapFragment =  (MapView) view.findViewById(R.id.map);
        }
        catch (InflateException e)
        {
            e.printStackTrace();
        }

        if (isOnline()) {
            Log.i("Planner Frag", "Starting Progress");
            startProgress();
            mapFragment.onCreate(savedInstanceState);
            mapFragment.getMapAsync(this);
        } else {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Please ensure you are connected to the internet!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        searchBar = view.findViewById(R.id.searchBar);

        dateChooser = view.findViewById(R.id.dateChooser);
        dateChooser.setInputType(InputType.TYPE_NULL);
        dateChooser.setOnClickListener(viewA -> { onClick(viewA); });

        searchBtn = view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(viewB ->  { onClick(viewB); });

        clearBtn = view.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(viewC -> { onClick(viewC); });

        return view;
    }

        public boolean isOnline () {
            ConnectivityManager conMgr = (ConnectivityManager) this.getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            return netInfo != null && netInfo.isConnected() && netInfo.isAvailable();
        }

        public void startProgress()
        {
            ExecutorService executerService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            Log.i("Planner Frag", "startProgress: executing");
            String[] urls = {"https://trafficscotland.org/rss/feeds/roadworks.aspx", "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx", "https://trafficscotland.org/rss/feeds/currentincidents.aspx"};
            executerService.execute(() -> {
                for (String url : urls) {
                    URL aurl;
                    URLConnection yc;
                    BufferedReader in = null;

                    try {
                        aurl = new URL(url);
                        yc = aurl.openConnection();
                        in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                        List<TrafficItem> tempItem = parser.parseItems(yc.getInputStream());

                        if (url == "https://trafficscotland.org/rss/feeds/roadworks.aspx") {
                            for (TrafficItem item : tempItem) {
                                item.setType(TrafficItemType.Roadworks);
                                trafficItemList.addAll(tempItem);
                            }
                        } else if (url == "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx") {
                            for (TrafficItem item : tempItem) {
                                item.setType(TrafficItemType.PlannedRoadworks);
                                trafficItemList.addAll(tempItem);
                            }
                        } else if (url == "https://trafficscotland.org/rss/feeds/currentincidents.aspx") {
                            for (TrafficItem item : tempItem) {
                                item.setType(TrafficItemType.Incidents);
                                trafficItemList.addAll(tempItem);
                            }
                        }
                        tempItem.clear();
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler.post(() -> {
                        Log.i("Planner Frag", "startProgress: in handler");

                        Collections.shuffle(trafficItemList);
                    });
                }
            });
        }

        @Override
        public void onPause()
        {
            super.onPause();
            mapFragment.onPause();
        }

        @Override
        public void onDestroy()
        {
            super.onDestroy();
            mapFragment.onDestroy();
        }

        @Override
        public void onSaveInstanceState(Bundle outState)
        {
            super.onSaveInstanceState(outState);
            mapFragment.onSaveInstanceState(outState);
        }

        @Override
        public void onLowMemory()
        {
            super.onLowMemory();
            mapFragment.onLowMemory();
        }

        @Override
        public void onResume()
        {
            super.onResume();
            mapFragment.onResume();
        }

        @Override
        public void onDestroyView()
        {
            super.onDestroyView();
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng scotland = new LatLng(56, -4);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(scotland));


        if(searchedTrafficList.isEmpty())
        {
              googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(56, -4))
                .title("Traffic Scotland Feed - Enter search terms to see markers!"));
        }
    }

    public void addMarkers(List<TrafficItem> searchedTrafficList)
    {
        gMap.clear();
        if(searchedTrafficList.isEmpty())
        {
            gMap.addMarker(new MarkerOptions()
                    .position(new LatLng(56, -4))
                    .title("Traffic Scotland Feed - Enter search terms to see markers!"));
        }
        else
        {
            for(TrafficItem item: searchedTrafficList)
            {
                gMap.addMarker(new MarkerOptions()
                        .position(new LatLng(item.getLat(), item.getLon()))
                        .title(item.getTitle()));
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.dateChooser:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                dPicker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateChooser.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                dPicker.show();
            break;

            case R.id.searchBtn:
                searchedTrafficList.clear();
                mapMarkers.clear();
                if(searchBar.getText().length() == 0 && dateChooser.getText().length() == 0)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage("Please enter a search term before attempting to search!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else if(searchBar.getText().length() > 0 && dateChooser.getText().length() == 0)
                {
                    String searchTerm = searchBar.getText().toString();
                        for (TrafficItem item : trafficItemList) {

                            if (item.getTitle().contains(searchTerm)) {
                                mapMarkers.add(item);
                            }
                        }
                    addMarkers(mapMarkers);
                }
                else if (dateChooser.getText().length() > 0 && searchBar.getText().length() == 0)
                {
                    Date tempDate = new Date();
                    try {
                        tempDate = formatDateNew.parse(String.valueOf(dateChooser.getText()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                        for(TrafficItem item: trafficItemList)
                        {
                            int equalToStartDate = item.getStartDate().compareTo(tempDate);
                            int equaltoEndDate = item.getEndDate().compareTo(tempDate);
                            boolean inbetweenDate = false;

                            if(item.getStartDate().before(tempDate) && item.getEndDate().after(tempDate))
                            {
                                inbetweenDate = true;
                            }

                            if (0 == equalToStartDate || 0 == equaltoEndDate || inbetweenDate) {
                                //Log.i("Home Frag", "Adding items to list" + item);
                                mapMarkers.add(item);
                            }
                        }
                    addMarkers(mapMarkers);
                }
                else
                {
                    String searchTerm = searchBar.getText().toString();

                    Date tempDate = new Date();
                    try {
                        tempDate = formatDateNew.parse(String.valueOf(dateChooser.getText()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                        for (TrafficItem item : trafficItemList) {

                            if (item.getTitle().contains(searchTerm)) {
                                searchedTrafficList.add(item);
                            }
                        }

                        for(TrafficItem item: searchedTrafficList)
                        {
                            int equalToStartDate = item.getStartDate().compareTo(tempDate);
                            int equaltoEndDate = item.getEndDate().compareTo(tempDate);
                            boolean inbetweenDate = false;

                            if(item.getStartDate().before(tempDate) && item.getEndDate().after(tempDate))
                            {
                                inbetweenDate = true;
                            }

                            if (0 == equalToStartDate || 0 == equaltoEndDate || inbetweenDate) {
                                //Log.i("Home Frag", "Adding items to list" + item);
                                mapMarkers.add(item);
                            }

                        }
                    addMarkers(mapMarkers);
                }
                break;

                case(R.id.clearBtn):
                    gMap.clear();
                    gMap.addMarker(new MarkerOptions()
                            .position(new LatLng(56, -4))
                            .title("Traffic Scotland Feed - Enter search terms to see markers!"));
                    dateChooser.setText("");
                    searchBar.setText("");
                break;
        }
    }
}

