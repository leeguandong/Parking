package aatest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

	public static String getTextFromStream(InputStream is){
		
		byte[] b = new byte[1024];
		int len = 0;
		//鍒涘缓瀛楄妭鏁扮粍杈撳嚭娴侊紝璇诲彇杈撳叆娴佺殑鏂囨湰鏁版嵁鏃讹紝鍚屾鎶婃暟鎹啓鍏ユ暟缁勮緭鍑烘祦
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while((len = is.read(b)) != -1){
				bos.write(b, 0, len);
			}
			//鎶婂瓧鑺傛暟缁勮緭鍑烘祦閲岀殑鏁版嵁杞崲鎴愬瓧鑺傛暟缁�
			String text = new String(bos.toByteArray(),"UTF-8");
			return text;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
