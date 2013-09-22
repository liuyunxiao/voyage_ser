package com.common.socket.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.socket.SessionData;

public class JSONDecoder extends CumulativeProtocolDecoder {
	private Logger logger = LoggerFactory.getLogger(JSONDecoder.class);

	public JSONDecoder() {
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		SessionData sessionData = (SessionData) session.getAttribute("sessionData");
		if (!sessionData.inited) {
			in.mark();
			int available = in.remaining();
			final int data_size = 4;
			if (available >= data_size) {
				int dataLen = in.getInt();
				if (available - 4 < dataLen) {
					in.reset();
					return false;
				}

				byte[] bytes = new byte[dataLen];
				in.get(bytes);
				String data = new String(bytes, "UTF-8");
				logger.debug(dataLen + " byte," + data);
				out.write(data);
				return true;
			}
		}
		return false;
	}
}
