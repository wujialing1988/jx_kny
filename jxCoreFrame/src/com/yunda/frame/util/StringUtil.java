package com.yunda.frame.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 字符串处理工具类,包括常用的处理方法
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-8-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public final class StringUtil {
    private static final int FILL_CHAR = '=';
    private static final String CVT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "abcdefghijklmnopqrstuvwxyz"
                                    + "0123456789+/";
    private StringUtil() {}
    /**
     * iso8859转gb2312,如果失败返回原字符串
     * @param string String
     * @return String
     */
    public static String encodeISO8859_To_GB2312(String string) {
      return encodeISO8859_To_GB2312(string, string);
    }

    /**
     * iso8859转gb2312,如果失败返回defaultString
     * @param string String
     * @param defaultString String
     * @return String
     */
    public static String encodeISO8859_To_GB2312(String string,
                                                 String defaultString) {
      try {
        return new String(string.getBytes("ISO-8859-1"), "GB2312");
      } catch (UnsupportedEncodingException ex) {
        ex.printStackTrace();
        return defaultString;
      }
    }

    /**
     * GB2312转ISO8859，如果失败返回原字符串
     * @param string String
     * @return String
     */
    public static String encodeGB2312_To_ISO8859(String string) {
      return encodeGB2312_To_ISO8859(string, string);
    }

    /**
     * GB2312_To_ISO8859,如果失败返回defaultString
     * @param string String
     * @param defaultString String
     * @return String
     */
    public static String encodeGB2312_To_ISO8859(String string,
                                                 String defaultString) {
      try {
        return new String(string.getBytes("GB2312"), "ISO-8859-1");
      } catch (UnsupportedEncodingException ex) {
        ex.printStackTrace();
        return defaultString;
      }
    }

    /**
     * GBK转ISO8859,如果失败返回defaultString
     * @param string String
     * @param defaultString String
     * @return String
     */
    public static String encodeGBK_To_ISO8859(String string,
                                              String defaultString) {
      try {
        return new String(string.getBytes("GBK"), "ISO-8859-1");
      } catch (UnsupportedEncodingException ex) {
        ex.printStackTrace();
        return defaultString;
      }
    }

    /**
     * GBK转ISO8859,如果失败返回defaultString
     * @param string String
     * @param defaultString String
     * @return String
     */
    public static String toISO(String string,
                               String defaultString) {
      try {
        return new String(string.getBytes("GBK"), "ISO-8859-1");
      } catch (UnsupportedEncodingException ex) {
        ex.printStackTrace();
        return defaultString;
      }
    }

    /**
     * GBK转ISO8859,如果失败返回原字符串
     * @param string String
     * @return String
     */
    public static String toISO(String string) {
      return encodeGBK_To_ISO8859(string, string);
    }

    /**
     * GBK转ISO8859,如果失败返回原字符串
     * @param string String
     * @return String
     */
    public static String encodeGBK_To_ISO8859(String string) {
      return encodeGBK_To_ISO8859(string, string);
    }

    /**
     * ISO8859转GBK，如果失败返回defaultString
     * @param string String
     * @param defaultString String
     * @return String
     */
    public static String encodeISO8859_To_GBK(String string,
                                              String defaultString) {
      try {
        return new String(string.getBytes("ISO-8859-1"), "GBK");
      } catch (UnsupportedEncodingException ex) {
        ex.printStackTrace();
        return defaultString;
      }
    }

    /**
     * ISO8859转GBK，如果失败返回defaultString
     * @param string String
     * @param defaultString String
     * @return String
     */
    public static String toGBK(String string,
                               String defaultString) {
      try {
        return new String(string.getBytes("ISO-8859-1"), "GBK");
      } catch (UnsupportedEncodingException ex) {
        ex.printStackTrace();
        return defaultString;
      }
    }

    /**
     * ISO8859转GBK,如果失败返回原字符串
     * @param string String
     * @return String
     */
    public static String encodeISO8859_To_GBK(String string) {
      return encodeISO8859_To_GBK(string, string);
    }

    /**
     * ISO8859转GBK,如果失败返回原字符串
     * @param string String
     * @return String
     */
    public static String toGBK(String string) {
      return encodeISO8859_To_GBK(string, string);
    }

    /**
     * 由分隔符compart分隔字符串str,返回String[]
     * @param str String
     * @param compart String
     * @return String[]
     */
    public static String[] tokenizer(String str, String compart) {
      String[] values = null;
      if (null == str || "".equals(str)) {
        return values;
      }
      StringTokenizer tokenizer = new StringTokenizer(str, compart);
      List<String> list = new ArrayList<String>();
      while (tokenizer.hasMoreElements()) {
        String value = (String) tokenizer.nextElement();
        list.add(value);
      }
      int size = list.size();
      values = new String[size];
      list.toArray(values);
      return values;
    }

    /**
     * 判断传入的值是否等于null、"null"(不区分大小写)、"" ；
     * 2016-08-05修改，将全角空格转换为半角空格进行判断 replace((char) 12288, ' ')
     * @param value
     * @return 当传入的值等于null、"null"(不区分大小写)、""时返回true。否则返回false。
     */
    public static boolean isNullOrBlank(String value) {
        return null == value || 0 >= value.replace((char) 12288, ' ').trim().length() || "null".equalsIgnoreCase(value) ? true : false;
    }
    
    /**
     * 全角转换为半角
     * @param value 被转换值
     * @return returnString 转换后返回值
     */
    public static String convertQjBlank(String value){
        if(StringUtil.isNullOrBlank(value)){
            return "" ;
        }
        char c[] = value.toCharArray();
        for (int i = 0; i < c.length; i++) {
          if (c[i] == '\u3000') {
            c[i] = ' ';
          } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
            c[i] = (char) (c[i] - 65248);

          }
        }
        String returnString = new String(c);
        return returnString;
    }
    
    
    /**
     * 将字符串数组转换为以join分隔的字符串
     * @param values String[] 字符串数组
     * @param join String 分隔符
     * @return String 转换后的字符串。若values为空则返回null
     */
    public static String join(String[] values, String join) {
      if (values == null || values.length == 0) {
        return null;
      }
      StringBuffer sb = new StringBuffer();
      sb.append(values[0]);
      for (int i = 1; i < values.length; i++) {
        sb.append(join).append(values[i]);
      }
      return sb.toString();
    }
    /**
     * 将long数组转换为以join分隔的字符串
     * @param values long[]
     * @param join String
     * @return String
     */
    public static String join(long[] values, String join) {
      return join(longArray_To_StrArray(values), join);
    }
    /**
     * 将Long数组转换为以join分隔的字符串
     * @param values Long[]
     * @param join String
     * @return String
     */
    public static String join(Long[] values, String join) {
      return join(longArray_To_StrArray(values), join);
    }    
    /**
     * 将int数组转换为以join分隔的字符串
     * @param values int[]
     * @param join String
     * @return String
     */
    public static String join(int[] values, String join) {
      return join(intArray_To_StrArray(values), join);
    }
    /**
     * 将Integer数组转换为以join分隔的字符串
     * @param values Integer[]
     * @param join String
     * @return String
     */
    public static String join(Integer[] values, String join) {
      return join(intArray_To_StrArray(values), join);
    }    
    /**
     * 将字符串数组转换为以逗号分隔的字符串
     * @param values 字符串数组
     * @return 转换后的字符串。若values为空则返回null
     */
    public static String join(String[] values) {
      return join(values, ",");
    }

    /**
     * 将long数组转换为以逗号分隔的字符串
     * @param values long[]
     * @return String 转换后的字符串。若values为空则返回null
     */
    public static String join(long[] values) {
      return join( longArray_To_StrArray(values) );
    }
    /**
     * 将Long数组转换为以逗号分隔的字符串
     * @param values Long[]
     * @return String 转换后的字符串。若values为空则返回null
     */
    public static String join(Long[] values) {
      return join( longArray_To_StrArray(values) );
    }    
    /**
     * 将int数组转换为以逗号分隔的字符串
     * @param values int[]
     * @return String 转换后的字符串。若values为空则返回null
     */
    public static String join(int[] values) {
      return join( intArray_To_StrArray(values) );
    }
    /**
     * 将int数组转换为以逗号分隔的字符串
     * @param values Integer[]
     * @return String 转换后的字符串。若values为空则返回null
     */
    public static String join(Integer[] values) {
      return join( intArray_To_StrArray(values) );
    }    
    /**
     * String数组转换为long数组
     * @param strings String[]
     * @return long[]
     */
    public static long[] tolongArray(String[] strings) {
      if (strings == null || strings.length < 1) {
        return null;
      }
      long[] longs = new long[strings.length];
      for (int i = 0; i < strings.length; i++) {
        longs[i] = Long.parseLong(strings[i]);
      }
      return longs;
    }
    /**
     * String数组转换为Long数组
     * @param strings String[]
     * @return Long[]
     */
    public static Long[] toLongArray(String[] strings) {
      if (strings == null || strings.length < 1) {
        return null;
      }
      Long[] longs = new Long[strings.length];
      for (int i = 0; i < strings.length; i++) {
        longs[i] = Long.parseLong(strings[i]);
      }
      return longs;
    }    

    /**
     * String数组转换为int数组
     * @param strings String[]
     * @return int[]
     */
    public static int[] toIntArray(String[] strings) {
      if (strings == null || strings.length < 1) {
        return null;
      }
      int[] ints = new int[strings.length];
      for (int i = 0; i < strings.length; i++) {
        ints[i] = Integer.parseInt(strings[i]);
      }
      return ints;
    }
    /**
     * String数组转换为Integer数组
     * @param strings String[]
     * @return Integer[]
     */
    public static Integer[] toIntegerArray(String[] strings) {
      if (strings == null || strings.length < 1) {
        return null;
      }
      Integer[] ints = new Integer[strings.length];
      for (int i = 0; i < strings.length; i++) {
        ints[i] = Integer.parseInt(strings[i]);
      }
      return ints;
    }    

    /**
     * long数组转换为String数组
     * @param longs long[]
     * @return String[]
     */
    public static String[] longArray_To_StrArray(long[] longs) {
      if (longs == null || longs.length < 1) {
        return null;
      }
      String[] strings = new String[longs.length];
      for (int i = 0; i < longs.length; i++) {
        strings[i] = String.valueOf(longs[i]);
      }
      return strings;
    }
    /**
     * Long数组转换为String数组
     * @param longs Long[]
     * @return String[]
     */
    public static String[] longArray_To_StrArray(Long[] longs) {
      if (longs == null || longs.length < 1) {
        return null;
      }
      String[] strings = new String[longs.length];
      for (int i = 0; i < longs.length; i++) {
        strings[i] = String.valueOf(longs[i]);
      }
      return strings;
    }
    /**
     * int数组转换为String数组
     * @param ints int[]
     * @return String[]
     */
    public static String[] intArray_To_StrArray(int[] ints) {
      if (ints == null || ints.length < 1) {
        return null;
      }
      String[] strings = new String[ints.length];
      for (int i = 0; i < ints.length; i++) {
        strings[i] = String.valueOf(ints[i]);
      }
      return strings;
    }
    /**
     * Integer数组转换为String数组
     * @param ints Integer[]
     * @return String[]
     */
    public static String[] intArray_To_StrArray(Integer[] ints) {
      if (ints == null || ints.length < 1) {
        return null;
      }
      String[] strings = new String[ints.length];
      for (int i = 0; i < ints.length; i++) {
        strings[i] = String.valueOf(ints[i]);
      }
      return strings;
    }    
    /**
     * 当参数str为空null时返回第2个参数returnStr
     * @param str String
     * @param returnStr String
     * @return String
     */
    public static String nvl(String str, String returnStr) {
      return str == null ? returnStr : str;
    }
    /**
     * 当参数str为空null时返回第2个参数returnStr
     * @param str Object
     * @param returnStr String
     * @return String
     */
    public static String nvl(Object str, String returnStr) {
      return str == null ? returnStr : str.toString();
    }
    /**
     * 当参数str为空null时返回""
     * @param str String
     * @return String
     */
    public static String nvl(String str) {
      return str == null ? "" : str;
    }
    /**
     * 当参数str为空null时返回""
     * @param str Object
     * @return String
     */
    public static String nvl(Object str) {
      return str == null ? "" : str.toString();
    }
    /**
     * 当参数str为空null或去除两边空白字符后为"",返回第2个参数returnStr
     * @param str String
     * @param returnStr String
     * @return String
     */
    public static String nvlTrim(String str, String returnStr) {
      return str == null || str.trim().length() < 1 || str.trim().equals("null")? returnStr : str.trim();
    }
    /**
     * 当参数str为空null或去除两边空白字符后为"",返回第2个参数returnStr
     * @param str Object
     * @param returnStr String
     * @return String
     */
    public static String nvlTrim(Object str, String returnStr) {
    	return str == null || str.toString().trim().length() < 1 ? returnStr : str.toString().trim();
    }
    /**
     * 当参数str为空null或去除两边空白字符后为"",返回第2个参数returnStr
     * @param str
     * @param returnStr
     * @return
     */
    public static Object nvlTrim(Object str, Object returnStr) {
    	return str == null || str.toString().trim().length() < 1 ? returnStr : str.toString().trim();
    }
    /**
     * 当参数str为空null或去除两边空白字符后为"",返回""
     * @param str String
     * @return String
     */
    public static String nvlTrim(String str) {
      return str == null || str.trim().length() < 1 ? "" : str.trim();
    }
    /**
     * 当参数str为null时返回returnValWhenNull，不为null时返回returnValWhenNotNull
     * @param str String
     * @param returnValWhenNull String
     * @param returnValWhenNotNull String
     * @return String
     */
    public static String nvl(String str, String returnValWhenNull, String returnValWhenNotNull) {
        return str == null ? returnValWhenNull : returnValWhenNotNull;
    }
    /**
     * 当参数obj为null时返回returnValWhenNull，不为null时返回returnValWhenNotNull
     * @param obj Object
     * @param returnValWhenNull Object
     * @param returnValWhenNotNull Object
     * @return Object
     */
    public static Object nvl(Object obj, Object returnValWhenNull, Object returnValWhenNotNull) {
        return obj == null ? returnValWhenNull : returnValWhenNotNull;
    }
    /**
     * 当参数str为空null或去除两边空白字符后为"",返回""
     * @param str Object
     * @return String
     */
    public static String nvlTrim(Object str) {
      return str == null ? "" : nvlTrim(str.toString());
    }
    /**
     * 当参数str为空null或去除两边空白字符后为"",返回"&nbsp;",适用于jsp页面表格的动态数据填充
     * @param str String
     * @return String
     */
    public static String nvlHtmlBlank(String str) {
      return nvlTrim(str, "&nbsp;");
    }
    /**
     * 当参数str为空null或去除两边空白字符后为"",返回"&nbsp;",适用于jsp页面表格的动态数据填充
     * @param str Object
     * @return String
     */
    public static String nvlHtmlBlank(Object str) {
      return nvlTrim(str, "&nbsp;");
    }
    /**
     * 将传入的byte型数组转化为对应的十六进制的字符串输出
     * @param bytes byte[] 字节数组
     * @return String 十六进制的字符串
     */
    public static final String encodeHex(byte[] bytes) {
        StringBuffer hex = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int)bytes[ i ] & 0xff) < 0x10) {
                hex.append("0");
            }
            hex.append(Long.toString((int)bytes[ i ] & 0xff, 16));
        }
        return hex.toString();
    }
    /**
     * 将十六进制数字型的字符串转化为byte型的数组，将字符串按两位分开
     * @param hex String 十六进制的字符串
     * @return byte[] 字节数组
     */
    public static final byte[] decodeHex(String hex) {
        char[] chars = hex.toCharArray();
        byte[] bytes = new byte[ chars.length / 2 ];
        int byteCount = 0;
        for (int i = 0; i < chars.length; i += 2) {
            byte newByte = 0x00;
            newByte |= hexCharToByte(chars[ i ]);
            newByte <<= 4;
            newByte |= hexCharToByte(chars[ i + 1 ]);
            bytes[ byteCount ] = newByte;
            byteCount++;
        }
        return bytes;
    }
    /**
     * 将字符转换为对应的16进制字节
     * @param ch char 字符0-f
     * @return byte 对应的16进制字节
     */
    public static final byte hexCharToByte(char ch) {
        switch(ch) {
            case '0': return 0x00;
            case '1': return 0x01;
            case '2': return 0x02;
            case '3': return 0x03;
            case '4': return 0x04;
            case '5': return 0x05;
            case '6': return 0x06;
            case '7': return 0x07;
            case '8': return 0x08;
            case '9': return 0x09;
            case 'a': return 0x0A;
            case 'b': return 0x0B;
            case 'c': return 0x0C;
            case 'd': return 0x0D;
            case 'e': return 0x0E;
            case 'f': return 0x0F;
        }
        return 0x00;
    }

    /**
     * 将字符串转化为所对应的hash码
     * @param value String 参数字符串
     * @return String hash码
     */
    public static final synchronized String hash(String value) {
        try {
            return encodeHex(MessageDigest.getInstance("MD5").digest(value.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * base64加密
     * @param data String 要加密的字符
     * @return String 返回加密后处理字符
     */
    public static String encodeBase64(String data) {
        return encodeBase64(data.getBytes());
    }

    /**
     * base64加密
     * @param data byte[] 要加密的byte[]
     * @return String 返回加密后处理字符
     */
    public static String encodeBase64(byte[] data) {
        int c;
        int len = data.length;
        StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
        for (int i = 0; i < len; ++i) {
            c = (data[i] >> 2) & 0x3f;
            ret.append(CVT.charAt(c));
            c = (data[i] << 4) & 0x3f;
            if (++i < len)
                c |= (data[i] >> 4) & 0x0f;
            ret.append(CVT.charAt(c));
            if (i < len) {
                c = (data[i] << 2) & 0x3f;
                if (++i < len)
                    c |= (data[i] >> 6) & 0x03;
                ret.append(CVT.charAt(c));
            }
            else {
                ++i;
                ret.append((char) FILL_CHAR);
            }
            if (i < len) {
                c = data[i] & 0x3f;
                ret.append(CVT.charAt(c));
            }
            else {
                ret.append((char) FILL_CHAR);
            }
        }
        return ret.toString();
    }

    /**
     * base64解密
     * @param data String 要解密的字符
     * @return String 返回解密后处理字符
     */
    public static String decodeBase64(String data) {
        return decodeBase64(data.getBytes());
    }

    /**
     * base64解密
     * @param data byte[] 要解密的byte[]
     * @return String 返回解密后处理字符
     */
    public static String decodeBase64(byte[] data) {
        int c, c1;
        int len = data.length;
        StringBuffer ret = new StringBuffer((len * 3) / 4);
        for (int i = 0; i < len; ++i) {
            c = CVT.indexOf(data[i]);
            ++i;
            c1 = CVT.indexOf(data[i]);
            c = ((c << 2) | ((c1 >> 4) & 0x3));
            ret.append((char) c);
            if (++i < len) {
                c = data[i];
                if (FILL_CHAR == c)
                    break;
                c = CVT.indexOf((char) c);
                c1 = ((c1 << 4) & 0xf0) | ((c >> 2) & 0xf);
                ret.append((char) c1);
            }
            if (++i < len) {
                c1 = data[i];
                if (FILL_CHAR == c1)
                    break;
                c1 = CVT.indexOf((char) c1);
                c = ((c << 6) & 0xc0) | c1;
                ret.append((char) c);
            }
        }
        return ret.toString();
    }
    /**
     * 将xml格式中的转义字符转换原来的字符
     * @param xmlStr String xml语法字符串
     * @return String 普通文本字符串
     */
    public final static String transferFromXML(String xmlStr) {
        return xmlStr == null ? null :  xmlStr.replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"")
                .replaceAll("&amp;", "&");
    }
    /**
     * 将转义字符转换为xml合法格式
     * @param text String 普通文本字符串
     * @return String 符合xml语法的字符串
     */
    public final static String transferToXML(String text) {
        return text == null ? null : text.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("&", "&amp;");
    }
    /**
     * 将html格式中的转义字符转换原来的字符
     * @param htmlStr String html语法字符串
     * @return String 普通文本字符串
     */
    public final static String tranferFromHTML(String htmlStr) {
        return htmlStr == null ? null :  htmlStr.replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"")
                .replaceAll("&nbsp;", " ")
                .replaceAll("<br/>", "\n")
                .replaceAll("<br>", "\n")
                .replaceAll("&amp;", "&");
    }
    /**
     * 将转义字符转换为html合法格式
     * @param text String 普通文本字符串
     * @return String 符合html语法的字符串
     */
    public final static String transferToHTML(String text) {
        return text == null ? null : text.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll(" ", "&nbsp;")
                .replaceAll("\n", "<br/>")
                .replaceAll("&", "&amp;");
    }
    /**
     * <li>说明：基于字符串模板方式，使用形参替换的方式返回
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-5-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String template 字符串模板，应包含${name}格式
     * @return Map params 替换参数集合，格式（key:name,value:要替换的字符串）
     * @throws String 替换后的字符串
     */
    public static String replaceAll(String template, Map params){
    	if(params == null)	return template;
    	Set keys = params.keySet();
    	for (Object key : keys) {
    		template = template.replace("${" + key.toString() + "}", params.get(key).toString());
		}
    	return template;
    }
    
}
