package com.voyage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyage.dao.CfgActivityMapVOMapper;
import com.voyage.dao.CfgBuffVOMapper;
import com.voyage.dao.CfgCorpsGradeVOMapper;
import com.voyage.dao.CfgCorpsLevelVOMapper;
import com.voyage.dao.CfgCorpsTypeVOMapper;
import com.voyage.dao.CfgCorpsVOMapper;
import com.voyage.dao.CfgDailyTaskVOMapper;
import com.voyage.dao.CfgFallRateVOMapper;
import com.voyage.dao.CfgFestivalVOMapper;
import com.voyage.dao.CfgGoodsVOMapper;
import com.voyage.dao.CfgLivenessVOMapper;
import com.voyage.dao.CfgMapNodeTypeVOMapper;
import com.voyage.dao.CfgMapNodeVOMapper;
import com.voyage.dao.CfgMatchZoneVOMapper;
import com.voyage.dao.CfgMonsterVOMapper;
import com.voyage.dao.CfgMsgFormatVOMapper;
import com.voyage.dao.CfgPrincessVOMapper;
import com.voyage.dao.CfgRechargeVOMapper;
import com.voyage.dao.CfgRewardVOMapper;
import com.voyage.dao.CfgSceneVOMapper;
import com.voyage.dao.CfgSkillVOMapper;
import com.voyage.dao.CfgSlotsVOMapper;
import com.voyage.dao.CfgTaskVOMapper;
import com.voyage.dao.CfgTimeZoneGiveVOMapper;
import com.voyage.dao.CfgUserLevelVOMapper;
import com.voyage.dao.CfgVipVOMapper;
import com.voyage.dao.SysServerVOMapper;
import com.voyage.data.vo.CfgActivityMapVO;
import com.voyage.data.vo.CfgBuffVO;
import com.voyage.data.vo.CfgCorpsGradeVO;
import com.voyage.data.vo.CfgCorpsLevelVO;
import com.voyage.data.vo.CfgCorpsTypeVO;
import com.voyage.data.vo.CfgCorpsVO;
import com.voyage.data.vo.CfgDailyTaskVO;
import com.voyage.data.vo.CfgFallRateVO;
import com.voyage.data.vo.CfgFestivalVO;
import com.voyage.data.vo.CfgGoodsVO;
import com.voyage.data.vo.CfgLivenessVO;
import com.voyage.data.vo.CfgMapNodeTypeVO;
import com.voyage.data.vo.CfgMapNodeVO;
import com.voyage.data.vo.CfgMatchZoneVO;
import com.voyage.data.vo.CfgMonsterVO;
import com.voyage.data.vo.CfgMsgFormatVO;
import com.voyage.data.vo.CfgPrincessVO;
import com.voyage.data.vo.CfgRechargeVO;
import com.voyage.data.vo.CfgRewardVO;
import com.voyage.data.vo.CfgSceneVO;
import com.voyage.data.vo.CfgSkillVO;
import com.voyage.data.vo.CfgSlotsVO;
import com.voyage.data.vo.CfgTaskVO;
import com.voyage.data.vo.CfgTimeZoneGiveVO;
import com.voyage.data.vo.CfgUserLevelVO;
import com.voyage.data.vo.CfgVipVO;
import com.voyage.data.vo.SysServerVO;
import com.voyage.service.ICfgService;

@Service
public class CfgService implements ICfgService {
	@Autowired
	private CfgCorpsVOMapper corpsMapper;
	@Autowired
	private CfgCorpsTypeVOMapper corpsTypeMapper;
	@Autowired
	private CfgBuffVOMapper buffMapper;
	@Autowired
	private CfgSkillVOMapper skillMapper;
	@Autowired
	private CfgSceneVOMapper sceneMapper;
	@Autowired
	private CfgCorpsGradeVOMapper corpsGradeMapper;
	@Autowired
	private CfgMonsterVOMapper monsterMapper;
	@Autowired
	private CfgMatchZoneVOMapper matchMapper;
	@Autowired
	private CfgCorpsLevelVOMapper corpsLvMapper;
	@Autowired
	private CfgPrincessVOMapper princessMapper;
	@Autowired
	private SysServerVOMapper serverMapper;
	@Autowired
	private CfgUserLevelVOMapper userLvMapper;
	@Autowired
	private CfgVipVOMapper vipMapper;
	@Autowired
	private CfgFallRateVOMapper fallMapper;
	@Autowired
	private CfgMapNodeTypeVOMapper mapNodeTypeMapper;
	@Autowired
	private CfgMapNodeVOMapper mapNodeMapper;
	@Autowired
	private CfgRechargeVOMapper rechargeMapper;
	@Autowired
	private CfgGoodsVOMapper goodsMapper;
	@Autowired
	private CfgTaskVOMapper taskMapper;
	@Autowired
	private CfgRewardVOMapper rewardMapper;
	@Autowired
	private CfgTimeZoneGiveVOMapper zoneMapper;
	@Autowired
	private CfgMsgFormatVOMapper msgMapper;
	@Autowired
	private CfgFestivalVOMapper festivalMapper;

