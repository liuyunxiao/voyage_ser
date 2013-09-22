package com.voyage.dao;

import java.util.List;

import com.voyage.data.vo.SysUserSlotsVO;

public interface SysUserSlotsVOMapper {
	int deleteByPrimaryKey(Integer susId);

	int insertSelective(SysUserSlotsVO record);

	SysUserSlotsVO selectByPrimaryKey(Integer susId);

	int updateByPrimaryKeySelective(SysUserSlotsVO record);

	int resetAllScore();

	List<SysUserSlotsVO> selectAllByScoreDesc(int nRecord);

	SysUserSlotsVO selectByUserId(int userId);

	int updateOffsetByUserId(SysUserSlotsVO record);
}