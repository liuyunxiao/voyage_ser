package com.voyage.data.bo;

import java.util.List;

import com.voyage.battle.bo.ExpBO;
import com.voyage.cache.RankMgr;
import com.voyage.config.BattleConfig;
import com.voyage.data.vo.SysUserVO;
import com.voyage.util.CommonUtil;

/**
 * 玩家信息
 * */
public class UserExtBO {
	public int userId;//玩家ID
	public String name;//名字
	public String img;//头像
	public int level;//等级
	public int vipLevel;//VIP等级
	public Integer ago;//离线时间,N小时前,为0时，前者代表1小时内
	public String remark;//备注
	public Integer friend;//是否好友（0：否；1：是）
	public List<GoodsBO> goods;//物品列表

	/**其它更详细的信息*/
	public Integer gold;//玩家金币数
	public Integer mine;//矿场成本金
	public Integer farm;//农场成本金
	public Integer rank;//排名
	public String idAlias;//ID别名
	public String contact;//联系
	public Double addi;//统帅度
	public ExpBO suExp;//玩家经验相关

	public UserExtBO(int userId) {
		super();
		this.userId = userId;
	}

	/**
	 * @param toDetail 是否显示其它更详细的信息
	 * */
	public UserExtBO(SysUserVO suVO, boolean toDetail) throws Exception {
		this.userId = suVO.getSuId();
		this.name = suVO.getSuName();
		this.img = suVO.getSuImg();
		this.level = suVO.getSuLevel();
		this.vipLevel = suVO.getSuVipLevel();
		this.remark = suVO.getSuRemark();
		this.ago = suVO.getSuLogout().before(suVO.getSuLogin()) ? 0 : Math.max(1, new Long((System.currentTimeMillis() - suVO.getSuLogout().getTime())
				/ (1000 * 60 * 60)).intValue());//离线时间
		if (toDetail) {
			gold = suVO.getSuGoldVO().getSugStorage();
			idAlias = suVO.getSuIdAlias();
			//contact = suVO.getSuContact();
			mine = suVO.getSuGoldVO().getSugMine();
			farm = suVO.getSuGoldVO().getSugFarm();
			addi = suVO.getSuLevel() * BattleConfig.getInstance().getPrincessFactorExpand();
			suExp = CommonUtil.getExpBO(suVO.getSuLevel(), suVO.getSuExp());
			rank = RankMgr.getInstance().getRank(suVO.getSuId()).rank;
		}
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getFriend() {
		return friend;
	}

	public void setFriend(int friend) {
		this.friend = friend;
	}

	public Integer getAgo() {
		return ago;
	}

	public void setAgo(Integer ago) {
		this.ago = ago;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getMine() {
		return mine;
	}

	public void setMine(Integer mine) {
		this.mine = mine;
	}

	public Integer getFarm() {
		return farm;
	}

	public void setFarm(Integer farm) {
		this.farm = farm;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getIdAlias() {
		return idAlias;
	}

	public void setIdAlias(String idAlias) {
		this.idAlias = idAlias;
	}

	public void setFriend(Integer friend) {
		this.friend = friend;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public List<GoodsBO> getGoods() {
		return goods;
	}

	public void setGoods(List<GoodsBO> goods) {
		this.goods = goods;
	}

	public Double getAddi() {
		return addi;
	}

	public void setAddi(Double addi) {
		this.addi = addi;
	}

	public ExpBO getSuExp() {
		return suExp;
	}

	public void setSuExp(ExpBO suExp) {
		this.suExp = suExp;
	}
}
