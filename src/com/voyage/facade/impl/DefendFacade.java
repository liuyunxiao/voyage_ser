package com.voyage.facade.impl;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;
import com.voyage.battle.bo.ReplayBO;
import com.voyage.battle.thread.ReplayWriteThread;
import com.voyage.constant.Const;
import com.voyage.data.bo.BattleReportBO;
import com.voyage.facade.IDefendFacade;
import com.voyage.service.IDefendService;

@Controller
public class DefendFacade implements IDefendFacade {
	@Autowired
	private IDefendService defendService;

	@Override
	public void enterReport(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		List<BattleReportBO> rt = defendService.enterReport(jo.getInt(Const.USERID));
		response.writeJson(rt);
	}

	@Override
	public void getReport(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		String battleId = jo.getString("battleId");//战报ID
		ReplayBO replay = ReplayWriteThread.getInstance().getReplay(battleId);
		if (replay != null) {
			replay.cdSkip = 1;//1秒后出现
			replay.speedUp = 3;//提供3倍加速
		}
		response.writeJson(replay);
	}
}
