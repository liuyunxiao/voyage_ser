package com.voyage.facade.impl;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;
import com.voyage.battle.entry.Battle;
import com.voyage.battle.enums.BattleType;
import com.voyage.battle.enums.SideType;
import com.voyage.cache.CfgDataMgr;
import com.voyage.constant.Const;
import com.voyage.data.bo.ActChallengeBO;
import com.voyage.data.bo.ActivityMapBO;
import com.voyage.data.bo.BorrowUserBO;
import com.voyage.data.bo.MapNodeBO;
import com.voyage.data.bo.MapNodeTypeBO;
import com.voyage.data.bo.MonsterChallengeBO;
import com.voyage.data.bo.UserExtBO;
import com.voyage.data.vo.CfgMapNodeTypeVO;
import com.voyage.facade.IMonsterFacade;
import com.voyage.service.IMonsterService;

@Controller
public class MonsterFacade implements IMonsterFacade {
	@Autowired
	private IMonsterService monsterService;

	@Override
	public void challenge(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		MonsterChallengeBO rt = new MonsterChallengeBO();
		rt.replay = Battle.getInstance().getReplay(BattleType.MONSTER, jo);
		if (rt.replay.winSide == SideType.LEFT.getV()) {
			int cmnId = jo.getInt("cmnId");//关卡ID
			CfgMapNodeTypeVO cmntVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MAP_NODE_TYPE,
					CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MAP_NODE, cmnId).getCmnType());

			rt.nodes = monsterService.enterNode(jo.getInt(Const.USERID), cmntVO.getCmntId());
			rt.nodeTypes = monsterService.enterNodeType(jo.getInt(Const.USERID));
		}
		response.writeJson(rt);
	}

	@Override
	public void enterNodeType(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		List<MapNodeTypeBO> mnList = monsterService.enterNodeType(jo.getInt(Const.USERID));
		response.writeJson(mnList);
	}

	@Override
	public void enterNode(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int cmntId = jo.getInt("cmntId");//副本节点类别ID
		List<MapNodeBO> mnList = monsterService.enterNode(jo.getInt(Const.USERID), cmntId);
		response.writeJson(mnList);
	}

	@Override
	public void ally(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int otherId = jo.getInt("otherId");//被结盟玩家的ID
		BorrowUserBO borrowed = monsterService.ally(jo.getInt(Const.USERID), otherId);
		response.writeJson(borrowed);
	}

	@Override
	public void enterAlliance(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		List<UserExtBO> rt = monsterService.enterAlliance(jo.getInt(Const.USERID));
		response.writeJson(rt);
	}

	@Override
	public void challengeAct(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		ActChallengeBO ac = new ActChallengeBO();
		ac.replay = Battle.getInstance().getReplay(BattleType.ACTIVITY_MAP, jo);
		if (ac.replay.winSide == SideType.LEFT.getV() && ac.replay.reward.newExp.level > ac.replay.reward.oldExp.level
				&& CfgDataMgr.getInstance().getNextActivityMapLevel(ac.replay.reward.oldExp.level) <= ac.replay.reward.newExp.level) {//有新開啓的活動FB
			ac.acts = monsterService.enterActivity(jo.getInt(Const.USERID));
		}
		response.writeJson(ac);
	}

	@Override
	public void enterActivity(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		List<ActivityMapBO> rt = monsterService.enterActivity(jo.getInt(Const.USERID));
		response.writeJson(rt);
	}
}
