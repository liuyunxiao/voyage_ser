package com.voyage.battle.handler.pvp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.voyage.battle.bo.PlayerBO;
import com.voyage.battle.bo.ReplayBO;
import com.voyage.battle.enums.BattleType;
import com.voyage.battle.enums.SideType;
import com.voyage.battle.handler.AbstractBattleHandler;
import com.voyage.battle.player.Player;
import com.voyage.config.BattleConfig;
import com.voyage.constant.Const;
import com.voyage.constant.ErCode;
import com.voyage.data.vo.SysUserBattleCorpsVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.exception.NotifyException;
import com.voyage.service.IUserService;
import com.voyage.socket.SocketSessionManager;

@Component
@Scope("prototype")
public class PkSingleHandler extends AbstractBattleHandler {
	@Autowired
	private IUserService userService;
	protected int atkId;
	protected int defId;

	@Override
	protected void after(ReplayBO rb) throws Exception {
		battleService.setPkSingleReward(rb);
	}

	@Override
	public BattleType getBattleType() {
		return BattleType.PK_SINGLE;
	}

	@Override
	protected void init() throws Exception {
		atkId = param.getInt(Const.USERID);
		defId = param.getInt(Const.DEFID);
		if (atkId == defId)
			throw new IllegalStateException("kill yourself");

		if (SocketSessionManager.INSTANCE.isOnLine(defId) || battleService.isProtected(defId)) {//判断对方是否处于保护期
			throw new NotifyException(ErCode.E302);
		}
		//解除攻方受保护期
		battleService.belayOff(atkId);

		SysUserVO atkUser = userService.selectWrapByPk(atkId);
		SysUserVO defUser = userService.selectWrapByPk(defId);
		atker = new PlayerBO(atkUser);
		defer = new PlayerBO(defUser);
		initPlayers(SideType.LEFT, atkUser);
		initPlayers(SideType.RIGHT, defUser);
	}

	private void initPlayers(SideType side, SysUserVO user) {
		if (user.getBattleCorpsList() == null || user.getBattleCorpsList().size() == 0)
			return;
		List<Player> players = side == SideType.LEFT ? atkers : defers;
		double pf = user.getSuLevel() * BattleConfig.getInstance().getPrincessFactor();//公主加成
		for (SysUserBattleCorpsVO tmp : user.getBattleCorpsList()) {
			players.add(new Player(side, tmp.getSubcPos(), tmp.getSubcCaptain(), tmp.getSubcCorpsVO(), tmp.getAddi(), pf));
		}
	}

}
