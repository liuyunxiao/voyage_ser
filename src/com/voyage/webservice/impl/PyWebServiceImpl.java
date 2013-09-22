package com.voyage.webservice.impl;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.voyage.SpringContext;
import com.voyage.webservice.IPyWebService;

@Component
@WebService
public class PyWebServiceImpl implements IPyWebService {
	private Logger logger = LoggerFactory.getLogger(PyWebServiceImpl.class);

	public static IPyWebService getInstance() {
		return SpringContext.getInstance().getSpringContext().getBean(IPyWebService.class);
	}

	@Override
	public String rtBuyGoods(String urlParams) {
		logger.info(urlParams);
		return "success";
	}
}
