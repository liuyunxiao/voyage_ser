package com.voyage.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyage.battle.enums.WhetherType;
import com.voyage.cache.CfgDataMgr;
import com.voyage.config.PingyangConfig;
import com.voyage.constant.Const;
import com.voyage.constant.ErCode;
import com.voyage.dao.SysUserBattleCorpsVOMapper;
import com.voyage.dao.SysUserCorpsVOMapper;
import com.voyage.dao.SysUserGoldVOMapper;
import com.voyage.dao.SysUserVOMapper;
import com.voyage.data.bo.CorpsImgBO;
import com.voyage.data.bo.CorpsItemBO;
import com.voyage.data.bo.CorpsUnitBO;
import com.voyage.data.vo.CfgCorpsVO;
import com.voyage.data.vo.FormationVO;
import com.voyage.data.vo.SysUserBattleCorpsVO;
import com.voyage.data.vo.SysUserCorpsVO;
import com.voyage.data.vo.SysUserGoldVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.enums.IncomeType;
import com.voyage.enums.MoneyType;
import com.voyage.exception.NotifyException;
import com.voyage.service.ICorpsService;
import com.voyage.service.IUserService;
import com.voyage.util.CommonUtil;

@Service
public class CorpsService implements ICorpsService {
	@Autowired
	private SysUserBattleCorpsVOMapper battleCorpsMapper;
	@Autowired
	private SysUserGoldVOMapper goldMapper;
	@Autowired
	private SysUserCorpsVOMapper corpsMapper;
	@Autowired
	private IUserService userService;
	@Autowired
	private SysUserVOMapper userMapper;

	/**更新队长列表
	 * @param reset 重置队长队列 
	 * */
	private void refreshCaptains(boolean reset, int userId) {
		SysUserVO updateVO = new SysUserVO(userId);
		if (reset) {
			updateVO.setSuCaptain(Const.NONE);
		} else {
			StringBuilder sb = new StringBuilder();
			List<SysUserBattleCorpsVO> battleCorpsList = battleCorpsMapper.selectCaptains(userId, WhetherType.YES.getV());
			for (SysUserBattleCorpsVO subcVO : battleCorpsList) {
				if (subcVO.getSubcCaptain() == WhetherType.YES.getV()) {
					if (sb.length() > 0)
						sb.append(Const.COMMA_SEP);
					sb.append(subcVO.getSubcCorpsVO().getCcId());
				}
			}
			updateVO.setSuCaptain(sb.length() > 0 ? sb.toString() : Const.NONE);
		}
		userMapper.updateByPrimaryKeySelective(updateVO);

	}

	/*@Override
	public void takeOff(JSONObject jo) throws Exception {
		moveAndCaptain(jo, false);//先保存1次阵型
		int subcId = jo.getInt("subcId");//玩家出战兵种表主键
		SysUserBattleCorpsVO subcVO = battleCorpsMapper.selectByPrimaryKey(subcId);
		if (subcVO == null)
			return;
		int hireGold = CommonUtil.getHireGold(subcVO.getSubcMagicAtk() + subcVO.getSubcSoma() + subcVO.getSubcShortAtk() + subcVO.getSubcShortDef());
		SysUserGoldVO sugVO = new SysUserGoldVO();
		sugVO.setSugUserId(subcVO.getSubcUserId());
		sugVO.setSugBattleCorps(-hireGold);
		goldMapper.updateOffsetByUserId(sugVO);
		battleCorpsMapper.deleteByPrimaryKey(subcId);

		userService.incMoney(IncomeType.DEFAULT, subcVO.getSubcUserId(), MoneyType.GOLD, hireGold);
		refreshCaptains(false, jo.getInt(Const.USERID));
	}
	*/
	@Override
	public void resetCorps(boolean clean, int userId) throws Exception {
		SysUserGoldVO sugVO = goldMapper.selectByUserId(userId);
		if (!clean) {
			userService.incMoney(IncomeType.DEFAULT, userId, MoneyType.GOLD, sugVO.getSugBattleCorps());
		}
		SysUserGoldVO updateGoldVO = new SysUserGoldVO();
		updateGoldVO.setSugUserId(userId);
		updateGoldVO.setSugBattleCorps(-sugVO.getSugBattleCorps());
		goldMapper.updateOffsetByUserId(updateGoldVO);
		battleCorpsMapper.resetCorps(userId);

		refreshCaptains(true, userId);
	}

