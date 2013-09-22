package com.voyage.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyage.battle.enums.WhetherType;
import com.voyage.cache.Cashback;
import com.voyage.cache.CfgDataMgr;
import com.voyage.cache.CommonMgr;
import com.voyage.cache.SlotsMgr;
import com.voyage.config.PingyangConfig;
import com.voyage.constant.Const;
import com.voyage.constant.ErCode;
import com.voyage.dao.SysUserGoldVOMapper;
import com.voyage.dao.SysUserMsgVOMapper;
import com.voyage.dao.SysUserSlotsVOMapper;
import com.voyage.data.bo.MsgBO;
import com.voyage.data.bo.PondBO;
import com.voyage.data.bo.SlotsBO;
import com.voyage.data.bo.SlotsMsgBO;
import com.voyage.data.bo.SlotsPlayBO;
import com.voyage.data.vo.CfgSlotsVO;
import com.voyage.data.vo.SysUserGoldVO;
import com.voyage.data.vo.SysUserMsgVO;
import com.voyage.data.vo.SysUserSlotsVO;
import com.voyage.enums.IncomeType;
import com.voyage.enums.MoneyType;
import com.voyage.enums.MsgFormatType;
import com.voyage.enums.MsgType;
import com.voyage.enums.PondType;
import com.voyage.enums.SlotsFruitType;
import com.voyage.exception.NotifyException;
import com.voyage.service.ISlotsService;
import com.voyage.service.IUserService;
import com.voyage.util.CommonUtil;

@Service
public class SlotsService implements ISlotsService {
	@Autowired
	private SysUserSlotsVOMapper slotsMapper;
	@Autowired
	private SysUserGoldVOMapper goldMapper;
	@Autowired
	private IUserService userService;
	@Autowired
	private SysUserMsgVOMapper msgMapper;

	@Override
	public int resetAllScore() {
		return slotsMapper.resetAllScore();
	}

	@Override
	public List<SysUserSlotsVO> selectAllByScoreDesc(int n) throws Exception {
		return slotsMapper.selectAllByScoreDesc(n);
	}

	@Override
	public SlotsBO enter(int userId) throws Exception {
		SlotsMgr.getInstance().enter(userId);
		SlotsBO slots = new SlotsBO();
		slots.ranks = SlotsMgr.getInstance().getRanks();
		slots.pond = SlotsMgr.getInstance().getPond();
		slots.score = initScore(userId);
		List<SysUserMsgVO> msgList = msgMapper.selectByType(MsgType.SLOTS.getV());
		for (SysUserMsgVO sumVO : msgList) {
			slots.msgs.add(new MsgBO(sumVO, Const.DEFAULT_TIME_FORMAT));
		}
		return slots;
	}

	/**
	 * 初始化玩家得分
	 * @return 玩家周得分
	 * */
	private int initScore(int userId) throws Exception {
		SysUserSlotsVO susVO = slotsMapper.selectByUserId(userId);
		if (susVO == null) {
			susVO = new SysUserSlotsVO();
			susVO.setSusScore(0);
			susVO.setSusUserId(userId);
			slotsMapper.insertSelective(susVO);
		}
		return susVO.getSusScore();
	}

	@Override
	public int roll(int userId, int nGold, int big) throws Exception {
		if (nGold <= 0)
			throw new IllegalStateException("illegal nGold");
		Cashback cb = SlotsMgr.getInstance().getCashback(userId);
		if (cb.getLastGold() == 0)//返金区为0
			throw new IllegalStateException("no cash back");
		SysUserGoldVO sugVO = goldMapper.selectByUserId(userId);
		if (nGold > sugVO.getSugStorage())
			throw new NotifyException(ErCode.E999);
		boolean isHit = hit(userId);
		updateSlotsMoney(userId, isHit, nGold, false, cb);
		return isHit ? WhetherType.YES.getV() : WhetherType.NO.getV();
	}

