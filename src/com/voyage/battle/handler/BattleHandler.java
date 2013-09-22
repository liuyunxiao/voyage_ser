package com.voyage.battle.handler;

import org.json.JSONObject;

import com.voyage.battle.bo.ReplayBO;
import com.voyage.battle.enums.BattleType;

public interface BattleHandler {
	/**单挑模式*/
	ReplayBO fight(JSONObject param) throws Exception;

	/**
	 * 战斗类型
	 * @return
	 */
	BattleType getBattleType();
}
