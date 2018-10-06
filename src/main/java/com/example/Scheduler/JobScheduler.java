package com.example.Scheduler;

import java.util.TimeZone;
import java.util.UUID;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Job.QuartzJob;
import com.example.Model.JobModel;

public class JobScheduler {
	
	Logger logger = LoggerFactory.getLogger(JobScheduler.class);
	private JobDetail job;
	private JobDetail simpleJob;
	private JobModel jobModel;
	private CronTrigger trigger;
	private SimpleTrigger simpleTrigger;
	private Scheduler scheduler;
	public JobScheduler(JobModel jobModel) {
		this.jobModel = jobModel;
		String cron = "* * * ? * *";
		String timeZone = "Asia/Kolkata";
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
				.cronSchedule(cron)
				.inTimeZone(TimeZone.getTimeZone(timeZone));
		//Simple Trigger
		SimpleScheduleBuilder simple = SimpleScheduleBuilder.repeatSecondlyForever(5);
		this.simpleTrigger = TriggerBuilder.newTrigger()
		.withIdentity(UUID.randomUUID().toString())
		.startNow()
		.withSchedule(simple)
		.build();
		
		// Cron Trigger
		this.trigger = TriggerBuilder.newTrigger()
						.withIdentity(UUID.randomUUID().toString())
						.withSchedule(cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires())
						.build();
	}
	
	private void initializer() {
		//Cron Job
		this.job = JobBuilder.newJob(QuartzJob.class).withIdentity(UUID.randomUUID().toString())
				.build();
		this.job.getJobDataMap().put("data", this.jobModel);
		// Simple Job
		this.simpleJob = JobBuilder.newJob(QuartzJob.class).withIdentity(UUID.randomUUID().toString())
				.build();
		this.simpleJob.getJobDataMap().put("simpleJob", "Simple Job excuted: ");
		try {
			this.scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			//
			scheduler.scheduleJob(simpleJob, simpleTrigger);
			logger.info("Scheduler started :");
//			Store Job_name and Job_group to delete job in future
			
//			job.getKey().getName();
//			job.getKey().getGroup();
		} catch (SchedulerException e) {
			logger.error("error while starting scheduler");
		}
	}
	
	public void registerScheduler() {
		initializer();
	}
	
	// Delete Job 
	public synchronized void dispose() {
		try {
			this.scheduler = new StdSchedulerFactory().getScheduler();
			JobKey jobKey = new JobKey("job_new", "job_group");
			if(null != scheduler.getJobDetail(jobKey)) {
				logger.info("Deleting Job " +jobKey.getName());
				scheduler.deleteJob(jobKey);
			}
		} catch (SchedulerException e) {
			logger.error("error while deleting job");
		}
		
	}

}
