package com.voyage.service;

import java.util.List;

import com.voyage.data.bo.PkSingleBO;

public interface IPkSingleService {
	/**组装对战界面
	 * @param only 是否查询指定用户
	 * */
	List<PkSingleBO> enter(boolean only, int oppoId) throws Exception;
}
