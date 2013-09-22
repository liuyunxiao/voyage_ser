package com.voyage.service;

import java.util.List;

import com.voyage.data.bo.BattleReportBO;

public interface IDefendService {
	/**获得战报列表*/
	List<BattleReportBO> enterReport(int userId) throws Exception;

	/**获得所有未读战报数*/
	int countOfUnread(int userId) throws Exception;
}
