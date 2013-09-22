package com.voyage.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface IPyWebService {
@WebMethod
String rtBuyGoods(@WebParam(name = "urlParams") String urlParams);
}
