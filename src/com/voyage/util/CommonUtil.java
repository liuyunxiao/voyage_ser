package com.voyage.util;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.voyage.battle.bo.ExpBO;
import com.voyage.cache.CfgDataMgr;
import com.voyage.constant.Const;
import com.voyage.constant.ErCode;
import com.voyage.data.bo.CorpsImgBO;
import com.voyage.data.vo.CfgCorpsLevelVO;
import com.voyage.data.vo.CfgCorpsVO;
import com.voyage.data.vo.CfgUserLevelVO;
import com.voyage.data.vo.CfgVipVO;
import com.voyage.data.vo.RewardVO;
import com.voyage.enums.MsgFormatType;
import com.voyage.exception.NotifyException;

public class CommonUtil {
	/**
	 * 是否满足给定的随机数
	 * */
	public static boolean isCrit(int critBase, int crit) {
		if (crit <= 0)
			return false;
		else if (crit >= critBase)
			return true;
		else
			return (Const.RAND.nextInt(critBase) + 1) <= crit ? true : false;
	}

	/**
	 * 是否满足给定的随机数(基数1000)
	 * */
	public static boolean isCrit(int crit) {
		return isCrit(Const.CRIT_BASE, crit);
	}

	/**是否在某个正数区间内，right为-1时表示无穷*/
	public static boolean inZone(int n, int left, int right) throws Exception {
		return n >= left && (right < 0 || n <= right);
	}

	/**
	 * 计算appendDots个加点需要的金币
	 * */
	public static int getAppendGold(int oldDots, int appendDots) throws Exception {
		return getDotGold(oldDots + appendDots) - getDotGold(oldDots);
	}

	/**根据总加点数计算招募金*/
	public static int getHireGold(int dots) throws Exception {
		return getDotGold(dots) / Const.CORPS_HIRE_GOLD_BASE + 1;
	}

	/**根据总加点数计算总加点金*/
	public static int getDotGold(int dots) throws Exception {
		if (dots <= 0)
			return 0;
		int dotGold = 0;
		int mod = dots % Const.CORPS_LEVEL_DOTS;
		CfgCorpsLevelVO lvVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS_LEVEL, dots / Const.CORPS_LEVEL_DOTS + (mod == 0 ? 0 : 1));
		if (lvVO == null)
			throw new NotifyException(ErCode.E201);
		if (mod == 0) {
			dotGold = lvVO.getCclTotalCost();
		} else {
			dotGold = lvVO.getCclTotalCost() - (Const.CORPS_LEVEL_DOTS - mod) * lvVO.getCclPerCost();
		}
		return dotGold;
	}

	/**解析奖励数组,格式：[{corpsId:1,minGold:1,maxGold:5}]*/
	public static List<RewardVO> parseArray(String jsonArrayReward) throws Exception {
		if (jsonArrayReward == null || Const.NONE.equals(jsonArrayReward))
			return null;
		List<RewardVO> rt = new ArrayList<RewardVO>(1);
		JSONArray ja = new JSONArray(jsonArrayReward);
		JSONObject jo = null;
		for (int i = 0; i < ja.length(); i++) {
			jo = (JSONObject) ja.getJSONObject(i);
			rt.add(new RewardVO(jo.getInt(Const.CORPSID), jo.getInt(Const.MINGOLD), jo.getInt(Const.MAXGOLD)));
		}
		return rt;
	}

	/**解析队长列表,格式：1,2,3*/
	public static List<CorpsImgBO> getCaptains(String suCaptain) throws Exception {
		List<CorpsImgBO> captainsList = new ArrayList<CorpsImgBO>(1);
		String[] captains = null;
		CfgCorpsVO corpsVO = null;
		if (suCaptain != null && !Const.NONE.equals(suCaptain)) {
			captains = suCaptain.split(Const.COMMA_SEP);
			for (String ca : captains) {
				corpsVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, Integer.parseInt(ca));
				captainsList.add(new CorpsImgBO(corpsVO.getCcImg(), corpsVO.getCcCorpsGradeVO().getCcgCorpsBg()));
			}
		}
		return captainsList;
	}

	/**获得经验相关
	 * @param level 当前等级
	 * @param exp 累计经验
	 * */
	public static ExpBO getExpBO(int level, int exp) throws Exception {
		CfgUserLevelVO culVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_USER_LEVEL, level);
		return new ExpBO(culVO.getCulId(), culVO.getExp(exp), culVO.getExpUp());
	}

	/**获得VIP经验相关
	 * @param all 是否总进度显示
	 * @param level 当前等级
	 * @param exp 累计经验
	 * */
	public static ExpBO getVipExpBO(boolean all, int level, int exp) throws Exception {
		CfgVipVO culVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_VIP, level);
		return new ExpBO(culVO.getCvId(), culVO.getExp(all, exp), culVO.getExpUp(all));
	}

	/**
	 * 返回带格式化的消息内容
	 * */
	public static String getFormatMsg(MsgFormatType mfType, Object... param) throws Exception {
		String msgFormat = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MSG_FORMAT, mfType.getV()).getCmfFormat();
		return MessageFormat.format(msgFormat, param);
	}

	/**
	 * 拼接GM操作日志
	 * */
	public static StringBuilder getGmDo(String method, Object[] params) {
		StringBuilder sb = new StringBuilder("[GM]->");
		sb.append(method).append("(");
		if (params != null) {
			boolean empty = true;
			for (Object param : params) {
				if (!empty)
					sb.append(",");
				else
					empty = false;
				sb.append("'").append(param).append("'");
			}
		}
		sb.append(")");
		return sb;
	}

	/**
	 * 转成指定格式的日期
	 * */
	public static Date toDate(DateFormat df, String str) throws Exception {
		return df.parse(str);
	}
}
