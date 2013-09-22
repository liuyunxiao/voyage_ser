package com.voyage.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyage.SpringContext;
import com.voyage.battle.enums.WhetherType;
import com.voyage.cache.CfgDataMgr;
import com.voyage.cache.CommonMgr;
import com.voyage.cache.RankMgr;
import com.voyage.cache.RelationMgr;
import com.voyage.cache.UserInfo;
import com.voyage.config.PingyangConfig;
import com.voyage.constant.Const;
import com.voyage.constant.ErCode;
import com.voyage.dao.SysAccountVOMapper;
import com.voyage.dao.SysNoticeVOMapper;
import com.voyage.dao.SysUserBattleCorpsVOMapper;
import com.voyage.dao.SysUserExpenseVOMapper;
import com.voyage.dao.SysUserGoldVOMapper;
import com.voyage.dao.SysUserVOMapper;
import com.voyage.data.bo.CommonBO;
import com.voyage.data.bo.MsgBO;
import com.voyage.data.bo.NoticeBO;
import com.voyage.data.bo.RankBO;
import com.voyage.data.bo.UserExtBO;
import com.voyage.data.vo.CfgCorpsVO;
import com.voyage.data.vo.CfgFestivalVO;
import com.voyage.data.vo.CfgGoodsVO;
import com.voyage.data.vo.CfgPrincessVO;
import com.voyage.data.vo.CfgRewardVO;
import com.voyage.data.vo.CfgTimeZoneGiveVO;
import com.voyage.data.vo.CfgUserLevelVO;
import com.voyage.data.vo.CfgVipVO;
import com.voyage.data.vo.SysAccountVO;
import com.voyage.data.vo.SysNoticeVO;
import com.voyage.data.vo.SysUserBattleCorpsVO;
import com.voyage.data.vo.SysUserGoldVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.enums.DailyTaskType;
import com.voyage.enums.ExpenseType;
import com.voyage.enums.GoodsType;
import com.voyage.enums.IncomeType;
import com.voyage.enums.MoneyType;
import com.voyage.enums.MsgType;
import com.voyage.enums.NoticeType;
import com.voyage.enums.RewardType;
import com.voyage.enums.TaskType;
import com.voyage.exception.NotifyException;
import com.voyage.service.IBackpackService;
import com.voyage.service.ICorpsService;
import com.voyage.service.IMonsterService;
import com.voyage.service.IRelationService;
import com.voyage.service.ITaskService;
import com.voyage.service.IUserService;

@Service
public class UserService implements IUserService {
	@Autowired
	private SysUserVOMapper userMapper;
	@Autowired
	private SysAccountVOMapper accountMapper;
	@Autowired
	private SysUserBattleCorpsVOMapper battleCorpsMapper;
	@Autowired
	private SysUserGoldVOMapper goldMapper;
	@Autowired
	private IMonsterService monsterService;
	@Autowired
	private IBackpackService backpackService;
	@Autowired
	private ITaskService taskService;
	@Autowired
	private ICorpsService corpsService;
	@Autowired
	private IRelationService relationService;
	@Autowired
	private SysUserExpenseVOMapper expenseMapper;
	@Autowired
	private SysNoticeVOMapper noticeMapper;

	public static IUserService getInstance() {
		return SpringContext.getInstance().getSpringContext().getBean(IUserService.class);
	}

	@Override
	public void initTestUser(String saName, int num) throws Exception {
		for (int i = 1; i <= num; i++) {
			int saId = register(null, saName + i + "@" + saName + ".com", Const.DEFAULT_PWD_LOGIN).getSaId();
			create(saId, saName + i, 1, true);
		}
	}

	@Override
	public SysUserVO selectByPk(Integer suId) throws Exception {
		return userMapper.selectByPk(suId);
	}

	@Override
	public SysUserVO selectFullByPk(Integer suId) throws Exception {
		return userMapper.selectFullByPk(suId);
	}

