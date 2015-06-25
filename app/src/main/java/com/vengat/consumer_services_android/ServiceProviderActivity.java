package com.vengat.consumer_services_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.vengat.consumer_services_android.com.vengat.consumer_services_android.adapter.JSONAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by vengat.r on 6/15/2015.
 */
public class ServiceProviderActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private static String QUERY_URL_GET_MATCHING_JOBS_BY_MOBILE_NUMBER = "http://10.0.2.2:8080/serviceProviders/openAssignJobs/mobileNumber/";
    //private static String QUERY_URL_GET_MATCHING_JOBS_BY_MOBILE_NUMBER = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/serviceProviders/openAssignJobs/mobileNumber/";


    private String mobileNumber, userName, pincode;
    private ListView jobListView;
    private JSONAdapter jsonAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serviceprovider_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra(MainActivity.USER_MOBILE_NUMBER);
        userName = intent.getStringExtra(MainActivity.USER_NAME);
        pincode = intent.getStringExtra(MainActivity.USER_PINCODE);

        jobListView = (ListView) findViewById(R.id.job_listview);//job_listview
        jobListView.setOnItemClickListener(this);

        jsonAdapter = new JSONAdapter(this, getLayoutInflater());

        // Set the ListView to use the ArrayAdapter
        jobListView.setAdapter(jsonAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting your jobs");
        progressDialog.setCancelable(false);

        queryMatchingJobsByServiceProviderMobileNumber(mobileNumber);

    }

    private void queryMatchingJobsByServiceProviderMobileNumber(String mobileNumber) {
        // Prepare your search string to be put in a URL
        // It might have reserved characters or something
        String urlString = "";
        try {
            urlString = URLEncoder.encode(mobileNumber, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        //setProgressBarIndeterminateVisibility(true);
        progressDialog.show();
        Log.d("Url string is " + QUERY_URL_GET_MATCHING_JOBS_BY_MOBILE_NUMBER + urlString, "");
        client.get(QUERY_URL_GET_MATCHING_JOBS_BY_MOBILE_NUMBER + urlString,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        //setProgressBarIndeterminateVisibility(false);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                        jsonAdapter.updateData(jsonArray);
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONArray error) {
                        //setProgressBarIndeterminateVisibility(false);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("omg android", statusCode + " " + throwable.getMessage());
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) jsonAdapter.getItem(position);
        String jobId = jsonObject.optString("id","");
        Log.d("On Item Click****", "");
        Intent jobSPAssignIntent = new Intent(this, JobSPAssignActivity.class);
        jobSPAssignIntent.putExtra("jobId", jobId);
        jobSPAssignIntent.putExtra("mobileNumber", mobileNumber);
        startActivity(jobSPAssignIntent);
    }
}