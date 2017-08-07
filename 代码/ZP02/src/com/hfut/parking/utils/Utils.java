package com.hfut.parking.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

	public static String getTextFromStream(InputStream is){
		
		byte[] b = new byte[1024];
		int len = 0;
		//创建字节数组输出流，读取输入流的文本数据时，同步把数据写入数组输出流
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while((len = is.read(b)) != -1){
				bos.write(b, 0, len);
			}
			//把字节数组输出流里的数据转换成字节数组
			String text = new String(bos.toByteArray());
			return text;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
