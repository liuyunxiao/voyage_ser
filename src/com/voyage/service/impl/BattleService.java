package com.voyage.service.impl;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyage.battle.bo.ReplayBO;
import com.voyage.battle.bo.RewardBO;
import com.voyage.battle.enums.BattleType;
import com.voyage.battle.enums.SideType;
import com.voyage.battle.enums.WhetherType;
import com.voyage.cache.CfgDataMgr;
import com.voyage.config.PingyangConfig;
import com.voyage.constant.Const;
import com.voyage.dao.SysUserActivityMapVOMapper;
import com.voyage.dao.SysUserBattleCorpsVOMapper;
import com.voyage.dao.SysUserBattleRecordVOMapper;
import com.voyage.dao.SysUserMapNodeVOMapper;
import com.voyage.dao.SysUserVOMapper;
import com.voyage.data.bo.BorrowUserBO;
import com.voyage.data.bo.CorpsImgBO;
import com.voyage.data.bo.MsgBO;
import com.voyage.data.vo.CfgActivityMapVO;
import com.voyage.data.vo.CfgCorpsVO;
import com.voyage.data.vo.CfgFallRateVO;
import com.voyage.data.vo.CfgMapNodeVO;
import com.voyage.data.vo.CfgMonsterVO;
import com.voyage.data.vo.RewardVO;
import com.voyage.data.vo.SysUserActivityMapVO;
import com.voyage.data.vo.SysUserBattleCorpsVO;
import com.voyage.data.vo.SysUserBattleRecordVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.enums.DailyTaskType;
import com.voyage.enums.IncomeType;
import com.voyage.enums.MoneyType;
import com.voyage.enums.MsgFormatType;
import com.voyage.enums.MsgType;
import com.voyage.enums.TaskType;
import com.voyage.service.IBattleService;
import com.voyage.service.ICorpsService;
import com.voyage.service.IMonsterService;
import com.voyage.service.IRelationService;
import com.voyage.service.ITaskService;
import com.voyage.service.IUserService;
import com.voyage.util.CommonUtil;

@Service
public class BattleService implements IBattleService {
	private final Logger logger = LoggerFactory.getLogger(BattleService.class);
	@Autowired
	private SysUserBattleCorpsVOMapper battleCorpsMapper;
	@Autowired
	private IUserService userService;
	@Autowired
	private SysUserVOMapper userMapper;

	@Autowired
	private ICorpsService corpsService;
	@Autowired
	private IMonsterService monsterService;
	@Autowired
	private SysUserMapNodeVOMapper mapNodeMapper;
	@Autowired
	private SysUserBattleRecordVOMapper battleRecordMapper;
	@Autowired
	private IRelationService relationService;
	@Autowired
	private ITaskService taskService;
	@Autowired
	private SysUserActivityMapVOMapper activityMapper;

