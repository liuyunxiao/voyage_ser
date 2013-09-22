package com.voyage.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voyage.SpringContext;
import com.voyage.config.PingyangConfig;
import com.voyage.data.bo.GoodsBO;
import com.voyage.data.vo.CfgGoodsVO;
import com.voyage.data.vo.SysUserBackpackVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.service.IBackpackService;
import com.voyage.service.IMonsterService;
import com.voyage.service.ITaskService;
import com.voyage.service.IUserService;

/**
 * 通用管理器
 * */
@Component
public class CommonMgr {
	private final Logger logger = LoggerFactory.getLogger(CommonMgr.class);
	private Map<Integer, UserInfo> userInfoMap = new HashMap<Integer, UserInfo>();//玩家信息的缓存 (key: userId)
	private Map<Integer, WrapBoolean> pkLocks = new HashMap<Integer, WrapBoolean>();//PK锁

	public final Object giveGoldLock = new Object();//赠送金币锁
	@Autowired
	private IUserService userService;
	@Autowired
	private IBackpackService bpService;
	@Autowired
	private ITaskService taskService;
	@Autowired
	private IMonsterService monsterService;

	private CommonMgr() {
	}

	public static CommonMgr getInstance() {
		return SpringContext.getInstance().getSpringContext().getBean(CommonMgr.class);
	}

	/**
	 * 获得玩家PK锁
	 * */
	public WrapBoolean getPkLock(int userId) {
		synchronized (pkLocks) {
			WrapBoolean rt = pkLocks.get(userId);
			if (rt == null) {
				rt = new WrapBoolean(false);
				pkLocks.put(userId, rt);
			}
			if (rt.getVa()) {
				return null;
			} else {
				rt.setVa(true);
				return rt;
			}
		}
	}

	/**
	 * 释放玩家PK锁
	 * */
	public void releasePkLock(int userId) {
		synchronized (pkLocks) {
			WrapBoolean rt = pkLocks.get(userId);
			if (rt != null && rt.getVa()) {
				rt.setVa(false);
			}
		}
	}

	public synchronized void reload() throws Exception {
		logger.info("reload common...");
		List<SysUserVO> users = userService.selectFullAll();
		initUserInfoMap(users);
	}

	private void initUserInfoMap(List<SysUserVO> users) throws Exception {
		UserInfo ui = null;
		for (SysUserVO user : users) {
			ui = new UserInfo(user);
			addUserInfo(ui);
		}
	}

	/**
	 * 加载货物相关
	 * */
	private void loadGoods(UserInfo ui) throws Exception {
		List<SysUserBackpackVO> bpList = bpService.selectAll(ui.getUserId());
		CfgGoodsVO cgVO = null;
		for (SysUserBackpackVO bp : bpList) {
			cgVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_GOODS, bp.getSubGoodsVO().getCgId());
			ui.getGoods().add(new GoodsBO(bp.getSubId(), bp.getSubPos(), cgVO));
		}
	}

	/**
	 * 重置可赠送金币相关
	 * */
	private void resetMaxGiveGold() throws Exception {
		List<UserInfo> uis = new ArrayList<UserInfo>(userInfoMap.values());
		synchronized (giveGoldLock) {
			for (UserInfo ui : uis) {
				ui.setMaxGiveSendGold(PingyangConfig.getInstance().getMaxGiveSendGold());
				ui.setMaxGiveReceiveGold(PingyangConfig.getInstance().getMaxGiveReceiveGold());
			}
		}
	}

	/**
	 * 重置玩家相关信息
	 * */
	public void resetUserInfos() throws Exception {
		logger.info("reset userinfo ...");
		//非同步信息
		List<UserInfo> uis = new ArrayList<UserInfo>(userInfoMap.values());
		for (UserInfo ui : uis) {
			ui.getFestivals().clear();//重置节假日相关
			resetLiveness(ui);//重置活跃奖励相关step1
		}
		taskService.resetAllDaily();//重置活跃奖励相关step2
		monsterService.resetAllActivityMap();//重围活动副本
		//同步信息
		resetMaxGiveGold();
	}

	/**
	 * 重置玩家活跃奖励相关-step1
	 * */
	private void resetLiveness(UserInfo ui) throws Exception {
		ui.getLiveness().clear();
		ui.setnLive(0);
	}

	public UserInfo getUserInfo(int userId) {
		return userInfoMap.get(userId);
	}

	/**
	 * 返回总玩家数(模糊且非同步版)
	 * 
	 * */
	public int getTotalOfUserInfo_UnLock() {
		return userInfoMap.size();
	}

	/**
	 * 添加一个玩家信息的缓存
	 * */
	public void addUserInfo(UserInfo ui) throws Exception {
		if (!userInfoMap.containsKey(ui.getUserId())) {
			loadGoods(ui);//加载已有货物
			userInfoMap.put(ui.getUserId(), ui);
			loadDaily(ui);//加载日常信息
		}
	}

	/**
	 * 加载日常信息
	 * */
	private void loadDaily(UserInfo ui) throws Exception {
		taskService.openDaily(ui.getUserId());//检测新增或删除的日常任务
		taskService.updateLiveness(ui.getUserId(), true);//设置活跃度
	}

	/**
	 * 添加 一个货物
	 * */
	public void addGoods(int userId, GoodsBO goodsBO) {
		userInfoMap.get(userId).getGoods().add(goodsBO);
	}

	/**
	 * 删除一个货物
	 * */
	public void delGoods(int userId, GoodsBO goodsBO) {
		userInfoMap.get(userId).getGoods().remove(goodsBO);
	}

	/**
	 * 移动货物位置
	 * */
	public void moveGoods(int userId, int subId, int pos) {
		GoodsBO oldPosGoods = null;
		//判断位置pos上是否有货物
		for (GoodsBO gBO : userInfoMap.get(userId).getGoods()) {
			if (gBO.pos == pos) {
				oldPosGoods = gBO;
				break;
			}
		}
		//移动到指定位置
		for (GoodsBO gBO : userInfoMap.get(userId).getGoods()) {
			if (gBO.subId == subId) {
				if (oldPosGoods != null)
					oldPosGoods.pos = gBO.pos;
				gBO.pos = pos;
				break;
			}
		}

	}

	/**
	 * 获得玩家的所有货物
	 * */
	public List<GoodsBO> getGoods(int userId) {
		return userInfoMap.get(userId).getGoods();
	}
}
