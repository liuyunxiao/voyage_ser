package com.voyage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysUserRelationVO;

public interface SysUserRelationVOMapper {
	int deleteByPrimaryKey(Integer surId);

	int insertSelective(SysUserRelationVO record);

	SysUserRelationVO selectByPrimaryKey(Integer surId);

	int updateByPrimaryKeySelective(SysUserRelationVO record);

	SysUserRelationVO selectFriend(@Param("userId") int userId, @Param("oppoId") int oppoId);

	void deleteFriend(@Param("userId") int userId, @Param("oppoId") int oppoId);

	List<SysUserRelationVO> selectAll();

}