package com.voyage.constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

public interface Const {

	String PINGYANG_CONFIG = "voyage.properties";
	String BATTLE_CONFIG = "battle.properties";
	String LOG4J_CONFIG = "log4j.properties";
	String FACADE_KEY = "FACADE_KEY";//后台方法的KEY
	String USERID = "USERID";
	String DOT_SEP = "\\.";
	String COMMA_SEP = ",";
	String NONE = "0";//无效资源
	String LINUX_SEP = "/";
	int CRIT_BASE = 1000;//概率基数1000
	int CRIT_BASE_100 = 100;//概率基数100
	int PERCENT_BASE = 100;//百分比基数
	Random RAND = new Random();
	int INFINITY = -1;//正无穷
	int CORPS_LEVEL_DOTS = 5;//每1级兵种等级需要加的点数
	int CORPS_HIRE_GOLD_BASE = 10;//兵种招募金基数
	//JSON中的参数名
	String POS = "pos";
	String CORPSID = "corpsId";
	String SHORTATK = "shortAtk";
	String SHORTDEF = "shortDef";
	String MAGICATK = "magicAtk";
	String SOMA = "soma";
	String MINGOLD = "minGold";
	String MAXGOLD = "maxGold";
	int N_ACTIVE = 5;//N个活跃玩家
	String DEFID = "defId";
	int PAGE_LIMIT = 10;//分页，每页记录数
	String DEFAULT_PWD_JMX = "123456";//默认JMX密码
	String DEFAULT_PWD_LOGIN = "123456";//默认登录密码
	int ZERO = 0;
	int RANK_FIRST_N = 10;//排行榜前N名
	int RANK_AROUND_N = 5;//排行榜我前后各N名
	DateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//默认时间格式 
	int SYSTEM_USER_ID = 0;//系统角色ID
	String NEW_COMER = "NEW_COMER";//新手
	int ID_ALIAS_BASE = 1478;//ID别名生成基数
	final Object CREATE_ROLE_LOCK = new Object();//创建角色锁 
	int SUDOKU = 9;//九宫格游戏
	int CAPTAINS = 3;//最多队长数
	int SLOTS_RANK_N = 10;//老虎机排行榜前N名
	int SLOTS_RP_MSG_MIN = 1000;//老虎机发送RP消息时需中的最小金币数
	int SLOTS_MAX_RECORD = 5;//老虎机消息最大記錄數
	String TO_RAND = "999";//代表要随机
}
