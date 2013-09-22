package com.voyage.facade.impl;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;
import com.voyage.battle.entry.Battle;
import com.voyage.battle.enums.BattleType;
import com.voyage.constant.Const;
import com.voyage.data.bo.PkChallengeBO;
import com.voyage.data.bo.PkSingleBO;
import com.voyage.facade.IPkSingleFacade;
import com.voyage.service.IPkSingleService;

@Controller
public class PkSingleFacade implements IPkSingleFacade {
	@Autowired
	private IPkSingleService pkSingleService;

	@Override
	public void challenge(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		PkChallengeBO rt = new PkChallengeBO();
		rt.replay = Battle.getInstance().getReplay(BattleType.PK_SINGLE, jo);
		//if (rt.replay.winSide == SideType.LEFT.getV()) {
		rt.users = pkSingleService.enter(false, jo.getInt(Const.USERID));
		//}
		response.writeJson(rt);
	}

	@Override
	public void enter(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int oppoId = jo.has("oppoId") ? jo.getInt("oppoId") : jo.getInt(Const.USERID);
		List<PkSingleBO> rt = pkSingleService.enter(jo.has("oppoId"), oppoId);
		response.writeJson(rt);
	}
}
