package com.voyage.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyage.config.PingyangConfig;
import com.voyage.constant.Const;
import com.voyage.dao.SysUserVOMapper;
import com.voyage.data.bo.PkSingleBO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.service.IPkSingleService;
import com.voyage.util.CommonUtil;

@Service
public class PkSingleService implements IPkSingleService {
	@Autowired
	private SysUserVOMapper userMapper;

	@Override
	public List<PkSingleBO> enter(boolean only, int oppoId) throws Exception {
		SysUserVO suVO = userMapper.selectFullByPk(oppoId);
		List<PkSingleBO> rt = new ArrayList<PkSingleBO>(10);
		if (only) {//指定玩家
			rt.add(new PkSingleBO(suVO.getSuId(), suVO.getSuName(), suVO.getSuLevel(), suVO.getSuVipLevel(), suVO.getSuImg(), suVO.getSuRemark(), CommonUtil
					.getCaptains(suVO.getSuCaptain())));
		} else {
			//PVP对战列表
			int gold = suVO.getSuGoldVO().getSugStorage() + suVO.getSuGoldVO().getSugUpCorpsProp() + suVO.getSuGoldVO().getSugBattleCorps();//计算流动资产
			List<SysUserVO> leftUserList = userMapper.selectLeftOfflineLive(gold, PingyangConfig.getInstance().getLiveHour(), PingyangConfig.getInstance()
					.getRankOffset());//查找左侧满足的离线活跃玩家
			List<SysUserVO> rightUserList = userMapper.selectRightOfflineLive(gold, PingyangConfig.getInstance().getLiveHour(), PingyangConfig.getInstance()
					.getRankOffset());//查找右侧侧满足的离线活跃玩家
			Set<SysUserVO> userSet = new HashSet<SysUserVO>();//去重复
			userSet.addAll(leftUserList);
			userSet.addAll(rightUserList);
			userSet.remove(new SysUserVO(oppoId));//排除自己

			List<SysUserVO> users = new ArrayList<SysUserVO>(userSet);

			List<SysUserVO> userList = new ArrayList<SysUserVO>();
			final int n = Const.PAGE_LIMIT;//随机取N个玩家
			if (users.size() > n) {
				for (int i = 0; i < n; i++) {
					userList.add(users.remove(Const.RAND.nextInt(users.size())));
				}
			} else {
				userList.addAll(users);
			}
			for (SysUserVO user : userList) {
				rt.add(new PkSingleBO(user.getSuId(), user.getSuName(), user.getSuLevel(), user.getSuVipLevel(), user.getSuImg(), user.getSuRemark(),
						CommonUtil.getCaptains(user.getSuCaptain())));
			}
		}
		return rt;
	}
}
