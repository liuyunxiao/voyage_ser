package com.voyage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysUserDailyTaskVO;

public interface SysUserDailyTaskVOMapper {
	int deleteByPrimaryKey(Integer sudtId);

	int insertSelective(SysUserDailyTaskVO record);

	SysUserDailyTaskVO selectByPrimaryKey(Integer sudtId);

	int updateByPrimaryKeySelective(SysUserDailyTaskVO record);

	int updateProByOffset(SysUserDailyTaskVO record);

	int resetAll(SysUserDailyTaskVO record);

	List<Integer> selectTypesByUserId(Integer userId);

	List<SysUserDailyTaskVO> selectByUserId(Integer userId);

	int deleteByTypes(@Param("userId") int userId, @Param("tTypes") String tTypes);
}