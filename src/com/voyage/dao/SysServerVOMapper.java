package com.voyage.dao;

import java.util.List;

import com.voyage.data.vo.SysServerVO;

public interface SysServerVOMapper {
	int deleteByPrimaryKey(Integer ssId);

	int insertSelective(SysServerVO record);

	SysServerVO selectByPrimaryKey(Integer ssId);

	int updateByPrimaryKeySelective(SysServerVO record);

	List<SysServerVO> selectAll();
}