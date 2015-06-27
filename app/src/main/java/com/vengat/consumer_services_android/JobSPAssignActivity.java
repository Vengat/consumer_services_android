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
import com.vengat.consumer_services_android.model.ServiceProvider;
import com.vengat.consumer_services_android.rest_classes.GetJob;
import com.vengat.consumer_services_android.rest_classes.GetServiceProvider;
import com.vengat.consumer_services_android.rest_classes.PutJob;

import java.io.IOException;

/**
 * Created by vengat.r on 6/25/2015.
 */
public class JobSPAssignActivity extends ActionBarActivity implements View.OnClickListener  {

    //private static final String QUERY_URL_GET_JOB_BY_ID = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/jobs/id/"; //It requires a further id value that would be appended
    private static final String QUERY_URL_GET_JOB_BY_ID = "http://10.0.2.2:8080/jobs/id/";
    private static final String CANCEL_JOB_URL = "";
    private TableLayout jobDetailTableLayout;
    private TableRow tableRow1, tableRow2, tableRow3, tableRow4, tableRow5, tableRow6, tableRow7, tableRow8, tableRow9, tableRow10, tableRow11;
    private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10, textView11;
    private Button assign;
    private ProgressDialog progressDialog;
    private String jobId;
    private String mobileNumber;

    private ServiceProvider currentServiceProvider;

    private GetServiceProviderAsyncHttpTask getServiceProviderAsyncHttpTask;
    private AssignJobAsyncHttpTask assignJobAsyncHttpTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity which XML layout is right
        setContentView(R.layout.sp_row_job_detail);

        // Enable the "Up" button for more navigation options
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        jobId = intent.getStringExtra("jobId");
        mobileNumber = intent.getStringExtra("mobileNumber");
        System.out.println("*********" + jobId + "**********");
        Log.d("", "Job id is" + jobId);
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
        tableRow10 = (TableRow) findViewById(R.id.tableRow10);
        tableRow11 = (TableRow) findViewById(R.id.tableRow11);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView8 = (TextView) findViewById(R.id.textView8);
        textView9 = (TextView) findViewById(R.id.textView9);
        textView10 = (TextView) findViewById(R.id.textView10);
        textView11 = (TextView) findViewById(R.id.textView11);

        assign = (Button) findViewById(R.id.sp_assign_job_button);
        assign.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting your jobs");
        progressDialog.setCancelable(false);

        new HttpAsyncTask().execute(QUERY_URL_GET_JOB_BY_ID + jobId);
        getServiceProviderAsyncHttpTask = new GetServiceProviderAsyncHttpTask();
        getServiceProviderAsyncHttpTask.execute(CANCEL_JOB_URL);
        //assignJobAsyncHttpTask = new AssignJobAsyncHttpTask();
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
            textView10.setText("Customer Name"+ job.getCustomerName());
            textView11.setText("Customer Phone"+ job.getCustomerMobileNumber());
        }

    }

    private class GetServiceProviderAsyncHttpTask extends AsyncTask<String, Void, ServiceProvider> {

        @Override
        protected ServiceProvider doInBackground(String... params) {
            com.vengat.consumer_services_android.rest_classes.GetServiceProvider getServiceProvider = new GetServiceProvider();
            ServiceProvider sp = null;

            try {
                sp =  getServiceProvider.getServiceProvider(Long.parseLong(mobileNumber));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sp;
        }

        @Override
        protected void onPostExecute(ServiceProvider sp) {
            //List<ServiceProvider> serviceProviders = new ArrayList<>();
            //serviceProviders.add(sp);
            //AssignJobAsyncHttpTask assignJobAsyncHttpTask = new AssignJobAsyncHttpTask();
            currentServiceProvider = sp;
        }
    }

    private class AssignJobAsyncHttpTask extends AsyncTask<String, Void, Job> {

        @Override
        protected Job doInBackground(String... params) {
            Job job = null;
            try {
                if (currentServiceProvider == null) System.out.println("&&&&&&Current Service Provider is null");
                System.out.println("&&&&&jobId "+jobId);
                job = new PutJob().assignJob(Long.parseLong(jobId), currentServiceProvider);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.d("*******Job customer name AssignJobAsyncHttpTask" + job.getCustomerName(), "");
            return job;
        }

        @Override
        protected void onPostExecute(Job job) {
            Toast.makeText(getBaseContext(), "Data Received!", Toast.LENGTH_LONG).show();
            Log.d("*******Job customer name" + job.getCustomerName(), "");
            textView1.setText("Job Id " + job.getId());
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
            textView10.setText("Customer Name"+ job.getCustomerName());
            textView11.setText("Customer Phone"+ job.getCustomerMobileNumber());
        }
    }

    @Override
    public void onClick(View v) {
        System.out.println("Clicked Assign currentServiceProvider" + currentServiceProvider.getName());
        //GetServiceProviderAsyncHttpTask getServiceProviderAsyncHttpTask = new GetServiceProviderAsyncHttpTask(); //CANCEL_JOB_URL is a dummy value. The AsyncHttpTask should be changed
        //getServiceProviderAsyncHttpTask.execute(CANCEL_JOB_URL);
        assignJobAsyncHttpTask = new AssignJobAsyncHttpTask();
        if(getServiceProviderAsyncHttpTask .getStatus() == AsyncTask.Status.FINISHED) {
            assignJobAsyncHttpTask.execute("http://invalidurl.com");
        }
        System.out.println("Assigned");
    }
}
