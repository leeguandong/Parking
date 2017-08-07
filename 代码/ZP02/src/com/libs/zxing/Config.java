package com.libs.zxing;


public class Config
{
    /** 自动对焦 */
    public static final boolean AOTO_FOCUS = true;
    
    /** 前置灯光 */
    public static boolean KEY_FRONT_LIGHT = false;
    
    /** 摄像头配置 Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE in 4.0+ */
    public static final boolean KEY_DISABLE_CONTINUOUS_FOCUS = true;
    
    /** 解码器 */
    public static final boolean KEY_DECODE_1D = false;
    public static final boolean KEY_DECODE_QR = false;
    public static final boolean KEY_DECODE_DATA_MATRIX = false;
    
    /** 扫描到结果后是否震动，默认为false,声音默认为true */
    public static final boolean KEY_PLAY_BEEP = false;
    public static final boolean KEY_VIBRATE = true;
    
}
