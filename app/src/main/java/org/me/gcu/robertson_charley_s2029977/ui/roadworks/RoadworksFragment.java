package org.me.gcu.robertson_charley_s2029977.ui.roadworks;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.me.gcu.robertson_charley_s2029977.R;

public class RoadworksFragment extends Fragment {

    private RoadworksViewModel mViewModel;

    public static RoadworksFragment newInstance() {
        return new RoadworksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.roadworks_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RoadworksViewModel.class);
        // TODO: Use the ViewModel
    }

}