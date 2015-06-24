package com.vengat.consumer_services_android.com.vengat.consumer_services_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;

/**
 * Created by vengat.r on 6/22/2015.
 */
public class JobDetailJSONAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    JSONArray jsonArray;

    public JobDetailJSONAdapter(Context context, LayoutInflater layoutInflater) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return jsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
