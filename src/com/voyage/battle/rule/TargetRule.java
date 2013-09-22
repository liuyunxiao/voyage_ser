package com.voyage.battle.rule;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.voyage.battle.board.Battleboard;
import com.voyage.battle.board.BoardPoint;
import com.voyage.battle.enums.RuleSecondType;
import com.voyage.battle.enums.SideType;
import com.voyage.battle.enums.WhetherType;
import com.voyage.battle.exception.BattleException;
import com.voyage.constant.Const;

public class TargetRule implements ITargetRule {

	@Override
	public BoardPoint getTarget(BoardPoint source, Battleboard board) throws Exception {
		int defSide = source.pos / Battleboard.OFFSET_BASE == SideType.LEFT.getV() ? SideType.RIGHT.getV() : SideType.LEFT.getV();
		int defFirstAlivePos = board.findFirstAlive(defSide);
		int oppDefPos = board.pointToPos(new Point(board.posToPoint(defFirstAlivePos, defSide).x, board.posToPoint(source.pos, source.pos
				/ Battleboard.OFFSET_BASE).y), defSide);//与defFirstAlivePos同一排且与source同一列的点
		int defAlivePos = board.getBoardPoint(oppDefPos).qAlive() ? oppDefPos : defFirstAlivePos;
		return board.getBoardPoint(defAlivePos);
	}

	@Override
	public List<BoardPoint> getTarget(BoardPoint source, BoardPoint target, String rule, Battleboard board) throws Exception {
		if (!rule.matches("\\d{4}"))
			throw new BattleException("illegal rule");
		List<BoardPoint> targets = new ArrayList<BoardPoint>(1);
		int[] rules = new int[4];
		for (int i = 0; i < rule.length(); i++) {
			rules[i] = Integer.parseInt(String.valueOf(rule.charAt(i)));
		}
		int side = (rules[0] == SideType.LEFT.getV() ? source.pos : target.pos) / Battleboard.OFFSET_BASE;
		RuleSecondType ruleSecondType = RuleSecondType.parse(rules[1]);
		List<BoardPoint> rands = new ArrayList<BoardPoint>(1);
		switch (ruleSecondType) {
		case SELF:
			if (source.qAlive())
				targets.add(source);
			break;
		case TARGET:
			if (target.qAlive())
				targets.add(target);
			break;
		case SOURCE:
			if (target.qAlive())
				targets.add(target);
			break;
		case ROW:
			rands = board.getAlives(side, true, rules[2]);
			fillTargets(targets, rules[3], rands);
			break;
		case COL:
			rands = board.getAlives(side, false, rules[2]);
			fillTargets(targets, rules[3], rands);
			break;
		case ALL:
			rands = board.getAlives(side);
			fillTargets(targets, rules[3], rands);
			break;
		case LIVEROW:
			for (int i = 0; i < 3 && rands.size() == 0; i++) {
				rands = board.getAlives(side, rules[2] == WhetherType.YES.getV(), i);
			}
			fillTargets(targets, rules[3], rands);
			break;
		default:
			break;
		}
		return targets;
	}

	private void fillTargets(List<BoardPoint> targets, int n, List<BoardPoint> rands) {
		BoardPoint bp = null;
		while (rands.size() > 0 && targets.size() < n) {
			bp = rands.get(Const.RAND.nextInt(rands.size()));
			targets.add(bp);
			rands.remove(bp);
		}
	}
}
