package com.common.socket;

import java.util.List;

import org.json.JSONObject;

import com.common.socket.vo.ResultDataVO;
import com.voyage.socket.SocketSessionManager;

public class ResponseUtil {

	public static void notifyJson(int userId, String event, Object obj) throws Exception {
		ISocket socket = SocketSessionManager.INSTANCE.getUserSocket(userId);
		if (socket != null) {
			ResultDataVO rd = new ResultDataVO(0, obj);
			rd.setEvent(event);
			socket.sendTheJson(new JSONObject(rd));
		}
	}

	public static void notifyJson(List<Integer> userIds, String event, Object obj) throws Exception {
		for (Integer userId : userIds) {
			notifyJson(userId, event, obj);
		}
	}
}
