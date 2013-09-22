package com.voyage.facade.impl;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;
import com.voyage.constant.Const;
import com.voyage.data.bo.GoodsBO;
import com.voyage.facade.IBackpackFacade;
import com.voyage.service.IBackpackService;

@Controller
public class BackpackFacade implements IBackpackFacade {
	@Autowired
	private IBackpackService backpackService;

	@Override
	public void enter(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int subType = jo.getInt("subType");
		List<GoodsBO> goods = backpackService.enter(jo.getInt(Const.USERID), subType);
		response.writeJson(goods);
	}

	@Override
	public void move(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		backpackService.move(jo);
		response.writeJson(null);
	}

	@Override
	public void reapBreed(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		backpackService.reapBreed(jo);
		response.writeJson(null);
	}
}
