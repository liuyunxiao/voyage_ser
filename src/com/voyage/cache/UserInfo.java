package com.voyage.cache;

import java.util.ArrayList;
import java.util.List;

import com.voyage.config.PingyangConfig;
import com.voyage.data.bo.GoodsBO;
import com.voyage.data.vo.CfgLivenessVO;
import com.voyage.data.vo.SysUserVO;

/**
 * 玩家的一些缓存信息
 * */
public class UserInfo {
	private int userId;
	private int accountId;
	private String name;
	private int maxGiveSendGold;//当天最大可赠送金币
	private int maxGiveReceiveGold;//当天最大可接收的赠送金币
	private List<Integer> festivals = new ArrayList<Integer>(1);//已领的节假日ID
	private List<GoodsBO> goods = new ArrayList<GoodsBO>(1);//货物列表
	private List<Integer> liveness = new ArrayList<Integer>(1);//已领的活跃奖励ID
	private int nLive;//当前活跃度

	public UserInfo(int userId) {
		super();
		this.userId = userId;
	}

	/**
	 * 获得可领取的活跃奖励数
	 * */
	public int getFinishedLiveness() {
		int n = 0;
		List<CfgLivenessVO> lives = CfgDataMgr.getInstance().getList(CfgDataMgr.KEY_CFG_LIVENESS);
		for (CfgLivenessVO clVO : lives) {
			if (clVO.getClN() <= nLive)
				n += 1;
		}
		return n - liveness.size();
	}

	public List<Integer> getLiveness() {
		return liveness;
	}

	public void setLiveness(List<Integer> liveness) {
		this.liveness = liveness;
	}

	public int getnLive() {
		return nLive;
	}

	public void setnLive(int nLive) {
		this.nLive = nLive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserInfo(SysUserVO suVO) {
		this.userId = suVO.getSuId();
		this.accountId = suVO.getSuAccountVO().getSaId();
		this.name = suVO.getSuName();
		this.maxGiveSendGold = PingyangConfig.getInstance().getMaxGiveSendGold();
		this.maxGiveReceiveGold = PingyangConfig.getInstance().getMaxGiveReceiveGold();
	}

	public List<GoodsBO> getGoods() {
		return goods;
	}

	public void setGoods(List<GoodsBO> goods) {
		this.goods = goods;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public List<Integer> getFestivals() {
		return festivals;
	}

	public void setFestivals(List<Integer> festivals) {
		this.festivals = festivals;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getMaxGiveSendGold() {
		return maxGiveSendGold;
	}

	public void setMaxGiveSendGold(int maxGiveSendGold) {
		this.maxGiveSendGold = maxGiveSendGold;
	}

	public int getMaxGiveReceiveGold() {
		return maxGiveReceiveGold;
	}

	public void setMaxGiveReceiveGold(int maxGiveReceiveGold) {
		this.maxGiveReceiveGold = maxGiveReceiveGold;
	}
}
