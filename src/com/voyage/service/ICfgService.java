package com.voyage.service;

import java.util.List;

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

public interface ICfgService {
	List<CfgCorpsVO> selectAllCfgCorpsVO() throws Exception;

	List<CfgCorpsTypeVO> selectAllCfgCorpsTypeVO() throws Exception;

	List<CfgBuffVO> selectAllCfgBuffVO() throws Exception;

	List<CfgSkillVO> selectAllCfgSkillVO() throws Exception;

	List<CfgSceneVO> selectAllCfgSceneVO() throws Exception;

	List<CfgCorpsGradeVO> selectAllCfgCorpsGradeVO() throws Exception;

	List<CfgMonsterVO> selectAllCfgMonsterVO() throws Exception;

	List<CfgMatchZoneVO> selectAllCfgMatchZoneVO() throws Exception;

	List<CfgCorpsLevelVO> selectAllCfgCorpsLevelVO() throws Exception;

	List<CfgPrincessVO> selectAllCfgPrincessVO() throws Exception;

	List<SysServerVO> selectAllSysServerVO() throws Exception;

	List<CfgUserLevelVO> selectAllCfgUserLevelVO() throws Exception;

	List<CfgVipVO> selectAllCfgVipVO() throws Exception;

	List<CfgFallRateVO> selectAllCfgFallRateVO() throws Exception;

	List<CfgMapNodeTypeVO> selectAllCfgMapNodeTypeVO() throws Exception;

	List<CfgMapNodeVO> selectAllCfgMapNodeVO() throws Exception;

	List<CfgRechargeVO> selectAllCfgRechargeVO() throws Exception;

	List<CfgGoodsVO> selectAllCfgGoodsVO() throws Exception;

	List<CfgTaskVO> selectAllCfgTaskVO() throws Exception;

	List<CfgRewardVO> selectAllCfgRewardVO() throws Exception;

	List<CfgTimeZoneGiveVO> selectAllCfgTimeZoneGiveVO() throws Exception;

	List<CfgMsgFormatVO> selectAllCfgMsgFormatVO() throws Exception;

	List<CfgFestivalVO> selectAllCfgFestivalVO() throws Exception;

	List<CfgDailyTaskVO> selectAllCfgDailyTaskVO() throws Exception;

	List<CfgLivenessVO> selectAllCfgLivenessVO() throws Exception;

	List<CfgSlotsVO> selectAllCfgSlotsVO() throws Exception;

	List<CfgActivityMapVO> selectAllCfgActivityMapVO() throws Exception;
}
