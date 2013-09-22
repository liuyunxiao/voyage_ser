package com.voyage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyage.battle.enums.WhetherType;
import com.voyage.cache.CfgDataMgr;
import com.voyage.cache.RelationMgr;
import com.voyage.config.BattleConfig;
import com.voyage.constant.Const;
import com.voyage.constant.ErCode;
import com.voyage.dao.SysUserActivityMapVOMapper;
import com.voyage.dao.SysUserMapNodeVOMapper;
import com.voyage.dao.SysUserVOMapper;
import com.voyage.data.bo.ActivityMapBO;
import com.voyage.data.bo.BorrowUserBO;
import com.voyage.data.bo.MapNodeBO;
import com.voyage.data.bo.MapNodeTypeBO;
import com.voyage.data.bo.UserExtBO;
import com.voyage.data.vo.CfgActivityMapVO;
import com.voyage.data.vo.CfgMapNodeTypeVO;
import com.voyage.data.vo.CfgMapNodeVO;
import com.voyage.data.vo.SysUserActivityMapVO;
import com.voyage.data.vo.SysUserMapNodeVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.enums.GoodsType;
import com.voyage.enums.PassType;
import com.voyage.exception.NotifyException;
import com.voyage.service.IBackpackService;
import com.voyage.service.IMonsterService;

@Service
public class MonsterService implements IMonsterService {
	@Autowired
	private SysUserMapNodeVOMapper mapNodeMapper;
	@Autowired
	private SysUserVOMapper userMapper;
	private Map<Integer, BorrowUserBO> borrowMap = new HashMap<Integer, BorrowUserBO>(1);//玩家借的公主 ,key:玩家ID，value:被借公主
	@Autowired
	private IBackpackService backpackService;
	@Autowired
	private SysUserActivityMapVOMapper activityMapper;

