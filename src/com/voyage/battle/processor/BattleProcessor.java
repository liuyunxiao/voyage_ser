package com.voyage.battle.processor;

import java.util.List;

import com.voyage.battle.bo.PlayerBO;
import com.voyage.battle.bo.ReplayBO;
import com.voyage.battle.player.Player;

public interface BattleProcessor {
	/**执行战斗*/
	ReplayBO fight(PlayerBO atker, List<Player> atkers, PlayerBO defer, List<Player> defers) throws Exception;
}
