package com.voyage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysUserMapNodeVO;

public interface SysUserMapNodeVOMapper {
	int deleteByPrimaryKey(Integer sumnId);

	int insertSelective(SysUserMapNodeVO record);

	SysUserMapNodeVO selectByPrimaryKey(Integer sumnId);

	int updateByPrimaryKeySelective(SysUserMapNodeVO record);

	/**根据主键倒序排列*/
	List<SysUserMapNodeVO> selectByUserId(@Param("userId") int userId);

	SysUserMapNodeVO selectByNodeId(@Param("userId") int userId, @Param("nodeId") int nodeId);

	/**根据主键倒序排列*/
	List<SysUserMapNodeVO> selectByType(@Param("userId") int userId, @Param("nodeTypeId") int nodeTypeId);

	int updateState(@Param("userId") int userId, @Param("nodeId") int nodeId, @Param("sumnState") int sumnState);

	/**某类副本节点通过的个数*/
	Integer selectCountByType(@Param("userId") int userId, @Param("nodeTypeId") int nodeTypeId, @Param("sumnState") int sumnState);
}