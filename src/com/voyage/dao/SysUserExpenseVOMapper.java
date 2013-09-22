package com.voyage.dao;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysUserExpenseVO;

public interface SysUserExpenseVOMapper {
	int deleteByPrimaryKey(Integer sueId);

	int insertSelective(SysUserExpenseVO record);

	SysUserExpenseVO selectByPrimaryKey(Integer sueId);

	int updateByPrimaryKeySelective(SysUserExpenseVO record);

	/**
	 * 统计某类消费的次数
	 * */
	Integer selectCountByType(@Param("userId") int userId, @Param("sueType") int sueType);

	/**
	 * 根据额外信息统计次数
	 * */
	Integer selectCountByDesc(@Param("userId") int userId, @Param("sueDesc") String sueDesc);

}