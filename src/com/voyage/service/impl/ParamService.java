package com.voyage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyage.dao.SysParamVOMapper;
import com.voyage.data.vo.SysParamVO;
import com.voyage.enums.SysParamType;
import com.voyage.service.IParamService;

@Service
public class ParamService implements IParamService {
	@Autowired
	private SysParamVOMapper paramMapper;

	@Override
	public List<SysParamVO> selectByType(SysParamType spType) throws Exception {
		return paramMapper.selectByType(spType.getV());
	}

	@Override
	public int insertSelective(SysParamVO record) throws Exception {
		return paramMapper.insertSelective(record);
	}

	@Override
	public int updateByOffset(SysParamVO record) throws Exception {
		return paramMapper.updateByOffset(record);
	}
}
