package com.voyage.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloneUtil {
	public static Logger logger = LoggerFactory.getLogger(CloneUtil.class);

	public static final Object deepClone(Object objToClone) {
		try {
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			ObjectOutputStream objectoutputstream = new ObjectOutputStream(
					bytearrayoutputstream);
			objectoutputstream.writeObject(objToClone);
			byte abyte0[] = bytearrayoutputstream.toByteArray();
			objectoutputstream.close();
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(
					abyte0);
			ObjectInputStream objectinputstream = new ObjectInputStream(
					bytearrayinputstream);
			Object clone = objectinputstream.readObject();
			objectinputstream.close();
			return clone;
		} catch (Exception e) {
			logger.error(null, e);
		}
		return null;
	}

	/**
	 * 序列化对象到本地文件
	 * 
	 * @param obj
	 * @param file
	 */
	public static final boolean serial(Object obj, String file) {
		boolean rt = false;
		try {
			File f = new File(file);
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.close();
			fos.close();
			rt = true;
		} catch (Exception e) {
			logger.error(null, e);
		}
		return rt;
	}

	/**
	 * 从本地文件反序列化出对象
	 * 
	 * @param obj
	 * @param file
	 */
	public static final Object deSerial(String file) {
		Object rt = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			rt = ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			logger.error(null, e);
		}
		return rt;
	}
}
