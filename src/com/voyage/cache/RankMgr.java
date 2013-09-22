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
import com.voyage.constant.Const;
import com.voyage.data.bo.RankBO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.service.IUserService;
import com.voyage.util.CloneUtil;

/**
 * 个人排行榜管理器
 * */
@Component
public class RankMgr {
	private final Logger logger = LoggerFactory.getLogger(RankMgr.class);
	protected Map<Integer, RankBO> rankMap = new HashMap<Integer, RankBO>();//玩家排名信息(key: userId,value: rankbo)
	protected List<RankBO> rankList = new ArrayList<RankBO>();//排名列表  rank=index+1

	protected Map<Integer, RankBO> rankMapBak = new HashMap<Integer, RankBO>();//玩家排名信息(key: userId,value: rankbo)
	protected List<RankBO> rankListBak = new ArrayList<RankBO>();//排名列表  rank=index+1
	protected final Object rankLock = new Object();
	@Autowired
	private IUserService userService;

	protected RankMgr() {
	}

	public static RankMgr getInstance() {
		return SpringContext.getInstance().getSpringContext().getBean(RankMgr.class);
	}

	@SuppressWarnings("unchecked")
	public synchronized void reload() throws Exception {
		logger.info("reload rank...");
		List<SysUserVO> users = userService.selectFullAll();
		for (SysUserVO user : users) {
			addRank(new RankBO(user, false));
		}
		setOffset();
		synchronized (rankLock) {
			//补上selectFullAll之后新增的角色
			for (Map.Entry<Integer, RankBO> rm : rankMap.entrySet()) {
				if (!rankMapBak.containsKey(rm.getKey())) {
					addNewRoleRank(rm.getValue(), rankMapBak, rankListBak);
				}
			}
			rankMap = (Map<Integer, RankBO>) CloneUtil.deepClone(rankMapBak);
			rankList = (List<RankBO>) CloneUtil.deepClone(rankListBak);
		}

		rankMapBak.clear();
		rankListBak.clear();
	}

	/**
	 * 设置offset
	 * */
	private void setOffset() {
		for (RankBO r : rankListBak) {
			r.offset = rankMap.containsKey(r.userId) ? (rankMap.get(r.userId).rank - r.rank) : 0;
		}
	}

	/**
	 * 添加新建角色时不进入排名
	 * */
	public void addNewRoleRank(RankBO rankBO) {
		synchronized (rankLock) {
			addNewRoleRank(rankBO, rankMap, rankList);
		}
	}

	protected void addNewRoleRank(RankBO rankBO, Map<Integer, RankBO> rankMap, List<RankBO> rankList) {
		rankBO.rank = rankList.size() + 1;
		rankMap.put(rankBO.userId, rankBO);
		rankList.add(rankBO);
	}

	/**
	 * 添加 一个排名
	 * */
	protected void addRank(RankBO rankBO) {
		rankMapBak.put(rankBO.userId, rankBO);
		addByOrder(rankBO, rankListBak);
	}

	/**
	 * 按顺序添加到rankListBak
	 * */
	private void addByOrder(RankBO rankBO, List<RankBO> rankListBak) {
		int rank = 0;//合适的排名
		for (RankBO r : rankListBak) {
			if (rank > 0) {//已找到位置
				r.rank += 1;
			} else {
				//查找合适的位置
				if (r.allGold < rankBO.allGold) {
					rank = r.rank;
					r.rank += 1;
				}
			}
		}
		rankBO.rank = rank > 0 ? rank : rankListBak.size() + 1;
		if (rank > 0) {
			rankListBak.add(rankBO.rank - 1, rankBO);
		} else {
			rankListBak.add(rankBO);
		}
	}

	/**
	 * 获得玩家排名信息
	 * */
	public RankBO getRank(int userId) {
		return rankMap.get(userId);
	}

	/**
	 * 返回排行榜: 排行前N名+userId前后各N名(包括userId)
	 * */
	@SuppressWarnings("unchecked")
	public List<RankBO> getRanks(int userId) {
		List<RankBO> ranks = null;
		Map<Integer, RankBO> rankMs = null;
		synchronized (rankLock) {
			ranks = (List<RankBO>) CloneUtil.deepClone(rankList);
			rankMs = (Map<Integer, RankBO>) CloneUtil.deepClone(rankMap);
		}
		List<RankBO> firstList = new ArrayList<RankBO>(ranks.subList(0, Math.min(ranks.size(), Const.RANK_FIRST_N)));
		int rank = rankMs.get(userId).rank;
		List<RankBO> aroundList = ranks.subList(Math.max(0, rank - 1 - Const.RANK_AROUND_N), Math.min(ranks.size(), rank + Const.RANK_AROUND_N));

		for (RankBO r : aroundList) {
			if (!firstList.contains(r))
				firstList.add(r);
		}
		return firstList;
	}
}