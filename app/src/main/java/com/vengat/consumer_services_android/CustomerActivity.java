package com.vengat.consumer_services_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.vengat.consumer_services_android.com.vengat.consumer_services_android.adapter.JSONAdapter;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by vengat.r on 6/15/2015.
 */
public class CustomerActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String QUERY_URL_GET_JOBS_BY_MOBILE_NUMBER = "http://10.0.2.2:8080/customers/jobs/mobileNumber/";//"http://jsonplaceholder.typicode.com/users/";//

    //private static final String QUERY_URL_GET_JOBS_BY_MOBILE_NUMBER ="http://openlibrary.org/search.json?q=";


    private static final String QUERY_URL_POST_PUT_JOB = "http://10.0.2.2:8080/jobs";

    ListView jobListView;

    JSONAdapter jsonAdapter;

    ProgressDialog progressDialog;

    EditText customerPhoneEditText;

    Button getJobsButton, postJobButton;

    String mobileNumber, userName, pincode;

    Spinner jobTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity which XML layout is right
        setContentView(R.layout.customer_main);

        // Enable the "Up" button for more navigation options
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra(MainActivity.USER_MOBILE_NUMBER);
        userName = intent.getStringExtra(MainActivity.USER_NAME);
        pincode = intent.getStringExtra(MainActivity.USER_PINCODE);


        jobListView = (ListView) findViewById(R.id.job_listview);//job_listview

        postJobButton = (Button) findViewById(R.id.postjob_button);
        postJobButton.setOnClickListener(this);

        createSpinner();

        //getJobsButton = (Button) findViewById(R.id.getjobs_button);
        //getJobsButton.setOnClickListener(this);

        //customerPhoneEditText = (EditText) findViewById(R.id.customer_phonenumber_edittext);

        //Create a JSONAdapter for the ListView
        jsonAdapter = new JSONAdapter(this, getLayoutInflater());

       // Set the ListView to use the ArrayAdapter
        jobListView.setAdapter(jsonAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting your jobs");
        progressDialog.setCancelable(false);

        queryJobs(mobileNumber);
    }



    public void createSpinner() {
        jobTypeSpinner = (Spinner) findViewById(R.id.jobtypes_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.job_types, android.R.layout.simple_spinner_item);


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // Apply the adapter to the spinner
        jobTypeSpinner.setAdapter(adapter);

        jobTypeSpinner.setOnItemSelectedListener(new JobTypeOnItemSelectedListener());
    }


    private void queryJobs(String searchString) {

        // Prepare your search string to be put in a URL
        // It might have reserved characters or something
        String urlString = "";
        try {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {

            // if this fails for some reason, let the user know why
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();

        progressDialog.show();

        // Have the client get a JSONArray of data
        // and define how to respond
        //urlString="";
        Log.d("Url string is " + QUERY_URL_GET_JOBS_BY_MOBILE_NUMBER + urlString, "");
        client.get(QUERY_URL_GET_JOBS_BY_MOBILE_NUMBER + urlString,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        //Log.d("omg android", jsonObject.toString());
                        progressDialog.dismiss();
                        // Display a "Toast" message
                        // to announce your success
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();

                        // 8. For now, just log results

                        Log.d("omg android", jsonArray.toString());
                        jsonAdapter.updateData(jsonArray);
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONArray error) {
                        progressDialog.dismiss();
                        // Display a "Toast" message
                        // to announce the failure
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();

                        // Log error message
                        // to help solve any problems
                        Log.e("omg android", statusCode + " " + throwable.getMessage());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        
        if(v.getId() == R.id.postjob_button) {
            Log.d("Posting a job", "");
        }

    }

}

/*
{
    "id": 0,
    "jobType": "UNDEFINED",
    "jobStatus": "OPEN",
    "customerName": "Your name",
    "customerMobileNumber": 987654321,
    "serviceProviderName": "Service Provider Name",
    "serviceProviderMobileNumber": 123456789,
    "pincode": "Pincode",
    "dateInitiated": 1434455317217,
    "dateDone": null,
    "description": "job description"
} */