package com.voyage.dao;

import java.util.List;

import com.voyage.data.vo.SysParamVO;

public interface SysParamVOMapper {
	int deleteByPrimaryKey(Integer spId);

	int insertSelective(SysParamVO record);

	SysParamVO selectByPrimaryKey(Integer spId);

	int updateByPrimaryKeySelective(SysParamVO record);

	List<SysParamVO> selectByType(Integer spType);

	int updateByOffset(SysParamVO record);

}