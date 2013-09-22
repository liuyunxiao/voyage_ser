package com.voyage.service;

import java.util.List;

import com.voyage.data.vo.SysParamVO;
import com.voyage.enums.SysParamType;

public interface IParamService {
	List<SysParamVO> selectByType(SysParamType spType) throws Exception;

	int insertSelective(SysParamVO record) throws Exception;

	int updateByOffset(SysParamVO record) throws Exception;
}
