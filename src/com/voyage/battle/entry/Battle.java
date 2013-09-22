package com.voyage.battle.entry;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voyage.SpringContext;
import com.voyage.battle.bo.ReplayBO;
import com.voyage.battle.enums.BattleType;
import com.voyage.battle.exception.BattleException;
import com.voyage.battle.handler.BattleHandler;
import com.voyage.battle.handler.pve.ActivityMapHandler;
import com.voyage.battle.handler.pve.MonsterHandler;
import com.voyage.battle.handler.pvp.PkSingleHandler;

public class Battle {
	private final Logger logger = LoggerFactory.getLogger(Battle.class);
	private static Battle instance;
	private Map<BattleType, Class<?>> handlers = new HashMap<BattleType, Class<?>>();

	private Battle() {
		registerHandlers();
	}

	/**注册handler*/
	private void registerHandlers() {
		logger.info("register battle handlers...");
		handlers.put(BattleType.PK_SINGLE, PkSingleHandler.class);
		handlers.put(BattleType.MONSTER, MonsterHandler.class);
		handlers.put(BattleType.ACTIVITY_MAP, ActivityMapHandler.class);
	}

	private BattleHandler getHandler(BattleType bt) throws Exception {
		if (!handlers.containsKey(bt))
			throw new BattleException("handler not register");
		return (BattleHandler) SpringContext.getInstance().getSpringContext().getBean(handlers.get(bt));
	}

	public static Battle getInstance() {
		if (instance == null)
			instance = new Battle();
		return instance;
	}

	/**返回战报*/
	public ReplayBO getReplay(BattleType bt, JSONObject param) throws Exception {
		return getHandler(bt).fight(param);
	}
}