	@Override
	public void setPkSingleReward(ReplayBO rb) throws Exception {
		int atkId = rb.atker.id;
		int defId = rb.defer.id;
		SysUserVO atkUser = userMapper.selectByPk(atkId);
		rb.reward.oldExp = CommonUtil.getExpBO(atkUser.getSuLevel(), atkUser.getSuExp());//起始经验

		List<SysUserBattleCorpsVO> atkCorpsList = new ArrayList<SysUserBattleCorpsVO>(1);
		int atkHireGold = getTotalHireGold(atkCorpsList, atkId);
		if (rb.winSide == SideType.LEFT.getV()) {
			List<SysUserBattleCorpsVO> defCorpsList = new ArrayList<SysUserBattleCorpsVO>(1);
			int defHireGold = getTotalHireGold(defCorpsList, defId);
			SysUserVO defUser = userMapper.selectFullByPk(defId);
			if (defHireGold >= atkHireGold) {
				rb.reward.gold = defHireGold;
			} else {
				int defLost = Math.min(new Double(PingyangConfig.getInstance().getPvpLossFactor() * (1 + (rb.defer.level - rb.atker.level) * 0.01)
						* (atkHireGold - defHireGold)).intValue(), defUser.getSuGoldVO().getSugStorage());//守方应失金币(有损失系数)
				rb.reward.gold = defHireGold + defLost;
				userService.decMoney(defId, MoneyType.GOLD, defLost);
			}
			rb.oppoReward.gold = -rb.reward.gold;//显示的金币损失包含阵容招募金

			rb.reward.gold = new Double(rb.reward.gold * PingyangConfig.getInstance().getPvpIncomeFactor()).intValue();//有收益系数

			userService.incMoney(IncomeType.BATTLE, atkId, MoneyType.GOLD, rb.reward.gold);//攻方获得金币
			setFallCorps(BattleType.PK_SINGLE, rb.reward, atkId, defUser.getSuLevel(), defCorpsList);

			Calendar now = Calendar.getInstance();
			now.add(Calendar.MINUTE,
					new Double(CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_VIP, defUser.getSuVipLevel()).getCvProtect() * 60).intValue());//转成分钟
			SysUserVO updateDefVO = new SysUserVO(defId);
			updateDefVO.setSuProtect(now.getTime());//防守者进入保护期
			userMapper.updateByPrimaryKeySelective(updateDefVO);
		} else {
			rb.reward.gold = -atkHireGold;//显示的金币损失包含阵容招募金
			rb.oppoReward.gold = new Double(atkHireGold * PingyangConfig.getInstance().getPvpIncomeFactor()).intValue();//有收益系数

			userService.incMoney(IncomeType.BATTLE, defId, MoneyType.GOLD, rb.oppoReward.gold);//守方获得金币
			setFallCorps(BattleType.PK_SINGLE, null, defId, atkUser.getSuLevel(), atkCorpsList);
		}
		corpsService.resetCorps(true, rb.winSide == SideType.LEFT.getV() ? defId : atkId);//清空阵型
		//设置最新经验
		SysUserVO newAtkUser = userMapper.selectByPk(atkId);
		rb.reward.newExp = CommonUtil.getExpBO(newAtkUser.getSuLevel(), newAtkUser.getSuExp());

