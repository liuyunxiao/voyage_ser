package com.voyage.data.bo;

/**
 * 日常任务类别的进度列表单元
 * */
public class DailyTaskBO {
	public int taskType;//任务类别
	public int pro;//当前进度值

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getPro() {
		return pro;
	}

	public void setPro(int pro) {
		this.pro = pro;
	}
}
