package com.voyage.config;

import java.awt.Point;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voyage.battle.bo.ReplayBO;
import com.voyage.constant.Const;
import com.voyage.util.CloneUtil;

public class PingyangConfig {
	private Logger logger = LoggerFactory.getLogger(PingyangConfig.class);
	private static PingyangConfig instance;
	private int rankOffset;
	private int liveHour;
	private String replayDiskDir;
	private int replayToDisk;
	private int socket_port;
	private int jmx_port;
	private double resetDotRepay;
	private int bigRateDrop;
	private int maxBattleRecord;
	private String crontabDailyReset;
	private int maxGiveSendGold;
	private int maxGiveReceiveGold;
	private Point borrowIncome;
	private String version;
	private int chatInterval;
	private String wsUrl;
	private String cfgDb;
	private String rsVer;
	private double pvpIncomeFactor;
	private double goodsSellFactor;
	private ReplayBO newerReplay;
	private double bigPondFactor;
	private double smallPondFactor;
	private double slotsWinDifficulty;
	private int bigPondInit;
	private int smallPondInit;
	private Point bigPondWin;
	private Point smallPondWin;
	private String crontabWeekReset;
	private int pondMax;
	private double pvpLossFactor;
	private double slotsWinRateFactor;
	private String receiptUrl;

	public static PingyangConfig getInstance() {
		if (instance == null)
			instance = new PingyangConfig();
		return instance;
	}

	public PingyangConfig() {
		final String bcName = Const.PINGYANG_CONFIG;
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(Thread.currentThread().getContextClassLoader().getResource(bcName).getPath()));

			socket_port = Integer.parseInt(props.getProperty("socket_port"));
			jmx_port = Integer.parseInt(props.getProperty("jmx_port"));

			replayToDisk = Integer.parseInt(props.getProperty("replay_to_disk"));
			replayDiskDir = props.getProperty("replay_disk_dir");
			liveHour = Integer.parseInt(props.getProperty("live_hour"));
			rankOffset = Integer.parseInt(props.getProperty("rank_offset"));
			bigRateDrop = Integer.parseInt(props.getProperty("big_rate_drop"));
			resetDotRepay = Double.parseDouble(props.getProperty("resetdot_repay"));
			maxBattleRecord = Integer.parseInt(props.getProperty("max_battle_record"));
			crontabDailyReset = props.getProperty("crontab_daily_reset");
			maxGiveSendGold = Integer.parseInt(props.getProperty("max_give_send_gold"));
			maxGiveReceiveGold = Integer.parseInt(props.getProperty("max_give_receive_gold"));
			String[] borrowIncomes = props.getProperty("borrow_income").split(Const.COMMA_SEP);
			borrowIncome = new Point(Integer.valueOf(borrowIncomes[0]), Integer.valueOf(borrowIncomes[1]));
			version = props.getProperty("version");
			chatInterval = Integer.parseInt(props.getProperty("chat_interval"));
			wsUrl = props.getProperty("ws_url");
			cfgDb = props.getProperty("cfg_db");
			rsVer = props.getProperty("rs_ver");
			pvpIncomeFactor = Double.parseDouble(props.getProperty("pvp_income_factor"));
			goodsSellFactor = Double.parseDouble(props.getProperty("goods_sell_factor"));

			newerReplay = (ReplayBO) CloneUtil.deSerial(getReplayDiskDir() + props.getProperty("newer_battle_id"));
			if (newerReplay != null) {
				newerReplay.cdSkip = 1;//1秒后出现
				newerReplay.speedUp = 0;//不提供加速
			}

			bigPondInit = Integer.parseInt(props.getProperty("big_pond_init"));
			smallPondInit = Integer.parseInt(props.getProperty("small_pond_init"));
			String[] bigPondWins = props.getProperty("big_pond_win").split(Const.LINUX_SEP);
			bigPondWin = new Point(Integer.parseInt(bigPondWins[0]), Integer.parseInt(bigPondWins[1]));
			String[] smallPondWins = props.getProperty("small_pond_win").split(Const.LINUX_SEP);
			smallPondWin = new Point(Integer.parseInt(smallPondWins[0]), Integer.parseInt(smallPondWins[1]));
			bigPondFactor = Double.parseDouble(props.getProperty("big_pond_factor"));
			smallPondFactor = Double.parseDouble(props.getProperty("small_pond_factor"));
			slotsWinDifficulty = Double.parseDouble(props.getProperty("slots_win_difficulty"));
			crontabWeekReset = props.getProperty("crontab_week_reset");
			pondMax = Integer.parseInt(props.getProperty("pond_max"));
			pvpLossFactor = Double.parseDouble(props.getProperty("pvp_loss_factor"));
			slotsWinRateFactor = Double.parseDouble(props.getProperty("slots_win_rate_factor"));

