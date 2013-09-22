package com.voyage.battle.handler.pve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.voyage.battle.bo.PlayerBO;
import com.voyage.battle.bo.PlayerHeadBO;
import com.voyage.battle.bo.ReplayBO;
import com.voyage.battle.enums.BattleType;
import com.voyage.battle.enums.SideType;
import com.voyage.battle.enums.WhetherType;
import com.voyage.battle.handler.AbstractBattleHandler;
import com.voyage.battle.player.Player;
import com.voyage.cache.CfgDataMgr;
import com.voyage.config.BattleConfig;
import com.voyage.constant.Const;
import com.voyage.constant.ErCode;
import com.voyage.data.bo.BorrowUserBO;
import com.voyage.data.vo.CfgMonsterVO;
import com.voyage.data.vo.SysUserBattleCorpsVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.exception.NotifyException;
import com.voyage.service.IMonsterService;
import com.voyage.service.IUserService;

@Component
@Scope("prototype")
public class MonsterHandler extends AbstractBattleHandler {
	@Autowired
	private IUserService userService;
	@Autowired
	private IMonsterService monsterService;
	protected int atkId;
	protected int defId;
	private int cmnId;

	@Override
	protected void after(ReplayBO rb) throws Exception {
		battleService.setMonsterReward(rb, cmnId);
	}

	@Override
	public BattleType getBattleType() {
		return BattleType.MONSTER;
	}

	@Override
	protected void init() throws Exception {
		cmnId = param.getInt("cmnId");//关卡ID
		atkId = param.getInt(Const.USERID);
		if (!monsterService.isNodeOpen(atkId, cmnId))
			throw new NotifyException(ErCode.E402);

		defId = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MAP_NODE, cmnId).getCmnMonsterId();
		BorrowUserBO borrowed = monsterService.getBorrowUser(atkId);//获得被借公主
		SysUserVO atkUser = userService.selectWrapByPk(atkId);
		CfgMonsterVO defMonster = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_MONSTER, defId);
		atker = new PlayerBO(atkUser);
		if (borrowed != null) {
			atker.borrowed.add(new PlayerHeadBO(userService.selectByPk(borrowed.userId)));
		}
		double pf = (atkUser.getSuLevel() + (borrowed == null ? 0 : borrowed.addi)) * BattleConfig.getInstance().getPrincessFactor();//公主加成
		for (SysUserBattleCorpsVO tmp : atkUser.getBattleCorpsList()) {
			atkers.add(new Player(SideType.LEFT, tmp.getSubcPos(), tmp.getSubcCaptain(), tmp.getSubcCorpsVO(), tmp.getAddi(), pf));
		}
		defer = new PlayerBO(defMonster);
		for (SysUserBattleCorpsVO tmp : defMonster.getBattleCorpsList()) {
			defers.add(new Player(SideType.RIGHT, tmp.getSubcPos(), WhetherType.NO.getV(), tmp.getSubcCorpsVO(), tmp.getAddi(), 0));
		}
	}
}
