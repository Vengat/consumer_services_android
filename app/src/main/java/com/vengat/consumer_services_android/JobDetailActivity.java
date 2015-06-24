package com.vengat.consumer_services_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.vengat.consumer_services_android.model.Job;
import com.vengat.consumer_services_android.model.JobStatus;
import com.vengat.consumer_services_android.model.JobType;
import com.vengat.consumer_services_android.rest_classes.GetJob;
import com.vengat.consumer_services_android.rest_classes.PutJob;

import java.io.IOException;

/**
 * Created by vengat.r on 6/19/2015.
 */
public class JobDetailActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String QUERY_URL_GET_JOB_BY_ID = "http://localhost:8080/jobs/id/"; //It requires a further id value that would be appended
    private static final String CANCEL_JOB_URL = "";
    private TableLayout jobDetailTableLayout;
    private TableRow tableRow1, tableRow2, tableRow3, tableRow4, tableRow5, tableRow6, tableRow7, tableRow8, tableRow9;
    private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9;
    private Button cancel;
    private ProgressDialog progressDialog;
    private String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity which XML layout is right
        setContentView(R.layout.row_job_detail);

        // Enable the "Up" button for more navigation options
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        jobId = intent.getStringExtra("jobId");
        System.out.println("*********" + jobId + "**********");
        Log.d("", "Job id is"+jobId);
        jobDetailTableLayout = (TableLayout) findViewById(R.id.job_details);

        tableRow1 = (TableRow) findViewById(R.id.tableRow1);
        tableRow2 = (TableRow) findViewById(R.id.tableRow2);
        tableRow3 = (TableRow) findViewById(R.id.tableRow3);
        tableRow4 = (TableRow) findViewById(R.id.tableRow4);
        tableRow5 = (TableRow) findViewById(R.id.tableRow5);
        tableRow6 = (TableRow) findViewById(R.id.tableRow6);
        tableRow7 = (TableRow) findViewById(R.id.tableRow7);
        tableRow8 = (TableRow) findViewById(R.id.tableRow8);
        tableRow9 = (TableRow) findViewById(R.id.tableRow9);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView8 = (TextView) findViewById(R.id.textView8);
        textView9 = (TextView) findViewById(R.id.textView9);

        cancel = (Button) findViewById(R.id.cancel_job_button);
        cancel.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting your jobs");
        progressDialog.setCancelable(false);

        new HttpAsyncTask().execute(QUERY_URL_GET_JOB_BY_ID + jobId);


    }

    private class HttpAsyncTask extends AsyncTask<String, Void, Job> {
        @Override
        protected Job doInBackground(String... urls) {
            GetJob getJob = new GetJob();
            Job job = null;
            try {
                job =  getJob.getJobById(Long.parseLong(jobId));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("*******Job customer name" + job.getCustomerName(), "");
            return job;
        }

        @Override
        protected void onPostExecute(Job job) {
            Toast.makeText(getBaseContext(), "Data Received!", Toast.LENGTH_LONG).show();
            Log.d("*******Job customer name" + job.getCustomerName(), "");
            textView1.setText("Job Id "+ job.getId());
            JobType jobType = job.getJobType();
            textView2.setText("Job Type "+String.valueOf(jobType));
            JobStatus jobStatus = job.getJobStatus();
            textView3.setText("Job Status "+String.valueOf(jobStatus));
            textView4.setText("Service Provider "+job.getServiceProviderName());
            textView5.setText("Service Provider Phone "+ job.getServiceProviderMobileNumber());
            textView6.setText("Pincode "+job.getPincode());
            textView7.setText("Date initiated "+ job.getDateInitiated());
            textView8.setText("Date Done "+ job.getDateDone());
            textView9.setText("Description "+job.getDescription());

        }

    }

    private class CancelJobAsyncHttpTask extends AsyncTask<String, Void, Job> {

        @Override
        protected Job doInBackground(String... urls) {
            GetJob getJob = new GetJob();
            Job job = null;
            try {
                job =  getJob.getJobById(Long.parseLong(jobId));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                new PutJob().cancelJob(job);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("*******Job customer name" + job.getCustomerName(), "");
            return job;
        }

        @Override
        protected void onPostExecute(Job job) {
            Toast.makeText(getBaseContext(), "Data Received!", Toast.LENGTH_LONG).show();
            Log.d("*******Job customer name" + job.getCustomerName(), "");
            textView1.setText("Job Id "+ job.getId());
            JobType jobType = job.getJobType();
            textView2.setText("Job Type "+String.valueOf(jobType));
            JobStatus jobStatus = job.getJobStatus();
            textView3.setText("Job Status "+String.valueOf(jobStatus));
            textView4.setText("Service Provider "+job.getServiceProviderName());
            textView5.setText("Service Provider Phone "+ job.getServiceProviderMobileNumber());
            textView6.setText("Pincode "+job.getPincode());
            textView7.setText("Date initiated "+ job.getDateInitiated());
            textView8.setText("Date Done "+ job.getDateDone());
            textView9.setText("Description " + job.getDescription());

        }
    }


    @Override
    public void onClick(View v) {
        new CancelJobAsyncHttpTask().execute(CANCEL_JOB_URL);
    }
}
