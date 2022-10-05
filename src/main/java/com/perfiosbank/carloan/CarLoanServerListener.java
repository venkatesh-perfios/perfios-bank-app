package com.perfiosbank.carloan;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.JobDetail;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

import org.apache.log4j.BasicConfigurator;

import static org.quartz.SimpleScheduleBuilder.*;


public class CarLoanServerListener implements ServletContextListener {
	Scheduler scheduler;
	
	public CarLoanServerListener() throws SchedulerException {
	    scheduler = StdSchedulerFactory.getDefaultScheduler();
	}
    
	@Override
	public void contextInitialized(ServletContextEvent event) {
        try {
        	BasicConfigurator.configure();

            scheduler.start();
            
            JobDetail job = newJob(CarLoanJob.class)
          	      .withIdentity("carLoanJob", "carLoanGroup")
          	      .build();

            Trigger trigger = newTrigger()
            		.withIdentity("carLoanTrigger", "carLoanGroup")
            		.startNow()
      	            .withSchedule(simpleSchedule()
      	            		.withIntervalInSeconds(20)
      	            		.repeatForever())
      	            .build();
            
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
			scheduler.shutdown();
		} catch (SchedulerException se) {
			se.printStackTrace();
		}
	}
}
