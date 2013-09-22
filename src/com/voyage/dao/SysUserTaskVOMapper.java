package com.voyage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysUserTaskVO;

public interface SysUserTaskVOMapper {
	int deleteByPrimaryKey(Integer sutId);

	int insertSelective(SysUserTaskVO record);

	SysUserTaskVO selectByPrimaryKey(Integer sutId);

	int updateByPrimaryKeySelective(SysUserTaskVO record);

	List<SysUserTaskVO> selectNotReceived(@Param("userId") int userId, @Param("sutState") int sutState);

	Integer selectCountByState(@Param("userId") int userId, @Param("sutState") int sutState);

	List<SysUserTaskVO> selectByState(@Param("userId") int userId, @Param("sutState") int sutState);

	List<SysUserTaskVO> selectByStateAndTypes(@Param("userId") int userId, @Param("sutState") int sutState, @Param("taskTypes") String taskTypes);
}