package com.yunda.util;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;

import java.io.File;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 多媒体工具类
 * <li>创建人：刘晓斌
 * <li>创建日期：2016-3-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public final class MediaUtil {

	private MediaUtil(){}
	
	/**
	 * <li>说明：转换为MP3格式的文件
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2016-3-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param sourcePath 源文件
	 * @param targetPath 目标文件
	 * @return 
	 */
    public static void toMp3(String sourcePath, String targetPath) {  
        File source = new File(sourcePath);  
        File target = new File(targetPath); 
        toMp3(source, target);
    }
    
    /**
     * <li>说明：转换为MP3格式的文件
     * <li>创建人：刘晓斌
     * <li>创建日期：2016-3-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
	 * @param source 源文件
	 * @param targetnm 目标文件
     * @return 返回值说明
     */
    public static void toMp3(File source, File target) {  
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame"); 
        audio.setBitRate(new Integer(32000));
        audio.setChannels(new Integer(2));
        audio.setSamplingRate(new Integer(44100));
        
        EncodingAttributes attrs = new EncodingAttributes();  
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);
        try {
            Encoder encoder = new Encoder();
            encoder.encode(source, target, attrs);
        } catch (Exception e) {  
            e.printStackTrace();
        }  
    }
}
