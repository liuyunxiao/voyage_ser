package com.voyage.util;

import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONObject;

import com.common.util.Base64Coder;

/**
* 发送HTTP网络请求类
*

*/

public class NetsUtil {

	// 编码方式
	private static final String CONTENT_CHARSET = "UTF-8";

	// 连接超时时间
	private static final int CONNECTION_TIMEOUT = 30000;

	// 读数据超时时间
	private static final int READ_DATA_TIMEOUT = 30000;

	/** 
	 * 发送POST请求
	 * 
	 * @param url 请求URL地址 
	 * @param params 请求参数 
	 * @return 服务器响应的请求结果
	 */
	public static String postRequest(String url, Map<String, String> params) throws Exception {

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);

		// 设置请求参数
		if (params != null && !params.isEmpty()) {
			NameValuePair[] data = new NameValuePair[params.size()];
			int i = 0;
			for (Map.Entry<String, String> entry : params.entrySet()) {
				data[i] = new NameValuePair((String) entry.getKey(), (String) entry.getValue());
				++i;
			}

			postMethod.setRequestBody(data);
		}

		// 设置建立连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);

		// 设置读数据超时时间
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(READ_DATA_TIMEOUT);

		// 设置编码
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, CONTENT_CHARSET);

		//使用系统提供的默认的恢复策略
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		try {
			int statusCode = httpClient.executeMethod(postMethod);

			if (statusCode != HttpStatus.SC_OK) {
				throw new IllegalStateException("Request [" + url + "] failed:" + postMethod.getStatusLine());
			}

			//读取内容 
			byte[] responseBody = postMethod.getResponseBody();

			return new String(responseBody, CONTENT_CHARSET);
		} finally {
			//释放链接
			postMethod.releaseConnection();
		}

	}

	/** 
	 * 发送POST请求
	 * 
	 * @param url 请求URL地址 
	 * @param params 请求参数 
	 * @return 服务器响应的请求结果
	 */
	public static String postRequest(String url, String body) throws Exception {

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);

		postMethod.setRequestBody(body);

		// 设置建立连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);

		// 设置读数据超时时间
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(READ_DATA_TIMEOUT);

		// 设置编码
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, CONTENT_CHARSET);

		//使用系统提供的默认的恢复策略
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		try {
			int statusCode = httpClient.executeMethod(postMethod);

			if (statusCode != HttpStatus.SC_OK) {
				throw new IllegalStateException("Request [" + url + "] failed:" + postMethod.getStatusLine());
			}

			//读取内容 
			byte[] responseBody = postMethod.getResponseBody();

			return new String(responseBody, CONTENT_CHARSET);
		} finally {
			//释放链接
			postMethod.releaseConnection();
		}

	}

	public static void main(String[] args) throws Exception {
		JSONObject jo = new JSONObject();
		String a = "{\n\t\"signature\" = \"ArDqxdKBYwm5lsfEWoBw7Lc6OG9eliQoWPqHZUKz6gsgaMepERlq7Ba4n24vDbF2dIsQxXWi0+YY6TVqlLgKTzfbB/WCb6rQYljBOI/ivGFFISN7WHIpjVIV27oDF9Q+ltBlEQNr65pk1PjktZTqKpbqOcMq4ziMNHnmEvMeVOX7AAADVzCCA1MwggI7oAMCAQICCGUUkU3ZWAS1MA0GCSqGSIb3DQEBBQUAMH8xCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSYwJAYDVQQLDB1BcHBsZSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEzMDEGA1UEAwwqQXBwbGUgaVR1bmVzIFN0b3JlIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTA5MDYxNTIyMDU1NloXDTE0MDYxNDIyMDU1NlowZDEjMCEGA1UEAwwaUHVyY2hhc2VSZWNlaXB0Q2VydGlmaWNhdGUxGzAZBgNVBAsMEkFwcGxlIGlUdW5lcyBTdG9yZTETMBEGA1UECgwKQXBwbGUgSW5jLjELMAkGA1UEBhMCVVMwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMrRjF2ct4IrSdiTChaI0g8pwv/cmHs8p/RwV/rt/91XKVhNl4XIBimKjQQNfgHsDs6yju++DrKJE7uKsphMddKYfFE5rGXsAdBEjBwRIxexTevx3HLEFGAt1moKx509dhxtiIdDgJv2YaVs49B0uJvNdy6SMqNNLHsDLzDS9oZHAgMBAAGjcjBwMAwGA1UdEwEB/wQCMAAwHwYDVR0jBBgwFoAUNh3o4p2C0gEYtTJrDtdDC5FYQzowDgYDVR0PAQH/BAQDAgeAMB0GA1UdDgQWBBSpg4PyGUjFPhJXCBTMzaN+mV8k9TAQBgoqhkiG92NkBgUBBAIFADANBgkqhkiG9w0BAQUFAAOCAQEAEaSbPjtmN4C/IB3QEpK32RxacCDXdVXAeVReS5FaZxc+t88pQP93BiAxvdW/3eTSMGY5FbeAYL3etqP5gm8wrFojX0ikyVRStQ+/AQ0KEjtqB07kLs9QUe8czR8UGfdM1EumV/UgvDd4NwNYxLQMg4WTQfgkQQVy8GXZwVHgbE/UC6Y7053pGXBk51NPM3woxhd3gSRLvXj+loHsStcTEqe9pBDpmG5+sk4tw+GK3GMeEN5/+e1QT9np/Kl1nj+aBw7C0xsy0bFnaAd1cSS6xdory/CUvM6gtKsmnOOdqTesbp0bs8sn6Wqs0C9dgcxRHuOMZ2tm8npLUm7argOSzQ==\";\n\t\"purchase-info\" = \"ewoJIm9yaWdpbmFsLXB1cmNoYXNlLWRhdGUtcHN0IiA9ICIyMDEzLTA5LTE1IDIzOjI2OjA5IEFtZXJpY2EvTG9zX0FuZ2VsZXMiOwoJInVuaXF1ZS1pZGVudGlmaWVyIiA9ICIwZDc5ZmM3MzdiZGU4NWRlYzZkMWZhYmU0NzE1OWJiZDkxN2M3YzJlIjsKCSJvcmlnaW5hbC10cmFuc2FjdGlvbi1pZCIgPSAiMTAwMDAwMDA4NzMxNTcwNyI7CgkiYnZycyIgPSAiMSI7CgkidHJhbnNhY3Rpb24taWQiID0gIjEwMDAwMDAwODczMTU3MDciOwoJInF1YW50aXR5IiA9ICIxIjsKCSJvcmlnaW5hbC1wdXJjaGFzZS1kYXRlLW1zIiA9ICIxMzc5MzEyNzY5MDAwIjsKCSJ1bmlxdWUtdmVuZG9yLWlkZW50aWZpZXIiID0gIkVGN0QyRTYzLTUwQzgtNDcwNC1BQzdCLUIyRDEzQTNGMkI2RSI7CgkicHJvZHVjdC1pZCIgPSAiY29tLmJsYWNrYmFuYW5hLnByb2R1Y3RfMiI7CgkiaXRlbS1pZCIgPSAiNzA1ODg5MzU3IjsKCSJiaWQiID0gImNvbS5ibGFja2JhbmFuYS5wcmluY2Vzc3dhciI7CgkicHVyY2hhc2UtZGF0ZS1tcyIgPSAiMTM3OTMxMjc2OTAwMCI7CgkicHVyY2hhc2UtZGF0ZSIgPSAiMjAxMy0wOS0xNiAwNjoyNjowOSBFdGMvR01UIjsKCSJwdXJjaGFzZS1kYXRlLXBzdCIgPSAiMjAxMy0wOS0xNSAyMzoyNjowOSBBbWVyaWNhL0xvc19BbmdlbGVzIjsKCSJvcmlnaW5hbC1wdXJjaGFzZS1kYXRlIiA9ICIyMDEzLTA5LTE2IDA2OjI2OjA5IEV0Yy9HTVQiOwp9\";\n\t\"environment\" = \"Sandbox\";\n\t\"pod\" = \"100\";\n\t\"signing-status\" = \"0\";\n}";
		jo.put("receipt-data", Base64Coder.encodeString(a));
		postRequest("https://sandbox.itunes.apple.com/verifyReceipt", jo.toString());
	}

}
