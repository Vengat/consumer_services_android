package com.vengat.consumer_services_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.vengat.consumer_services_android.com.vengat.consumer_services_android.adapter.JSONAdapter;
import com.vengat.consumer_services_android.model.Job;
import com.vengat.consumer_services_android.model.JobStatus;
import com.vengat.consumer_services_android.model.JobType;
import com.vengat.consumer_services_android.rest_classes.GetJob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.vengat.consumer_services_android.rest_classes.PostJob.POST;

/**
 * Created by vengat.r on 6/15/2015.
 */
public class CustomerActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String QUERY_URL_GET_JOBS_BY_MOBILE_NUMBER = "http://10.0.2.2:8080/customers/jobs/mobileNumber/";//"http://jsonplaceholder.typicode.com/users/";//

    //private static final String QUERY_URL_GET_JOBS_BY_MOBILE_NUMBER ="http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/customers/jobs/mobileNumber/";
    private static final String QUERY_URL_GET_JOB_BY_ID = "http://10.0.2.2:8080/jobs/id";

    //private static final String QUERY_URL_GET_JOB_BY_ID = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/jobs/id";

    private static final String QUERY_URL_POST_PUT_JOB = "http://10.0.2.2:8080/jobs";



    //private static final String QUERY_URL_POST_PUT_JOB = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/jobs";

    private String jobTypeSpinnerSelectionValue = null;

    public final static String JOB_ID = "com.vengat.consumer_services_android.JOB_ID";

    ListView jobListView;

    JSONAdapter jsonAdapter;

    ProgressDialog progressDialog;

    EditText customerPhoneEditText, jobDescriptionEditText;

    Button getJobsButton, postJobButton;

    String mobileNumber, userName, pincode;

    Spinner jobTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //setProgressBarIndeterminateVisibility(false);

        // Tell the activity which XML layout is right
        setContentView(R.layout.customer_main);

        // Enable the "Up" button for more navigation options
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra(MainActivity.USER_MOBILE_NUMBER);
        userName = intent.getStringExtra(MainActivity.USER_NAME);
        pincode = intent.getStringExtra(MainActivity.USER_PINCODE);


        jobListView = (ListView) findViewById(R.id.job_listview);//job_listview
        jobListView.setOnItemClickListener(this);

        postJobButton = (Button) findViewById(R.id.postjob_button);
        postJobButton.setOnClickListener(this);

        createSpinner();

        //getJobsButton = (Button) findViewById(R.id.getjobs_button);
        //getJobsButton.setOnClickListener(this);

        jobDescriptionEditText = (EditText) findViewById(R.id.job_description);
        //customerPhoneEditText = (EditText) findViewById(R.id.customer_phonenumber_edittext);

        //Create a JSONAdapter for the ListView
        jsonAdapter = new JSONAdapter(this, getLayoutInflater());

       // Set the ListView to use the ArrayAdapter
        jobListView.setAdapter(jsonAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting your jobs");
        progressDialog.setCancelable(false);

        queryJobsByCustomerMobileNumber(mobileNumber);
    }



    public void createSpinner() {
        jobTypeSpinner = (Spinner) findViewById(R.id.jobtypes_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.job_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobTypeSpinner.setAdapter(adapter);
        jobTypeSpinner.setOnItemSelectedListener(new JobTypeOnItemSelectedListener());
        jobTypeSpinnerSelectionValue = jobTypeSpinner.getSelectedItem().toString();
    }


    private void queryJobsByCustomerMobileNumber(String mobileNumber) {
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
        Log.d("Url string is " + QUERY_URL_GET_JOBS_BY_MOBILE_NUMBER + urlString, "");
        client.get(QUERY_URL_GET_JOBS_BY_MOBILE_NUMBER + urlString,
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
    public void onClick(View v) {
        
        if(v.getId() == R.id.postjob_button) {
            Log.d("Posting a job", "");
            progressDialog.show();
            new HttpAsyncTask().execute(QUERY_URL_POST_PUT_JOB);
            queryJobsByCustomerMobileNumber(mobileNumber);
            progressDialog.dismiss();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) jsonAdapter.getItem(position);
        String jobId = jsonObject.optString("id","");
        Log.d("On Item Click****", "");
        Intent jobDetailIntent = new Intent(this, JobDetailActivity.class);
        jobDetailIntent.putExtra("jobId", jobId);
        startActivity(jobDetailIntent);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            GetJob getJob = new GetJob();
            Job job = null;
            try {
                job =  getJob.getNewJob();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("*******Job customer name"+job.getCustomerName(), "");
            job.setJobType(JobType.valueOf(jobTypeSpinnerSelectionValue));
            //job.setJobType(JobType.PLUMBING);
            job.setJobStatus(JobStatus.OPEN);
            job.setCustomerName(userName);
            job.setCustomerMobileNumber(Long.parseLong(mobileNumber, 10));
            job.setPincode(pincode);
            job.setDescription(jobDescriptionEditText.getText().toString());

            String result = "";

            try {
                result = POST(urls[0], job);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            queryJobsByCustomerMobileNumber(mobileNumber);
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