	@Override
	public SysUserVO selectWrapByPk(Integer suId) throws Exception {
		SysUserVO suVO = userMapper.selectFullByPk(suId);
		return wrapUser(suVO);
	}

	/** 填充玩家信息及加成信息等 */
	private SysUserVO wrapUser(SysUserVO suVO) throws Exception {
		if (suVO == null || suVO.getSuId() == null)
			return null;
		fillUser(suVO);// 填充玩家信息
		setUserAdd(suVO);// 设置加成信息
		return suVO;
	}

	/** 填充玩家信息 */
	private void fillUser(SysUserVO suVO) throws Exception {
		if (suVO == null || suVO.getSuId() == null)
			return;
		suVO.setBattleCorpsList(battleCorpsMapper.selectBattle(suVO.getSuId()));
		//填充兵种
		for (SysUserBattleCorpsVO tmp : suVO.getBattleCorpsList()) {
			tmp.setSubcCorpsVO(CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, tmp.getSubcCorpsVO().getCcId()));
		}

	}

	/** 设置加成信息 */
	private void setUserAdd(SysUserVO suVO) {
		if (suVO == null)
			return;
	}

	@Override
	public SysAccountVO loginAccount(String saUuid, String saName, String saPassword) throws Exception {
		SysAccountVO accountVO = null;
		if (saUuid != null) {
			accountVO = accountMapper.selectByUuid(saUuid);
			//创建新账号
			if (accountVO == null) {
				accountVO = register(saUuid, saName, saPassword);
			}
		} else {
			accountVO = accountMapper.selectByName(saName);
			if (accountVO == null)
				throw new NotifyException(ErCode.E102);
			if (!saPassword.equals(accountVO.getSaPassword()))
				throw new NotifyException(ErCode.E103);
		}
		if (accountVO.getSaThaw() != null && accountVO.getSaThaw().after(new Date())) {
			throw new NotifyException(ErCode.E108);
		}
		return accountVO;
	}

	@Override
	public void logout(int userId) throws Exception {
		SysUserVO updateVO = new SysUserVO(userId);
		updateVO.setSuLogout(new Date());
		userMapper.updateByPrimaryKeySelective(updateVO);
	}

	@Override
	public void incMoney(IncomeType incomeType, int userId, MoneyType moneyType, int n) throws Exception {
		money(incomeType, userId, moneyType, Math.abs(n));
	}

	@Override
	public void decMoney(int userId, MoneyType moneyType, int n) throws Exception {
		money(null, userId, moneyType, -Math.abs(n));
	}

	/**
	 *货币相关操作
	 *@param n 变动数量（带符号）
	 * */
	private void money(IncomeType incomeType, int userId, MoneyType mt, int n) throws Exception {
		if (n == 0)
			return;
		SysUserVO suVO = userMapper.selectFullByPk(userId);
		SysUserGoldVO sugVO = suVO.getSuGoldVO();
		int rtCode = isMoneyEnough(mt, n, sugVO);
		if (rtCode != ErCode.E0) {
			throw new NotifyException(rtCode);
		}
		Set<TaskType> taskTypes = new HashSet<TaskType>();//需要跟踪的任务
		CommonBO cb = new CommonBO(userId);
		cb.gold = sugVO.getSugStorage() + n;//最新金币
		SysUserGoldVO updateVO = new SysUserGoldVO();
		updateVO.setSugUserId(userId);
		switch (mt) {
		case GOLD:
			updateVO.setSugStorage(n);
			break;
		default:
			break;
		}
		if (n > 0) {
			switch (incomeType) {
			case BATTLE: //战斗获得收益时需要计算升级
				updateVO.setSugBattle(n);
			case DAILY_TASK://日常任务获得收益时需要计算升级
				cb.level = tryUpLevel(userId, suVO.getSuLevel(), suVO.getSuExp(), n);
				if (cb.level != suVO.getSuLevel().intValue()) {
					taskTypes.add(TaskType.LEVEL_UP);
					monsterService.openActivityMap(userId, cb.level);//开启新的活动FB
				}
				break;
			case RECHARGE://充值需要计算VIP
				updateVO.setSugRecharge(n);
				cb.vipLevel = tryUpVipLevel(userId, suVO.getSuVipLevel(), suVO.getSuVipExp(), n);
				if (cb.vipLevel != suVO.getSuVipLevel().intValue())
					taskTypes.add(TaskType.VIP_LEVEL_UP);
				taskTypes.add(TaskType.RECHARGE_GOLD);
				taskTypes.add(TaskType.RECHARGE_TIMES);
				break;
			default:
				break;
			}
			//设置仓库金币峰值
			if (sugVO.getSugStorage() + n > sugVO.getSugStorePeak()) {
				updateVO.setSugStorePeak(sugVO.getSugStorage() + n);
				taskTypes.add(TaskType.GOLD_PEAK);
			}
		}
		goldMapper.updateOffsetByUserId(updateVO);

		cb.nt = taskService.monitorTask(userId, false, taskTypes);
		//通知前台
		relationService.sendCommon(cb);
	}

	/**
	 * VIP升级
	 * @param vipLevel VIP等级
	 *@param vipExp 累计经验
	 *@param n 增加的经验
	 * @return 最新等级
	 * */
	private int tryUpVipLevel(int userId, int vipLevel, int vipExp, int n) throws Exception {
		CfgVipVO curVip = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_VIP, vipLevel);
		if (curVip.getCvPerCost() == Const.INFINITY)//已满级
			return vipLevel;
		CfgVipVO uLvVO = CfgDataMgr.getInstance().getVipLevel(vipLevel, vipExp + n);
		if (uLvVO == null)
			return vipLevel;
		SysUserVO updateSuVO = new SysUserVO(userId);
		if (uLvVO.getCvId() > vipLevel) {
			updateSuVO.setSuVipLevel(uLvVO.getCvId());

		}
		updateSuVO.setSuVipExp(uLvVO.getCvPerCost() == Const.INFINITY ? uLvVO.getCvTotalCost() : (vipExp + n));
		userMapper.updateByPrimaryKeySelective(updateSuVO);
		return Math.max(vipLevel, uLvVO.getCvId());
	}

	/**
	 * 玩家升级
	 * @param level 当前等级
	 * @param exp 累计经验
	 *@param n 增加的经验
	 * @return 最新等级
	 * */
	private int tryUpLevel(int userId, int level, int exp, int n) throws Exception {
		CfgUserLevelVO curVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_USER_LEVEL, level);
		if (curVO.getCulPerCost() == Const.INFINITY)//已满级
			return level;
		CfgUserLevelVO uLvVO = CfgDataMgr.getInstance().getUserLevel(level, exp + n);
		if (uLvVO == null)
			return level;
		SysUserVO updateSuVO = new SysUserVO(userId);
		if (uLvVO.getCulId() > level) {
			updateSuVO.setSuLevel(uLvVO.getCulId());
		}
		updateSuVO.setSuExp(uLvVO.getCulPerCost() == Const.INFINITY ? uLvVO.getCulTotalCost() : (exp + n));
		userMapper.updateByPrimaryKeySelective(updateSuVO);
		return Math.max(uLvVO.getCulId(), level);
	}

	/**
	 * 判断货币是否充足
	 * */
	private int isMoneyEnough(MoneyType mt, int n, SysUserGoldVO sugVO) throws Exception {
		if (n >= 0)
			return ErCode.E0;
		int total = 0;
		int rtCode = 0;
		switch (mt) {
		case GOLD:
			rtCode = ErCode.E999;
			total = sugVO.getSugStorage();
			break;
		default:
			throw new NotifyException("illegal money type");
		}
		return total >= -n ? ErCode.E0 : rtCode;
	}

	@Override
	public SysAccountVO register(String saUuid, String saName, String saPassword) throws Exception {
		SysAccountVO accountVO = null;
		if (saUuid != null) {
			accountVO = accountMapper.selectByUuid(saUuid);
		} else {
			accountVO = accountMapper.selectByName(saName);
		}
		if (accountVO != null)
			throw new NotifyException(ErCode.E101);

		SysAccountVO insertAccountVO = new SysAccountVO();
		if (saUuid != null) {
			insertAccountVO.setSaUuid(saUuid);
		} else {
			insertAccountVO.setSaName(saName);
			insertAccountVO.setSaPassword(saPassword);
		}
		insertAccountVO.setSaCreate(new Date());
		accountMapper.insertSelective(insertAccountVO);
		return insertAccountVO;
	}

	@Override
	public int isExist(String saName) throws Exception {
		SysAccountVO accountVO = accountMapper.selectByName(saName);
		return accountVO == null ? ErCode.E102 : ErCode.E101;
	}

	@Override
	public void changePwd(String saName, String oldPwd, String newPwd) throws Exception {
		SysAccountVO accountVO = loginAccount(null, saName, oldPwd);
		SysAccountVO updateVO = new SysAccountVO();
		updateVO.setSaId(accountVO.getSaId());
		updateVO.setSaPassword(newPwd);
		accountMapper.updateByPrimaryKeySelective(updateVO);
	}

	@Override
	public SysUserVO login(int saId, int serverId) throws Exception {
		SysUserVO suVO = userMapper.selectByAccountId(saId);
		if (suVO == null)
			throw new NotifyException(ErCode.E107);
		SysUserVO updateSuVO = new SysUserVO(suVO.getSuId());
		Date now = new Date();
		Set<TaskType> taskTypes = new HashSet<TaskType>();//需要跟踪的任务
		if (!DateUtils.isSameDay(suVO.getSuLogin(), now)) {//累计登录天数(首次登录时已错开一天)
			updateSuVO.setSuLoginDays(suVO.getSuLoginDays() + 1);
			taskTypes.add(TaskType.LOGIN_DAYS);
		}
		taskService.updateDailyProByOne(suVO.getSuId(), DailyTaskType.LOGIN);//登录日常

		Integer zg = getZoneGiveGold(suVO.getSuLogin(), now);//分时间段登录奖励
		if (zg != null) {
			zg += CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_VIP, suVO.getSuVipLevel()).getCvDaySubsidy();//VIP额外赠送
			incMoney(IncomeType.DEFAULT, suVO.getSuId(), MoneyType.GOLD, zg);
		}

		festivalReward(suVO.getSuId(), now);//领取节假日奖励

		if (suVO.getSuNewer() == WhetherType.NO.getV() && suVO.getSuLoginDays() > 0) {//新手引导过程中意外退出,再次登录时补领新手奖励
			List<CfgRewardVO> new_rewards = CfgDataMgr.getInstance().getRewardByType(RewardType.NEWCOMER.getV());
			if (new_rewards.size() > 0) {
				CfgRewardVO newRewardVO = new_rewards.get(Const.RAND.nextInt(new_rewards.size()));//随机选一种
				reapReward(IncomeType.DEFAULT, suVO.getSuId(), newRewardVO.getCrId());
				updateSuVO.setSuNewer(WhetherType.YES.getV());
			}
		}
		//刷新登录时间
		updateSuVO.setSuLogin(now);
		userMapper.updateByPrimaryKeySelective(updateSuVO);
		taskService.monitorTask(suVO.getSuId(), false, taskTypes);//由UserBO.nt来提醒
		suVO = userMapper.selectFullByPk(suVO.getSuId());
		suVO.setZg(zg);
		return suVO;
	}

	/**
	 * 领取节假日奖励
	 * */
	private void festivalReward(int userId, Date now) throws Exception {
		List<CfgFestivalVO> fList = CfgDataMgr.getInstance().getFestivalByDate(now);
		if (fList.size() == 0)
			return;
		List<MsgBO> msgs = new ArrayList<MsgBO>(1);
		List<Integer> festivals = CommonMgr.getInstance().getUserInfo(userId).getFestivals();
		for (CfgFestivalVO cfVO : fList) {
			if (!festivals.contains(cfVO.getCfId())) {
				reapReward(IncomeType.DEFAULT, userId, cfVO.getCfRewardVO());
				festivals.add(cfVO.getCfId());
				msgs.add(new MsgBO(MsgType.SYSTEM.getV(), 0, userId, cfVO.getCfDesc().concat(cfVO.getCfRewardVO().getCrDesc()), null));
			}
		}
		//存为离线消息
		relationService.saveOfflineMsg(msgs);
	}

	/**
	 * 返回分时间段登录时系统送的钱
	 * */
	private Integer getZoneGiveGold(Date lastLogin, Date now) throws Exception {
		Integer gold = null;
		CfgTimeZoneGiveVO lastZg = null;
		CfgTimeZoneGiveVO nowZg = null;
		int nowHour24 = DateUtils.toCalendar(now).get(Calendar.HOUR_OF_DAY);
		if (!DateUtils.isSameDay(lastLogin, now)) {
			nowZg = CfgDataMgr.getInstance().getGive(nowHour24);
		} else {
			int lastHour24 = DateUtils.toCalendar(lastLogin).get(Calendar.HOUR_OF_DAY);
			if (lastHour24 != nowHour24) {
				lastZg = CfgDataMgr.getInstance().getGive(lastHour24);
				nowZg = CfgDataMgr.getInstance().getGive(nowHour24);
			}
		}
		if (nowZg != null && (lastZg == null || nowZg.getCtzgId() != lastZg.getCtzgId().intValue())) {//不同时间段
			gold = nowZg.getCtzgMin() + Const.RAND.nextInt(nowZg.getCtzgMax() - nowZg.getCtzgMin());
		}
		return gold;
	}

	@Override
	public Integer create(int saId, String suName, int cpId) throws Exception {
		return create(saId, suName, cpId, false);
	}

	/**
	 * @param isTest 生成测试账号
	 * */
	private Integer create(int saId, String suName, int cpId, boolean isTest) throws Exception {
		Integer nName = userMapper.selectCountByName(suName);
		if (nName != null && nName > 0)//不允许重名
			throw new NotifyException(ErCode.E106);

		SysAccountVO saVO = accountMapper.selectByPrimaryKey(saId);
		if (saVO == null)
			throw new NotifyException(ErCode.E102);
		SysUserVO suVO = userMapper.selectByAccountId(saId);
		if (suVO != null)//1个账号只允许建1个角色
			throw new NotifyException(ErCode.E106);
		// XXX 默认账号
		SysUserGoldVO suGoldVO = new SysUserGoldVO();
		suGoldVO.setSugStorage(0);
		goldMapper.insertSelective(suGoldVO);//创建玩家钱包
		CfgPrincessVO cpVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_PRINCESS, cpId);
		SysUserVO insertVO = new SysUserVO();
		insertVO.setSuAccountVO(saVO);
		insertVO.setSuContact(saVO.getSaName());
		insertVO.setSuName(suName);
		insertVO.setSuGoldVO(suGoldVO);
		insertVO.setSuLevel(1);
		insertVO.setSuCreate(new Date());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -Math.max(PingyangConfig.getInstance().getLiveHour() * 2 / 24, 1));//不在活跃离线玩家的范围内，且使登录时间至少错开一天（登录中有判断）
		insertVO.setSuLogout(cal.getTime());
		insertVO.setSuProtect(cal.getTime());
		cal.add(Calendar.MINUTE, -1);//处于离线状态
		insertVO.setSuLogin(cal.getTime());
		insertVO.setSuImg(cpVO.getCpImg());
		userMapper.insertSelective(insertVO);
		suGoldVO.setSugUserId(insertVO.getSuId());
		goldMapper.updateByPrimaryKeySelective(suGoldVO);
		//设置UserInfo
		CommonMgr.getInstance().addUserInfo(new UserInfo(insertVO));
		//建号初始物品
		List<CfgRewardVO> new_rewards = CfgDataMgr.getInstance().getRewardByType(RewardType.CREATE_ROLE.getV());
		for (CfgRewardVO crVO : new_rewards) {
			reapReward(IncomeType.DEFAULT, insertVO.getSuId(), crVO);
		}
		//新手教程奖励
		/*CfgRewardVO newRewardVO = null;
		new_rewards = CfgDataMgr.getInstance().getRewardByType(RewardType.NEWCOMER.getV());
		if (new_rewards.size() > 0) {
			newRewardVO = new_rewards.get(Const.RAND.nextInt(new_rewards.size()));//随机选一种
			reapReward(IncomeType.DEFAULT, insertVO.getSuId(), newRewardVO);
		}*/
		//XXX 测试开启所有账号
		if (isTest) {
			for (CfgCorpsVO corps : CfgDataMgr.getInstance().getList(CfgDataMgr.KEY_CFG_CORPS)) {
				corpsService.addCorpsIfNotExist(insertVO.getSuId(), corps);
			}
		}
		//开启第1个副本节点
		monsterService.openNextMapNode(insertVO.getSuId(), 0);

		//设置排名
		RankMgr.getInstance().addNewRoleRank(new RankBO(insertVO, true));

		//开启任务头
		taskService.openHeadTask(insertVO.getSuId());
		//开启日常任务
		taskService.openDaily(insertVO.getSuId());
		//开启活动FB
		monsterService.openActivityMap(insertVO.getSuId(), insertVO.getSuLevel());
		//设置别名
		SysUserVO updateUserVO = new SysUserVO(insertVO.getSuId());
		synchronized (Const.CREATE_ROLE_LOCK) {
			Integer n = userMapper.selectAllCount();//此时已包含本玩家
			updateUserVO.setSuIdAlias(makeIdAlias(n - 1));
			userMapper.updateByPrimaryKeySelective(updateUserVO);
			return null;//新手奖励已移到引导中
		}
	}

	/**
	 * 生成ID别名
	 * @param n 服务器总人数
	 * */
	private String makeIdAlias(int n) {
		return String.valueOf(new Double(Const.ID_ALIAS_BASE + (n / 4) * 12 + Math.pow(n % 4, 2)).intValue());
	}

	@Override
	public UserExtBO info(int userId, String idAlias, int oppoId) throws Exception {
		SysUserVO suVO = null;
		if (idAlias == null) {
			suVO = userMapper.selectFullByPk(oppoId);
		} else {
			suVO = userMapper.selectFullByIdAlias(idAlias);
		}
		if (suVO == null)
			throw new NotifyException(ErCode.E105);
		UserExtBO ueBO = new UserExtBO(suVO, true);
		ueBO.goods = backpackService.getAllGoods(suVO.getSuId(), GoodsType.ALL.getV());
		ueBO.friend = (RelationMgr.getInstance().isFriend(userId, suVO.getSuId()) ? WhetherType.YES : WhetherType.NO).getV();
		return ueBO;
	}

	@Override
	public void changeInfo(int userId, String name, String remark) throws Exception {
		if (name == null && remark == null)
			return;
		SysUserVO updateVO = new SysUserVO(userId);
		updateVO.setSuName(name);
		updateVO.setSuRemark(remark);
		userMapper.updateByPrimaryKeySelective(updateVO);
	}

	@Override
	public void reapReward(IncomeType rt, int userId, int rewardId) throws Exception {
		CfgRewardVO ctVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_REWARD, rewardId);
		reapReward(rt, userId, ctVO);
	}

	@Override
	public void reapReward(IncomeType rt, int userId, CfgRewardVO ctVO) throws Exception {
		if (ctVO == null)
			return;
		//金币
		if (ctVO.getCrGold() > 0)
			incMoney(rt, userId, MoneyType.GOLD, ctVO.getCrGold());
		String[] ids = null;
		//物品
		if (!ctVO.getCrGoods().equals(Const.NONE)) {
			ids = ctVO.getCrGoods().split(Const.COMMA_SEP);
			CfgGoodsVO cgVO = null;
			for (String id : ids) {
				cgVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_GOODS, Integer.parseInt(id));
				backpackService.addToBackpack(GoodsType.parse(cgVO.getCgType()), userId, cgVO);
			}
		}
		//兵种
		if (!ctVO.getCrCorps().equals(Const.NONE)) {
			ids = ctVO.getCrCorps().split(Const.COMMA_SEP);
			CfgCorpsVO ccVO = null;
			for (String id : ids) {
				ccVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, Integer.parseInt(id));
				corpsService.addCorpsIfNotExist(userId, ccVO);
			}
		}
	}

	@Override
	public List<SysUserVO> selectFullAll() throws Exception {
		return userMapper.selectFullAll();
	}

	@Override
	public List<UserExtBO> multiInfo(int userId, String userIds) throws Exception {
		if (userIds == null || userIds.trim().length() == 0)
			return null;

		List<UserExtBO> rt = new ArrayList<UserExtBO>(1);
		List<SysUserVO> userList = userMapper.selectFullByUserIds(userIds);
		UserExtBO ueBO = null;
		for (SysUserVO suVO : userList) {
			ueBO = new UserExtBO(suVO, true);
			ueBO.goods = backpackService.getAllGoods(suVO.getSuId(), GoodsType.ALL.getV());
			ueBO.friend = (RelationMgr.getInstance().isFriend(userId, suVO.getSuId()) ? WhetherType.YES : WhetherType.NO).getV();
			rt.add(ueBO);
		}
		return rt;
	}

	@Override
	public void gmSendRewardTo(String userIds, int rewardId) throws Exception {
		if (userIds == null || "".equals(userIds.trim()))
			throw new IllegalStateException("userIds empty,e.g: 1,2,3");
		List<Integer> userIdList = new ArrayList<Integer>(1);
		String[] users = userIds.trim().split(Const.COMMA_SEP);
		for (String user : users) {
			userIdList.add(Integer.parseInt(user));
		}
		gmSendReward(userIdList, rewardId);
	}

	@Override
	public void gmSendRewardToAll(int rewardId) throws Exception {
		List<Integer> userIds = userMapper.selectAllUserIds();
		gmSendReward(userIds, rewardId);
	}

	/**
	 * GM发送奖励到指定玩家
	 * */
	private void gmSendReward(List<Integer> userIds, int rewardId) throws Exception {
		if (userIds == null || userIds.size() == 0)
			return;
		CfgRewardVO ctVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_REWARD, rewardId);
		if (ctVO == null)
			throw new IllegalStateException("reward not exist");
		for (Integer userId : userIds) {
			reapReward(IncomeType.DEFAULT, userId, ctVO);
		}
	}

	@Override
	public void gmSayTo(String userIds, String content) throws Exception {
		if (content == null || "".equals(content.trim()))
			throw new IllegalStateException("content empty");
		if (userIds == null || "".equals(userIds.trim()))
			throw new IllegalStateException("userIds empty,e.g: 1,2,3");

		List<Integer> userIdList = new ArrayList<Integer>(1);
		String[] users = userIds.trim().split(Const.COMMA_SEP);
		for (String user : users) {
			userIdList.add(Integer.parseInt(user));
		}
		gmSay(userIdList, content);
	}

	@Override
	public void gmSayToAll(String content) throws Exception {
		if (content == null || "".equals(content.trim()))
			throw new IllegalStateException("content empty");

		List<Integer> userIds = userMapper.selectAllUserIds();
		gmSay(userIds, content);
	}

	private void gmSay(List<Integer> userIds, String content) throws Exception {
		if (userIds == null || userIds.size() == 0)
			return;
		if (content == null || "".equals(content.trim()))
			return;
		for (Integer userId : userIds) {
			//通知前台
			relationService.sendMsg(new MsgBO(MsgType.SYSTEM.getV(), 0, userId, content, null));
		}
	}

	@Override
	public void gmFreeze(String accountIds, Date thaw) throws Exception {
		if (accountIds == null || "".equals(accountIds.trim()))
			throw new IllegalStateException("accountIds empty,e.g: 1,2,3");
		List<Integer> accountIdList = new ArrayList<Integer>(1);
		String[] accounts = accountIds.trim().split(Const.COMMA_SEP);
		for (String account : accounts) {
			accountIdList.add(Integer.parseInt(account));
		}
		gmFreeze(accountIdList, thaw);
	}

	private void gmFreeze(List<Integer> accountIds, Date thaw) throws Exception {
		if (accountIds == null || accountIds.size() == 0)
			return;
		SysAccountVO saUpdateVO = new SysAccountVO();
		for (Integer accountId : accountIds) {
			saUpdateVO.setSaId(accountId);
			saUpdateVO.setSaThaw(thaw);
			accountMapper.updateByPrimaryKeySelective(saUpdateVO);
		}
	}

	@Override
	public void reapNew(int userId, int rewardId) throws Exception {
		CfgRewardVO ctVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_REWARD, rewardId);
		if (ctVO.getCrType() != RewardType.NEWCOMER.getV())
			throw new IllegalStateException("not a newer reward");

		SysUserVO suVO = selectByPk(userId);
		if (suVO.getSuNewer() == WhetherType.YES.getV())//已领取
			throw new IllegalStateException("you reap already");

		reapReward(IncomeType.DEFAULT, userId, rewardId);
		//更新领取状态
		SysUserVO updateVO = new SysUserVO(userId);
		updateVO.setSuNewer(WhetherType.YES.getV());
		userMapper.updateByPrimaryKeySelective(updateVO);
	}

	@Override
	public int didFirstCharge(int userId) throws Exception {
		Integer counts = expenseMapper.selectCountByType(userId, ExpenseType.RECHARGE.getV());
		return (counts == null || counts == 0 ? WhetherType.NO : WhetherType.YES).getV();
	}

	@Override
	public void reapFirst(int userId, int rewardId) throws Exception {
		CfgRewardVO ctVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_REWARD, rewardId);
		if (ctVO.getCrType() != RewardType.FIRST_CHARGE.getV())
			throw new IllegalStateException("not a first charge reward");

		SysUserVO suVO = selectByPk(userId);
		if (suVO.getSuFirstCharge() == WhetherType.YES.getV())//已领取
			throw new IllegalStateException("you reap already");

		reapReward(IncomeType.DEFAULT, userId, rewardId);
		//更新领取状态
		SysUserVO updateVO = new SysUserVO(userId);
		updateVO.setSuFirstCharge(WhetherType.YES.getV());
		userMapper.updateByPrimaryKeySelective(updateVO);
	}

	@Override
	public List<NoticeBO> getNotices() throws Exception {
		//删除过期的
		noticeMapper.deleteInvalid();
		//查找有效的
		List<SysNoticeVO> validList = noticeMapper.selectValid();
		List<NoticeBO> rt = new ArrayList<NoticeBO>(5);
		for (SysNoticeVO snVO : validList)
			rt.add(new NoticeBO(snVO));
		return rt;
	}

	@Override
	public void gmNotice(int type, String name, Date start, Date end, String content) throws Exception {
		if (NoticeType.parse(type) == null)
			throw new IllegalStateException("illegal type");
		if (name == null || "".equals(name.trim()))
			throw new IllegalStateException("name empty");
		if (start.after(end))
			throw new IllegalStateException("illegal date zone");
		if (content == null || "".equals(content.trim()))
			throw new IllegalStateException("content empty");
		noticeMapper.insertSelective(new SysNoticeVO(null, type, name, start, end, content));
	}
}