			receiptUrl = props.getProperty("receipt_url");
			logger.info(bcName + " init success");
		} catch (Exception e) {
			logger.error(bcName + " init fail", e);
		}
	}

	public String getReceiptUrl() {
		return receiptUrl;
	}

	public void setReceiptUrl(String receiptUrl) {
		this.receiptUrl = receiptUrl;
	}

	public double getSlotsWinRateFactor() {
		return slotsWinRateFactor;
	}

	public void setSlotsWinRateFactor(double slotsWinRateFactor) {
		this.slotsWinRateFactor = slotsWinRateFactor;
	}

	public double getPvpLossFactor() {
		return pvpLossFactor;
	}

	public void setPvpLossFactor(double pvpLossFactor) {
		this.pvpLossFactor = pvpLossFactor;
	}

	public int getPondMax() {
		return pondMax;
	}

	public void setPondMax(int pondMax) {
		this.pondMax = pondMax;
	}

	public String getCrontabWeekReset() {
		return crontabWeekReset;
	}

	public void setCrontabWeekReset(String crontabWeekReset) {
		this.crontabWeekReset = crontabWeekReset;
	}

	public int getBigPondInit() {
		return bigPondInit;
	}

	public void setBigPondInit(int bigPondInit) {
		this.bigPondInit = bigPondInit;
	}

	public int getSmallPondInit() {
		return smallPondInit;
	}

	public void setSmallPondInit(int smallPondInit) {
		this.smallPondInit = smallPondInit;
	}

	public Point getBigPondWin() {
		return bigPondWin;
	}

	public void setBigPondWin(Point bigPondWin) {
		this.bigPondWin = bigPondWin;
	}

	public Point getSmallPondWin() {
		return smallPondWin;
	}

	public void setSmallPondWin(Point smallPondWin) {
		this.smallPondWin = smallPondWin;
	}

	public double getSlotsWinDifficulty() {
		return slotsWinDifficulty;
	}

	public void setSlotsWinDifficulty(double slotsWinDifficulty) {
		this.slotsWinDifficulty = slotsWinDifficulty;
	}

	public double getBigPondFactor() {
		return bigPondFactor;
	}

	public void setBigPondFactor(double bigPondFactor) {
		this.bigPondFactor = bigPondFactor;
	}

	public double getSmallPondFactor() {
		return smallPondFactor;
	}

	public void setSmallPondFactor(double smallPondFactor) {
		this.smallPondFactor = smallPondFactor;
	}

	public String getCrontabDailyReset() {
		return crontabDailyReset;
	}

	public void setCrontabDailyReset(String crontabDailyReset) {
		this.crontabDailyReset = crontabDailyReset;
	}

	public ReplayBO getNewerReplay() {
		return newerReplay;
	}

	public void setNewerReplay(ReplayBO newerReplay) {
		this.newerReplay = newerReplay;
	}

	public double getGoodsSellFactor() {
		return goodsSellFactor;
	}

	public void setGoodsSellFactor(double goodsSellFactor) {
		this.goodsSellFactor = goodsSellFactor;
	}

	public double getPvpIncomeFactor() {
		return pvpIncomeFactor;
	}

	public void setPvpIncomeFactor(double pvpIncomeFactor) {
		this.pvpIncomeFactor = pvpIncomeFactor;
	}

	public String getCfgDb() {
		return cfgDb;
	}

	public void setCfgDb(String cfgDb) {
		this.cfgDb = cfgDb;
	}

	public String getRsVer() {
		return rsVer;
	}

	public void setRsVer(String rsVer) {
		this.rsVer = rsVer;
	}

	public String getWsUrl() {
		return wsUrl;
	}

	public void setWsUrl(String wsUrl) {
		this.wsUrl = wsUrl;
	}

	public int getChatInterval() {
		return chatInterval;
	}

	public void setChatInterval(int chatInterval) {
		this.chatInterval = chatInterval;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Point getBorrowIncome() {
		return borrowIncome;
	}

	public void setBorrowIncome(Point borrowIncome) {
		this.borrowIncome = borrowIncome;
	}

	public void setRankOffset(int rankOffset) {
		this.rankOffset = rankOffset;
	}

	public void setLiveHour(int liveHour) {
		this.liveHour = liveHour;
	}

	public void setSocket_port(int socketPort) {
		socket_port = socketPort;
	}

	public void setJmx_port(int jmxPort) {
		jmx_port = jmxPort;
	}

	public void setResetDotRepay(double resetDotRepay) {
		this.resetDotRepay = resetDotRepay;
	}

	public void setBigRateDrop(int bigRateDrop) {
		this.bigRateDrop = bigRateDrop;
	}

	public void setMaxBattleRecord(int maxBattleRecord) {
		this.maxBattleRecord = maxBattleRecord;
	}

	public void setMaxGiveSendGold(int maxGiveSendGold) {
		this.maxGiveSendGold = maxGiveSendGold;
	}

	public void setMaxGiveReceiveGold(int maxGiveReceiveGold) {
		this.maxGiveReceiveGold = maxGiveReceiveGold;
	}

	public int getMaxGiveSendGold() {
		return maxGiveSendGold;
	}

	public int getMaxGiveReceiveGold() {
		return maxGiveReceiveGold;
	}

	public int getMaxBattleRecord() {
		return maxBattleRecord;
	}

	public double getResetDotRepay() {
		return resetDotRepay;
	}

	public int getBigRateDrop() {
		return bigRateDrop;
	}

	public int getRankOffset() {
		return rankOffset;
	}

	public int getLiveHour() {
		return liveHour;
	}

	public String getReplayDiskDir() {
		return replayDiskDir;
	}

	public void setReplayDiskDir(String replayDiskDir) {
		this.replayDiskDir = replayDiskDir;
	}

	public int getReplayToDisk() {
		return replayToDisk;
	}

	public void setReplayToDisk(int replayToDisk) {
		this.replayToDisk = replayToDisk;
	}

	public int getSocket_port() {
		return socket_port;
	}

	public int getJmx_port() {
		return jmx_port;
	}
}
