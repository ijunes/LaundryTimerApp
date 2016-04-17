package com.ijunes.laundrytimer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijunes.laundrytimer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ijunes on 4/17/2016.
 */
public class MachineCardViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.cardMachineName)
    protected TextView nameTextView;
    @Bind(R.id.cardMachineStatus)
    protected TextView statusTextView;
    @Bind(R.id.cardMachineInfo)
    protected TextView infoTextView;
    @Bind(R.id.cardMachineSubTitle)
    protected  TextView subTitleTextView;
    @Bind(R.id.cardMachineActive)
    protected ImageView activeImageView;
    @Bind(R.id.cardMachineType)
    protected ImageView typeImageView;

    public MachineCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
