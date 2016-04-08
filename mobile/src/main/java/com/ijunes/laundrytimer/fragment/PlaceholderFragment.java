package com.ijunes.laundrytimer.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.zeng1990java.widget.WaveProgressView;
import com.ijunes.laundrytimer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaceholderFragment extends Fragment {

    @Bind(R.id.section_label) TextView tempTextView;
    @Bind(R.id.wave_progress_view) WaveProgressView waveProgressView;
    @Bind(R.id.temp_timer_wash_button) AppCompatButton washButton;
    @Bind(R.id.temp_timer_dry_button) AppCompatButton dryButton;
    @Bind(R.id.temp_timer_reset_button) AppCompatButton resetButton;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

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
        ButterKnife.bind(this, rootView);
        tempTextView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        setupButtonListeners();
    }

    private void washWave(WaveProgressView waveProgressView, long duration){
        ObjectAnimator progressAnim = ObjectAnimator.ofInt(waveProgressView, "progress", 0, waveProgressView.getMax());
        progressAnim.setDuration(duration);
        progressAnim.setRepeatMode(ObjectAnimator.REVERSE);
        progressAnim.start();
    }

    private void dryWave(WaveProgressView waveProgressView, long duration){
        ObjectAnimator progressAnim = ObjectAnimator.ofInt(waveProgressView, "progress", waveProgressView.getMax(), 0);
        progressAnim.setDuration(duration);
        progressAnim.setRepeatMode(ObjectAnimator.REVERSE);
        progressAnim.start();
    }

    private void setupButtonListeners(){
        washButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveProgressView.setMax(100);
                washWave(waveProgressView, 10 * 1000);
            }
        });

        dryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveProgressView.setWaveColor(R.color.dryWaveColor);
                waveProgressView.setMax(100);
                dryWave(waveProgressView, 20 * 1000);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveProgressView.setWaveColor(R.color.colorAccent);
                dryWave(waveProgressView, 1000);
            }
        });
    }
}