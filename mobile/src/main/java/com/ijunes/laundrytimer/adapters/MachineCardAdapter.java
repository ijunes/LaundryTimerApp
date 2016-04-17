package com.ijunes.laundrytimer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ijunes.laundrytimer.R;
import com.ijunes.laundrytimer.model.Machine;

import java.util.List;

/**
 * Created by ijunes on 4/17/2016.
 */
public class MachineCardAdapter extends RecyclerView.Adapter<MachineCardViewHolder> {


    private List<Machine> contents;
    private Context context;

    public MachineCardAdapter(Context context, List<Machine> contents){
        this.contents = contents;
        this.context = context;
    }
    @Override
    public MachineCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MachineCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_machine, parent, false));
    }

    @Override
    public void onBindViewHolder(MachineCardViewHolder machineCard, int position) {
        Machine machine = contents.get(position);
        machineCard.nameTextView.setText(machine.getName());
        //holder.infoTextView.setText((int) machine.getId());

        String type = machine.getType();
        machineCard.statusTextView.setText(type);
        int typeId = type.matches("wash") ? R.drawable.ic_wash_icon : R.drawable.ic_flame_icon;
        machineCard.typeImageView.setImageDrawable(context.getResources().getDrawable(typeId));

    }

    @Override
    public int getItemCount() {
        return contents.size();
    }
}
