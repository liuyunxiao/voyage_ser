package com.voyage.cache;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voyage.SpringContext;
import com.voyage.data.vo.SysUserRelationVO;
import com.voyage.service.IRelationService;

/**
 * 好友管理器
 * */
@Component
public class RelationMgr {
	private final Logger logger = LoggerFactory.getLogger(RelationMgr.class);
	private List<MsgAttrKey> relationList = new ArrayList<MsgAttrKey>();//好友关系缓存
	private final Object relationLock = new Object();//好友关系锁

	@Autowired
	private IRelationService relationService;

	private RelationMgr() {
	}

	public static RelationMgr getInstance() {
		return SpringContext.getInstance().getSpringContext().getBean(RelationMgr.class);
	}

	public synchronized void reload() throws Exception {
		logger.info("reload relation...");
		reloadRelation();

	}

	private void reloadRelation() throws Exception {
		List<SysUserRelationVO> relations = relationService.selectAll();
		for (SysUserRelationVO surVO : relations) {
			addOrDeleteRelation(new MsgAttrKey(surVO.getSurUserId(), surVO.getSurOppoId()), true);
		}
	}

	/**
	 * 是否好友
	 * */
	public boolean isFriend(int userId, int oppoId) {
		if (userId == oppoId)
			return true;
		return relationList.contains(new MsgAttrKey(userId, oppoId));
	}

	/**
	 * 获得玩家的好友总数
	 * */
	public int getRelationTotal(int userId) {
		return getRelation(userId).size();
	}

	/**
	 * 获得玩家的好友
	 * */
	public List<Integer> getRelation(int userId) {
		List<Integer> friends = new ArrayList<Integer>(1);
		List<MsgAttrKey> reList = new ArrayList<MsgAttrKey>(relationList);
		for (MsgAttrKey k : reList) {
			if (k.getuId1() == userId) {
				friends.add(k.getuId2());
			} else if (k.getuId2() == userId) {
				friends.add(k.getuId1());
			}
		}
		return friends;
	}

	/**
	 * 添加或删除好友
	 * */
	public void addOrDeleteRelation(MsgAttrKey msgKey, boolean isAdd) {
		synchronized (relationLock) {
			if (isAdd) {
				if (!relationList.contains(msgKey)) {
					relationList.add(msgKey);
				}
			} else {
				relationList.remove(msgKey);
			}
		}
	}
}
