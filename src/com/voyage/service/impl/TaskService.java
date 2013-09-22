package com.voyage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyage.battle.enums.WhetherType;
import com.voyage.cache.CfgDataMgr;
import com.voyage.cache.CommonMgr;
import com.voyage.cache.UserInfo;
import com.voyage.constant.Const;
import com.voyage.dao.SysUserDailyTaskVOMapper;
import com.voyage.dao.SysUserExpenseVOMapper;
import com.voyage.dao.SysUserMapNodeVOMapper;
import com.voyage.dao.SysUserTaskVOMapper;
import com.voyage.dao.SysUserVOMapper;
import com.voyage.data.bo.CommonBO;
import com.voyage.data.bo.DailyTaskBO;
import com.voyage.data.bo.TaskBO;
import com.voyage.data.vo.CfgDailyTaskVO;
import com.voyage.data.vo.CfgLivenessVO;
import com.voyage.data.vo.CfgTaskVO;
import com.voyage.data.vo.SysUserDailyTaskVO;
import com.voyage.data.vo.SysUserTaskVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.enums.DailyTaskType;
import com.voyage.enums.ExpenseType;
import com.voyage.enums.IncomeType;
import com.voyage.enums.MoneyType;
import com.voyage.enums.TaskStateType;
import com.voyage.enums.TaskType;
import com.voyage.service.IRelationService;
import com.voyage.service.ITaskService;
import com.voyage.service.IUserService;

@Service
public class TaskService implements ITaskService {
	private final Logger logger = LoggerFactory.getLogger(TaskService.class);
	@Autowired
	private SysUserTaskVOMapper taskMapper;
	@Autowired
	private IUserService userService;
	@Autowired
	private SysUserExpenseVOMapper expenseMapper;
	@Autowired
	private SysUserMapNodeVOMapper mapNodeMapper;
	@Autowired
	private IRelationService relationService;
	@Autowired
	private SysUserDailyTaskVOMapper dailyTaskMapper;
	@Autowired
	private SysUserVOMapper userMapper;

	@Override
	public List<TaskBO> enter(int userId) throws Exception {
		//查找未领奖任务
		List<SysUserTaskVO> tasks = taskMapper.selectNotReceived(userId, TaskStateType.RECEIVED.getV());
		List<TaskBO> rt = new ArrayList<TaskBO>();
		TaskBO taskBO = null;
		if (tasks.size() > 0) {
			SysUserVO fullUserVO = userService.selectFullByPk(userId);
			for (SysUserTaskVO tmp : tasks) {
				taskBO = new TaskBO(tmp.getSutId(), tmp.getSutTaskId(), tmp.getSutState());
				rt.add(taskBO);
				//设置任务进度显示
				wrapTask(fullUserVO, taskBO);
			}
		}
		return rt;
	}

