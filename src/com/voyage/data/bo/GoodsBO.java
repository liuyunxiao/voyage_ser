package com.voyage.data.bo;

import com.voyage.data.vo.CfgGoodsVO;

/**
 * 物品信息
 * */
public class GoodsBO {
	public String name;//名字
	public String img;//图片
	public int reap;//可收入金币（农场、矿场界面有效）
	public int price;//物品价格
	public int subId;//背包主键
	public int type;//类别（1：礼物；2：勋章；4：农场；8：矿场)
	public int pos;//位置
	public int goodsId;//物品表主键

	public GoodsBO(int subId) {
		this.subId = subId;
	}

	public GoodsBO(int subId, int pos, CfgGoodsVO cgVO) {
		this.subId = subId;
		this.pos = pos;
		this.name = cgVO.getCgName();
		this.img = cgVO.getCgImg();
		this.price = cgVO.getCgPrice();
		this.type = cgVO.getCgType();
		this.goodsId = cgVO.getCgId();
	}

	public int getReap() {
		return reap;
	}

	public void setReap(int reap) {
		this.reap = reap;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSubId() {
		return subId;
	}

	public void setSubId(int subId) {
		this.subId = subId;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		int h = 17 * 37 + this.subId;
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof GoodsBO)) {
			return false;
		}

		GoodsBO other = (GoodsBO) obj;

		return this.subId == other.subId;
	}
}
