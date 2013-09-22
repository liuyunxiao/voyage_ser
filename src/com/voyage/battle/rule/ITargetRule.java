package com.voyage.battle.rule;

import java.util.List;

import com.voyage.battle.board.Battleboard;
import com.voyage.battle.board.BoardPoint;

public interface ITargetRule {
	/**技能目标
	 * @param source 攻击者
	 * @param target 物理防守者
	 * @param rule 技能规则
	 * */
	List<BoardPoint> getTarget(BoardPoint source, BoardPoint target, String rule, Battleboard board) throws Exception;

	/**物理目标
	 * @param source 攻击者
	 * */
	BoardPoint getTarget(BoardPoint source, Battleboard board) throws Exception;
}
