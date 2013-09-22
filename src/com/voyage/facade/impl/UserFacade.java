package com.voyage.facade.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.common.socket.ISocket;
import com.common.socket.SessionData;
import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;
import com.common.socket.vo.ResultDataVO;
import com.voyage.SpringContext;
import com.voyage.cache.CfgDataMgr;
import com.voyage.cache.CommonMgr;
import com.voyage.cache.RankMgr;
import com.voyage.config.PingyangConfig;
import com.voyage.constant.Const;
import com.voyage.constant.ErCode;
import com.voyage.constant.EventDef;
import com.voyage.data.bo.ConfigBO;
import com.voyage.data.bo.NewComerBO;
import com.voyage.data.bo.NoticeBO;
import com.voyage.data.bo.RankBO;
import com.voyage.data.bo.ServerBO;
import com.voyage.data.bo.SyncRsBO;
import com.voyage.data.bo.UserBO;
import com.voyage.data.bo.UserExtBO;
import com.voyage.data.bo.VipBO;
import com.voyage.data.vo.CfgCorpsVO;
import com.voyage.data.vo.CfgGoodsVO;
import com.voyage.data.vo.CfgRewardVO;
import com.voyage.data.vo.CfgVipVO;
import com.voyage.data.vo.SysAccountVO;
import com.voyage.data.vo.SysServerVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.enums.RewardType;
import com.voyage.enums.TaskType;
import com.voyage.exception.NotifyException;
import com.voyage.facade.IUserFacade;
import com.voyage.service.IDefendService;
import com.voyage.service.IRelationService;
import com.voyage.service.ITaskService;
import com.voyage.service.IUserService;
import com.voyage.socket.SocketSessionManager;
import com.voyage.util.CommonUtil;

@Controller
public class UserFacade implements IUserFacade {
	@Autowired
	private IUserService userService;
	@Autowired
	private IRelationService relationService;
	@Autowired
	private IDefendService defendService;
	@Autowired
	private ITaskService taskService;

	public static IUserFacade getInstance() {
		return SpringContext.getInstance().getSpringContext().getBean(IUserFacade.class);
	}

	@Override
	public void loginAccount(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();
		//版本验证
		String version = params.getString("version");
		if (!version.equals(PingyangConfig.getInstance().getVersion()))
			throw new NotifyException(ErCode.E2);
		String saUuid = params.has("saUuid") ? params.getString("saUuid") : null;
		String saName = params.has("saName") ? params.getString("saName") : null;
		String saPassword = params.has("saPassword") ? params.getString("saPassword") : null;

		SysAccountVO saVO = userService.loginAccount(saUuid, saName, saPassword);
		kickExist(saVO.getSaId());//踢掉已存在的

		//account socket
		response.getSocket().setAccountId(saVO.getSaId());
		SocketSessionManager.INSTANCE.addAccountSocket(saVO.getSaId(), response.getSocket());
		response.writeJson(saVO.getSaId());

	}

	@Override
	public void logout(Integer userId, Integer accountId) throws Exception {
		if (userId == null && accountId == null)
			return;
		if (userId != null) {
			userService.logout(userId);
			SocketSessionManager.INSTANCE.removeAccountSocket(CommonMgr.getInstance().getUserInfo(userId).getAccountId());
		} else {
			SocketSessionManager.INSTANCE.removeAccountSocket(accountId);
		}
	}

	@Override
	public void changePwd(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();
		String saName = params.getString("saName");
		String oldPwd = params.getString("oldPwd");
		String newPwd = params.getString("newPwd");
		userService.changePwd(saName, oldPwd, newPwd);
		response.writeJson(null);

	}

	@Override
	public void register(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();
		String saName = params.getString("saName");
		String saPassword = params.getString("saPassword");

		userService.register(null, saName, saPassword);
		response.writeJson(null);
	}

	@Override
	public void isExist(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();
		String saName = params.getString("saName");
		response.writeJson(userService.isExist(saName));
	}

