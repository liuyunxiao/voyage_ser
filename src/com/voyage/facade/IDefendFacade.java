package com.voyage.facade;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;

public interface IDefendFacade {

	/**进入战报界面*/
	void enterReport(IRequest request, IResponse response) throws Exception;

	/**获得某个战报*/
	void getReport(IRequest request, IResponse response) throws Exception;
}
