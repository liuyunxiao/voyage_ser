package com.voyage.schedule;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voyage.config.PingyangConfig;
import com.voyage.schedule.job.DailyResetJob;
import com.voyage.schedule.job.WeekResetJob;

/**
 *	CronExpression配置在JAVA代码中
 */
public class ScheduleDefault {
	private final Logger logger = LoggerFactory.getLogger(ScheduleDefault.class);
	private static ScheduleDefault instance = new ScheduleDefault();

	private ScheduleDefault() {
	}

	public static ScheduleDefault getInstance() throws Exception {
		return instance;
	}

	public void start() throws Exception {
		logger.info("register jobs...");
		ScheduleMgr.INSTANCE.start();
		//		String CROM_TEST = "0 38 14 * * ?"; // 每天下午14点38分触发

		/*		String CRON_FIVE_OCLOCK = "0 0 5 * * ?"; // 每天5点
				addJob(DailyResetJob.class, CRON_FIVE_OCLOCK);*/

		//		String CRON_ONE_MINUTE = "0 0/1 * * * ?"; // 每1分钟

		/*String CRON_FIVE_OCLOCK = "0 0 5 * * ?"; // 每天5点*/
		addJob(DailyResetJob.class, PingyangConfig.getInstance().getCrontabDailyReset());
		addJob(WeekResetJob.class, PingyangConfig.getInstance().getCrontabWeekReset());
	}

	private void addJob(Class<? extends Job> jobClazz, String cronExpression) throws Exception {
		JobDetail job = JobBuilder.newJob(jobClazz).withIdentity(jobClazz.getName()).build();
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClazz.getName()).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
				.build();
		ScheduleMgr.INSTANCE.addJob(job, trigger);
	}
}