	/**
	 * 增加或扣除金币,并更新得分及返金区
	 * @param initCashBack 是否初始化返金区
	 * */
	private void updateSlotsMoney(int userId, boolean isHit, int nGold, boolean initCashBack, Cashback cb) throws Exception {
		if (nGold > 0) {
			SysUserSlotsVO record = new SysUserSlotsVO();
			record.setSusUserId(userId);
			if (isHit) {
				record.setSusScore(nGold);
				userService.incMoney(IncomeType.DEFAULT, userId, MoneyType.GOLD, nGold);
			} else {
				record.setSusScore(-cb.getnGolds());
				userService.decMoney(userId, MoneyType.GOLD, nGold);
			}
			record.setSusTotalScore(record.getSusScore());
			slotsMapper.updateOffsetByUserId(record);
		}
		//更新返金区
		cb.udpate(!isHit ? 0 : (initCashBack ? nGold : 2 * nGold));
	}

	/**
	 * 是否进入胜利集（猜大小也是该公式）
	 * */
	private boolean hit(int userId) throws Exception {
		SysUserSlotsVO susVO = slotsMapper.selectByUserId(userId);
		//赢钱率
		double winRate = (susVO.getSusTotalScore() - susVO.getSusTotalInvest()) / (PingyangConfig.getInstance().getSlotsWinRateFactor() * 1000.0);
		//胜率
		double rate = PingyangConfig.getInstance().getSlotsWinDifficulty() * (0.5 - Math.atan(winRate - Math.PI / 4.0) / (2 * Math.PI));
		return CommonUtil.isCrit(Const.CRIT_BASE_100, new Double(rate * Const.CRIT_BASE_100).intValue());
	}

	@Override
	public SlotsPlayBO play(JSONObject param) throws Exception {
		JSONArray joa = param.getJSONArray("press");
		int userId = param.getInt(Const.USERID);
		if (joa.length() == 0)
			throw new IllegalStateException("press is empty");
		Map<Integer, Integer> press = new HashMap<Integer, Integer>(joa.length());
		int costGold = 0;//所压的金币总额
		JSONObject jo = null;
		for (int i = 0; i < joa.length(); i++) {
			jo = joa.getJSONObject(i);
			press.put(jo.getInt("csId"), jo.getInt("n"));
			costGold += jo.getInt("n");
			if (jo.getInt("csId") == SlotsFruitType.SF_10.getV()) {//如果为大BAR，则视小BAR也压了
				press.put(SlotsFruitType.SF_9.getV(), jo.getInt("n"));
			}
		}
		if (costGold <= 0)
			throw new IllegalStateException("illegal press");

		//扣成本
		userService.decMoney(userId, MoneyType.GOLD, costGold);

		List<MsgBO> msgs = new ArrayList<MsgBO>();
		//// 尝试领取奖池
		SlotsPlayBO rt = new SlotsPlayBO();
		PondBO pb = SlotsMgr.getInstance().tryReapPond(userId, costGold);
		if (pb.hit()) {//中奖了
			msgs.add(new MsgBO(MsgType.SLOTS.getV(), 0, 0, CommonUtil.getFormatMsg(MsgFormatType.SLOTS_POND, new Object[] {
					CommonMgr.getInstance().getUserInfo(userId).getName(), String.valueOf(pb.bigHit == WhetherType.YES.getV() ? pb.big : pb.small) }), null));
			rt.inPond = (pb.bigHit == WhetherType.YES.getV() ? PondType.BIG : PondType.SMALL).getV();
			rt.gold += (pb.bigHit == WhetherType.YES.getV() ? pb.big : pb.small);//獎池獲得的金幣
		}
		////具體流程
		Cashback cb = SlotsMgr.getInstance().getCashback(userId);
		cb.udpate(0);//重置返金區
		//选择结果集

		List<Integer> wins = new ArrayList<Integer>(Arrays.asList(new Integer[] { SlotsFruitType.SF_1.getV(), SlotsFruitType.SF_11.getV(),
				SlotsFruitType.SF_12.getV(), SlotsFruitType.SF_13.getV(), SlotsFruitType.SF_14.getV(), SlotsFruitType.SF_15.getV(),
				SlotsFruitType.SF_16.getV(), SlotsFruitType.SF_17.getV() }));//胜利集
		List<Integer> fails = new ArrayList<Integer>(Arrays.asList(new Integer[] { SlotsFruitType.SF_1.getV(), SlotsFruitType.SF_15.getV() }));//失败集
		CfgSlotsVO csVO = null;
		for (Map.Entry<Integer, Integer> me : press.entrySet()) {
			csVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_SLOTS, me.getKey());
			if (csVO.getCsTimes() * me.getValue() < costGold)
				fails.add(csVO.getCsId());
			else
				wins.add(csVO.getCsId());
		}

