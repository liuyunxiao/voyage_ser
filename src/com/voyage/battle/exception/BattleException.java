package com.voyage.battle.exception;

/**战斗异常*/
public class BattleException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1906056232321182955L;

	public BattleException() {
	}

	public BattleException(String message) {
		super(message);
	}

	public BattleException(String message, Throwable cause) {
		super(message, cause);
	}

	public BattleException(Throwable cause) {
		super(cause);
	}
}
