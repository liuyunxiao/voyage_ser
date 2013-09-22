package com.voyage.facade.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;
import com.common.util.Base64Coder;
import com.voyage.cache.CfgDataMgr;
import com.voyage.config.PingyangConfig;
import com.voyage.constant.Const;
import com.voyage.facade.IMarketFacade;
import com.voyage.service.IMarketService;
import com.voyage.util.NetsUtil;

@Controller
public class MarketFacade implements IMarketFacade {
	@Autowired
	private IMarketService marketService;

	@Override
	public void buyGold(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		JSONObject receiptRt = checkReceipt(jo.getString("receipt"));//验证 receipt
		String transactionId = receiptRt.getString("transaction_id");//作为本次交易的唯一标记
		//int rechargeId = jo.getInt("rechargeId");//充值价格表主键
		int rechargeId = CfgDataMgr.getInstance().getRechargeByProId(receiptRt.getString("product_id")).getCrId();
		int userId = jo.getInt(Const.USERID);
		int oppoId = jo.has("oppoId") ? jo.getInt("oppoId") : userId;
		marketService.buyGold(userId, oppoId, rechargeId, transactionId);
		response.writeJson(null);
	}

	/**
	 * 验证receipt
	 * */
	private JSONObject checkReceipt(String receipt) throws Exception {
		JSONObject receiptJo = new JSONObject();
		receiptJo.put("receipt-data", Base64Coder.encodeString(receipt));
		JSONObject receiptRt = new JSONObject(NetsUtil.postRequest(PingyangConfig.getInstance().getReceiptUrl(), receiptJo.toString()));
		if (0 != receiptRt.getInt("status"))
			throw new IllegalStateException("receipt status error");

		return receiptRt.getJSONObject("receipt");
	}

	@Override
	public void buyGoods(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int goodsId = jo.getInt("goodsId");//物品ID
		int userId = jo.getInt(Const.USERID);
		int oppoId = jo.has("oppoId") ? jo.getInt("oppoId") : userId;
		marketService.buyGoods(userId, oppoId, goodsId);
		response.writeJson(null);
	}

	@Override
	public void sellGoods(IRequest request, IResponse response) throws Exception {
		JSONObject jo = (JSONObject) request.getData();
		int subId = jo.getInt("subId");
		marketService.sellGoods(jo.getInt(Const.USERID), subId);
		response.writeJson(null);
	}
}
