package com.ijunes.laundrytimer.fragment;

import android.animation.ObjectAnimator;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.zeng1990java.widget.WaveProgressView;
import com.ijunes.laundrytimer.R;
import com.ijunes.laundrytimer.service.TimerService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MachineFragment extends Fragment {

    @Bind(R.id.tempTextView) TextView tempTextView;
    @Bind(R.id.wave_progress_view) WaveProgressView waveProgressView;
    @Bind(R.id.tempTimerWashButton) AppCompatButton washButton;
    @Bind(R.id.tempTimerDryButton) AppCompatButton dryButton;
    @Bind(R.id.tempTimerResetButton) AppCompatButton resetButton;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Creating two timers just to be lazy and will refactor
     */
    private CountDownTimer washTimer;
    private CountDownTimer dryTimer;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MachineFragment newInstance(int sectionNumber) {
        MachineFragment fragment = new MachineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MachineFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView;

           rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.bind(this, rootView);


        WaveProgressView newProgressView = new WaveProgressView(this.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newProgressView.setLayoutParams(layoutParams);
        container.addView(newProgressView);
        container.invalidate();
        tempTextView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        setupButtonListeners();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void washWave(WaveProgressView waveProgressView, long duration){
        ObjectAnimator progressAnim = ObjectAnimator.ofInt(waveProgressView, "progress", 0, waveProgressView.getMax());
        progressAnim.setDuration(duration);
        progressAnim.setRepeatMode(ObjectAnimator.REVERSE);
        progressAnim.start();
        washTimer.start();
        Timber.d("Starting Wash Animation");
    }

    private void dryWave(WaveProgressView waveProgressView, long duration){
        ObjectAnimator progressAnim = ObjectAnimator.ofInt(waveProgressView, "progress", waveProgressView.getMax(), 0);
        progressAnim.setDuration(duration);
        progressAnim.setRepeatMode(ObjectAnimator.REVERSE);
        progressAnim.start();
        Timber.d("Starting Dry Animation");
    }

    private void sendTimerBroadcast(String message){
        Intent stopIntent = new Intent(message);
        PendingIntent startPendingIntent = PendingIntent.getBroadcast(getActivity(),
                0, stopIntent, 0);
        try {
            startPendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
    private void setupButtonListeners(){



    }
    @OnClick(R.id.tempTimerWashButton)
    void startWash(){
        washTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tempTextView.setText(String.valueOf(millisUntilFinished));
                if(millisUntilFinished % 10 == 0){
                    long timeLeft =  (millisUntilFinished);
                    waveProgressView.setProgress((int) timeLeft);
                }
            }

            @Override
            public void onFinish() {
                dryButton.setVisibility(View.VISIBLE);
            }
        }.start();
        washButton.setVisibility(View.INVISIBLE);
        //dryButton.setEnabled(false);
        waveProgressView.setMax(100);

        //washWave(waveProgressView, 10 * 1000);
        sendTimerBroadcast(TimerService.START_ACTION);
        Timber.d("Starting Wash");
    }

    @OnClick(R.id.tempTimerDryButton)
    void startDry(){
        dryButton.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.VISIBLE);
        waveProgressView.setWaveColor(R.color.dryWaveColor);
        waveProgressView.setMax(100);
        dryWave(waveProgressView, 20 * 1000);
        sendTimerBroadcast(TimerService.RESTART_ACTION);
        Timber.d("Starting Dry");
    }

    @OnClick(R.id.tempTimerResetButton)
    void reset(){
        resetButton.setVisibility(View.INVISIBLE);
        washButton.setVisibility(View.VISIBLE);
        waveProgressView.setWaveColor(R.color.colorAccent);
        dryWave(waveProgressView, 1000);
        sendTimerBroadcast(TimerService.STOP_ACTION);
        Timber.d("Stopping");
    }
}