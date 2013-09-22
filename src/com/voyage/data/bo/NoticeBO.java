package com.voyage.data.bo;

import com.voyage.data.vo.SysNoticeVO;

/**
 * 系统公告信息
 * */
public class NoticeBO {
	public int type;//类别(1.活动2.维护3.补偿 4.更新) 
	public String name;//公告名称
	public String content;//内容
	public long start;//起如时间
	public long end;//截止时间

	public NoticeBO(SysNoticeVO snVO) {
		super();
		this.type = snVO.getSnType();
		this.name = snVO.getSnName();
		this.content = snVO.getSnContent();
		this.start = snVO.getSnStart().getTime();
		this.end = snVO.getSnEnd().getTime();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
}
