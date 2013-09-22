package com.common.socket.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONEncoder implements ProtocolEncoder {
	private Logger logger = LoggerFactory.getLogger(JSONEncoder.class);

	public JSONEncoder() {
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		IoBuffer buffer;
		if (message instanceof JSONObject) {
			String data = ((JSONObject) message).toString();
			byte[] bytes = data.getBytes("UTF-8");
			logger.debug(bytes.length + " byte," + data);
			buffer = IoBuffer.allocate(4 + bytes.length);
			buffer.putInt(bytes.length);
			buffer.put(bytes);
			buffer.flip();
			out.write(buffer);
			out.flush();
		}
	}

	public void dispose(IoSession arg0) throws Exception {

	}
}
