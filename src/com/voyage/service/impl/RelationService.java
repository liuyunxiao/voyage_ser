package com.voyage.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.socket.ResponseUtil;
import com.voyage.battle.enums.WhetherType;
import com.voyage.cache.CommonMgr;
import com.voyage.cache.MsgAttrKey;
import com.voyage.cache.RelationMgr;
import com.voyage.cache.UserInfo;
import com.voyage.constant.Const;
import com.voyage.constant.ErCode;
import com.voyage.constant.EventDef;
import com.voyage.dao.SysUserMsgVOMapper;
import com.voyage.dao.SysUserRelationVOMapper;
import com.voyage.dao.SysUserVOMapper;
import com.voyage.data.bo.CommonBO;
import com.voyage.data.bo.MsgBO;
import com.voyage.data.bo.UserExtBO;
import com.voyage.data.vo.SysUserMsgVO;
import com.voyage.data.vo.SysUserRelationVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.enums.DailyTaskType;
import com.voyage.enums.GoodsType;
import com.voyage.enums.IncomeType;
import com.voyage.enums.MoneyType;
import com.voyage.enums.MsgFormatType;
import com.voyage.enums.MsgType;
import com.voyage.enums.RelationDealType;
import com.voyage.exception.NotifyException;
import com.voyage.service.IBackpackService;
import com.voyage.service.IRelationService;
import com.voyage.service.ITaskService;
import com.voyage.service.IUserService;
import com.voyage.socket.SocketSessionManager;
import com.voyage.util.CommonUtil;

@Service
public class RelationService implements IRelationService {
	private final Logger logger = LoggerFactory.getLogger(RelationService.class);
	@Autowired
	private SysUserRelationVOMapper relationMapper;
	@Autowired
	private SysUserVOMapper userMapper;
	@Autowired
	private IUserService userService;
	@Autowired
	private IBackpackService backpackService;
	@Autowired
	private SysUserMsgVOMapper msgMapper;
	@Autowired
	private ITaskService taskService;

	@Override
	public List<UserExtBO> enter(int userId, int page) throws Exception {
		List<UserExtBO> users = new ArrayList<UserExtBO>(1);
		int pStart = (page - 1) * Const.PAGE_LIMIT;
		List<SysUserVO> friends = userMapper.selectFriend(userId, pStart, Const.PAGE_LIMIT);
		UserExtBO ue = null;
		for (SysUserVO suVO : friends) {
			ue = new UserExtBO(suVO, true);
			ue.setFriend(WhetherType.YES.getV());
			ue.goods = backpackService.getAllGoods(suVO.getSuId(), GoodsType.ALL.getV());
			users.add(ue);
		}
		return users;
	}

	@Override
	public void deal(JSONObject param) throws Exception {
		final String posmf = "dealmf";
		if (!param.has(posmf))
			return;
		JSONArray joa = param.getJSONArray(posmf);
		JSONObject jo = null;
		for (int i = 0; i < joa.length(); i++) {
			jo = joa.getJSONObject(i);
			deal(jo.getInt("todo"), param.getInt(Const.USERID), jo.getInt("oppoId"));
		}
	}

	private void deal(int todo, int userId, int oppoId) throws Exception {
		if (userId == oppoId)
			return;

		RelationDealType doType = RelationDealType.parse(todo);
		switch (doType) {
		case REFUSE:
			refuseOrAcceptFriend(userId, oppoId, false);
			break;
		case ACCEPT:
			refuseOrAcceptFriend(userId, oppoId, true);
			break;
		case APPLY:
			applyFriend(userId, oppoId);
			break;
		case DELETE:
			deleteFriend(userId, oppoId);
			break;
		}
	}

	/**
	 * 发送好友请求
	 * */
	private void applyFriend(int userId, int oppoId) throws Exception {
		SysUserRelationVO surVO = relationMapper.selectFriend(userId, oppoId);
		if (surVO != null)
			return;
		SysUserVO suVO = userMapper.selectByPk(userId);
		sendMsg(new MsgBO(MsgType.FRIEND_APPLY.getV(), userId, oppoId, suVO.getSuName(), null));

		taskService.updateDailyProByOne(userId, DailyTaskType.APPLY_FRIEND);//添加好友日常
	}