		taskService.updateDailyProByOne(atkId, DailyTaskType.PVP);//PVP日常
	}

	/**设置掉落兵种*/
	private void setFallCorps(BattleType battleType, RewardBO reward, int atkId, int defLevel, List<SysUserBattleCorpsVO> defCorpsList) throws Exception {
		if (defCorpsList.size() == 0)
			return;
		//攻方可获得兵种
		CfgCorpsVO fallCorps = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS,
				defCorpsList.get(Const.RAND.nextInt(defCorpsList.size())).getSubcCorpsVO().getCcId());
		CfgFallRateVO cfrVO = CfgDataMgr.getInstance().getFallRate(battleType, fallCorps.getCcCorpsGradeVO().getCcgId(), defLevel);
		if (!(cfrVO != null && CommonUtil.isCrit(cfrVO.getCfrRate())))
			return;
		saveFallCorps(reward, atkId, fallCorps);
	}

	/**保存兵种*/
	private void saveFallCorps(RewardBO reward, int atkId, CfgCorpsVO fallCorps) throws Exception {
		boolean added = corpsService.addCorpsIfNotExist(atkId, fallCorps);
		if (added && reward != null) {
			reward.corps = new ArrayList<CorpsImgBO>(1);
			reward.corps.add(new CorpsImgBO(fallCorps.getCcId(), fallCorps.getCcName(), fallCorps.getCcImg(), fallCorps.getCcCorpsGradeVO().getCcgCorpsBg()));
		}
	}

	/**
	 * 计算阵容招募金
	 * */
	private int getTotalHireGold(List<SysUserBattleCorpsVO> wrapBattleCorps, int userId) throws Exception {
		int total = 0;
		List<SysUserBattleCorpsVO> battleCorps = battleCorpsMapper.selectBattle(userId);
		if (wrapBattleCorps != null && battleCorps.size() > 0)
			wrapBattleCorps.addAll(battleCorps);
		for (SysUserBattleCorpsVO tmp : battleCorps) {
			total += CommonUtil.getHireGold(tmp.getSubcMagicAtk() + tmp.getSubcSoma() + tmp.getSubcShortAtk() + tmp.getSubcShortDef());
		}
		return total;
	}

	@Override
	public void belayOff(int userId) throws Exception {
		SysUserVO updateVO = new SysUserVO(userId);
		updateVO.setSuProtect(new Date());
		userMapper.updateByPrimaryKeySelective(updateVO);
	}

	@Override
	public void setActivityMapReward(ReplayBO rb, int camId) throws Exception {
		int atkId = rb.atker.id;
		SysUserVO atkUser = userMapper.selectFullByPk(atkId);
		rb.reward.oldExp = CommonUtil.getExpBO(atkUser.getSuLevel(), atkUser.getSuExp());//起始经验
		int atkHireGold = atkUser.getSuGoldVO().getSugBattleCorps();

		if (rb.winSide == SideType.LEFT.getV()) {
			CfgActivityMapVO cmnVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_ACTIVITY_MAP, camId);
			List<RewardVO> rewards = cmnVO.getRewards();
			CfgMonsterVO cmVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MONSTER, cmnVO.getCamMonsterId());
			if (rewards != null) {
				RewardVO rd = rewards.get(Const.RAND.nextInt(rewards.size()));
				//金币奖励
				if (rd.getMinGold() >= 0 && rd.getMinGold() <= rd.getMaxGold()) {
					rb.reward.gold = Const.RAND.nextInt(rd.getMaxGold() - rd.getMinGold() + 1) + rd.getMinGold();
					userService.incMoney(IncomeType.BATTLE, atkId, MoneyType.GOLD, rb.reward.gold);
				}
				//兵种奖励
				if (rd.getCorpsId() > 0 && CommonUtil.isCrit(PingyangConfig.getInstance().getBigRateDrop())) {//大概率兵种
					List<SysUserBattleCorpsVO> fallCorpsList = new ArrayList<SysUserBattleCorpsVO>(1);
					SysUserBattleCorpsVO subcVO = new SysUserBattleCorpsVO();
					subcVO.setSubcCorpsVO(new CfgCorpsVO(rd.getCorpsId()));
					fallCorpsList.add(subcVO);
					setFallCorps(BattleType.ACTIVITY_MAP, rb.reward, atkId, cmnVO.getCamLevel(), fallCorpsList);
				} else {//怪物出战兵种
					setFallCorps(BattleType.ACTIVITY_MAP, rb.reward, atkId, cmnVO.getCamLevel(), cmVO.getBattleCorpsList());
				}
			}
			//更新挑战次数
			SysUserActivityMapVO suamUpdateVO = new SysUserActivityMapVO();
			suamUpdateVO.setSuamUserId(atkId);
			suamUpdateVO.setSuamActivityMapId(camId);
			suamUpdateVO.setSuamN(1);
			activityMapper.updateOffsetByUserId(suamUpdateVO);
			//对被借公主的奖励
			rewardBorrowUser(atkId, rb.atker.name);
		} else {
			rb.reward.gold = -atkHireGold;
			corpsService.resetCorps(true, atkId);//清空阵型
		}
		//设置最新经验
		SysUserVO newAtkUser = userMapper.selectByPk(atkId);
		rb.reward.newExp = CommonUtil.getExpBO(newAtkUser.getSuLevel(), newAtkUser.getSuExp());

		taskService.updateDailyProByOne(atkId, DailyTaskType.PVE);//PVE日常
		if (monsterService.getBorrowUser(atkId) != null) {
			taskService.updateDailyProByOne(atkId, DailyTaskType.BORROW);//借公主日常
		}
	}

	@Override
	public void setMonsterReward(ReplayBO rb, int cmnId) throws Exception {
		int atkId = rb.atker.id;
		SysUserVO atkUser = userMapper.selectFullByPk(atkId);
		rb.reward.oldExp = CommonUtil.getExpBO(atkUser.getSuLevel(), atkUser.getSuExp());//起始经验
		int atkHireGold = atkUser.getSuGoldVO().getSugBattleCorps();

		if (rb.winSide == SideType.LEFT.getV()) {
			CfgMapNodeVO cmnVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MAP_NODE, cmnId);
			List<RewardVO> rewards = cmnVO.getRewards();
			CfgMonsterVO cmVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MONSTER, cmnVO.getCmnMonsterId());
			if (rewards != null) {
				RewardVO rd = rewards.get(Const.RAND.nextInt(rewards.size()));
				//金币奖励
				if (rd.getMinGold() >= 0 && rd.getMinGold() <= rd.getMaxGold()) {
					rb.reward.gold = Const.RAND.nextInt(rd.getMaxGold() - rd.getMinGold() + 1) + rd.getMinGold();
					userService.incMoney(IncomeType.BATTLE, atkId, MoneyType.GOLD, rb.reward.gold);
				}
				//兵种奖励
				if (rd.getCorpsId() > 0 && CommonUtil.isCrit(PingyangConfig.getInstance().getBigRateDrop())) {//大概率兵种
					List<SysUserBattleCorpsVO> fallCorpsList = new ArrayList<SysUserBattleCorpsVO>(1);
					SysUserBattleCorpsVO subcVO = new SysUserBattleCorpsVO();
					subcVO.setSubcCorpsVO(new CfgCorpsVO(rd.getCorpsId()));
					fallCorpsList.add(subcVO);
					setFallCorps(BattleType.MONSTER, rb.reward, atkId, cmnVO.getCmnLevel(), fallCorpsList);
				} else {//怪物出战兵种
					setFallCorps(BattleType.MONSTER, rb.reward, atkId, cmnVO.getCmnLevel(), cmVO.getBattleCorpsList());
				}
			}
			//更改通过状态
			mapNodeMapper.updateState(atkId, cmnId, WhetherType.YES.getV());
			//开启新节点	
			monsterService.openNextMapNode(atkId, cmnId);

			//对被借公主的奖励
			rewardBorrowUser(atkId, rb.atker.name);

			Set<TaskType> taskTypes = new HashSet<TaskType>();//需要跟踪的任务
			taskTypes.add(TaskType.MAP_PASS);
			taskService.monitorTask(atkId, true, taskTypes);
		} else {
			rb.reward.gold = -atkHireGold;
			corpsService.resetCorps(true, atkId);//清空阵型
		}
		//设置最新经验
		SysUserVO newAtkUser = userMapper.selectByPk(atkId);
		rb.reward.newExp = CommonUtil.getExpBO(newAtkUser.getSuLevel(), newAtkUser.getSuExp());

		taskService.updateDailyProByOne(atkId, DailyTaskType.PVE);//PVE日常
		if (monsterService.getBorrowUser(atkId) != null) {
			taskService.updateDailyProByOne(atkId, DailyTaskType.BORROW);//借公主日常
		}
	}

	/**
	 * 对被借公主的奖励
	 * */
	private void rewardBorrowUser(int atkId, String atkName) throws Exception {
		BorrowUserBO borrowed = monsterService.getBorrowUser(atkId);
		if (borrowed != null) {
			Point zone = PingyangConfig.getInstance().getBorrowIncome();
			int bi = zone.x + Const.RAND.nextInt(zone.y - zone.x);
			userService.incMoney(IncomeType.DEFAULT, borrowed.userId, MoneyType.GOLD, bi);
			if (bi > 0) {
				try {
					relationService.sendMsg(new MsgBO(MsgType.SYSTEM.getV(), Const.SYSTEM_USER_ID, borrowed.userId, CommonUtil.getFormatMsg(
							MsgFormatType.BORROW, new Object[] { atkId, atkName, String.valueOf(bi) }), null));
				} catch (Exception e) {
					logger.debug("borrow: ignore send error", e);
				}
			}
		}
	}

	@Override
	public boolean isProtected(int defId) throws Exception {
		SysUserVO suVO = userMapper.selectByPk(defId);
		return suVO.getSuProtect().after(new Date());
	}

	@Override
	public List<SysUserBattleRecordVO> saveRecord(ReplayBO replay) throws Exception {
		List<SysUserBattleRecordVO> rt = new ArrayList<SysUserBattleRecordVO>(1);

		//保存新战斗记录
		SysUserBattleRecordVO insertVO = new SysUserBattleRecordVO();
		insertVO.setSubrReplayId(replay.battleId);
		insertVO.setSubrType(replay.battleType);
		insertVO.setSubrTime(new Date());
		insertVO.setSubrUserVO(new SysUserVO(replay.atker.id));
		insertVO.setSubrOppoId(replay.defer.id);
		insertVO.setSubrWinside(replay.winSide);
		insertVO.setSubrOppoGold(replay.oppoReward.getGold());
		battleRecordMapper.insertSelective(insertVO);
		//查找过期记录

		Integer limitId = battleRecordMapper.selectLimitId(replay.defer.id, replay.battleType, PingyangConfig.getInstance().getMaxBattleRecord());
		if (limitId != null) {
			rt = battleRecordMapper.selectOutDateRecords(replay.defer.id, replay.battleType, limitId);
			battleRecordMapper.deleteByLimitId(replay.defer.id, replay.battleType, limitId);
		}

		return rt;
	}
}
