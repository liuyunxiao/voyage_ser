package com.voyage.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyage.battle.enums.WhetherType;
import com.voyage.config.PingyangConfig;
import com.voyage.dao.SysUserBattleRecordVOMapper;
import com.voyage.data.bo.BattleReportBO;
import com.voyage.data.vo.SysUserBattleRecordVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.service.IDefendService;

@Service
public class DefendService implements IDefendService {
	@Autowired
	private SysUserBattleRecordVOMapper battleRecordMapper;

	@Override
	public List<BattleReportBO> enterReport(int userId) throws Exception {
		List<BattleReportBO> rt = new ArrayList<BattleReportBO>(1);
		battleRecordMapper.updateAllRead(userId, WhetherType.YES.getV());//更新所有战报为已读
		List<SysUserBattleRecordVO> records = battleRecordMapper.selectBattleRecords(userId, PingyangConfig.getInstance().getMaxBattleRecord());

		SysUserVO user = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (SysUserBattleRecordVO tmp : records) {
			user = tmp.getSubrUserVO();
			rt.add(new BattleReportBO(tmp.getSubrReplayId(), sdf.format(tmp.getSubrTime()), user.getSuId(), user.getSuName(), user.getSuLevel(), user
					.getSuVipLevel(), user.getSuImg(), tmp.getSubrWinside(), tmp.getSubrOppoGold()));
		}
		return rt;
	}

	@Override
	public int countOfUnread(int userId) throws Exception {
		Integer un = battleRecordMapper.selectUnreadCount(userId, WhetherType.NO.getV());
		return un == null ? 0 : un;
	}
}
