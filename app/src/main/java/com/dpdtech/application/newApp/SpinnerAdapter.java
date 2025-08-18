package com.dpdtech.application.newApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dpdtech.application.network.models.response.LoginResponse;
import com.tgw.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private final Context context;
    private final List<LoginResponse.FlapBarrierCounter> flapBarrierCounters;

    public SpinnerAdapter(Context context, List<LoginResponse.FlapBarrierCounter> flapBarrierCounters) {
        this.context = context;
        this.flapBarrierCounters = flapBarrierCounters;
    }

    @Override
    public int getCount() {
        return flapBarrierCounters.size();
    }

    @Override
    public Object getItem(int position) {
        return flapBarrierCounters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.textSpinner);
        textView.setText(flapBarrierCounters.get(position).counterName);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.textSpinner);
        textView.setText(flapBarrierCounters.get(position).counterName);
        return convertView;
    }
}