	@Autowired
	private CfgDailyTaskVOMapper dailyTaskMapper;
	@Autowired
	private CfgLivenessVOMapper livenessMapper;
	@Autowired
	private CfgSlotsVOMapper slotsMapper;
	@Autowired
	private CfgActivityMapVOMapper activityMapper;

	@Override
	public List<CfgCorpsGradeVO> selectAllCfgCorpsGradeVO() throws Exception {
		return corpsGradeMapper.selectAll();
	}

	@Override
	public List<CfgSceneVO> selectAllCfgSceneVO() throws Exception {
		return sceneMapper.selectAll();
	}

	@Override
	public List<CfgCorpsVO> selectAllCfgCorpsVO() throws Exception {
		return corpsMapper.selectAll();
	}

	@Override
	public List<CfgBuffVO> selectAllCfgBuffVO() throws Exception {
		return buffMapper.selectAll();
	}

	@Override
	public List<CfgCorpsTypeVO> selectAllCfgCorpsTypeVO() throws Exception {
		return corpsTypeMapper.selectAll();
	}

	@Override
	public List<CfgSkillVO> selectAllCfgSkillVO() throws Exception {
		return skillMapper.selectAll();
	}

	@Override
	public List<CfgMonsterVO> selectAllCfgMonsterVO() throws Exception {
		return monsterMapper.selectAll();
	}

	@Override
	public List<CfgMatchZoneVO> selectAllCfgMatchZoneVO() throws Exception {
		return matchMapper.selectAll();
	}

	@Override
	public List<CfgCorpsLevelVO> selectAllCfgCorpsLevelVO() throws Exception {
		return corpsLvMapper.selectAll();
	}

	@Override
	public List<CfgPrincessVO> selectAllCfgPrincessVO() throws Exception {
		return princessMapper.selectAll();
	}

	@Override
	public List<SysServerVO> selectAllSysServerVO() throws Exception {
		return serverMapper.selectAll();
	}

	@Override
	public List<CfgUserLevelVO> selectAllCfgUserLevelVO() throws Exception {
		return userLvMapper.selectAll();
	}

	@Override
	public List<CfgVipVO> selectAllCfgVipVO() throws Exception {
		return vipMapper.selectAll();
	}

	@Override
	public List<CfgFallRateVO> selectAllCfgFallRateVO() throws Exception {
		return fallMapper.selectAll();
	}

	@Override
	public List<CfgMapNodeTypeVO> selectAllCfgMapNodeTypeVO() throws Exception {
		return mapNodeTypeMapper.selectAll();
	}

	@Override
	public List<CfgMapNodeVO> selectAllCfgMapNodeVO() throws Exception {
		return mapNodeMapper.selectAll();
	}

	@Override
	public List<CfgRechargeVO> selectAllCfgRechargeVO() throws Exception {
		return rechargeMapper.selectAll();
	}

	@Override
	public List<CfgGoodsVO> selectAllCfgGoodsVO() throws Exception {
		return goodsMapper.selectAll();
	}

	@Override
	public List<CfgRewardVO> selectAllCfgRewardVO() throws Exception {
		return rewardMapper.selectAll();
	}

	@Override
	public List<CfgTaskVO> selectAllCfgTaskVO() throws Exception {
		return taskMapper.selectAll();
	}

	@Override
	public List<CfgTimeZoneGiveVO> selectAllCfgTimeZoneGiveVO() throws Exception {
		return zoneMapper.selectAll();
	}

	@Override
	public List<CfgMsgFormatVO> selectAllCfgMsgFormatVO() throws Exception {
		return msgMapper.selectAll();
	}

	@Override
	public List<CfgFestivalVO> selectAllCfgFestivalVO() throws Exception {
		return festivalMapper.selectAll();
	}

	@Override
	public List<CfgDailyTaskVO> selectAllCfgDailyTaskVO() throws Exception {
		return dailyTaskMapper.selectAll();
	}

	@Override
	public List<CfgLivenessVO> selectAllCfgLivenessVO() throws Exception {
		return livenessMapper.selectAll();
	}

	@Override
	public List<CfgSlotsVO> selectAllCfgSlotsVO() throws Exception {
		return slotsMapper.selectAll();
	}

	@Override
	public List<CfgActivityMapVO> selectAllCfgActivityMapVO() throws Exception {
		return activityMapper.selectAll();
	}
}
