package org.me.gcu.robertson_charley_s2029977.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import org.me.gcu.robertson_charley_s2029977.R;
import org.me.gcu.robertson_charley_s2029977.models.ListAdapter;
import org.me.gcu.robertson_charley_s2029977.models.TrafficItem;
import org.me.gcu.robertson_charley_s2029977.models.TrafficItemType;
import org.me.gcu.robertson_charley_s2029977.helpers.ItemParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//Name: Charley Robertson - Student ID: S2029977
public class HomeFragment extends Fragment implements View.OnClickListener {

    //Initialising Traffic Item Lists/Traffic items
    private List<TrafficItem> trafficItemList = new ArrayList<>();
    private List<TrafficItem> searchedTrafficList = new ArrayList<>();
    private TrafficItem item;

    ItemParser parser = new ItemParser();

    private ListView listView;
    private EditText datePicker;
    private DatePickerDialog picker;
    private Button searchBtn;
    private Button clearBtn;

    SimpleDateFormat formatDateNew =  new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        listView = view.findViewById(R.id.listview);
        datePicker = view.findViewById(R.id.datePicker);

        Log.i("Home Frag", "Starting Progress");
        startProgress();

        datePicker.setInputType(InputType.TYPE_NULL);
        datePicker.setOnClickListener(v -> { onClick(v); });

        searchBtn = view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(viewB ->  { onClick(viewB); });

        clearBtn = view.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(viewC -> {onClick(viewC); });

        return view;
    }

    public void startProgress() {
        ExecutorService executerService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        Log.i("Home Frag", "startProgress: executing");
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
                    Log.i("Home Frag", "startProgress: in handler");

                    Collections.shuffle(trafficItemList);
                    ListAdapter listAdapter = new ListAdapter(this.getContext(), trafficItemList);
                    listView.setAdapter(listAdapter);
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            //If the date picker is clicked
            case R.id.datePicker:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                datePicker.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            break;
            //If the date picker was clicked

            //If the search button is clicked
            case R.id.searchBtn:
                if (datePicker.getText().toString().length() == 0)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage("Please select a date before attempting to search!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {  public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } });
                    alertDialog.show();
                }
                else
                {
                    Date tempDate = new Date();
                    try {
                        tempDate = formatDateNew.parse(String.valueOf(datePicker.getText()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    searchedTrafficList.clear();

                    for (TrafficItem item : trafficItemList) {
                        //Log.e("Home Frag", "onClick: " + item.toString());
                        int equalToStartDate = item.getStartDate().compareTo(tempDate);
                        int equaltoEndDate = item.getEndDate().compareTo(tempDate);
                        boolean inbetweenDate = false;

                        if(item.getStartDate().before(tempDate) && item.getEndDate().after(tempDate))
                        {
                            inbetweenDate = true;
                        }

                        if (0 == equalToStartDate || 0 == equaltoEndDate || inbetweenDate) {
                           //Log.i("Home Frag", "Adding items to list" + item);
                            searchedTrafficList.add(item);
                        }
                    }

                    if(searchedTrafficList.isEmpty())
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage("Please select a different date. There are no scheduled roadworks or incidents on this day!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } });
                        alertDialog.show();
                    }
                    else
                    {
                        ListAdapter listAdapter = new ListAdapter(this.getContext(), searchedTrafficList);
                        listView.setAdapter(listAdapter);
                    }
                }
            break;
            //If the search button was clicked

            //If the clear button is clicked
            case R.id.clearBtn:
                datePicker.getText().clear();
                ListAdapter listAdapter = new ListAdapter(this.getContext(), trafficItemList);
                listView.setAdapter(listAdapter);
            break;
            //If the clear button was clicked
        }
    }
}

