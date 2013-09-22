package com.voyage.facade.impl;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;
import com.common.socket.vo.ResultDataVO;
import com.voyage.cache.CommonMgr;
import com.voyage.cache.RelationMgr;
import com.voyage.constant.Const;
import com.voyage.data.bo.GiveBO;
import com.voyage.data.bo.MsgBO;
import com.voyage.data.bo.UserExtBO;
import com.voyage.enums.MsgType;
import com.voyage.facade.IRelationFacade;
import com.voyage.service.IRelationService;

@Controller
public class RelationFacade implements IRelationFacade {

	@Autowired
	private IRelationService relationService;

	@Override
	public void enter(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int page = jo.getInt("page");
		int userId = jo.getInt(Const.USERID);
		List<UserExtBO> rt = relationService.enter(userId, page);
		ResultDataVO rd = new ResultDataVO(0, rt);
		rd.setPageTotal(RelationMgr.getInstance().getRelationTotal(userId));
		response.writeTheJson(new JSONObject(rd));
	}

	@Override
	public void deal(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		relationService.deal(jo);//处理方式  (0:拒绝 1：同意  2：发出请求 3：删除)
		response.writeJson(null);
	}

	@Override
	public void enterGive(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int userId = jo.getInt(Const.USERID);
		int oppoId = jo.getInt("oppoId");//被赠送者ID
		response.writeJson(new GiveBO(CommonMgr.getInstance().getUserInfo(userId).getMaxGiveSendGold(), userId == oppoId ? 0 : CommonMgr.getInstance()
				.getUserInfo(oppoId).getMaxGiveReceiveGold()));
	}

	@Override
	public void give(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int userId = jo.getInt(Const.USERID);
		int oppoId = jo.getInt("oppoId");//被赠送者ID
		int gold = jo.getInt("gold");//赠送的金币数
		relationService.give(userId, oppoId, gold);
		enterGive(request, response);
	}

	@Override
	public void say(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int userId = jo.getInt(Const.USERID);
		int toId = jo.getInt("toId");//接收者ID
		String content = jo.getString("content");//内容

		relationService.sendMsg(new MsgBO(MsgType.PERSONAL.getV(), userId, toId, content,null));
		response.writeJson(null);
	}
}
