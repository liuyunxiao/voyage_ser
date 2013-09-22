package com.voyage.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.common.util.ReflectUtil;
import com.voyage.SpringContext;
import com.voyage.battle.enums.BattleType;
import com.voyage.battle.enums.MatchType;
import com.voyage.battle.enums.WhetherType;
import com.voyage.constant.Const;
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
import com.voyage.data.vo.SysUserBattleCorpsVO;
import com.voyage.service.ICfgService;
import com.voyage.util.CommonUtil;

@Component
public class CfgDataMgr {
	private final Logger LOG = LoggerFactory.getLogger(CfgDataMgr.class);
	private Map<Class<?>, Map<Object, Object>> cacheMap = new HashMap<Class<?>, Map<Object, Object>>();// {key:类，value:{key:主键,value:对象}}
	public static final Class<CfgCorpsTypeVO> KEY_CFG_CORPS_TYPE = CfgCorpsTypeVO.class;
	public static final Class<CfgCorpsVO> KEY_CFG_CORPS = CfgCorpsVO.class;
	public static final Class<CfgBuffVO> KEY_CFG_BUFF = CfgBuffVO.class;
	public static final Class<CfgSkillVO> KEY_CFG_SKILL = CfgSkillVO.class;
	public static final Class<CfgSceneVO> KEY_CFG_SCENE = CfgSceneVO.class;
	public static final Class<CfgCorpsGradeVO> KEY_CFG_CORPS_GRADE = CfgCorpsGradeVO.class;
	public static final Class<CfgMonsterVO> KEY_CFG_MONSTER = CfgMonsterVO.class;
	public static final Class<CfgMatchZoneVO> KEY_CFG_MATCH_ZONE = CfgMatchZoneVO.class;
	public static final Class<CfgCorpsLevelVO> KEY_CFG_CORPS_LEVEL = CfgCorpsLevelVO.class;
	public static final Class<CfgPrincessVO> KEY_CFG_PRINCESS = CfgPrincessVO.class;
	public static final Class<SysServerVO> KEY_SYS_SERVER = SysServerVO.class;
	public static final Class<CfgUserLevelVO> KEY_CFG_USER_LEVEL = CfgUserLevelVO.class;
	public static final Class<CfgVipVO> KEY_CFG_VIP = CfgVipVO.class;
	public static final Class<CfgFallRateVO> KEY_CFG_FALL_RATE = CfgFallRateVO.class;
	public static final Class<CfgMapNodeTypeVO> KEY_CFG_MAP_NODE_TYPE = CfgMapNodeTypeVO.class;
	public static final Class<CfgMapNodeVO> KEY_CFG_MAP_NODE = CfgMapNodeVO.class;
	public static final Class<CfgRechargeVO> KEY_CFG_RECHARGE = CfgRechargeVO.class;
	public static final Class<CfgGoodsVO> KEY_CFG_GOODS = CfgGoodsVO.class;
	public static final Class<CfgTaskVO> KEY_CFG_TASK = CfgTaskVO.class;
	public static final Class<CfgRewardVO> KEY_CFG_REWARD = CfgRewardVO.class;
	public static final Class<CfgTimeZoneGiveVO> KEY_CFG_TIME_ZONE_GIVE = CfgTimeZoneGiveVO.class;
	public static final Class<CfgMsgFormatVO> KEY_CFG_MSG_FORMAT = CfgMsgFormatVO.class;
	public static final Class<CfgFestivalVO> KEY_CFG_FESTIVAL = CfgFestivalVO.class;
	public static final Class<CfgDailyTaskVO> KEY_CFG_DAILY_TASK = CfgDailyTaskVO.class;
	public static final Class<CfgLivenessVO> KEY_CFG_LIVENESS = CfgLivenessVO.class;
	public static final Class<CfgSlotsVO> KEY_CFG_SLOTS = CfgSlotsVO.class;
	public static final Class<CfgActivityMapVO> KEY_CFG_ACTIVITY_MAP = CfgActivityMapVO.class;
	@Autowired
	private ICfgService cfgService;

	private CfgDataMgr() {
	}

