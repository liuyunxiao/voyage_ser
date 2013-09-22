package com.voyage.facade.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;
import com.voyage.constant.Const;
import com.voyage.data.bo.SlotsBO;
import com.voyage.data.bo.SlotsPlayBO;
import com.voyage.facade.ISlotsFacade;
import com.voyage.service.ISlotsService;

@Controller
public class SlotsFacade implements ISlotsFacade {
	@Autowired
	private ISlotsService slotsService;

	@Override
	public void enter(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		SlotsBO rt = slotsService.enter(jo.getInt(Const.USERID));
		response.writeJson(rt);
	}

	@Override
	public void play(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		SlotsPlayBO rt = slotsService.play(jo);
		response.writeJson(rt);
	}

	@Override
	public void roll(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int nGold = jo.getInt("nGold");
		int big = jo.getInt("big");
		int hit = slotsService.roll(jo.getInt(Const.USERID), nGold, big);
		response.writeJson(hit);
	}

}
