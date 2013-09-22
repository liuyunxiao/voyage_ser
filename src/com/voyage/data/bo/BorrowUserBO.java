package com.voyage.data.bo;

/**
 * 被借公主的信息
 * */
public class BorrowUserBO {
	public double addi;//属性加成值
	public String img;//公主头像
	public int userId;//玩家ID，不生成JSON

	public BorrowUserBO(int userId, String img, double addi) {
		super();
		this.userId = userId;
		this.img = img;
		this.addi = addi;
	}

	public double getAddi() {
		return addi;
	}

	public void setAddi(double addi) {
		this.addi = addi;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
