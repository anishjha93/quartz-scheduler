package com.example.quartzscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.Model.JobModel;
import com.example.Scheduler.JobScheduler;

@SpringBootApplication
public class QuartzSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuartzSchedulerApplication.class, args);
		
		// 
		JobModel jobModel = new JobModel();
		jobModel.setName("Anish");
		jobModel.setCompany("Google");
		JobScheduler scheduler = new JobScheduler(jobModel);
		scheduler.registerScheduler();
	}
}
