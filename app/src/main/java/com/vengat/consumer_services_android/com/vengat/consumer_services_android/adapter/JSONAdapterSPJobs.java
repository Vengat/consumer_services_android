package com.vengat.consumer_services_android.com.vengat.consumer_services_android.adapter;

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
 * Created by vengat.r on 6/25/2015.
 */
public class JSONAdapterSPJobs   extends BaseAdapter {


    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    public JSONAdapterSPJobs(Context context, LayoutInflater inflater) {
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
            holder.serviceProviderName = (TextView) convertView.findViewById(R.id.text_serviceprovicer_name);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }


        JSONObject jsonObject = (JSONObject) getItem(position);

        String jobId = "";
        String jobType = "";
        String jobStatus = "";
        String serviceProviderMobileNumber = "";
        String serviceProviderName = "";

        if (jsonObject.has("jobId")) {
            jobId = jsonObject.optString("id");
        }

        if (jsonObject.has("jobType")) {
            jobType = jsonObject.optString("jobType");
        }

        if (jsonObject.has("jobStatus")) {
            jobStatus = jsonObject.optString("jobStatus");
        }

        if (jsonObject.has("serviceProviderMobileNumber")) {
            serviceProviderMobileNumber = jsonObject.optString("serviceProviderMobileNumber");
        }

        if (jsonObject.has("serviceProviderName")) {
            serviceProviderName = jsonObject.optString("serviceProviderName");
        }

        // Send these Strings to the TextViews for display
        holder.jobTypeTextView.setText(jobType);
        holder.jobStatusTextView.setText(jobStatus);
        holder.serviceProviderPhoneTextView.setText(serviceProviderMobileNumber);
        holder.serviceProviderName.setText(serviceProviderName);



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
        public TextView serviceProviderName;
    }

}
