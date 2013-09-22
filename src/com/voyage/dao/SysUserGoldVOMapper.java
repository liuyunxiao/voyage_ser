package com.voyage.dao;

import com.voyage.data.vo.SysUserGoldVO;

public interface SysUserGoldVOMapper {
	int deleteByPrimaryKey(Integer sugId);

	int insertSelective(SysUserGoldVO record);

	SysUserGoldVO selectByPrimaryKey(Integer sugId);

	int updateByPrimaryKeySelective(SysUserGoldVO record);

	int updateOffsetByUserId(SysUserGoldVO record);

	SysUserGoldVO selectByUserId(Integer userId);
}