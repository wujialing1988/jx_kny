package com.yunda.baseapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.yunda.frame.util.StringUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 取得给定汉字串的首字母串,即声母串 Title: ChineseCharToEn（此段代码来源于网络）
 * <li>注：只支持GB2312字符集中的汉字
 * <li>创建人：何涛
 * <li>创建日期：2016-5-18
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public final class ChineseCharToEn {

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>标题: 机车检修管理信息系统
     * <li>说明: 汉字首拼存储大小写样式
     * <li>创建人：何涛
     * <li>创建日期：2016-5-19
     * <li>修改人:
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部检修系统项目组
     * @version 1.0
     */
    public static enum Sensitive {
        /** 全部大写 */
        upper, 
        /** 全部小写 */
        lower, 
        /** 正常 */
        normal
    }

    /** 汉字区位码 */
    private final static int[] LI_SECPOSVALUE =
        { 1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590 };
    
    /** 英文字母集 */
    private final static String[] LC_FIRSTLETTER =
        { "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "w", "x", "y", "z" };

    /** 实例对象 */
    private static ChineseCharToEn instance = null;
    
    /**
     * <li>说明：获取实例对象
     * <li>创建人：何涛
     * <li>创建日期：2016-5-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 实例对象
     */
    public static ChineseCharToEn getInstance() {
        if (null == instance) {
            instance = new ChineseCharToEn();
        }
        return instance;
    }
    
    /**
     * <li>说明：取得给定汉字串的首字母串,即声母串
     * <li>创建人：何涛
     * <li>创建日期：2016-5-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param str 给定汉字串
     * @param sen 汉字首拼存储大小写样式，Sensitive.upper：全部大写，Sensitive.lower：全部小写，Sensitive.normal：正常
     * @return 声母串
     */
    public String getAllFirstLetter(String str, Sensitive sen) {
        if (str == null || str.trim().length() == 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            sb.append(this.getFirstLetter(str.substring(i, i + 1)));
        }
        String resutl = sb.toString();
        if(null == sen){
            return resutl;
        }
        switch (sen) {
            case upper: return resutl.toUpperCase();
            case lower: return resutl.toLowerCase();
            default: return resutl;
        }
    }
    
    /**
     * <li>说明：取得给定汉字串的首字母串,即声母串（全部小写）
     * <li>创建人：何涛
     * <li>创建日期：2016-9-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param str 给定汉字串
     * @return 声母串（全部小写）
     */
    public String getAllFirstLetter(String str) {
        return this.getAllFirstLetter(str, null);
    }

    /**
     * <li>说明：取得给定汉字的首字母,即声母
     * <li>创建人：何涛
     * <li>创建日期：2016-5-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param chinese 给定的汉字
     * @return 给定汉字的声母
     */
    private String getFirstLetter(String chinese) {
        if (chinese == null || chinese.trim().length() == 0) {
            return "";
        }
        chinese = this.conversionStr(chinese, "GB2312", "ISO8859-1");
        
        // 判断是不是汉字
        if (chinese.length() > 1) {
            int liSectorCode = (int) chinese.charAt(0);             // 汉字区码
            int liPositionCode = (int) chinese.charAt(1);           // 汉字位码
            liSectorCode = liSectorCode - 160;
            liPositionCode = liPositionCode - 160;
            int liSecPosCode = liSectorCode * 100 + liPositionCode; // 汉字区位码
            if (liSecPosCode > 1600 && liSecPosCode < 5590) {
                for (int i = 0; i < 23; i++) {
                    if (liSecPosCode >= LI_SECPOSVALUE[i] && liSecPosCode < LI_SECPOSVALUE[i + 1]) {
                        chinese = LC_FIRSTLETTER[i];
                        break;
                    }
                }
            // 非汉字字符,如图形符号或ASCII码
            } else {
                chinese = this.conversionStr(chinese, "ISO8859-1", "GB2312");
                chinese = chinese.substring(0, 1);
            }
        }
        return chinese;
    }
    
    /**
     * <li>说明：字符串编码转换
     * <li>创建人：何涛
     * <li>创建日期：2016-5-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param str 要转换编码的字符串
     * @param charsetName 原来的编码
     * @param toCharsetName 转换后的编码
     * @return 经过编码转换后的字符串
     */
    private String conversionStr(String str, String charsetName, String toCharsetName) {
        try {
            str = new String(str.getBytes(charsetName), toCharsetName);
        } catch (UnsupportedEncodingException ex) {
            logger.error("字符串编码转换异常：" + ex.getMessage());
        }
        return str;
    }
    
    /**
     * <li>说明：测试方法
     * <li>创建人：何涛
     * <li>创建日期：2016-5-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param args 参数数组
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getLogger(ChineseCharToEn.class.getName());
        String result = "";
        while (true) {
            logger.info("请输入汉字：");
            InputStreamReader reader = new InputStreamReader(System.in);
            result = new BufferedReader(reader).readLine();
            if ("exit".equalsIgnoreCase(result)) {
                return;
            }
            ChineseCharToEn cte = ChineseCharToEn.getInstance();
            logger.info("获取拼音首字母：" + cte.getAllFirstLetter(result, null));
        }
    }
    
}
