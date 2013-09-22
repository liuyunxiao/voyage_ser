package com.voyage.schedule.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voyage.cache.CommonMgr;
import com.voyage.cache.RankMgr;

public class DailyResetJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.debug("exe DailyResetJob ...");
		try {
			CommonMgr.getInstance().resetUserInfos();
			RankMgr.getInstance().reload();
		} catch (Exception e) {
			logger.error(null, e);
		}
	}
}