		Set<Integer> pressKeys = press.keySet();
		for (int csId = 2; csId < 11; csId++) {//检测2~10号水果中未压的(水果1放入默认的水果集)
			if (!pressKeys.contains(csId)) {
				fails.add(csId);
			}
		}

		List<Integer> selected = hit(userId) ? wins : fails;
		//选择水果
		List<Integer> seeds = new ArrayList<Integer>();
		for (Integer sid : selected) {
			csVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_SLOTS, sid);
			for (int i = 0; i < csVO.getCsWeight(); i++)
				seeds.add(sid);
		}
		Collections.shuffle(seeds);//乱序
		rt.csId = seeds.get(Const.RAND.nextInt(seeds.size()));
		csVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_SLOTS, rt.csId);
		//设置rt.fruits
		if (csVO.getCsLuck() == WhetherType.YES.getV()) {
			if (Const.TO_RAND.equals(csVO.getCsEquals())) {
				JSONArray fjoa = new JSONArray(csVO.getCsSeed());
				Object obj = null;
				for (int i = 0; i < csVO.getCsN(); i++) {
					obj = fjoa.get(Const.RAND.nextInt(fjoa.length()));
					if (obj instanceof JSONArray) {
						JSONArray fjoa2 = (JSONArray) obj;
						for (int j = 0; j < fjoa2.length(); j++)
							rt.fruits.add(fjoa2.getInt(j));
					} else {//int类型
						rt.fruits.add((Integer) obj);
					}
				}
			} else if (!Const.NONE.equals(csVO.getCsEquals())) {
				String[] eqs = csVO.getCsEquals().split(Const.COMMA_SEP);
				for (String eq : eqs) {
					rt.fruits.add(Integer.parseInt(eq));
				}
			}
			CfgSlotsVO tCsVO = null;
			for (Integer fId : rt.fruits) {
				if (press.containsKey(fId)) {
					tCsVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_SLOTS, fId);
					rt.gold += tCsVO.getCsTimes() * press.get(fId);//水果獲得的總金幣
				}
			}
		} else if (press.containsKey(rt.csId)) {//普通情況
			rt.gold += csVO.getCsTimes() * press.get(rt.csId);//水果獲得的總金幣
		} else if (rt.csId == SlotsFruitType.SF_1.getV()) {//中了水果1
			rt.gold += csVO.getCsTimes() * costGold;
		}

		SysUserSlotsVO investVO = new SysUserSlotsVO();
		investVO.setSusUserId(userId);
		investVO.setSusTotalInvest(costGold);
		slotsMapper.updateOffsetByUserId(investVO);//更新歷史投資額

		updateSlotsMoney(userId, rt.gold > 0, rt.gold, true, cb);//設置返金區及保存收益

		if (!pb.hit() && rt.gold > Const.SLOTS_RP_MSG_MIN)
			msgs.add(new MsgBO(MsgType.SLOTS.getV(), 0, 0, CommonUtil.getFormatMsg(MsgFormatType.SLOTS_RP,
					new Object[] { CommonMgr.getInstance().getUserInfo(userId).getName(), String.valueOf(rt.gold) }), null));
		//发送及保存消息
		saveMsg(msgs);
		return rt;
	}

	/**
	 * 保存老虎機消息并通知前台
	 * */
	private void saveMsg(List<MsgBO> msgs) throws Exception {
		if (msgs == null || msgs.size() == 0)
			return;
		for (MsgBO msg : msgs) {
			msgMapper.insertSelective(new SysUserMsgVO(msg));
			SlotsMgr.getInstance().sendSlotsMsg(new SlotsMsgBO(null, msg));
		}
		Integer limitId = msgMapper.selectLimitId(MsgType.SLOTS.getV(), Const.SLOTS_MAX_RECORD);
		if (limitId != null)
			msgMapper.deleteByLimitId(MsgType.SLOTS.getV(), limitId);
	}
}
