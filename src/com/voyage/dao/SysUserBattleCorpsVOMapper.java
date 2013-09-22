package com.voyage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysUserBattleCorpsVO;

public interface SysUserBattleCorpsVOMapper {
	int deleteByPrimaryKey(Integer subcId);

	int insertSelective(SysUserBattleCorpsVO record);

	SysUserBattleCorpsVO selectByPrimaryKey(Integer subcId);

	int updateByPrimaryKeySelective(SysUserBattleCorpsVO record);

	List<SysUserBattleCorpsVO> selectBattle(@Param("userId") int userId);

	List<SysUserBattleCorpsVO> selectCaptains(@Param("userId") int userId, @Param("state") int state);

	SysUserBattleCorpsVO selectByPos(@Param("userId") int userId, @Param("subcPos") int subcPos);

	int selectCaptainCount(@Param("userId") int userId);

	int resetCorps(@Param("userId") int userId);

	int deleteByIds(@Param("subcIds") String subcIds);
}