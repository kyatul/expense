package com.bigdreamslab.expensebox.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigdreamslab.expensebox.R;

public class LaunchScreenFragment2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_launch_screen_slide, container, false);

        ImageView iv = (ImageView) rootView.findViewById(R.id.ivScreenshot);
        iv.setImageResource(R.drawable.screenshot2);

        return rootView;
    }
}