	public static CfgDataMgr getInstance() {
		return SpringContext.getInstance().getSpringContext().getBean(CfgDataMgr.class);
	}

	@SuppressWarnings("unchecked")
	public <T> T getUnique(Class<T> key, Object pk) throws Exception {
		if (cacheMap.get(key) == null) {// 还未初始化
			ReflectUtil.invokeMethod(getInstance(), "reload_" + key.getSimpleName(), null, false);
		}
		return (T) cacheMap.get(key).get(pk);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(Class<T> key) {
		Collection<Object> coll = cacheMap.get(key).values();
		if (coll == null)
			return null;
		List<Object> rt = new ArrayList<Object>(coll);
		return (List<T>) (rt);
	}

	public synchronized void reloadCache() throws Exception {
		LOG.info("+++reload CfgDataMgr cache begin+++");
		cacheMap.clear();// 完全清除旧数据
		/** 注意前后加载顺序 */
		reload_SysServerVO();
		reload_CfgBuffVO();
		reload_CfgSkillVO();
		reload_CfgCorpsGradeVO();
		reload_CfgCorpsTypeVO();
		reload_CfgCorpsVO();
		reload_CfgSceneVO();
		reload_CfgMatchZoneVO();
		reload_CfgCorpsLevelVO();
		reload_CfgMonsterVO();
		reload_CfgPrincessVO();
		reload_CfgUserLevelVO();
		reload_CfgVipVO();
		reload_CfgFallRateVO();
		reload_CfgMapNodeTypeVO();
		reload_CfgMapNodeVO();
		reload_CfgRechargeVO();
		reload_CfgGoodsVO();
		reload_CfgRewardVO();
		reload_CfgTaskVO();
		reload_CfgTimeZoneGiveVO();
		reload_CfgMsgFormatVO();
		reload_CfgFestivalVO();
		reload_CfgDailyTaskVO();
		reload_CfgLivenessVO();
		reload_CfgSlotsVO();
		reload_CfgActivityMapVO();
		LOG.info("+++reload CfgDataMgr cache end+++");
	}

	private synchronized void reload_CfgActivityMapVO() throws Exception {
		LOG.info("reload cfg_activity_map...");
		if (!cacheMap.containsKey(KEY_CFG_ACTIVITY_MAP)) {
			cacheMap.put(KEY_CFG_ACTIVITY_MAP, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_ACTIVITY_MAP);
		List<CfgActivityMapVO> ccList = cfgService.selectAllCfgActivityMapVO();
		for (CfgActivityMapVO ccVO : ccList) {
			ccVO.setRewards(CommonUtil.parseArray(ccVO.getCamReward()));
			ccMap.put(ccVO.getCamId(), ccVO);
		}
	}

	private synchronized void reload_CfgSlotsVO() throws Exception {
		LOG.info("reload cfg_slots...");
		if (!cacheMap.containsKey(KEY_CFG_SLOTS)) {
			cacheMap.put(KEY_CFG_SLOTS, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_SLOTS);
		List<CfgSlotsVO> ccList = cfgService.selectAllCfgSlotsVO();
		for (CfgSlotsVO ccVO : ccList)
			ccMap.put(ccVO.getCsId(), ccVO);
	}

	private synchronized void reload_CfgDailyTaskVO() throws Exception {
		LOG.info("reload cfg_daily_task...");
		if (!cacheMap.containsKey(KEY_CFG_DAILY_TASK)) {
			cacheMap.put(KEY_CFG_DAILY_TASK, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_DAILY_TASK);
		List<CfgDailyTaskVO> ccList = cfgService.selectAllCfgDailyTaskVO();
		for (CfgDailyTaskVO ccVO : ccList)
			ccMap.put(ccVO.getCdtId(), ccVO);
	}

	private synchronized void reload_CfgLivenessVO() throws Exception {
		LOG.info("reload cfg_liveness...");
		if (!cacheMap.containsKey(KEY_CFG_LIVENESS)) {
			cacheMap.put(KEY_CFG_LIVENESS, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_LIVENESS);
		List<CfgLivenessVO> ccList = cfgService.selectAllCfgLivenessVO();
		for (CfgLivenessVO ccVO : ccList)
			ccMap.put(ccVO.getClId(), ccVO);
	}

	private synchronized void reload_CfgFestivalVO() throws Exception {
		LOG.info("reload cfg_festival...");
		if (!cacheMap.containsKey(KEY_CFG_FESTIVAL)) {
			cacheMap.put(KEY_CFG_FESTIVAL, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_FESTIVAL);
		List<CfgFestivalVO> ccList = cfgService.selectAllCfgFestivalVO();
		for (CfgFestivalVO ccVO : ccList) {
			ccVO.setCfRewardVO(getUnique(KEY_CFG_REWARD, ccVO.getCfRewardVO().getCrId()));
			ccMap.put(ccVO.getCfId(), ccVO);
		}
	}

	private synchronized void reload_CfgMsgFormatVO() throws Exception {
		LOG.info("reload cfg_msg_format...");
		if (!cacheMap.containsKey(KEY_CFG_MSG_FORMAT)) {
			cacheMap.put(KEY_CFG_MSG_FORMAT, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_MSG_FORMAT);
		List<CfgMsgFormatVO> ccList = cfgService.selectAllCfgMsgFormatVO();
		for (CfgMsgFormatVO ccVO : ccList)
			ccMap.put(ccVO.getCmfId(), ccVO);
	}

	private synchronized void reload_CfgTimeZoneGiveVO() throws Exception {
		LOG.info("reload cfg_time_zone_give...");
		if (!cacheMap.containsKey(KEY_CFG_TIME_ZONE_GIVE)) {
			cacheMap.put(KEY_CFG_TIME_ZONE_GIVE, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_TIME_ZONE_GIVE);
		List<CfgTimeZoneGiveVO> ccList = cfgService.selectAllCfgTimeZoneGiveVO();
		for (CfgTimeZoneGiveVO ccVO : ccList)
			ccMap.put(ccVO.getCtzgId(), ccVO);
	}

	private synchronized void reload_CfgRewardVO() throws Exception {
		LOG.info("reload cfg_reward...");
		if (!cacheMap.containsKey(KEY_CFG_REWARD)) {
			cacheMap.put(KEY_CFG_REWARD, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_REWARD);
		List<CfgRewardVO> ccList = cfgService.selectAllCfgRewardVO();
		for (CfgRewardVO ccVO : ccList)
			ccMap.put(ccVO.getCrId(), ccVO);
	}

	private synchronized void reload_CfgTaskVO() throws Exception {
		LOG.info("reload cfg_task...");
		if (!cacheMap.containsKey(KEY_CFG_TASK)) {
			cacheMap.put(KEY_CFG_TASK, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_TASK);
		List<CfgTaskVO> ccList = cfgService.selectAllCfgTaskVO();
		for (CfgTaskVO ccVO : ccList)
			ccMap.put(ccVO.getCtId(), ccVO);
	}

	private synchronized void reload_CfgGoodsVO() throws Exception {
		LOG.info("reload cfg_goods...");
		if (!cacheMap.containsKey(KEY_CFG_GOODS)) {
			cacheMap.put(KEY_CFG_GOODS, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_GOODS);
		List<CfgGoodsVO> ccList = cfgService.selectAllCfgGoodsVO();
		for (CfgGoodsVO ccVO : ccList)
			ccMap.put(ccVO.getCgId(), ccVO);
	}

	private synchronized void reload_CfgRechargeVO() throws Exception {
		LOG.info("reload cfg_recharge...");
		if (!cacheMap.containsKey(KEY_CFG_RECHARGE)) {
			cacheMap.put(KEY_CFG_RECHARGE, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_RECHARGE);
		List<CfgRechargeVO> ccList = cfgService.selectAllCfgRechargeVO();
		for (CfgRechargeVO ccVO : ccList)
			ccMap.put(ccVO.getCrId(), ccVO);
	}

	private synchronized void reload_CfgMapNodeVO() throws Exception {
		LOG.info("reload cfg_map_node...");
		if (!cacheMap.containsKey(KEY_CFG_MAP_NODE)) {
			cacheMap.put(KEY_CFG_MAP_NODE, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_MAP_NODE);
		List<CfgMapNodeVO> ccList = cfgService.selectAllCfgMapNodeVO();
		for (CfgMapNodeVO ccVO : ccList) {
			ccVO.setRewards(CommonUtil.parseArray(ccVO.getCmnReward()));
			ccMap.put(ccVO.getCmnId(), ccVO);
		}
	}

	private synchronized void reload_CfgMapNodeTypeVO() throws Exception {
		LOG.info("reload cfg_map_node_type...");
		if (!cacheMap.containsKey(KEY_CFG_MAP_NODE_TYPE)) {
			cacheMap.put(KEY_CFG_MAP_NODE_TYPE, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_MAP_NODE_TYPE);
		List<CfgMapNodeTypeVO> ccList = cfgService.selectAllCfgMapNodeTypeVO();
		for (CfgMapNodeTypeVO ccVO : ccList)
			ccMap.put(ccVO.getCmntId(), ccVO);
	}

	private synchronized void reload_CfgFallRateVO() throws Exception {
		LOG.info("reload cfg_fall_rate...");
		if (!cacheMap.containsKey(KEY_CFG_FALL_RATE)) {
			cacheMap.put(KEY_CFG_FALL_RATE, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_FALL_RATE);
		List<CfgFallRateVO> ccList = cfgService.selectAllCfgFallRateVO();
		for (CfgFallRateVO ccVO : ccList)
			ccMap.put(ccVO.getCfrId(), ccVO);
	}

	private synchronized void reload_CfgVipVO() throws Exception {
		LOG.info("reload cfg_vip...");
		if (!cacheMap.containsKey(KEY_CFG_VIP)) {
			cacheMap.put(KEY_CFG_VIP, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_VIP);
		List<CfgVipVO> ccList = cfgService.selectAllCfgVipVO();
		for (CfgVipVO ccVO : ccList)
			ccMap.put(ccVO.getCvId(), ccVO);
	}

	private synchronized void reload_CfgUserLevelVO() throws Exception {
		LOG.info("reload cfg_user_level...");
		if (!cacheMap.containsKey(KEY_CFG_USER_LEVEL)) {
			cacheMap.put(KEY_CFG_USER_LEVEL, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_USER_LEVEL);
		List<CfgUserLevelVO> ccList = cfgService.selectAllCfgUserLevelVO();
		for (CfgUserLevelVO ccVO : ccList)
			ccMap.put(ccVO.getCulId(), ccVO);
	}

	private synchronized void reload_SysServerVO() throws Exception {
		LOG.info("reload sys_server...");
		if (!cacheMap.containsKey(KEY_SYS_SERVER)) {
			cacheMap.put(KEY_SYS_SERVER, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_SYS_SERVER);
		List<SysServerVO> ccList = cfgService.selectAllSysServerVO();
		for (SysServerVO ccVO : ccList)
			ccMap.put(ccVO.getSsId(), ccVO);
	}

	private synchronized void reload_CfgPrincessVO() throws Exception {
		LOG.info("reload cfg_princess...");
		if (!cacheMap.containsKey(KEY_CFG_PRINCESS)) {
			cacheMap.put(KEY_CFG_PRINCESS, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_PRINCESS);
		List<CfgPrincessVO> ccList = cfgService.selectAllCfgPrincessVO();
		for (CfgPrincessVO ccVO : ccList)
			ccMap.put(ccVO.getCpId(), ccVO);
	}

	private synchronized void reload_CfgCorpsLevelVO() throws Exception {
		LOG.info("reload cfg_corps_level...");
		if (!cacheMap.containsKey(KEY_CFG_CORPS_LEVEL)) {
			cacheMap.put(KEY_CFG_CORPS_LEVEL, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_CORPS_LEVEL);
		List<CfgCorpsLevelVO> ccList = cfgService.selectAllCfgCorpsLevelVO();
		for (CfgCorpsLevelVO ccVO : ccList)
			ccMap.put(ccVO.getCclId(), ccVO);
	}

	private synchronized void reload_CfgMatchZoneVO() throws Exception {
		LOG.info("reload cfg_match_zone...");
		if (!cacheMap.containsKey(KEY_CFG_MATCH_ZONE)) {
			cacheMap.put(KEY_CFG_MATCH_ZONE, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_MATCH_ZONE);
		List<CfgMatchZoneVO> ccList = cfgService.selectAllCfgMatchZoneVO();
		for (CfgMatchZoneVO ccVO : ccList)
			ccMap.put(ccVO.getCmzId(), ccVO);
	}

	private synchronized void reload_CfgMonsterVO() throws Exception {
		LOG.info("reload cfg_monster...");
		if (!cacheMap.containsKey(KEY_CFG_MONSTER)) {
			cacheMap.put(KEY_CFG_MONSTER, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_MONSTER);
		List<CfgMonsterVO> ccList = cfgService.selectAllCfgMonsterVO();
		List<SysUserBattleCorpsVO> battleCorpsList = null;
		for (CfgMonsterVO ccVO : ccList) {
			battleCorpsList = new ArrayList<SysUserBattleCorpsVO>(1);
			if (!Const.NONE.equals(ccVO.getCmCorps())) {
				int battleCorpsGold = 0;
				JSONArray ja = new JSONArray(ccVO.getCmCorps());
				JSONObject jo = null;
				for (int i = 0; i < ja.length(); i++) {
					jo = ja.getJSONObject(i);
					SysUserBattleCorpsVO subcVO = new SysUserBattleCorpsVO();
					subcVO.setSubcPos(jo.getInt(Const.POS));
					subcVO.setSubcCorpsVO(getUnique(KEY_CFG_CORPS, jo.getInt(Const.CORPSID)));
					subcVO.setSubcShortAtk(jo.getInt(Const.SHORTATK));
					subcVO.setSubcShortDef(jo.getInt(Const.SHORTDEF));
					subcVO.setSubcMagicAtk(jo.getInt(Const.MAGICATK));
					subcVO.setSubcSoma(jo.getInt(Const.SOMA));
					battleCorpsList.add(subcVO);
					battleCorpsGold += CommonUtil.getHireGold(subcVO.getSubcMagicAtk() + subcVO.getSubcSoma() + subcVO.getSubcShortAtk()
							+ subcVO.getSubcShortDef());
				}
				ccVO.setBattleCorpsGold(battleCorpsGold);
			}
			ccVO.setBattleCorpsList(battleCorpsList);
			ccMap.put(ccVO.getCmId(), ccVO);
		}
	}

	private synchronized void reload_CfgCorpsGradeVO() throws Exception {
		LOG.info("reload cfg_corps_grade...");
		if (!cacheMap.containsKey(KEY_CFG_CORPS_GRADE)) {
			cacheMap.put(KEY_CFG_CORPS_GRADE, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_CORPS_GRADE);
		List<CfgCorpsGradeVO> ccList = cfgService.selectAllCfgCorpsGradeVO();
		for (CfgCorpsGradeVO ccVO : ccList)
			ccMap.put(ccVO.getCcgId(), ccVO);
	}

	private synchronized void reload_CfgSceneVO() throws Exception {
		LOG.info("reload cfg_scene...");
		if (!cacheMap.containsKey(KEY_CFG_SCENE)) {
			cacheMap.put(KEY_CFG_SCENE, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_SCENE);
		List<CfgSceneVO> ccList = cfgService.selectAllCfgSceneVO();
		for (CfgSceneVO ccVO : ccList)
			ccMap.put(ccVO.getCsId(), ccVO);
	}

	private synchronized void reload_CfgCorpsTypeVO() throws Exception {
		LOG.info("reload cfg_corps_type...");
		if (!cacheMap.containsKey(KEY_CFG_CORPS_TYPE)) {
			cacheMap.put(KEY_CFG_CORPS_TYPE, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_CORPS_TYPE);
		List<CfgCorpsTypeVO> ccList = cfgService.selectAllCfgCorpsTypeVO();
		for (CfgCorpsTypeVO ccVO : ccList)
			ccMap.put(ccVO.getCctId(), ccVO);
	}

	private synchronized void reload_CfgSkillVO() throws Exception {
		LOG.info("reload cfg_skill...");
		if (!cacheMap.containsKey(KEY_CFG_SKILL)) {
			cacheMap.put(KEY_CFG_SKILL, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_SKILL);
		List<CfgSkillVO> ccList = cfgService.selectAllCfgSkillVO();
		for (CfgSkillVO ccVO : ccList) {
			if (ccVO.getCsBuffVO().getCbId() != 0)
				ccVO.setCsBuffVO(getUnique(KEY_CFG_BUFF, ccVO.getCsBuffVO().getCbId()));
			if (ccVO.getCsBuffVO2().getCbId() != 0)
				ccVO.setCsBuffVO2(getUnique(KEY_CFG_BUFF, ccVO.getCsBuffVO2().getCbId()));
			ccMap.put(ccVO.getCsId(), ccVO);
		}
	}

	private synchronized void reload_CfgBuffVO() throws Exception {
		LOG.info("reload cfg_buff...");
		if (!cacheMap.containsKey(KEY_CFG_BUFF)) {
			cacheMap.put(KEY_CFG_BUFF, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_BUFF);
		List<CfgBuffVO> ccList = cfgService.selectAllCfgBuffVO();
		for (CfgBuffVO ccVO : ccList) {
			ccMap.put(ccVO.getCbId(), ccVO);
		}
	}

	private synchronized void reload_CfgCorpsVO() throws Exception {
		LOG.info("reload cfg_corps...");
		if (!cacheMap.containsKey(KEY_CFG_CORPS)) {
			cacheMap.put(KEY_CFG_CORPS, new TreeMap<Object, Object>());
		}
		Map<Object, Object> ccMap = cacheMap.get(KEY_CFG_CORPS);
		List<CfgCorpsVO> ccList = cfgService.selectAllCfgCorpsVO();
		for (CfgCorpsVO ccVO : ccList) {
			ccVO.setCcCorpsTypeVO(getUnique(KEY_CFG_CORPS_TYPE, ccVO.getCcCorpsTypeVO().getCctId()));
			ccVO.setCcCorpsGradeVO(getUnique(KEY_CFG_CORPS_GRADE, ccVO.getCcCorpsGradeVO().getCcgId()));

			if (ccVO.getCcAtkSkillVO().getCsId() != 0)
				ccVO.setCcAtkSkillVO(getUnique(KEY_CFG_SKILL, ccVO.getCcAtkSkillVO().getCsId()));

			if (ccVO.getCcDefSkillVO().getCsId() != 0)
				ccVO.setCcDefSkillVO(getUnique(KEY_CFG_SKILL, ccVO.getCcDefSkillVO().getCsId()));

			ccMap.put(ccVO.getCcId(), ccVO);
		}
	}

	public CfgMatchZoneVO getMatchZone(MatchType type, int gold) throws Exception {
		List<CfgMatchZoneVO> list = getList(KEY_CFG_MATCH_ZONE);
		for (CfgMatchZoneVO vo : list) {
			if (vo.getCmzType() == type.getV() && CommonUtil.inZone(gold, vo.getCmzLeft(), vo.getCmzRight()))
				return vo;
		}
		return null;
	}

	/**
	 * 从某个等级开始根据总金币查找
	 * */
	public CfgUserLevelVO getUserLevel(int level, int total) throws Exception {
		level = Math.max(1, level);
		List<CfgUserLevelVO> uLvList = getList(KEY_CFG_USER_LEVEL);
		CfgUserLevelVO culVO = null;
		for (int i = level - 1; i < uLvList.size(); i++) {
			culVO = uLvList.get(i);
			if (culVO.getCulTotalCost() > total || culVO.getCulPerCost() == Const.INFINITY)
				return culVO;
		}
		return null;
	}

	/**
	 * 从某个等级开始根据总金币查找
	 * */
	public CfgVipVO getVipLevel(int level, int total) throws Exception {
		level = Math.max(0, level);
		List<CfgVipVO> uLvList = getList(KEY_CFG_VIP);
		CfgVipVO culVO = null;
		for (int i = level; i < uLvList.size(); i++) {
			culVO = uLvList.get(i);
			if (culVO.getCvTotalCost() > total || culVO.getCvPerCost() == Const.INFINITY)
				return culVO;
		}
		return null;
	}

	public CfgFallRateVO getFallRate(BattleType bType, int grade, int level) throws Exception {
		List<CfgFallRateVO> frList = getList(KEY_CFG_FALL_RATE);
		for (CfgFallRateVO cfrVO : frList) {
			if (cfrVO.getCfrType() == bType.getV() && cfrVO.getCfrGrade() == grade && CommonUtil.inZone(level, cfrVO.getCfrLeft(), cfrVO.getCfrRight()))
				return cfrVO;
		}
		return null;
	}

	public List<CfgMapNodeVO> getNodesByType(int type) throws Exception {
		List<CfgMapNodeVO> mnList = getList(KEY_CFG_MAP_NODE);
		List<CfgMapNodeVO> rt = new ArrayList<CfgMapNodeVO>(1);
		for (CfgMapNodeVO vo : mnList) {
			if (vo.getCmnType() == type) {
				rt.add(vo);
			}
		}
		return rt;
	}

	public List<CfgTaskVO> getTaskHeads() throws Exception {
		List<CfgTaskVO> rt = new ArrayList<CfgTaskVO>();
		for (CfgTaskVO ctVO : getList(KEY_CFG_TASK)) {
			if (ctVO.getCtHead() == WhetherType.YES.getV())
				rt.add(ctVO);
		}
		return rt;
	}

	public CfgTimeZoneGiveVO getGive(int hour24) throws Exception {
		List<CfgTimeZoneGiveVO> zoneList = getList(KEY_CFG_TIME_ZONE_GIVE);
		if (zoneList != null) {
			for (CfgTimeZoneGiveVO tmp : zoneList) {
				if (tmp.getCtzgLeft() > tmp.getCtzgRight()) {//跨天
					if (hour24 >= tmp.getCtzgLeft() || hour24 < tmp.getCtzgRight()) {
						return tmp;
					}
				} else {
					if (hour24 >= tmp.getCtzgLeft() && hour24 < tmp.getCtzgRight()) {
						return tmp;
					}
				}
			}
		}
		return null;
	}

	public List<CfgRewardVO> getRewardByType(int rType) throws Exception {
		List<CfgRewardVO> rt = new ArrayList<CfgRewardVO>();
		List<CfgRewardVO> rewards = getList(KEY_CFG_REWARD);
		for (CfgRewardVO reward : rewards) {
			if (reward.getCrType() == rType)
				rt.add(reward);
		}
		return rt;
	}

	public List<CfgFestivalVO> getFestivalByDate(Date now) throws Exception {
		List<CfgFestivalVO> rt = new ArrayList<CfgFestivalVO>(1);
		List<CfgFestivalVO> fList = getList(KEY_CFG_FESTIVAL);
		if (fList != null) {
			for (CfgFestivalVO cfVO : fList) {
				if (cfVO.getCfStart().before(now) && cfVO.getCfEnd().after(now))
					rt.add(cfVO);
			}
		}
		return rt;
	}

	public List<CfgActivityMapVO> getActivityMapByLevel(int level) throws Exception {
		List<CfgActivityMapVO> rt = new ArrayList<CfgActivityMapVO>(1);
		List<CfgActivityMapVO> fList = getList(KEY_CFG_ACTIVITY_MAP);
		if (fList != null) {
			for (CfgActivityMapVO cfVO : fList) {
				if (cfVO.getCamLevel() <= level)
					rt.add(cfVO);
			}
		}
		return rt;
	}

	public int getNextActivityMapLevel(int level) throws Exception {
		int rt = 999;
		List<CfgActivityMapVO> fList = getList(KEY_CFG_ACTIVITY_MAP);
		if (fList != null) {
			for (CfgActivityMapVO cfVO : fList) {
				if (cfVO.getCamLevel() > level)
					return cfVO.getCamLevel();
			}
		}
		return rt;
	}

	public CfgRechargeVO getRechargeByProId(String proId) throws Exception {
		List<CfgRechargeVO> fList = getList(KEY_CFG_RECHARGE);
		if (fList != null) {
			for (CfgRechargeVO cfVO : fList) {
				if (cfVO.getCrProId().equals(proId))
					return cfVO;
			}
		}
		return null;
	}
}