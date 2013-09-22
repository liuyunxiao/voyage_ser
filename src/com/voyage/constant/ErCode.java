package com.voyage.constant;

public interface ErCode {
	int E0 = 0;//正常
	int E_1 = -1;//默认失败(预留)
	int E1 = 1;//带颜色的正常(预留)
	int E2 = 2;//版本不一致
	int E999 = 999;//金币不足
	//UserService
	int E101 = 101;//账号已存在
	int E102 = 102;//账号不存在
	int E103 = 103;//密码错误
	int E104 = 104;//昵称已存在
	int E105 = 105;//角色不存在
	int E106 = 106;//角色已存在
	int E107 = 107;//角色不存在(创建角色时的标记，前台无须提示)
	int E108 = 108;//账号已冻结
	//CorpsService
	int E201 = 201;//已超出兵种等级上限(需要提示加点上限)
	//Battle
	int E301 = 301;//攻方出战阵容为空
	int E302 = 302;//对方处于保护期
	int E303 = 303;//战报已过期
	//MonsterService
	int E401 = 401;//前置关卡未通过
	int E402 = 402;//关卡未开启

	//RelationService
	int E601 = 601;//我的可赠送金币达到上限
	int E602 = 602;//对方可接收的赠送金币达到上限
}
