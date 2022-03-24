package org.me.gcu.robertson_charley_s2029977;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.me.gcu.robertson_charley_s2029977.databinding.ActivityMainBinding;
import org.me.gcu.robertson_charley_s2029977.models.Incidents;
import org.me.gcu.robertson_charley_s2029977.models.PlannedRoadworks;
import org.me.gcu.robertson_charley_s2029977.models.Roadworks;
import org.me.gcu.robertson_charley_s2029977.parsers.IncidentParser;
import org.me.gcu.robertson_charley_s2029977.parsers.PlannedRoadworkParser;
import org.me.gcu.robertson_charley_s2029977.parsers.RoadworkParser;
import org.me.gcu.robertson_charley_s2029977.ui.home.HomeViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    //Roadworks
    private String roadworksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    RoadworkParser roadworkParser = new RoadworkParser();
    private List<Roadworks> roadworkList = new ArrayList<>();

    //Planned roadworks
    private String plannedRoadworksURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    PlannedRoadworkParser pRoadworkParser = new PlannedRoadworkParser();
    private List<PlannedRoadworks> pRoadworkList = new ArrayList<>();

    //Incidents
    private String incidentsURL = "https://trafficscotland.org/rss/feeds/incidents.aspx";
    IncidentParser incidentsParser = new IncidentParser();
    private List<Incidents> incidentsList = new ArrayList<>();

    //test
    HomeViewModel hmv = new HomeViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_roadworks, R.id.navigation_search, R.id.navigation_planner)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //Threading the calling of parser
        Log.e("Starting progress", "StartProgress()");
        startProgress();



    }

    public void startProgress() {
        new Thread(new Task(roadworksURL)).start();
    }

    private class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;

            try {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                roadworkList = roadworkParser.parseRoadworks(yc.getInputStream());
                hmv.getRoadworks(roadworkList);

                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("UI Thread", "I am in the UI Thread");
                }
            });


        }
    }
}