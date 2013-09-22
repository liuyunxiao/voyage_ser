package com.voyage.dao;

import java.util.List;

import com.voyage.data.vo.SysNoticeVO;

public interface SysNoticeVOMapper {
	int deleteByPrimaryKey(Integer snId);

	int insertSelective(SysNoticeVO record);

	SysNoticeVO selectByPrimaryKey(Integer snId);

	int updateByPrimaryKeySelective(SysNoticeVO record);

	int deleteInvalid();

	List<SysNoticeVO> selectValid();
}