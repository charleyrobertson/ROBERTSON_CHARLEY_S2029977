package org.me.gcu.robertson_charley_s2029977.ui.home;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import org.me.gcu.robertson_charley_s2029977.models.Roadworks;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    public List<Roadworks> roadworksList = new ArrayList<>();

    public void getRoadworks(List<Roadworks> list)
    {
        roadworksList = list;
        Log.e("HMV", "" + roadworksList);
    }

}