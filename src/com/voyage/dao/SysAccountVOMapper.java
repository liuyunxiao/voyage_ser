package com.voyage.dao;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysAccountVO;

public interface SysAccountVOMapper {
	int deleteByPrimaryKey(Integer saId);

	int insertSelective(SysAccountVO record);

	SysAccountVO selectByPrimaryKey(Integer saId);

	int updateByPrimaryKeySelective(SysAccountVO record);

	SysAccountVO selectByName(String name);

	SysAccountVO selectByUuid(String saUuid);

	SysAccountVO selectOne(@Param("name") String name, @Param("password") String password);
}