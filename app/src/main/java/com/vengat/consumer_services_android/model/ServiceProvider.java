package com.vengat.consumer_services_android.model;



import java.io.Serializable;
import java.util.List;
import java.util.Set;


public class ServiceProvider implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private long id;
	

	private long mobileNumber;
	

	private String name;
	

	private Set<JobType> jobTypes;
	

	private List<Job> jobs;
	
	protected ServiceProvider() {
		
	}
	
	public ServiceProvider(long mobileNumber, String name, Set<JobType> jobTypes) {
		this.mobileNumber = mobileNumber;
		this.name = name;
		this.jobTypes = jobTypes;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<JobType> getJobTypes() {
		return jobTypes;
	}

	public void setJobTypes(Set<JobType> jobTypes) {
		this.jobTypes = jobTypes;
	}

	public long getId() {
		return id;
	}
		

}