	@Override
	public void login(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();
		int accountId = params.getInt("accountId");
		int serverId = params.getInt("serverId");
		SysUserVO suVO = userService.login(accountId, serverId);
		UserBO user = new UserBO(suVO);
		user.msgs = relationService.getAllOfflineMsg(suVO.getSuId(), true);
		user.nb = defendService.countOfUnread(suVO.getSuId());

		if (params.has(Const.NEW_COMER)) {//设置新手战报，create方法调用login时有效
			user.replay = PingyangConfig.getInstance().getNewerReplay();
		}

		Set<TaskType> taskTypes = new HashSet<TaskType>();
		for (TaskType tt : TaskType.values())
			taskTypes.add(tt);
		taskService.monitorTask(suVO.getSuId(), false, taskTypes);//刷新一下任务

		user.nt = taskService.countOfFinished(suVO.getSuId());
		user.ndt = CommonMgr.getInstance().getUserInfo(suVO.getSuId()).getFinishedLiveness();
		user.config = new ConfigBO();
		user.config.chatInterval = PingyangConfig.getInstance().getChatInterval();
		user.config.goodsSellFactor = PingyangConfig.getInstance().getGoodsSellFactor();
		//user socket
		response.getSocket().setUserId(suVO.getSuId());
		SocketSessionManager.INSTANCE.addUserSocket(suVO.getSuId(), response.getSocket(), accountId);
		response.writeJson(user);

	}

