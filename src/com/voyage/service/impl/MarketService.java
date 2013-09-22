package com.voyage.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyage.cache.CfgDataMgr;
import com.voyage.cache.CommonMgr;
import com.voyage.cache.UserInfo;
import com.voyage.config.PingyangConfig;
import com.voyage.dao.SysUserExpenseVOMapper;
import com.voyage.data.bo.GoodsBO;
import com.voyage.data.bo.MsgBO;
import com.voyage.data.vo.CfgGoodsVO;
import com.voyage.data.vo.CfgRechargeVO;
import com.voyage.data.vo.CfgVipVO;
import com.voyage.data.vo.SysUserExpenseVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.enums.DailyTaskType;
import com.voyage.enums.ExpenseType;
import com.voyage.enums.GoodsType;
import com.voyage.enums.IncomeType;
import com.voyage.enums.MoneyType;
import com.voyage.enums.MsgFormatType;
import com.voyage.enums.MsgType;
import com.voyage.service.IBackpackService;
import com.voyage.service.IMarketService;
import com.voyage.service.IRelationService;
import com.voyage.service.ITaskService;
import com.voyage.service.IUserService;
import com.voyage.util.CommonUtil;

@Service
public class MarketService implements IMarketService {
	private final Logger logger = LoggerFactory.getLogger(MarketService.class);
	@Autowired
	private SysUserExpenseVOMapper expenseMapper;
	@Autowired
	private IUserService userService;
	@Autowired
	private IBackpackService backpackService;
	@Autowired
	private IRelationService relationService;
	@Autowired
	private ITaskService taskService;

	@Override
	public void buyGold(int userId, int oppoId, int rechargeId, String desc) throws Exception {
		if (desc != null) {//苹果平台时将transactionId设置成desc
			Integer n = expenseMapper.selectCountByDesc(oppoId, desc);
			if (n != null && n > 0)//已经发放
				return;
		}

		CfgRechargeVO crVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_RECHARGE, rechargeId);
		logExpense(ExpenseType.RECHARGE, oppoId, rechargeId, crVO.getCrRmb(), desc);//必须先日志，因为incMoney中有对充值次数的跟踪
		userService.incMoney(IncomeType.RECHARGE, oppoId, MoneyType.GOLD, crVO.getCrGold());
		if (userId != oppoId) {//代充
			try {
				UserInfo sender = CommonMgr.getInstance().getUserInfo(userId);
				//通知前台
				relationService.sendMsg(new MsgBO(MsgType.SYSTEM.getV(), 0, oppoId, CommonUtil.getFormatMsg(MsgFormatType.GIVE_RECHARGE, new Object[] { userId,
						sender.getName(), crVO.getCrGold().toString() }), null));
			} catch (Exception e) {
				logger.debug("give_recharge: ignore send error", e);
			}
		}
	}

	@Override
	public void logExpense(ExpenseType et, int userId, int expenseId, int cost, String desc) throws Exception {
		SysUserExpenseVO insertVO = new SysUserExpenseVO();
		insertVO.setSueUserId(userId);
		insertVO.setSueType(et.getV());
		insertVO.setSueExpenseId(expenseId);
		insertVO.setSueCost(cost);
		insertVO.setSueDesc(desc);
		expenseMapper.insertSelective(insertVO);
	}

	@Override
	public void buyGoods(int userId, int oppoId, int goodsId) throws Exception {
		SysUserVO suVO = userService.selectByPk(userId);
		CfgGoodsVO goodsVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_GOODS, goodsId);
		GoodsType gt = GoodsType.parse(goodsVO.getCgType());
		CfgVipVO vipVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_VIP, suVO.getSuVipLevel());
		int n = goodsVO.getCgPrice();
		if (gt == GoodsType.GIFT) {
			n = new Double(n * vipVO.getCvGiftRebate()).intValue();//享受VIP礼物折扣
		}
		userService.decMoney(userId, MoneyType.GOLD, n);
		backpackService.addToBackpack(gt, oppoId, goodsVO);//放入背包
		logExpense(ExpenseType.MARKET, oppoId, goodsId, n, null);
		if (userId != oppoId && gt == GoodsType.GIFT) {//赠送他人礼物
			try {
				//通知前台
				relationService.sendMsg(new MsgBO(MsgType.SYSTEM.getV(), 0, oppoId, CommonUtil.getFormatMsg(MsgFormatType.GIVE_GIFT, new Object[] { userId,
						suVO.getSuName(), goodsVO.getCgName() }), null));
			} catch (Exception e) {
				logger.debug("give_gift: ignore send error", e);
			}

			taskService.updateDailyProByOne(userId, DailyTaskType.GIVE_GIFT);//赠送礼物日常
		}
	}

	@Override
	public void sellGoods(int userId, int subId) throws Exception {
		GoodsBO gBO = null;
		for (GoodsBO tmp : CommonMgr.getInstance().getGoods(userId)) {
			if (tmp.getSubId() == subId) {
				gBO = tmp;
				break;
			}
		}
		if (gBO == null)
			throw new IllegalStateException("not have the goods");

		if (gBO.getType() == GoodsType.MEDAL.getV())
			throw new IllegalStateException("not allowed to sell");

		int selled = new Double(gBO.getPrice() * PingyangConfig.getInstance().getGoodsSellFactor()).intValue();
		userService.incMoney(IncomeType.DEFAULT, userId, MoneyType.GOLD, selled);
		backpackService.delFromBackpack(userId, subId);//从背包中删除
	}
}
