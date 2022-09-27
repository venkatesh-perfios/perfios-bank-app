package com.perfiosbank.fixeddeposit;

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


public class ServerStateListener implements ServletContextListener {
	Scheduler scheduler;
	
	public ServerStateListener() throws SchedulerException {
	    scheduler = StdSchedulerFactory.getDefaultScheduler();
	}
    
	@Override
	public void contextInitialized(ServletContextEvent event) {
        try {
        	BasicConfigurator.configure();

            scheduler.start();
            
            JobDetail job = newJob(FixedDepositJob.class)
          	      .withIdentity("job1", "group1")
          	      .build();

            Trigger trigger = newTrigger()
            		.withIdentity("trigger1", "group1")
            		.startNow()
      	            .withSchedule(simpleSchedule()
      	            		.withIntervalInSeconds(86400)
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
