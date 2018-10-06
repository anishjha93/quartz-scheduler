package com.example.Job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.example.Model.JobModel;

public class QuartzJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobs = context.getJobDetail().getJobDataMap();
		JobModel data = (JobModel) jobs.get("data");
		if(null != data) {			
			System.out.println("Name :"+ data.getName());
			System.out.println("Company :"+ data.getCompany());
		}
		//Simple Job
		if(null != jobs.getString("simpleJob")) {
			System.out.println(jobs.getString("simpleJob"));
		}
	}

}
