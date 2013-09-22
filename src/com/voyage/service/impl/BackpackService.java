package com.voyage.service.impl;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyage.cache.CfgDataMgr;
import com.voyage.cache.CommonMgr;
import com.voyage.dao.SysUserBackpackVOMapper;
import com.voyage.dao.SysUserGoldVOMapper;
import com.voyage.data.bo.GoodsBO;
import com.voyage.data.vo.CfgGoodsVO;
import com.voyage.data.vo.SysUserBackpackVO;
import com.voyage.data.vo.SysUserGoldVO;
import com.voyage.enums.DailyTaskType;
import com.voyage.enums.GoodsType;
import com.voyage.enums.IncomeType;
import com.voyage.enums.MoneyType;
import com.voyage.service.IBackpackService;
import com.voyage.service.ITaskService;
import com.voyage.service.IUserService;

@Service
public class BackpackService implements IBackpackService {
	@Autowired
	private SysUserBackpackVOMapper backpackMapper;
	@Autowired
	private IUserService userService;
	@Autowired
	private SysUserGoldVOMapper goldMapper;
	@Autowired
	private ITaskService taskService;

	@Override
	public int addToBackpack(GoodsType gt, int userId, int gId) throws Exception {
		CfgGoodsVO cgVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_GOODS, gId);
		return addToBackpack(gt, userId, cgVO);
	}

	@Override
	public int addToBackpack(GoodsType gt, int userId, CfgGoodsVO cgVO) throws Exception {
		int subType = (gt == GoodsType.GIFT || gt == GoodsType.MEDAL) ? (GoodsType.GIFT.getV() | GoodsType.MEDAL.getV()) : gt.getV();
		Integer maxPos = backpackMapper.selectMaxPosByType(userId, subType);
		if (maxPos == null)
			maxPos = 0;
		SysUserBackpackVO insertVO = new SysUserBackpackVO();
		insertVO.setSubUserId(userId);
		insertVO.setSubPos(maxPos + 1);
		insertVO.setSubType(subType);
		insertVO.setSubGoodsVO(cgVO);
		if (gt == GoodsType.FARM || gt == GoodsType.MINE) {
			whenGetBreed(insertVO, cgVO.getCgUnit());
		}

		backpackMapper.insertSelective(insertVO);

		//更新礼物、矿场、农场成本金
		SysUserGoldVO updateGoldVO = new SysUserGoldVO();
		updateGoldVO.setSugUserId(userId);
		switch (gt) {
		case FARM:
			updateGoldVO.setSugFarm(cgVO.getCgPrice());
			break;
		case MINE:
			updateGoldVO.setSugMine(cgVO.getCgPrice());
			break;
		case GIFT:
			updateGoldVO.setSugGift(cgVO.getCgPrice());
			break;
		default:
			break;
		}
		if (gt == GoodsType.FARM || gt == GoodsType.MINE || gt == GoodsType.GIFT)
			goldMapper.updateOffsetByUserId(updateGoldVO);

		//加入缓存
		CommonMgr.getInstance().addGoods(userId, new GoodsBO(insertVO.getSubId(), insertVO.getSubPos(), cgVO));
		return insertVO.getSubId();
	}

	/**获得养殖物品时的福利*/

	private void whenGetBreed(SysUserBackpackVO insertVO, int cgUnit) throws Exception {
		//第1次可直接收获
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR_OF_DAY, -cgUnit);
		insertVO.setSubMtime(now.getTime());
	}

	@Override
	public void move(JSONObject param) throws Exception {
		final String posmf = "posmf";
		if (!param.has(posmf))
			return;
		JSONArray joa = param.getJSONArray(posmf);
		JSONObject jo = null;
		for (int i = 0; i < joa.length(); i++) {
			jo = joa.getJSONObject(i);
			move(jo.getInt("subId"), jo.getInt("pos"));
		}
	}

	private void move(int subId, int pos) throws Exception {
		SysUserBackpackVO subVO = backpackMapper.selectByPrimaryKey(subId);
		if (subVO == null)
			return;
		if (pos == subVO.getSubPos())
			return;
		SysUserBackpackVO existVO = backpackMapper.selectByTypeAndPos(subVO.getSubUserId(), subVO.getSubType(), pos);
		if (existVO != null) {//更新已存在的
			SysUserBackpackVO updateExistVO = new SysUserBackpackVO();
			updateExistVO.setSubId(existVO.getSubId());
			updateExistVO.setSubPos(subVO.getSubPos());
			backpackMapper.updateByPrimaryKeySelective(updateExistVO);
		}
		SysUserBackpackVO updateVO = new SysUserBackpackVO();
		updateVO.setSubId(subId);
		updateVO.setSubPos(pos);
		backpackMapper.updateByPrimaryKeySelective(updateVO);
		//更新缓存
		CommonMgr.getInstance().moveGoods(subVO.getSubUserId(), subId, pos);
	}

	@Override
	public List<GoodsBO> getAllGoods(int userId, int subType) throws Exception {
		List<GoodsBO> goodsList = null;
		if (subType == GoodsType.ALL.getV()) {
			goodsList = CommonMgr.getInstance().getGoods(userId);
		} else {
			List<SysUserBackpackVO> bpList = backpackMapper.selectByType(userId, subType);
			CfgGoodsVO cgVO = null;
			for (SysUserBackpackVO bp : bpList) {
				cgVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_GOODS, bp.getSubGoodsVO().getCgId());
				bp.setSubGoodsVO(cgVO);
			}
			GoodsBO gBO = null;
			goodsList = new ArrayList<GoodsBO>();
			for (SysUserBackpackVO bp : bpList) {
				gBO = new GoodsBO(bp.getSubId(), bp.getSubPos(), bp.getSubGoodsVO());
				if (bp.getSubType() == GoodsType.FARM.getV() || bp.getSubType() == GoodsType.MINE.getV()) {
					gBO.reap = canReapBreed(bp, bp.getSubGoodsVO()).y;//设置养殖收入
				}
				goodsList.add(gBO);
			}
		}
		//根据价格降序排列
		if (goodsList != null && goodsList.size() > 1) {
			Collections.sort(goodsList, new Comparator<GoodsBO>() {
				@Override
				public int compare(GoodsBO o1, GoodsBO o2) {
					return o2.getPrice() - o1.getPrice();
				}
			});
		}
		return goodsList;
	}

	@Override
	public List<GoodsBO> enter(int userId, int subType) throws Exception {
		return getAllGoods(userId, subType);
	}

	@Override
	public void reapBreed(JSONObject param) throws Exception {
		final String reaps = "reaps";
		if (!param.has(reaps))
			return;
		JSONArray joa = param.getJSONArray(reaps);
		JSONObject jo = null;
		for (int i = 0; i < joa.length(); i++) {
			jo = joa.getJSONObject(i);
			reapBreed(jo.getInt("subId"));
		}
	}

	private void reapBreed(int subId) throws Exception {
		SysUserBackpackVO subVO = backpackMapper.selectByPrimaryKey(subId);
		if (subVO == null)
			return;
		CfgGoodsVO cgVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_GOODS, subVO.getSubGoodsVO().getCgId());
		Point p = canReapBreed(subVO, cgVO);//可获得的收益
		if (p.y == 0)//不可领取
			return;
		userService.incMoney(IncomeType.BREED, subVO.getSubUserId(), MoneyType.GOLD, p.y);
		SysUserBackpackVO updateVO = new SysUserBackpackVO(subId);
		if (p.x + subVO.getSubN() >= cgVO.getCgMax()) {//达到上限时重置
			updateVO.setSubN(0);
			updateVO.setSubMtime(new Date());
		} else {
			updateVO.setSubN(subVO.getSubN() + 1);
		}
		backpackMapper.updateByPrimaryKeySelective(updateVO);

		taskService.updateDailyProByOne(subVO.getSubUserId(), subVO.getSubType() == GoodsType.FARM.getV() ? DailyTaskType.REAP_FARM : DailyTaskType.REAP_MINE);//养殖日常
	}

	/**养殖可收获的金额
	 * return x:可领次数，y:可领金额
	 * */
	private Point canReapBreed(SysUserBackpackVO bp, CfgGoodsVO cgVO) throws Exception {
		//CfgGoodsVO cgVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_GOODS, bp.getSubGoodsVO().getCgId());
		if (bp.getSubN() >= cgVO.getCgMax())//必须设置上限
			return new Point(0, 0);
		int nHour = new Long((System.currentTimeMillis() - bp.getSubMtime().getTime()) / 3600000).intValue();
		int n = Math.min(nHour / cgVO.getCgUnit(), cgVO.getCgMax()) - bp.getSubN();//可领次数
		return new Point(n, n * cgVO.getCgGain());
	}

	@Override
	public List<SysUserBackpackVO> selectAll(int userId) throws Exception {
		return backpackMapper.selectByUserId(userId);
	}

	@Override
	public void delFromBackpack(int userId, int subId) throws Exception {
		backpackMapper.deleteByPrimaryKey(subId);
		//清除缓存
		CommonMgr.getInstance().delGoods(userId, new GoodsBO(subId));
	}

}
