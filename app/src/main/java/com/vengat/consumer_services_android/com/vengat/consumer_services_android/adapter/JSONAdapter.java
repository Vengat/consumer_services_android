package com.vengat.consumer_services_android.com.vengat.consumer_services_android.adapter;

/**
 * Created by vengat.r on 6/15/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vengat.consumer_services_android.R;

import org.json.JSONArray;
import org.json.JSONObject;



/**
 * Created by vengat.r on 6/15/2015.
 */
public class JSONAdapter  extends BaseAdapter {

    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    public JSONAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.row_job, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.jobTypeTextView = (TextView) convertView.findViewById(R.id.text_job_type);
            holder.jobStatusTextView = (TextView) convertView.findViewById(R.id.text_job_status);
            holder.serviceProviderPhoneTextView = (TextView) convertView.findViewById(R.id.text_serviceprovicer_phone);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }


        JSONObject jsonObject = (JSONObject) getItem(position);


        String jobType = "";
        String jobStatus = "";
        String serviceProviderMobileNumber = "";

        if (jsonObject.has("jobType")) {
            jobType = jsonObject.optString("jobType");
        }

        if (jsonObject.has("jobStatus")) {
            jobStatus = jsonObject.optString("jobStatus");
        }

        if (jsonObject.has("serviceProviderMobileNumber")) {
            serviceProviderMobileNumber = jsonObject.optString("serviceProviderMobileNumber");
        }

        // Send these Strings to the TextViews for display
        holder.jobTypeTextView.setText(jobType);
        holder.jobStatusTextView.setText(jobStatus);
        holder.serviceProviderPhoneTextView.setText(serviceProviderMobileNumber);



        return convertView;

    }

    public void updateData(JSONArray jsonArray) {
        // update the adapter's dataset
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }


    // this is used so you only ever have to do
// inflation and finding by ID once ever per View
    private static class ViewHolder {
        public TextView jobTypeTextView;
        public TextView jobStatusTextView;
        public TextView serviceProviderPhoneTextView;
    }

}

