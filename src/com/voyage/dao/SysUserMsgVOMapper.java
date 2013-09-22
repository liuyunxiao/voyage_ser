package com.voyage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysUserMsgVO;

public interface SysUserMsgVOMapper {
	int deleteByPrimaryKey(Integer sumId);

	int insertSelective(SysUserMsgVO record);

	SysUserMsgVO selectByPrimaryKey(Integer sumId);

	int updateByPrimaryKeySelective(SysUserMsgVO record);

	void deleteApply(@Param("sumType") int sumType, @Param("userId") int userId, @Param("oppoId") int oppoId);

	void deleteOffline(@Param("userId") int userId, @Param("maxSumId") int maxSumId);

	List<SysUserMsgVO> selectOffline(int userId);

	List<SysUserMsgVO> selectByType(@Param("sumType") int sumType);

	/**
	 * 根据最大保存数量获取超出的记录ID起始值
	 */
	Integer selectLimitId(@Param("sumType") int sumType, @Param("maxRecord") int maxRecord);

	/**
	 * 删除超出的记录
	 * @param limitId 超出数量的起始ID
	 */
	public void deleteByLimitId(@Param("sumType") int sumType, @Param("limitId") Integer limitId);
}