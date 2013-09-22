package com.voyage.facade.impl;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;
import com.voyage.cache.CfgDataMgr;
import com.voyage.constant.Const;
import com.voyage.data.bo.CorpsItemBO;
import com.voyage.data.bo.CorpsUnitBO;
import com.voyage.data.vo.CfgCorpsVO;
import com.voyage.facade.ICorpsFacade;
import com.voyage.service.ICorpsService;
import com.voyage.service.IMonsterService;

@Controller
public class CorpsFacade implements ICorpsFacade {
	@Autowired
	private ICorpsService corpsService;
	@Autowired
	private IMonsterService monsterService;

	/*@Override
	public void takeOff(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		corpsService.takeOff(jo);
		enterCorps(request, response, false);
	}
	*/
	@Override
	public void enterCorps(IRequest request, IResponse response) throws Exception {
		enterCorps(request, response, true);
	}

	/**
	 * @enter 是否通过enterCorps(IRequest request, IResponse response)方法进入
	 * */
	private void enterCorps(IRequest request, IResponse response, boolean enter) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int userId = jo.getInt(Const.USERID);
		if (enter) {//enterCorps方法进入的需要清除借公主信息
			monsterService.breakAlliance(userId);
		}
		JSONObject rt = new JSONObject();
		List<CorpsUnitBO> curr = corpsService.enterCorps(userId);//当前阵型
		List<CorpsUnitBO> last = corpsService.getLastArmy(userId);//最近一次非空阵型
		rt.put("curr", curr);
		rt.put("last", last);
		response.writeJson(rt);
	}

	/*@Override
	public void resetCorps(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		corpsService.resetCorps(false, jo.getInt(Const.USERID));
		response.writeJson(null);
	}*/

	/*@Override
	public void hire(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		corpsService.hire(jo);
		enterCorps(request, response, false);
	}*/

	@Override
	public void addDot(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int sucId = jo.getInt("sucId");//玩家兵种表主键
		int shortAtk = jo.getInt("shortAtk");//物理攻击加点  
		int shortDef = jo.getInt("shortDef");//物理防御加点  
		int magicAtk = jo.getInt("magicAtk");//技能攻击加点  
		int soma = jo.getInt("soma");//体质加点    
		int corpsId = corpsService.addDot(sucId, shortAtk, shortDef, magicAtk, soma);
		CfgCorpsVO ccVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, corpsId);
		jo.put("corpsType", ccVO.getCcCorpsTypeVO().getCctId());
		enterDot(request, response);
	}

	@Override
	public void enterDot(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int corpsType = jo.getInt("corpsType");//兵种类别
		List<CorpsItemBO> items = corpsService.enterDot(jo.getInt(Const.USERID), corpsType);
		response.writeJson(items);
	}

	@Override
	public void resetDot(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int sucId = jo.getInt("sucId");//玩家兵种表主键
		int corpsId = corpsService.resetDot(sucId);
		CfgCorpsVO ccVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, corpsId);
		jo.put("corpsType", ccVO.getCcCorpsTypeVO().getCctId());
		enterDot(request, response);
	}

	@Override
	public void moveAndCaptain(IRequest request, IResponse response) throws Exception {
		corpsService.moveAndCaptain((JSONObject) request.getData());
		response.writeJson(null);
	}
}