	@Override
	public Integer monitorTask(int userId, boolean notify, Set<TaskType> taskTypes) throws Exception {
		if (taskTypes == null || taskTypes.size() == 0)
			return null;
		//查找未完成的任务
		StringBuilder tTypes = new StringBuilder();
		for (TaskType t : taskTypes) {
			if (tTypes.length() > 0)
				tTypes.append(Const.COMMA_SEP);
			tTypes.append(t.getV());
		}
		SysUserVO fullUserVO = userService.selectFullByPk(userId);
		List<SysUserTaskVO> tasks = taskMapper.selectByStateAndTypes(fullUserVO.getSuId(), TaskStateType.NOT_FINISH.getV(), tTypes.toString());
		int n = 0;//可完成的
		for (SysUserTaskVO sutVO : tasks) {
			CfgTaskVO ctVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_TASK, sutVO.getSutTaskId());
			int x = 0;
			int y = 0;
			TaskType tt = TaskType.parse(ctVO.getCtType());
			y = ctVO.getCtRestrain();
			Integer counts = null;
			switch (tt) {
			case RECHARGE_GOLD: //累计充值金币
				x = fullUserVO.getSuGoldVO().getSugRecharge();
				break;
			case LEVEL_UP: //公主达到某级
				x = fullUserVO.getSuLevel();
				break;
			case MAP_PASS: //某副本通关
				counts = mapNodeMapper.selectCountByType(fullUserVO.getSuId(), ctVO.getCtRestrain(), WhetherType.YES.getV());
				x = (counts == null || counts < CfgDataMgr.getInstance().getNodesByType(ctVO.getCtRestrain()).size()) ? 0 : 1;
				y = 1;
				break;
			case GOLD_PEAK: //仓库金币峰值
				x = fullUserVO.getSuGoldVO().getSugStorePeak();
				break;
			case LOGIN_DAYS: //累计登录天数
				x = fullUserVO.getSuLoginDays();
				break;
			case RECHARGE_TIMES: //充值累计次数
				counts = expenseMapper.selectCountByType(fullUserVO.getSuId(), ExpenseType.RECHARGE.getV());
				x = (counts == null ? 0 : counts);
				break;
			case VIP_LEVEL_UP://VIP达到某级等级
				x = fullUserVO.getSuVipLevel();
				break;
			default:
				break;
			}
			if (x >= y) {//可完成的任务
				stepTask(sutVO.getSutId(), TaskStateType.FINISHED);
				n++;
			}
		}
		if (n > 0 && notify) {
			try {
				CommonBO cb = new CommonBO(fullUserVO.getSuId());
				cb.nt = n;
				relationService.sendCommon(cb);
			} catch (Exception e) {
				logger.debug("task finish: ignore send error", e);
			}
		}
		return n;
	}

	/**
	 * 设置任务进度显示
	 * */
	private void wrapTask(SysUserVO fullUserVO, TaskBO taskBO) throws Exception {
		CfgTaskVO ctVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_TASK, taskBO.getTaskId());
		int x = 0;
		int y = 0;
		if (taskBO.state != TaskStateType.NOT_FINISH.getV()) {
			if (ctVO.getCtType() == TaskType.MAP_PASS.getV()) {
				x = 1;
				y = 1;
			} else {
				x = ctVO.getCtRestrain();
				y = ctVO.getCtRestrain();
			}
		} else {//未完成的
			TaskType tt = TaskType.parse(ctVO.getCtType());
			y = ctVO.getCtRestrain();
			Integer counts = null;
			switch (tt) {
			case RECHARGE_GOLD: //累计充值金币
				x = fullUserVO.getSuGoldVO().getSugRecharge();
				break;
			case LEVEL_UP: //公主达到某级
				x = fullUserVO.getSuLevel();
				break;
			case MAP_PASS: //某副本通关
				x = 0;
				y = 1;
				break;
			case GOLD_PEAK: //仓库金币峰值
				x = fullUserVO.getSuGoldVO().getSugStorePeak();
				break;
			case LOGIN_DAYS: //累计登录天数
				x = fullUserVO.getSuLoginDays();
				break;
			case RECHARGE_TIMES: //充值累计次数
				counts = expenseMapper.selectCountByType(fullUserVO.getSuId(), ExpenseType.RECHARGE.getV());
				x = (counts == null ? 0 : counts);
				break;
			case VIP_LEVEL_UP://VIP达到某级等级
				x = fullUserVO.getSuVipLevel();
				break;
			default:
				break;
			}
		}
		taskBO.pro = getProgress(x, y);
	}

	/**
	 * 设置任务进度
	 * */
	/*private void monitorTask(SysUserVO fullUserVO, TaskBO taskBO) throws Exception {
		CfgTaskVO ctVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_TASK, taskBO.getTaskId());
		int x = 0;
		int y = 0;
		if (taskBO.state != TaskStateType.NOT_FINISH.getV()) {
			if (ctVO.getCtType() == TaskType.MAP_PASS.getV()) {
				x = 1;
				y = 1;
			} else {
				x = ctVO.getCtRestrain();
				y = ctVO.getCtRestrain();
			}
		} else {//当前未完成的,判断是否有可完成的并更新状态
			TaskType tt = TaskType.parse(ctVO.getCtType());
			y = ctVO.getCtRestrain();
			Integer counts = null;
			switch (tt) {
			case RECHARGE_GOLD: //累计充值金币
				x = fullUserVO.getSuGoldVO().getSugRecharge();
				break;
			case LEVEL_UP: //公主达到某级
				x = fullUserVO.getSuLevel();
				break;
			case MAP_PASS: //某副本通关
				counts = mapNodeMapper.selectCountByType(fullUserVO.getSuId(), ctVO.getCtRestrain(), WhetherType.YES.getV());
				x = (counts == null || counts < CfgDataMgr.getInstance().getNodesByType(ctVO.getCtRestrain()).size()) ? 0 : 1;
				y = 1;
				break;
			case GOLD_PEAK: //仓库金币峰值
				x = fullUserVO.getSuGoldVO().getSugStorePeak();
				break;
			case LOGIN_DAYS: //累计登录天数
				x = fullUserVO.getSuLoginDays();
				break;
			case RECHARGE_TIMES: //充值累计次数
				counts = expenseMapper.selectCountByType(fullUserVO.getSuId(), ExpenseType.RECHARGE.getV());
				x = (counts == null ? 0 : counts);
				break;
			case VIP_LEVEL_UP://VIP达到某级等级
				x = fullUserVO.getSuVipLevel();
				break;
			default:
				break;
			}
			if (x >= y) {//可完成的任务
				stepTask(taskBO.sutId, TaskStateType.FINISHED);
				taskBO.state = TaskStateType.FINISHED.getV();
			}
		}
		taskBO.pro = getProgress(x, y);
	}*/
	private String getProgress(int x, int y) {
		x = Math.min(x, y);
		return x + Const.LINUX_SEP + y;
	}

	@Override
	public void reap(int sutId) throws Exception {
		SysUserTaskVO sutVO = taskMapper.selectByPrimaryKey(sutId);
		if (sutVO == null || sutVO.getSutState() != TaskStateType.FINISHED.getV())
			return;
		CfgTaskVO ctVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_TASK, sutVO.getSutTaskId());
		userService.reapReward(IncomeType.TASK, sutVO.getSutUserId(), ctVO.getCtRewardId());
		//更新任务状态
		stepTask(sutId, TaskStateType.RECEIVED);
		//接新任务
		openNextTask(sutVO.getSutUserId(), ctVO);
	}

	private void openTask(int userId, CfgTaskVO ctVO) throws Exception {
		if (ctVO == null)
			return;

		SysUserTaskVO insertVO = new SysUserTaskVO();
		insertVO.setSutUserId(userId);
		insertVO.setSutTaskId(ctVO.getCtId());
		insertVO.setSutState(TaskStateType.NOT_FINISH.getV());
		insertVO.setSutType(ctVO.getCtType());
		taskMapper.insertSelective(insertVO);
	}

	@Override
	public void openNextTask(int userId, CfgTaskVO ctVO) throws Exception {
		if (ctVO == null || ctVO.getCtNextId() == Const.INFINITY)
			return;
		CfgTaskVO nextVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_TASK, ctVO.getCtNextId());
		openTask(userId, nextVO);
		//是否可完成任务
		Set<TaskType> taskTypes = new HashSet<TaskType>(1);
		taskTypes.add(TaskType.parse(nextVO.getCtType()));
		monitorTask(userId, true, taskTypes);
	}

	@Override
	public void openHeadTask(int userId) throws Exception {
		List<CfgTaskVO> taskHeads = CfgDataMgr.getInstance().getTaskHeads();
		for (CfgTaskVO head : taskHeads) {
			openTask(userId, head);
		}
	}

	@Override
	public void stepTask(int sutId, TaskStateType tst) throws Exception {
		SysUserTaskVO updateVO = new SysUserTaskVO(sutId);
		updateVO.setSutState(tst.getV());
		updateVO.setSutMtime(new Date());
		taskMapper.updateByPrimaryKeySelective(updateVO);
	}

	@Override
	public int countOfFinished(int userId) throws Exception {
		Integer n = taskMapper.selectCountByState(userId, TaskStateType.FINISHED.getV());
		return n == null ? 0 : n;
	}

	@Override
	public void openDaily(int userId) throws Exception {
		List<Integer> taskTypes = dailyTaskMapper.selectTypesByUserId(userId);
		List<Integer> inserts = new ArrayList<Integer>();//新增的类别
		DailyTaskType[] dtts = DailyTaskType.values();
		List<Integer> dttTypes = new ArrayList<Integer>(dtts.length);

		for (DailyTaskType dtt : dtts) {
			dttTypes.add(dtt.getV());
			if (!taskTypes.contains(dtt.getV())) {
				inserts.add(dtt.getV());
			}
		}
		SysUserDailyTaskVO sudtVO = new SysUserDailyTaskVO(userId);
		sudtVO.setSudtPro(0);
		sudtVO.setSudtTime(new Date());
		//新增的
		for (Integer tType : inserts) {
			sudtVO.setSudtType(tType);
			dailyTaskMapper.insertSelective(sudtVO);
		}
		StringBuilder sb = new StringBuilder();//删除的类别
		for (Integer tType : taskTypes) {
			if (!dttTypes.contains(tType)) {
				if (sb.length() != 0) {
					sb.append(Const.COMMA_SEP);
				}
				sb.append(tType);
			}
		}
		if (sb.length() > 0) {
			//删除不存在的类别
			dailyTaskMapper.deleteByTypes(userId, sb.toString());
		}
	}

	@Override
	public void resetAllDaily() throws Exception {
		SysUserDailyTaskVO sudtVO = new SysUserDailyTaskVO();
		sudtVO.setSudtPro(0);
		sudtVO.setSudtTime(new Date());
		dailyTaskMapper.resetAll(sudtVO);//重置所有日常任务
		userMapper.resetAllDaily();//重置所有活跃为未领取
	}

	@Override
	public void updateDailyProByOne(int userId, DailyTaskType dtt) throws Exception {
		SysUserDailyTaskVO updateVO = new SysUserDailyTaskVO(userId);
		updateVO.setSudtType(dtt.getV());
		updateVO.setSudtPro(1);
		dailyTaskMapper.updateProByOffset(updateVO);
		updateLiveness(userId, false);
	}

	/**
	 * 更新活跃度
	 * */
	public void updateLiveness(int userId, boolean reset) throws Exception {
		UserInfo ui = CommonMgr.getInstance().getUserInfo(userId);
		List<CfgDailyTaskVO> dailyTasks = CfgDataMgr.getInstance().getList(CfgDataMgr.KEY_CFG_DAILY_TASK);
		Map<Integer, Integer> typePros = getTypePros(dailyTaskMapper.selectByUserId(userId));
		int nLive = 0;//最新活跃度
		Integer count = null;
		for (CfgDailyTaskVO cdtVO : dailyTasks) {
			count = typePros.get(cdtVO.getCdtType());
			if (count != null && cdtVO.getCdtRestrain() <= count)
				nLive += cdtVO.getCdtLive();
		}
		if (reset) {//重置缓存中已领的liveness
			String suDaily = userMapper.selectByPk(userId).getSuDaily();
			if (!suDaily.equals(Const.NONE)) {
				String[] liveness = suDaily.split(Const.COMMA_SEP);
				List<Integer> uiLives = new ArrayList<Integer>(liveness.length);
				for (String liveId : liveness) {
					uiLives.add(Integer.parseInt(liveId));
				}
				ui.setLiveness(uiLives);
			}
		}
		int ndt = ui.getFinishedLiveness();//当前可领取的活跃奖励数(旧的）
		ui.setnLive(nLive);//更新缓存
		ndt = ui.getFinishedLiveness() - ndt;//偏移量
		if (ndt > 0) {
			try {
				CommonBO cb = new CommonBO(userId);
				cb.ndt = ndt;
				relationService.sendCommon(cb);
			} catch (Exception e) {
				logger.debug("daily task finish: ignore send error", e);
			}
		}
	}

	/**
	 * 获得各日常类别的进度
	 * */
	private Map<Integer, Integer> getTypePros(List<SysUserDailyTaskVO> dailyTasks) {
		Map<Integer, Integer> pros = new HashMap<Integer, Integer>();
		for (SysUserDailyTaskVO sudtVO : dailyTasks)
			pros.put(sudtVO.getSudtType(), sudtVO.getSudtPro());
		return pros;
	}

	@Override
	public void reapDaily(int userId, int clId) throws Exception {
		UserInfo ui = CommonMgr.getInstance().getUserInfo(userId);
		if (ui.getLiveness().contains(clId))
			throw new IllegalStateException("reap already");

		CfgLivenessVO clVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_LIVENESS, clId);

		if (ui.getnLive() < clVO.getClN())
			throw new IllegalStateException("liveness not enough");

		SysUserVO suVO = userService.selectByPk(userId);
		int nGold = clVO.getClA() + suVO.getSuLevel() * clVO.getClB();//奖励的金币数
		userService.incMoney(IncomeType.DAILY_TASK, userId, MoneyType.GOLD, nGold);

		//更新已领列表
		StringBuilder sb = new StringBuilder(clVO.getClId().toString());
		for (Integer n : ui.getLiveness()) {
			sb.append(Const.COMMA_SEP).append(n);
		}

		SysUserVO updateSuVO = new SysUserVO(userId);
		updateSuVO.setSuDaily(sb.toString());
		userMapper.updateByPrimaryKeySelective(updateSuVO);
		//更新缓存
		ui.getLiveness().add(clVO.getClId());
	}

	@Override
	public List<DailyTaskBO> enterDaily(int userId) throws Exception {
		List<DailyTaskBO> rt = new ArrayList<DailyTaskBO>();
		List<SysUserDailyTaskVO> sudts = dailyTaskMapper.selectByUserId(userId);
		for (SysUserDailyTaskVO sudtVO : sudts) {
			DailyTaskBO dtBO = new DailyTaskBO();
			dtBO.pro = sudtVO.getSudtPro();
			dtBO.taskType = sudtVO.getSudtType();
			rt.add(dtBO);
		}
		return rt;
	}

}
