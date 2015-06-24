package com.vengat.consumer_services_android.rest_classes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vengat.consumer_services_android.model.Job;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by vengat.r on 6/17/2015.
 */
public class GetJob {

    private static final String QUERY_NEW_JOB_URL = "http://10.0.2.2:8080/jobs";

    private static final String QUERY_JOB_BY_ID = "http://10.0.2.2:8080/jobs/id/";//append the id

    private static final String QUERY_URL_GET_JOBS_BY_MOBILE_NUMBER = "http://10.0.2.2:8080/customers/jobs/mobileNumber/";

    private Job job;


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

    public Job getNewJob() throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(QUERY_NEW_JOB_URL);
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        String jsonResponse = stringBuffer.toString();
        Job job = objectMapper.readValue(jsonResponse, Job.class);
        return job;
    }

    public Job getJobById(long id) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(QUERY_JOB_BY_ID+id);
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        String jsonResponse = stringBuffer.toString();
        Job job = objectMapper.readValue(jsonResponse, Job.class);
        return job;
    }

    private void setJob(Job job) {
        this.job = job;
    }

    public Job getJob() {
        return this.job;
    }

}
