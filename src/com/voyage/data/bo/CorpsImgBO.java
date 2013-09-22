package com.voyage.data.bo;

import java.io.Serializable;

/**
 * 兵种头像相关
 * */
public class CorpsImgBO implements Serializable {
	private static final long serialVersionUID = 3675410918562581680L;
	public String name;//兵种名
	public String img;//兵种头像
	public String bgImg;//兵种背景图片
	public Integer corpsId;//兵种ID

	public Integer getCorpsId() {
		return corpsId;
	}

	public void setCorpsId(Integer corpsId) {
		this.corpsId = corpsId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getBgImg() {
		return bgImg;
	}

	public void setBgImg(String bgImg) {
		this.bgImg = bgImg;
	}

	public CorpsImgBO(String img, String bgImg) {
		this(null, null, img, bgImg);
	}

	public CorpsImgBO(Integer corpsId, String img, String bgImg) {
		this(corpsId, null, img, bgImg);
	}

	public CorpsImgBO(String name, String img, String bgImg) {
		this(null, name, img, bgImg);
	}

	public CorpsImgBO(Integer corpsId, String name, String img, String bgImg) {
		super();
		this.corpsId = corpsId;
		this.name = name;
		this.img = img;
		this.bgImg = bgImg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