	public List<MapNodeBO> enterNode(int userId, int cmntId) throws Exception {
		List<MapNodeBO> rt = new ArrayList<MapNodeBO>(1);
		List<SysUserMapNodeVO> sumnList = mapNodeMapper.selectByType(userId, cmntId);
		MapNodeBO mnBO = null;
		CfgMapNodeVO cmnVO = null;
		for (SysUserMapNodeVO tmp : sumnList) {
			cmnVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MAP_NODE, tmp.getSumnMapNodeId());
			mnBO = new MapNodeBO(cmnVO.getCmnId(), cmnVO.getCmnName(), cmnVO.getCmnLevel(), cmnVO.getCmnImg());
			mnBO.gold = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MONSTER, cmnVO.getCmnMonsterId()).getBattleCorpsGold();
			mnBO.state = tmp.getSumnState() == WhetherType.YES.getV() ? PassType.PASSED.getV() : PassType.NOT_PASS.getV();
			rt.add(mnBO);
		}
		return rt;
	}

	@Override
	public List<MapNodeTypeBO> enterNodeType(int userId) throws Exception {
		List<SysUserMapNodeVO> sumnList = mapNodeMapper.selectByUserId(userId);
		List<CfgMapNodeVO> cmnList = CfgDataMgr.getInstance().getList(CfgDataMgr.KEY_CFG_MAP_NODE);
		Map<Integer, Integer> cfgTypeCountMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> sysTypeCountMap = new HashMap<Integer, Integer>();
		for (CfgMapNodeVO cmnVO : cmnList) {
			addCount(cfgTypeCountMap, cmnVO.getCmnType(), true);
		}
		for (SysUserMapNodeVO sumnVO : sumnList) {
			addCount(sysTypeCountMap, sumnVO.getSumnMapNodeTypeId(), sumnVO.getSumnState() == WhetherType.YES.getV());
		}
		List<MapNodeTypeBO> rt = new ArrayList<MapNodeTypeBO>(1);
		MapNodeTypeBO tmp = null;
		Integer count = null;
		CfgMapNodeTypeVO cmntVO = null;
		for (SysUserMapNodeVO sumnVO : sumnList) {
			cmntVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MAP_NODE_TYPE, sumnVO.getSumnMapNodeTypeId());
			tmp = new MapNodeTypeBO(cmntVO.getCmntId(), cmntVO.getCmntName());
			count = sysTypeCountMap.get(cmntVO.getCmntId());
			tmp.state = count.intValue() == cfgTypeCountMap.get(cmntVO.getCmntId()) ? PassType.PASSED.getV() : PassType.NOT_PASS.getV();
			if (!rt.contains(tmp)) {
				rt.add(tmp);
			}
		}
		return rt;
	}

	/**
	 * 统计已通过的nodeTypeId的次数
	 * */
	private void addCount(Map<Integer, Integer> nodeMap, int nodeTypeId, boolean passed) throws Exception {
		int n = nodeMap.containsKey(nodeTypeId) ? nodeMap.get(nodeTypeId) : 0;
		if (passed)
			n++;
		nodeMap.put(nodeTypeId, n);
	}

	@Override
	public void openNextMapNode(int userId, int cmnId) throws Exception {
		CfgMapNodeVO nextNodeVO = null;
		if (cmnId == 0) {//开启第1个节点
			nextNodeVO = CfgDataMgr.getInstance().getList(CfgDataMgr.KEY_CFG_MAP_NODE).get(0);
		} else {
			SysUserMapNodeVO curVO = mapNodeMapper.selectByNodeId(userId, cmnId);
			if (curVO == null || curVO.getSumnState() == WhetherType.NO.getV())
				throw new NotifyException(ErCode.E401);
			nextNodeVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MAP_NODE,
					CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MAP_NODE, cmnId).getCmnNextId());
		}
		openMapNode(userId, nextNodeVO);
	}

	@Override
	public void openMapNode(int userId, CfgMapNodeVO nextNodeVO) throws Exception {
		if (nextNodeVO == null)
			return;
		SysUserMapNodeVO nextVO = mapNodeMapper.selectByNodeId(userId, nextNodeVO.getCmnId());
		if (nextVO != null)//已经开启
			return;
		nextVO = new SysUserMapNodeVO();
		nextVO.setSumnMapNodeId(nextNodeVO.getCmnId());
		nextVO.setSumnMapNodeTypeId(nextNodeVO.getCmnType());
		nextVO.setSumnUserId(userId);
		mapNodeMapper.insertSelective(nextVO);
	}

	@Override
	public boolean isNodeOpen(int userId, int cmnId) throws Exception {
		SysUserMapNodeVO sumnVO = mapNodeMapper.selectByNodeId(userId, cmnId);
		return sumnVO != null;
	}

	@Override
	public BorrowUserBO getBorrowUser(int userId) throws Exception {
		return borrowMap.get(userId);
	}

	@Override
	public BorrowUserBO ally(int userId, int otherId) throws Exception {
		if (userId == otherId)
			return null;
		SysUserVO otherVO = userMapper.selectByPk(otherId);
		BorrowUserBO borrowed = new BorrowUserBO(otherId, otherVO.getSuImg(), otherVO.getSuLevel() * BattleConfig.getInstance().getPrincessFactorExpand());
		borrowMap.put(userId, borrowed);//加成只跟等级有关
		return borrowed;
	}

	@Override
	public void breakAlliance(int userId) throws Exception {
		borrowMap.remove(userId);
	}

	@Override
	public List<UserExtBO> enterAlliance(int userId) throws Exception {
		List<UserExtBO> users = new ArrayList<UserExtBO>(1);
		List<SysUserVO> friendList = userMapper.selectOnlineFriend(userId, Const.N_ACTIVE);//好友列表
		List<SysUserVO> notFriendList = userMapper.selectNotFriend(userId, Const.N_ACTIVE);//非好友列表
		friendList.addAll(notFriendList);
		UserExtBO ue = null;
		for (SysUserVO suVO : friendList) {
			ue = new UserExtBO(suVO, true);
			ue.goods = backpackService.getAllGoods(suVO.getSuId(), GoodsType.ALL.getV());
			ue.setFriend((RelationMgr.getInstance().isFriend(userId, suVO.getSuId()) ? WhetherType.YES : WhetherType.NO).getV());
			users.add(ue);
		}
		return users;
	}

	@Override
	public List<ActivityMapBO> enterActivity(int userId) throws Exception {
		List<ActivityMapBO> rt = new ArrayList<ActivityMapBO>();
		List<SysUserActivityMapVO> actList = activityMapper.selectByUserId(userId);
		for (SysUserActivityMapVO suamVO : actList) {
			rt.add(new ActivityMapBO(suamVO.getSuamActivityMapId(), suamVO.getSuamN()));
		}
		return rt;
	}

	@Override
	public void openActivityMap(int userId, int level) throws Exception {
		List<CfgActivityMapVO> acts = CfgDataMgr.getInstance().getActivityMapByLevel(level);//该等级可以开启的活动FB列表
		if (acts.size() > 0) {
			List<SysUserActivityMapVO> actList = activityMapper.selectByUserId(userId);//已经开启的列表
			SysUserActivityMapVO insertVO = new SysUserActivityMapVO();
			insertVO.setSuamUserId(userId);
			for (CfgActivityMapVO camVO : acts) {
				insertVO.setSuamActivityMapId(camVO.getCamId());
				if (!actList.contains(insertVO))
					activityMapper.insertSelective(insertVO);
			}
		}
	}

	@Override
	public SysUserActivityMapVO selectByActivityId(int userId, int activityId) throws Exception {
		return activityMapper.selectByActivityId(userId, activityId);
	}

	@Override
	public void resetAllActivityMap() throws Exception {
		activityMapper.resetAllActivityMap();
	}
}
