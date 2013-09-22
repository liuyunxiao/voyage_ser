package com.voyage.webservice.client;

public class TestPyWebService {
	public static void main(String[] args) {
		PyWebServiceImplService pyWs = new PyWebServiceImplService();
		String rt = pyWs.getPyWebServiceImplPort().rtBuyGoods("test");
		System.out.println(rt);
	}
}