	/**
	 * 接受或拒绝好友请求
	 * */
	private void refuseOrAcceptFriend(int userId, int oppoId, boolean accept) throws Exception {
		if (accept) {
			SysUserRelationVO surVO = relationMapper.selectFriend(userId, oppoId);
			if (surVO != null)
				return;
			SysUserRelationVO insertVO = new SysUserRelationVO();
			//userId
			insertVO.setSurUserId(userId);
			insertVO.setSurOppoId(oppoId);
			relationMapper.insertSelective(insertVO);
			//oppoId
			insertVO.setSurUserId(oppoId);
			insertVO.setSurOppoId(userId);
			relationMapper.insertSelective(insertVO);
			//更新缓存
			RelationMgr.getInstance().addOrDeleteRelation(new MsgAttrKey(userId, oppoId), true);

			taskService.updateDailyProByOne(userId, DailyTaskType.APPLY_FRIEND);//添加好友日常
		}
	}

	private void deleteFriend(int userId, int oppoId) throws Exception {
		//userId
		relationMapper.deleteFriend(userId, oppoId);
		//oppoId
		relationMapper.deleteFriend(oppoId, userId);

		//更新缓存
		RelationMgr.getInstance().addOrDeleteRelation(new MsgAttrKey(userId, oppoId), false);
	}

	@Override
	public void give(int userId, int oppoId, int gold) throws Exception {
		if (userId == oppoId || gold <= 0)
			return;

		UserInfo sender = CommonMgr.getInstance().getUserInfo(userId);
		UserInfo receiver = CommonMgr.getInstance().getUserInfo(oppoId);
		//需要同步
		synchronized (CommonMgr.getInstance().giveGoldLock) {
			if (sender.getMaxGiveSendGold() < gold)
				throw new NotifyException(ErCode.E601);
			if (receiver.getMaxGiveReceiveGold() < gold)
				throw new NotifyException(ErCode.E602);
			userService.decMoney(userId, MoneyType.GOLD, gold);
			userService.incMoney(IncomeType.GIVE, oppoId, MoneyType.GOLD, gold);
			//更新到缓存
			sender.setMaxGiveSendGold(sender.getMaxGiveSendGold() - gold);
			receiver.setMaxGiveReceiveGold(receiver.getMaxGiveReceiveGold() - gold);
		}
		try {
			//通知前台
			sendMsg(new MsgBO(MsgType.SYSTEM.getV(), 0, oppoId, CommonUtil.getFormatMsg(MsgFormatType.GIVE_GOLD, new Object[] { userId, sender.getName(),
					String.valueOf(gold) }), null));//数字转成String用来去除数字格式
		} catch (Exception e) {
			logger.debug("give_gold: ignore send error", e);
		}

		taskService.updateDailyProByOne(userId, DailyTaskType.GIVE_GOLD);//赠送金币日常
	}

	@Override
	public List<SysUserRelationVO> selectAll() throws Exception {
		return relationMapper.selectAll();
	}

	@Override
	public List<MsgBO> getAllOfflineMsg(int userId, boolean clear) throws Exception {
		List<MsgBO> msgs = new ArrayList<MsgBO>(1);
		List<SysUserMsgVO> msgList = msgMapper.selectOffline(userId);//查找所有离线消息
		for (SysUserMsgVO sumVO : msgList) {
			msgs.add(new MsgBO(sumVO, Const.DEFAULT_TIME_FORMAT));
		}
		if (msgList.size() > 0 && clear)
			msgMapper.deleteOffline(userId, msgList.get(msgList.size() - 1).getSumId());//清空
		return msgs;
	}

	@Override
	public void sendMsg(MsgBO msgBO) {
		if (msgBO.fromId == msgBO.toId)
			return;
		CommonBO cb = new CommonBO(msgBO.toId);
		cb.getMsgs().add(msgBO);
		sendCommon(cb);
	}

	@Override
	public void sendCommon(CommonBO cb) {
		try {
			if (SocketSessionManager.INSTANCE.isOnLine(cb.userId)) {
				//通知前台
				ResponseUtil.notifyJson(cb.userId, EventDef.COMMON, cb);
			} else {//保存离线消息
				saveOfflineMsg(cb.msgs);
			}
		} catch (Exception e) {
			logger.debug("msg: ignore send error", e);
		}
	}

	@Override
	public void sendBrt(int userId) {
		CommonBO cb = new CommonBO(userId);
		cb.nb = 1;
		sendCommon(cb);
	}

	@Override
	public void saveOfflineMsg(List<MsgBO> msgs) {
		if (msgs == null || msgs.size() == 0)
			return;
		SysUserMsgVO insertVO = null;
		for (MsgBO mb : msgs) {
			insertVO = new SysUserMsgVO(mb);
			msgMapper.insertSelective(insertVO);
		}
	}
}
