package com.voyage.service;

import java.util.List;
import java.util.Set;

import com.voyage.data.bo.DailyTaskBO;
import com.voyage.data.bo.TaskBO;
import com.voyage.data.vo.CfgTaskVO;
import com.voyage.enums.DailyTaskType;
import com.voyage.enums.TaskStateType;
import com.voyage.enums.TaskType;

public interface ITaskService {
	/**
	 * 领取任务奖励
	 * */
	void reap(int sutId) throws Exception;

	/**
	 * 组装任务列表数据
	 * */
	List<TaskBO> enter(int userId) throws Exception;

	/**
	 * 开启任务头
	 * @param ctVO 为null时表示开启所有任务头
	 * */
	void openHeadTask(int userId) throws Exception;

	/**
	 * 开启下一个任务
	 * @param ctVO 当前任务
	 * */
	void openNextTask(int userId, CfgTaskVO ctVO) throws Exception;

	/**
	 * 更改任务状态
	 * */
	void stepTask(int sutId, TaskStateType tst) throws Exception;

	/**
	 * 已完成的任务数
	 * */
	int countOfFinished(int userId) throws Exception;

	/**
	 * 设置任务进度
	 * @param notify 是否通知前台
	 * @return 可完成的任务数
	 * */
	Integer monitorTask(int userId, boolean notify, Set<TaskType> taskTypes) throws Exception;

	/**
	 * 开启玩家日常任务(开启新的，删除不存在的)
	 * */
	void openDaily(int userId) throws Exception;

	/**
	 * 重置玩所有玩家家日常任务
	 * */
	void resetAllDaily() throws Exception;

	/**
	 * 更新玩家日常任务进度
	 * */
	void updateDailyProByOne(int userId, DailyTaskType dtt) throws Exception;

	/**
	 * 领取活跃奖励
	 * */
	void reapDaily(int userId, int clId) throws Exception;

	/**
	 * 组装日常任务列表数据
	 * */
	List<DailyTaskBO> enterDaily(int userId) throws Exception;

	/**
	 * 更新活跃度
	 * @param reset 是否重新设置已领的liveness
	 * */
	void updateLiveness(int userId, boolean reset) throws Exception;
}
