package com.voyage.config;

import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voyage.constant.Const;

/**battle.properties*/
public class BattleConfig {
	private Logger logger = LoggerFactory.getLogger(BattleConfig.class);

	private static BattleConfig instance;
	private double hpFactor;
	private double critFactor;
	private int critRate;
	private double shortAtkFactor;
	private double shortDefFactor;

	private double moreDefFactor;
	private double lessDefFactor;
	private double moreAtkFactor;
	private double lessAtkFactor;
	private double specialAtkFactor;
	private int maxRounds;
	private double princessFactor;

	public static BattleConfig getInstance() {
		if (instance == null)
			instance = new BattleConfig();
		return instance;
	}

	private BattleConfig() {
		final String bcName = Const.BATTLE_CONFIG;
		Properties properties = new Properties();
		try {
			String battleConfig = Thread.currentThread().getContextClassLoader().getResource(bcName).getPath();

			properties.load(new FileInputStream(battleConfig));
			maxRounds = Integer.parseInt(properties.getProperty("max_rounds"));
			hpFactor = Double.parseDouble(properties.getProperty("hp_factor"));
			critRate = Integer.parseInt(properties.getProperty("crit_rate"));
			critFactor = Double.parseDouble(properties.getProperty("crit_factor"));
			shortAtkFactor = Double.parseDouble(properties.getProperty("short_atk_factor"));
			shortDefFactor = Double.parseDouble(properties.getProperty("short_def_factor"));
			moreAtkFactor = Double.parseDouble(properties.getProperty("more_atk_factor"));
			lessAtkFactor = Double.parseDouble(properties.getProperty("less_atk_factor"));
			moreDefFactor = Double.parseDouble(properties.getProperty("more_def_factor"));
			lessDefFactor = Double.parseDouble(properties.getProperty("less_def_factor"));
			specialAtkFactor = Double.parseDouble(properties.getProperty("special_atk_factor"));
			princessFactor = Double.parseDouble(properties.getProperty("princess_factor"));
			logger.info(bcName + " init success");
		} catch (Exception e) {
			logger.error(bcName + " init fail", e);
		}
	}

	public double getSpecialAtkFactor() {
		return specialAtkFactor;
	}

	public void setSpecialAtkFactor(double specialAtkFactor) {
		this.specialAtkFactor = specialAtkFactor;
	}

	public double getHpFactor() {
		return hpFactor;
	}

	public void setHpFactor(double hpFactor) {
		this.hpFactor = hpFactor;
	}

	public double getCritFactor() {
		return critFactor;
	}

	public void setCritFactor(double critFactor) {
		this.critFactor = critFactor;
	}

	public int getCritRate() {
		return critRate;
	}

	public void setCritRate(int critRate) {
		this.critRate = critRate;
	}

	public double getShortAtkFactor() {
		return shortAtkFactor;
	}

	public void setShortAtkFactor(double shortAtkFactor) {
		this.shortAtkFactor = shortAtkFactor;
	}

	public double getShortDefFactor() {
		return shortDefFactor;
	}

	public void setShortDefFactor(double shortDefFactor) {
		this.shortDefFactor = shortDefFactor;
	}

	public double getMoreDefFactor() {
		return moreDefFactor;
	}

	public void setMoreDefFactor(double moreDefFactor) {
		this.moreDefFactor = moreDefFactor;
	}

	public double getLessDefFactor() {
		return lessDefFactor;
	}

	public void setLessDefFactor(double lessDefFactor) {
		this.lessDefFactor = lessDefFactor;
	}

	public double getMoreAtkFactor() {
		return moreAtkFactor;
	}

	public void setMoreAtkFactor(double moreAtkFactor) {
		this.moreAtkFactor = moreAtkFactor;
	}

	public double getLessAtkFactor() {
		return lessAtkFactor;
	}

	public void setLessAtkFactor(double lessAtkFactor) {
		this.lessAtkFactor = lessAtkFactor;
	}

	public int getMaxRounds() {
		return maxRounds;
	}

	public void setMaxRounds(int maxRounds) {
		this.maxRounds = maxRounds;
	}

	public double getPrincessFactor() {
		return princessFactor;
	}

	public double getPrincessFactorExpand() {
		return princessFactor * Const.PERCENT_BASE;
	}
}
