package com.ijunes.laundrytimer.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.zeng1990java.widget.WaveProgressView;
import com.ijunes.laundrytimer.R;

public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private WaveProgressView waveProgressView;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        waveProgressView = (WaveProgressView) rootView.findViewById(R.id.wave_progress_view);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        waveProgressView.setMax(100);
        animWave(waveProgressView, 10 * 1000);
    }
    private void animWave(WaveProgressView waveProgressView, long duration){
        ObjectAnimator progressAnim = ObjectAnimator.ofInt(waveProgressView, "progress", 0, waveProgressView.getMax());
        progressAnim.setDuration(duration);
        progressAnim.setRepeatCount(ObjectAnimator.INFINITE);
        progressAnim.setRepeatMode(ObjectAnimator.RESTART);
        progressAnim.start();
    }
}