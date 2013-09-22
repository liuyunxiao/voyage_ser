package com.voyage.schedule;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 * ScheduleMgr包外不可见 
 */
class ScheduleMgr {
	public static ScheduleMgr INSTANCE = new ScheduleMgr();

	SchedulerFactory sf = new StdSchedulerFactory();
	private Scheduler scheduler;

	private ScheduleMgr() {
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public Date addJob(JobDetail jobDetail, Trigger trigger) {
		Date date = null;
		try {
			date = scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return date;
	}

	public void deleteJob(JobKey jobKey) {
		try {
			scheduler.deleteJob(jobKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkExists(JobKey jobKey) {
		boolean isExists = false;
		try {
			isExists = scheduler.checkExists(jobKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isExists;
	}

	public void start() throws SchedulerException {
		if (!scheduler.isStarted()) {
			scheduler.start();
		}
	}

	public void stop() throws SchedulerException {
		scheduler.shutdown();
	}

	/**
	 * 获取job trigger下次触发时间 
	 * @param triggerKey
	 */
	public Date getNextFireTime(String triggerKey) throws Exception {
		TriggerKey key = new TriggerKey(triggerKey);
		Trigger trigger = scheduler.getTrigger(key);
		if (trigger != null) {
			return trigger.getNextFireTime();
		}
		return null;
	}
}