	@Override
	public List<CorpsUnitBO> enterCorps(int userId) throws Exception {
		List<CorpsUnitBO> corpsUnits = new ArrayList<CorpsUnitBO>(1);
		List<SysUserBattleCorpsVO> battleCorps = battleCorpsMapper.selectBattle(userId);
		CfgCorpsVO corpsVO = null;
		for (SysUserBattleCorpsVO tmp : battleCorps) {
			corpsVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, tmp.getSubcCorpsVO().getCcId());
			CorpsUnitBO unit = new CorpsUnitBO();
			unit.setSubcId(tmp.getSubcId());
			unit.setCorpsImg(new CorpsImgBO(corpsVO.getCcId(), corpsVO.getCcImg(), corpsVO.getCcCorpsGradeVO().getCcgCorpsBg()));
			unit.setCaptain(tmp.getSubcCaptain());
			unit.setHireGold(CommonUtil.getHireGold(tmp.getSubcMagicAtk() + tmp.getSubcSoma() + tmp.getSubcShortAtk() + tmp.getSubcShortDef()));
			unit.setPos(tmp.getSubcPos());
			corpsUnits.add(unit);
		}
		return corpsUnits;
	}

	@Override
	public List<CorpsUnitBO> getLastArmy(int userId) throws Exception {
		List<CorpsUnitBO> corpsUnits = new ArrayList<CorpsUnitBO>(1);
		SysUserVO suVO = userMapper.selectByPk(userId);
		if (!suVO.getSuArmy().equals(Const.NONE)) {
			List<SysUserCorpsVO> corps = corpsMapper.selectByUserId(userId);
			JSONArray lastArmy = new JSONArray(suVO.getSuArmy());
			JSONObject jo = null;
			SysUserCorpsVO tmp = null;
			CfgCorpsVO corpsVO = null;
			for (int i = 0; i < lastArmy.length(); i++) {
				jo = lastArmy.getJSONObject(i);
				tmp = getSucVO(jo.getInt(Const.CORPSID), corps);
				corpsVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, tmp.getSucCorpsVO().getCcId());
				CorpsUnitBO unit = new CorpsUnitBO();
				unit.setSubcId(Const.ZERO);
				unit.setCorpsImg(new CorpsImgBO(corpsVO.getCcId(), corpsVO.getCcImg(), corpsVO.getCcCorpsGradeVO().getCcgCorpsBg()));
				unit.setCaptain(Const.ZERO);
				unit.setHireGold(CommonUtil.getHireGold(tmp.getSucMagicAtk() + tmp.getSucSoma() + tmp.getSucShortAtk() + tmp.getSucShortDef()));
				unit.setPos(jo.getInt(Const.POS));
				corpsUnits.add(unit);
			}
		}
		return corpsUnits;
	}

	/*
		@Override
		public CorpsUnitBO hire(JSONObject jo) throws Exception {
			moveAndCaptain(jo);//先保存1次阵型
			int sucId = jo.getInt("sucId");//玩家兵种表主键
			int pos = jo.getInt("pos");//目的位置
			SysUserCorpsVO sucVO = corpsMapper.selectByPrimaryKey(sucId);
			SysUserBattleCorpsVO existVO = battleCorpsMapper.selectByPos(sucVO.getSucUserId(), pos);
			if (existVO != null)
				throw new IllegalStateException("pos not idle");
			int hireGold = CommonUtil.getHireGold(sucVO.getSucMagicAtk() + sucVO.getSucSoma() + sucVO.getSucShortAtk() + sucVO.getSucShortDef());
			//扣除招募金
			userService.decMoney(sucVO.getSucUserId(), MoneyType.GOLD, hireGold);
			SysUserGoldVO updateGoldVO = new SysUserGoldVO();
			updateGoldVO.setSugUserId(sucVO.getSucUserId());
			updateGoldVO.setSugBattleCorps(hireGold);
			goldMapper.updateOffsetByUserId(updateGoldVO);
			SysUserBattleCorpsVO subcVO = new SysUserBattleCorpsVO(sucVO);
			subcVO.setSubcPos(pos);
			battleCorpsMapper.insertSelective(subcVO);
			CfgCorpsVO corpsVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, subcVO.getSubcCorpsVO().getCcId());
			return new CorpsUnitBO(subcVO.getSubcId(), WhetherType.NO.getV(), hireGold, new CorpsImgBO(corpsVO.getCcImg(), corpsVO.getCcCorpsGradeVO()
					.getCcgCorpsBg()), pos);
		}*/

	@Override
	public int resetDot(int sucId) throws Exception {
		SysUserCorpsVO sucVO = corpsMapper.selectByPrimaryKey(sucId);
		int dotGold = CommonUtil.getDotGold(sucVO.getSucMagicAtk() + sucVO.getSucSoma() + sucVO.getSucShortAtk() + sucVO.getSucShortDef());
		SysUserGoldVO updateGoldVO = new SysUserGoldVO();
		updateGoldVO.setSugUserId(sucVO.getSucUserId());
		int reback = new Double(dotGold * PingyangConfig.getInstance().getResetDotRepay()).intValue();//返还部分金额

		updateGoldVO.setSugUpCorpsProp(-reback);
		goldMapper.updateOffsetByUserId(updateGoldVO);

		userService.incMoney(IncomeType.DEFAULT, sucVO.getSucUserId(), MoneyType.GOLD, reback);
		//洗点
		SysUserCorpsVO updateCorpsVO = new SysUserCorpsVO();
		updateCorpsVO.setSucId(sucId);
		updateCorpsVO.setSucMagicAtk(0);
		updateCorpsVO.setSucSoma(0);
		updateCorpsVO.setSucShortAtk(0);
		updateCorpsVO.setSucShortDef(0);
		corpsMapper.updateByPrimaryKeySelective(updateCorpsVO);
		return sucVO.getSucCorpsVO().getCcId();
	}

	@Override
	public int addDot(int sucId, int shortAtk, int shortDef, int magicAtk, int soma) throws Exception {
		SysUserCorpsVO sucVO = corpsMapper.selectByPrimaryKey(sucId);
		int appendGold = CommonUtil.getAppendGold(sucVO.getSucMagicAtk() + sucVO.getSucSoma() + sucVO.getSucShortAtk() + sucVO.getSucShortDef(), shortAtk
				+ shortDef + magicAtk + soma);
		userService.decMoney(sucVO.getSucUserId(), MoneyType.GOLD, appendGold);//扣除本次加点金
		//累计加点金币
		SysUserGoldVO updateGoldVO = new SysUserGoldVO();
		updateGoldVO.setSugUserId(sucVO.getSucUserId());
		updateGoldVO.setSugUpCorpsProp(appendGold);
		goldMapper.updateOffsetByUserId(updateGoldVO);
		//更新加点
		SysUserCorpsVO updateCorpsVO = new SysUserCorpsVO();
		updateCorpsVO.setSucId(sucId);
		updateCorpsVO.setSucShortAtk(sucVO.getSucShortAtk() + shortAtk);
		updateCorpsVO.setSucShortDef(sucVO.getSucShortDef() + shortDef);
		updateCorpsVO.setSucMagicAtk(sucVO.getSucMagicAtk() + magicAtk);
		updateCorpsVO.setSucSoma(sucVO.getSucSoma() + soma);
		corpsMapper.updateByPrimaryKeySelective(updateCorpsVO);
		return sucVO.getSucCorpsVO().getCcId();
	}

	@Override
	public List<CorpsItemBO> enterDot(int userId, int corpsType) throws Exception {
		List<SysUserCorpsVO> corpsList = corpsMapper.selectByUserId(userId);
		List<SysUserCorpsVO> destList = new ArrayList<SysUserCorpsVO>();
		CfgCorpsVO corpsVO = null;
		for (SysUserCorpsVO tmp : corpsList) {
			corpsVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, tmp.getSucCorpsVO().getCcId());
			tmp.setSucCorpsVO(corpsVO);
			if (corpsVO.getCcCorpsTypeVO().getCctId() == corpsType) {
				destList.add(tmp);
			}
		}
		if (destList.size() > 1) {
			Collections.sort(destList, new Comparator<SysUserCorpsVO>() {
				@Override
				public int compare(SysUserCorpsVO o1, SysUserCorpsVO o2) {
					if (o1.getSucCorpsVO().getCcCorpsGradeVO().getCcgId().intValue() == o2.getSucCorpsVO().getCcCorpsGradeVO().getCcgId()) {
						return o2.getSucId() - o1.getSucId();
					} else {
						return o2.getSucCorpsVO().getCcCorpsGradeVO().getCcgId() - o1.getSucCorpsVO().getCcCorpsGradeVO().getCcgId();
					}
				}
			});
		}
		List<CorpsItemBO> rt = new ArrayList<CorpsItemBO>();

		for (SysUserCorpsVO tmp : destList) {
			int base = tmp.getSucCorpsVO().getPropBase(tmp.getSucShortAtk() + tmp.getSucShortDef() + tmp.getSucMagicAtk() + tmp.getSucSoma());
			rt.add(new CorpsItemBO(tmp.getSucId(), tmp.getSucCorpsVO().getCcId(), getXAndY(base, tmp.getSucShortAtk()), getXAndY(base, tmp.getSucShortDef()),
					getXAndY(base, tmp.getSucMagicAtk()), getXAndY(base, tmp.getSucSoma()), CommonUtil.getHireGold(tmp.getSucShortAtk() + tmp.getSucShortDef()
							+ tmp.getSucMagicAtk() + tmp.getSucSoma())));
		}
		return rt;
	}

	/**属性格式：x+y*/
	private String getXAndY(int base, int add) {
		return base + "+" + add;
	}

	/*	@Override
		public void moveAndCaptain(JSONObject param) throws Exception {
			moveAndCaptain(param, true);
		}*/

	@Override
	public void moveAndCaptain(JSONObject param) throws Exception {
		final String lineup = "lineup";
		JSONArray joa = param.getJSONArray(lineup);//必传
		if (joa.length() > Const.SUDOKU)//最多只能有9个位置
			throw new IllegalStateException("not sudoku");

		JSONObject jo = null;
		List<FormationVO> fmtList = new ArrayList<FormationVO>(9);
		List<Integer> posList = new ArrayList<Integer>(9);
		FormationVO fmt = null;
		int captains = 0;
		for (int i = 0; i < joa.length(); i++) {
			jo = joa.getJSONObject(i);
			fmt = new FormationVO(jo.getInt("subcId"), jo.getInt("corpsId"), jo.getInt("captain"), jo.getInt("pos"));
			//校验pos是否有效
			if (!(fmt.getPos() >= 1 && fmt.getPos() <= 9)) {
				throw new IllegalStateException("invalid pos");
			}
			if (posList.contains(fmt.getPos()))
				throw new IllegalStateException("repeat pos");
			else
				posList.add(fmt.getPos());
			fmtList.add(fmt);
			captains += fmt.getCaptain();
		}
		//检验队长数
		if (captains > Const.CAPTAINS)
			throw new IllegalStateException("captain excess");

		moveAndCaptain(param.getInt(Const.USERID), fmtList);
	}

	/**保存阵型*/
	private void moveAndCaptain(int userId, List<FormationVO> fmtList) throws Exception {
		if (fmtList.size() > 0) {
			List<SysUserBattleCorpsVO> battleCorps = battleCorpsMapper.selectBattle(userId);
			List<SysUserCorpsVO> sucList = corpsMapper.selectByUserId(userId);
			JSONArray lastArmy = new JSONArray();//作为最近一次非空阵型
			int newHireGold = 0;//新招募兵种的金额
			List<SysUserBattleCorpsVO> newHireSubcList = new ArrayList<SysUserBattleCorpsVO>(9);//新招募的
			StringBuilder captains = new StringBuilder();//队长列表
			SysUserBattleCorpsVO newSubcVO = null;
			for (FormationVO fmt : fmtList) {
				JSONObject jo = new JSONObject();
				jo.put(Const.CORPSID, fmt.getCorpsId());
				jo.put(Const.POS, fmt.getPos());
				lastArmy.put(jo);

				if (fmt.getSubcId() == 0) {//新招募的
					newSubcVO = new SysUserBattleCorpsVO(getSucVO(fmt.getCorpsId(), sucList));
					newSubcVO.setSubcCaptain(fmt.getCaptain());
					newSubcVO.setSubcPos(fmt.getPos());
					newHireSubcList.add(newSubcVO);
					newHireGold += CommonUtil.getHireGold(newSubcVO.getSubcMagicAtk() + newSubcVO.getSubcSoma() + newSubcVO.getSubcShortAtk()
							+ newSubcVO.getSubcShortDef());
				}
				if (fmt.getCaptain() > 0) {//加入队长列表
					if (captains.length() > 0)
						captains.append(Const.COMMA_SEP);
					captains.append(fmt.getCorpsId());
				}
			}
			int takeOffGold = 0;//兵种移除返还的金额
			List<SysUserBattleCorpsVO> existSubcList = new ArrayList<SysUserBattleCorpsVO>(9);//已有的(且有变动)
			StringBuilder takeOffSubcIds = new StringBuilder();//被移除的
			FormationVO fmt = null;
			for (SysUserBattleCorpsVO subcVO : battleCorps) {
				fmt = getFormationVO(subcVO.getSubcId(), fmtList);
				if (fmt == null) {
					takeOffGold += CommonUtil
							.getHireGold(subcVO.getSubcMagicAtk() + subcVO.getSubcSoma() + subcVO.getSubcShortAtk() + subcVO.getSubcShortDef());
					if (takeOffSubcIds.length() > 0)
						takeOffSubcIds.append(Const.COMMA_SEP);
					takeOffSubcIds.append(subcVO.getSubcId());
				} else if (!subcVO.getSubcCaptain().equals(fmt.getCaptain()) || !subcVO.getSubcPos().equals(fmt.getPos())) {//有位置或队长变动
					existSubcList.add(new SysUserBattleCorpsVO(fmt.getSubcId(), fmt.getCaptain(), fmt.getPos()));
				}
			}
			SysUserVO suVO = userMapper.selectFullByPk(userId);
			if (suVO.getSuGoldVO().getSugStorage() + takeOffGold < newHireGold) {//金币不足以招募
				throw new NotifyException(ErCode.E999);
			}
			//添加新招募的
			for (SysUserBattleCorpsVO subcVO : newHireSubcList) {
				battleCorpsMapper.insertSelective(subcVO);
			}
			//更新已有的
			for (SysUserBattleCorpsVO subcVO : existSubcList) {
				battleCorpsMapper.updateByPrimaryKeySelective(subcVO);
			}
			//删除移除的
			if (takeOffSubcIds.length() > 0)
				battleCorpsMapper.deleteByIds(takeOffSubcIds.toString());

			SysUserVO suUpdateVO = new SysUserVO(userId);
			//更新队长列表
			if (captains.length() == 0)//无队长
				captains.append(Const.NONE);
			if (!suVO.getSuCaptain().equals(captains.toString())) {
				suUpdateVO.setSuCaptain(captains.toString());
			}
			//更新最近非空阵型
			suUpdateVO.setSuArmy(lastArmy.toString());
			userMapper.updateByPrimaryKeySelective(suUpdateVO);
			//金币相关变动
			int nGold = newHireGold - takeOffGold;
			if (nGold != 0) {
				SysUserGoldVO sugVO = new SysUserGoldVO();
				sugVO.setSugUserId(userId);
				sugVO.setSugBattleCorps(nGold);
				goldMapper.updateOffsetByUserId(sugVO);
				if (nGold > 0)
					userService.decMoney(userId, MoneyType.GOLD, nGold);
				else
					userService.incMoney(IncomeType.DEFAULT, userId, MoneyType.GOLD, -nGold);
			}
		} else {
			resetCorps(false, userId);
		}
	}

	private FormationVO getFormationVO(int subcId, List<FormationVO> fmtList) {
		for (FormationVO fmt : fmtList) {
			if (fmt.getSubcId() == subcId) {
				return fmt;
			}
		}
		return null;
	}

	private SysUserCorpsVO getSucVO(int corpsId, List<SysUserCorpsVO> sucList) {
		for (SysUserCorpsVO sucVO : sucList) {
			if (sucVO.getSucCorpsVO().getCcId() == corpsId)
				return sucVO;
		}
		return null;
	}

	/**
	 * @param refresh 是否级联更新队长列表
	 * */
	/*

	private void moveAndCaptain(JSONObject param, boolean refresh) throws Exception {
	final String lineup = "lineup";
	if (!param.has(lineup))
	return;
	JSONArray joa = param.getJSONArray(lineup);
	JSONObject jo = null;
	for (int i = 0; i < joa.length(); i++) {
	jo = joa.getJSONObject(i);
	moveAndCaptain(jo.getInt("subcId"), jo.getInt("sucId"), jo.getInt("captain"), jo.getInt("pos"));
	}
	if (refresh)
	refreshCaptains(false, param.getInt(Const.USERID));
	}*/

	/*private void moveAndCaptain(int subcId, int sucId, int captain, int pos) throws Exception {
		SysUserBattleCorpsVO subcVO = battleCorpsMapper.selectByPrimaryKey(subcId);
		if (subcVO == null)//已经takeOff的垃圾数据
			return;
		if (pos == subcVO.getSubcPos() && captain == subcVO.getSubcCaptain())
			return;
		SysUserBattleCorpsVO updateVO = new SysUserBattleCorpsVO();
		updateVO.setSubcId(subcId);
		if (pos != subcVO.getSubcPos()) {//位置有变动
			SysUserBattleCorpsVO existVO = battleCorpsMapper.selectByPos(subcVO.getSubcUserId(), pos);
			if (existVO != null) {//更新已存在的
				SysUserBattleCorpsVO updateExistVO = new SysUserBattleCorpsVO();
				updateExistVO.setSubcId(existVO.getSubcId());
				updateExistVO.setSubcPos(subcVO.getSubcPos());
				battleCorpsMapper.updateByPrimaryKeySelective(updateExistVO);
			}
			updateVO.setSubcPos(pos);
		}

		if (captain != subcVO.getSubcCaptain()) {//队长有变动
			if (subcVO.getSubcCaptain() == WhetherType.NO.getV()) {
				int n = battleCorpsMapper.selectCaptainCount(subcVO.getSubcUserId());
				if (n >= 3)
					throw new NotifyException("captain is enough");
			}
			updateVO.setSubcCaptain(subcVO.getSubcCaptain() == WhetherType.YES.getV() ? WhetherType.NO.getV() : WhetherType.YES.getV());
		}

		battleCorpsMapper.updateByPrimaryKeySelective(updateVO);
	}*/

	public boolean addCorpsIfNotExist(int userId, CfgCorpsVO ccVO) throws Exception {
		if (ccVO == null)
			return false;
		SysUserCorpsVO sucVO = corpsMapper.selectByCorpsId(userId, ccVO.getCcId());
		if (sucVO != null)
			return false;
		SysUserCorpsVO insertCorpsVO = new SysUserCorpsVO();
		insertCorpsVO.setSucCorpsVO(ccVO);
		insertCorpsVO.setSucUserId(userId);
		corpsMapper.insertSelective(insertCorpsVO);
		return true;
	}

}
