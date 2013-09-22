package com.voyage.facade.impl;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;
import com.voyage.cache.CommonMgr;
import com.voyage.cache.UserInfo;
import com.voyage.constant.Const;
import com.voyage.data.bo.DailyTaskBO;
import com.voyage.data.bo.TaskBO;
import com.voyage.facade.ITaskFacade;
import com.voyage.service.ITaskService;

@Controller
public class TaskFacade implements ITaskFacade {
	@Autowired
	private ITaskService taskService;

	@Override
	public void reap(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int sutId = jo.getInt("sutId");// 玩家任务表主键
		taskService.reap(sutId);
		enter(request, response);
	}

	@Override
	public void enter(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		List<TaskBO> taskList = taskService.enter(jo.getInt(Const.USERID));
		response.writeJson(taskList);
	}

	@Override
	public void reapDaily(IRequest request, IResponse response)
			throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int liveId = jo.getInt("liveId");// 活跃表主键
		taskService.reapDaily(jo.getInt(Const.USERID), liveId);
		response.writeJson(null);
	}

	@Override
	public void enterDaily(IRequest request, IResponse response)
			throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int userId = jo.getInt(Const.USERID);
		List<DailyTaskBO> dailyTaskList = taskService.enterDaily(userId);
		JSONObject rt = new JSONObject();
		UserInfo ui = CommonMgr.getInstance().getUserInfo(userId);
		rt.put("nLive", ui.getnLive());
		rt.put("rLiveIds", ui.getLiveness());
		rt.put("tasks", dailyTaskList);
		response.writeJson(rt);
	}
}
