package com.voyage.data.bo;

/**
 * 任务列表单元
 * */
public class TaskBO {
	public int sutId;//玩家任务表主键
	public int taskId;//任务ID
	public int state;//任务状态(0:未完成，1：已完成)
	public String pro;//任务进度

	public TaskBO(int sutId, int taskId, int state) {
		super();
		this.sutId = sutId;
		this.taskId = taskId;
		this.state = state;
	}

	public String getPro() {
		return pro;
	}

	public void setPro(String pro) {
		this.pro = pro;
	}

	public int getSutId() {
		return sutId;
	}

	public void setSutId(int sutId) {
		this.sutId = sutId;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
