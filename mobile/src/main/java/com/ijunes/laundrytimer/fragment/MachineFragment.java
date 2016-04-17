package com.ijunes.laundrytimer.fragment;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.zeng1990java.widget.WaveProgressView;
import com.ijunes.laundrytimer.R;
import com.ijunes.laundrytimer.adapters.MachineCardAdapter;
import com.ijunes.laundrytimer.model.Machine;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MachineFragment extends Fragment {


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    @Bind(R.id.machine_card_list) RecyclerView machineRecyclerView;
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
        final View rootView = inflater.inflate(R.layout.fragment_machine, container, false);
        ButterKnife.bind(this, rootView);
        machineRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        machineRecyclerView.setLayoutManager(llm);

        createAndSetDummyAdapter(machineRecyclerView);

        return rootView;
    }

    private void createAndSetDummyAdapter(RecyclerView machineRecyclerView) {
        final List<Machine> dummyContents = new ArrayList<>(5);
        for(int i=0; i<5; i++){
            final Machine dummyMachine = new Machine();
            dummyMachine.setId(i);
            dummyMachine.setName("Machine".concat(String.valueOf(i)));
            String type = i % 2 == 0 ? "wash" : "dry";
            dummyMachine.setType(type);
            dummyContents.add(dummyMachine);
        }
        machineRecyclerView.setAdapter(new MachineCardAdapter(this.getContext(), dummyContents));
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



}