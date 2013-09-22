package com.voyage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysUserActivityMapVO;

public interface SysUserActivityMapVOMapper {
	int deleteByPrimaryKey(Integer suamId);

	int insertSelective(SysUserActivityMapVO record);

	SysUserActivityMapVO selectByPrimaryKey(Integer suamId);

	int updateByPrimaryKeySelective(SysUserActivityMapVO record);

	List<SysUserActivityMapVO> selectByUserId(Integer userId);

	int updateOffsetByUserId(SysUserActivityMapVO record);

	SysUserActivityMapVO selectByActivityId(@Param("userId") int userId, @Param("activityId") int activityId);

	int resetAllActivityMap();
}