package com.voyage.battle.round;

import java.util.List;

import com.voyage.battle.board.Battleboard;
import com.voyage.battle.board.BoardPoint;
import com.voyage.battle.enums.SideType;
import com.voyage.battle.player.Player;
import com.voyage.battle.rule.ITargetRule;

/**
 *回合上下文数据
 */
public class RoundContext {
	public List<Player> atkers;
	public List<Player> defers;
	public Battleboard board;
	public ITargetRule targetRule;
	public int round;//当前回合
	public BoardPoint source;//行动者
	public BoardPoint target;//物理目标
	public Player atk;//行动者
	public Player def;//物理目标

	public RoundContext(Battleboard board, ITargetRule targetRule) {
		super();
		this.board = board;
		this.targetRule = targetRule;
	}

	public void setPlayers(List<Player> atkers, List<Player> defers) {
		this.atkers = atkers;
		this.defers = defers;
	}

	public void resetContext(int lastActPos) throws Exception {
		this.source = board.getBoardPoint(lastActPos);
		this.target = targetRule.getTarget(source, board);
		this.atk = getPlayer(source.pos);
		this.def = getPlayer(target.pos);
	}

	public Player getPlayer(int pos) {
		List<Player> players = pos / Battleboard.OFFSET_BASE == SideType.LEFT.getV() ? atkers : defers;
		for (Player p : players) {
			if (p.pos == pos) {
				return p;
			}
		}
		return null;
	}

	public List<Player> getPlayers(int side) {
		return side == SideType.LEFT.getV() ? atkers : defers;
	}
}
