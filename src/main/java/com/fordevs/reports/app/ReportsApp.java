package com.fordevs.reports.app;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableBatchProcessing
@ComponentScan({"com.fordevs.config","com.fordevs.reader","com.fordevs.writer"})
@EnableScheduling
public class ReportsApp {
	@Autowired private Job job;
	@Autowired private JobLauncher jobLauncher;


	public static void main(String[] args) {
		SpringApplication.run(ReportsApp.class, args);
	}

	@Scheduled(cron = "0 */1 * * * ?")
	public void perform() throws Exception
	{
		JobParameters params = new JobParametersBuilder()
				.addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(job, params);
	}
}
