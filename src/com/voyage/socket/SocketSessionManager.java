package com.voyage.socket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.common.socket.ISocket;

public class SocketSessionManager {
	public static SocketSessionManager INSTANCE = new SocketSessionManager();

	private ConcurrentHashMap<Integer, ISocket> userSocketMap = new ConcurrentHashMap<Integer, ISocket>(); //userId
	private ConcurrentHashMap<Integer, ISocket> accountSocketMap = new ConcurrentHashMap<Integer, ISocket>(); //accountId
	private Map<Integer, Integer> accountUserMap = new HashMap<Integer, Integer>();//key:accountId value:userId

	public SocketSessionManager() {
	}

	//accountId
	public void addAccountSocket(Integer accountId, ISocket socket) {
		accountSocketMap.put(accountId, socket);
	}

	public ISocket getAccountSocket(Integer accountId) {
		return accountSocketMap.get(accountId);
	}

	public void removeAccountSocket(Integer accountId) {
		if (accountId != null) {
			accountSocketMap.remove(accountId);
			Integer userId = accountUserMap.remove(accountId);
			if (userId != null)
				userSocketMap.remove(userId);
		}
	}

	//userId
	public void addUserSocket(Integer userId, ISocket socket, Integer accountId) {
		userSocketMap.put(userId, socket);
		accountUserMap.put(accountId, userId);
	}

	public ISocket getUserSocket(Integer userId) {
		return userSocketMap.get(userId);
	}

	public Collection<ISocket> getAllUserSocket() {
		return userSocketMap.values();
	}

	/**
	 * 根据userid列表获得isocket列表
	 * 
	 * @param ids
	 * @return
	 */
	public List<ISocket> getUserSocketListByIds(List<Integer> ids) {
		List<ISocket> list = new ArrayList<ISocket>();
		for (Integer id : ids) {
			if (userSocketMap.containsKey(id)) {
				list.add(userSocketMap.get(id));
			}
		}
		return list;
	}

	public boolean isOnLine(Integer userId) {
		return getUserSocket(userId) != null;
	}

	public Integer getUserSocketSize() {
		return userSocketMap != null ? userSocketMap.size() : 0;
	}
}