	/**
	 * 组装新手教程奖励
	 * */
	private NewComerBO getNewComerBO(int rewardId) throws Exception {
		NewComerBO newer = new NewComerBO(rewardId);
		CfgRewardVO newRewardVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_REWARD, rewardId);
		newer.gold = newRewardVO.getCrGold();
		String[] ids = null;
		//物品
		if (!newRewardVO.getCrGoods().equals(Const.NONE)) {
			ids = newRewardVO.getCrGoods().split(Const.COMMA_SEP);
			CfgGoodsVO cgVO = null;
			for (String id : ids) {
				cgVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_GOODS, Integer.parseInt(id));
				newer.goods.add(cgVO.getCgImg());
			}
		}
		//兵种
		if (!newRewardVO.getCrCorps().equals(Const.NONE)) {
			ids = newRewardVO.getCrCorps().split(Const.COMMA_SEP);
			CfgCorpsVO ccVO = null;
			for (String id : ids) {
				ccVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, Integer.parseInt(id));
				newer.goods.add(ccVO.getCcImg());
			}
		}
		return newer;
	}

	@Override
	public void create(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();
		int accountId = params.getInt("accountId");
		int cpId = params.getInt("cpId");
		String suName = params.getString("suName");
		userService.create(accountId, suName, cpId);
		params.put(Const.NEW_COMER, Const.NEW_COMER);//是个新手
		login(request, response);
	}

	@Override
	public void logoutAccount(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();
		logout(params.getInt(Const.USERID), null);
		response.writeJson(null);
	}

	@Override
	public void info(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();
		String idAlias = params.has("idAlias") ? params.getString("idAlias") : null;
		int oppoId = params.has("oppoId") ? params.getInt("oppoId") : params.getInt(Const.USERID);//查询指定用户（默认查自己）
		UserExtBO user = userService.info(params.getInt(Const.USERID), idAlias, oppoId);
		response.writeJson(user);
	}

	@Override
	public void changeInfo(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();
		String name = params.has("name") ? params.getString("name") : null;//玩家名
		String remark = params.has("remark") ? params.getString("remark") : null;//备注
		userService.changeInfo(params.getInt(Const.USERID), name, remark);
		response.writeJson(null);
	}

	@Override
	public void enterVip(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		VipBO vbo = new VipBO();
		SysUserVO suVO = userService.selectByPk(jo.getInt(Const.USERID));
		vbo.vipExp = CommonUtil.getVipExpBO(true, suVO.getSuVipLevel(), suVO.getSuVipExp());
		vbo.img = suVO.getSuImg();
		vbo.level = suVO.getSuLevel();
		List<CfgVipVO> vipList = CfgDataMgr.getInstance().getList(CfgDataMgr.KEY_CFG_VIP);
		for (CfgVipVO cvVO : vipList) {
			//XXX 过滤掉VIP0
			if (cvVO.getCvId() != Const.ZERO)
				vbo.vips.add(cvVO.getCvId());
		}
		response.writeJson(vbo);
	}

	@Override
	public void enterRank(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		List<RankBO> ranks = RankMgr.getInstance().getRanks(jo.getInt(Const.USERID));
		response.writeJson(ranks);
	}

	@Override
	public void multiInfo(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		List<UserExtBO> userList = userService.multiInfo(jo.getInt(Const.USERID), jo.getString("userIds"));
		response.writeJson(userList);
	}

	@Override
	public void getServers(IRequest request, IResponse response) throws Exception {
		List<ServerBO> servers = new ArrayList<ServerBO>(1);
		//XXX 替换成平台相关
		List<SysServerVO> serverList = CfgDataMgr.getInstance().getList(CfgDataMgr.KEY_SYS_SERVER);
		for (SysServerVO ssVO : serverList) {
			servers.add(new ServerBO(ssVO.getSsId(), ssVO.getSsName(), ssVO.getSsState()));
		}
		
		List<NoticeBO> notices = userService.getNotices();//公告列表
		JSONObject rt = new JSONObject();
		rt.put("servers", servers);
		rt.put("notices", notices);
		response.writeJson(rt);
	}

	@Override
	public void syncRs(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		String rsVer = jo.getString("rsVer");//资源版本
		SyncRsBO rt = new SyncRsBO(PingyangConfig.getInstance().getRsVer());
		if (!rsVer.equals(rt.rsVer)) {
			File dbFile = new File(PingyangConfig.getInstance().getCfgDb());
			if (dbFile.exists()) {
				rt.dbData = FileUtils.readFileToByteArray(dbFile);
			}
		}
		response.writeJson(rt);
	}

	private void kickExist(int accountId) throws Exception {
		//如果已经登录，则下线
		ISocket socket = SocketSessionManager.INSTANCE.getAccountSocket(accountId);
		if (socket != null) {
			SessionData sessionData = (SessionData) socket.getSession().getAttribute("sessionData");
			if (sessionData != null && (sessionData.userId != null || sessionData.accountId != null)) {
				logout(sessionData.userId, sessionData.accountId);
				//忽略sessionClosed中的处理
				sessionData.ignoreClosedDispose = true;
			}
			ResultDataVO rd = new ResultDataVO();
			rd.setEvent(EventDef.RELOGIN);
			socket.sendTheJson(new JSONObject(rd));
			socket.closeSocket();
		}
	}

	@Override
	public void rebind(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();
		int userId = params.getInt(Const.USERID);
		int accountId = CommonMgr.getInstance().getUserInfo(userId).getAccountId();
		kickExist(accountId);//踢掉已存在的

		//account socket
		response.getSocket().setAccountId(accountId);
		SocketSessionManager.INSTANCE.addAccountSocket(accountId, response.getSocket());
		//user socket
		response.getSocket().setUserId(userId);
		SocketSessionManager.INSTANCE.addUserSocket(userId, response.getSocket(), accountId);
		response.writeJson(null);
	}

	@Override
	public void getNew(IRequest request, IResponse response) throws Exception {
		List<CfgRewardVO> new_rewards = CfgDataMgr.getInstance().getRewardByType(RewardType.NEWCOMER.getV());
		NewComerBO newer = null;
		if (new_rewards.size() > 0) {
			CfgRewardVO newRewardVO = new_rewards.get(Const.RAND.nextInt(new_rewards.size()));//随机选一种
			newer = getNewComerBO(newRewardVO.getCrId());
		}
		response.writeJson(newer);
	}

	@Override
	public void reapNew(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();

		int rewardId = params.getInt("rewardId");
		userService.reapNew(params.getInt(Const.USERID), rewardId);
		response.writeJson(null);
	}

	@Override
	public void getFirst(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();
		List<CfgRewardVO> first_rewards = CfgDataMgr.getInstance().getRewardByType(RewardType.FIRST_CHARGE.getV());
		NewComerBO first = null;
		if (first_rewards.size() > 0) {
			CfgRewardVO firstRewardVO = first_rewards.get(0);
			first = getNewComerBO(firstRewardVO.getCrId());
		}
		int reap = userService.didFirstCharge(params.getInt(Const.USERID));
		JSONObject rt = new JSONObject();
		rt.put("reap", reap);
		if (first != null)
			rt.put("reward", new JSONObject(first));
		response.writeJson(rt);
	}

	@Override
	public void reapFirst(IRequest request, IResponse response) throws Exception {
		JSONObject params = (JSONObject) request.getData();
		int rewardId = params.getInt("rewardId");
		userService.reapFirst(params.getInt(Const.USERID), rewardId);
		response.writeJson(null);
	}
}
