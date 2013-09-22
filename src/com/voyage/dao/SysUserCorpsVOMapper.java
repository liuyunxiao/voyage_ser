package com.voyage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysUserCorpsVO;

public interface SysUserCorpsVOMapper {
	int deleteByPrimaryKey(Integer sucId);

	int insertSelective(SysUserCorpsVO record);

	SysUserCorpsVO selectByPrimaryKey(Integer sucId);

	int updateByPrimaryKeySelective(SysUserCorpsVO record);

	List<SysUserCorpsVO> selectByUserId(@Param("userId") int userId);

	SysUserCorpsVO selectByCorpsId(@Param("userId") int userId, @Param("corpsId") int corpsId);
}