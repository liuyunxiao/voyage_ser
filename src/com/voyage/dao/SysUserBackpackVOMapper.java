package com.voyage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysUserBackpackVO;

public interface SysUserBackpackVOMapper {
	int deleteByPrimaryKey(Integer subId);

	int insertSelective(SysUserBackpackVO record);

	SysUserBackpackVO selectByPrimaryKey(Integer subId);

	int updateByPrimaryKeySelective(SysUserBackpackVO record);

	List<SysUserBackpackVO> selectByUserId(@Param("userId") int userId);

	List<SysUserBackpackVO> selectByType(@Param("userId") int userId, @Param("subType") int subType);

	Integer selectMaxPosByType(@Param("userId") int userId, @Param("subType") int subType);

	SysUserBackpackVO selectByTypeAndPos(@Param("userId") int userId, @Param("subType") int subType, @Param("pos") int pos);
}