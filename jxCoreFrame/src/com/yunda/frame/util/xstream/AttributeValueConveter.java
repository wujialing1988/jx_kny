package com.yunda.frame.util.xstream;  
  
import java.lang.reflect.Constructor;  
import java.lang.reflect.Field;  
import java.util.ArrayList;  
import java.util.Iterator;  
import java.util.List;  
  
import com.thoughtworks.xstream.converters.Converter;  
import com.thoughtworks.xstream.converters.MarshallingContext;  
import com.thoughtworks.xstream.converters.UnmarshallingContext;  
import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;  
import com.thoughtworks.xstream.io.HierarchicalStreamReader;  
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;  
import com.thoughtworks.xstream.mapper.Mapper;  
  
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 解决xstream属性value映射的问题
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * @author （原作者Zhao.Allen，代码出自ITEYE网站论坛技术贴） 
 * @version 1.0
 */
public class AttributeValueConveter implements Converter {  
	/** 定义需要特殊处理的属性名称，此次为“value”，也可以定义其他名称进行特殊处理 */
    public String textName = "value";  
    /** XStream映射器（处理bean和xml之间的映射） */
    private Mapper mapper;  
    /** 需要转换的类集合 */
    private List convertClass = new ArrayList();
    
    /**
     * <li>说明：设置映射器
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-4-12
     * <li>修改人： 
     * <li>修改日期：
     */
    public AttributeValueConveter(Mapper mapper) {  
        this.mapper = mapper;  
    }  
    /**
     * <li>说明：序列化
     * <li>创建人：
     * <li>创建日期：
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */    
    public void marshal(Object obj, HierarchicalStreamWriter wt, MarshallingContext context) {  
        Field[] values = obj.getClass().getDeclaredFields();  
        for (int i = 0; i < values.length; i++) {  
            Field value = values[i];  
            String name = value.getName();  
            if (name.equals(textName)) {  
                continue;  
            }  
            try {  
                if (!value.isAccessible()) {  
                    value.setAccessible(true);  
                }  
                Object v = value.get(obj);  
                if (v == null) {  
                    continue;  
                }  
                if (Number.class.isAssignableFrom(v.getClass()) || v.getClass().isPrimitive()) {  
                    wt.addAttribute(mapper.serializedMember(obj.getClass(), name), String.valueOf(v));  
                } else {  
                    wt.addAttribute(mapper.serializedMember(obj.getClass(), name), v.toString());  
                }  
            } catch (Exception e) {  
                throw new ObjectAccessException("Cannot set Field " + value.getName() + obj.getClass(), e);  
            }  
        }  
        try{  
            Field field = obj.getClass().getDeclaredField(textName);  
            if(!field.isAccessible()){  
                field.setAccessible(true);  
            }  
            Object v = field.get(obj);  
            if (null != v && !"".equals(v.toString())) {  
                wt.setValue(v.toString());  
            }  
        } catch (Exception e) {  
            throw new ObjectAccessException("Cannot set Field " + textName + obj.getClass(), e);  
        }  
    }  
    /**
     * <li>说明：反序列化
     * <li>创建人：
     * <li>创建日期：
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */    
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {  
        Object obj = context.currentObject();  
        if (obj == null) {  
            try {  
                obj = context.getRequiredType().newInstance();  
            } catch (Exception e) {  
                throw new ObjectAccessException("Cannot construct " + context.getRequiredType().getName(), e);  
            }  
        }  
  
        Iterator attNames = reader.getAttributeNames();  
        while (attNames.hasNext()) {  
            String attName = (String) attNames.next();  
            if (attName.equals(textName)) {  
                continue;  
            }  
  
            try {  
                Field field = obj.getClass().getDeclaredField(mapper.realMember(obj.getClass(), attName));  
                if (!field.isAccessible()) {  
                    field.setAccessible(true);  
                }  
                String v = reader.getAttribute(attName);  
                if (null == v || "".equals(v)) {  
                    continue;  
                }  
                Class fieldType = field.getType();  
                Constructor strnum = fieldType.getDeclaredConstructor(String.class);  
                field.set(obj, strnum.newInstance(v));  
            } catch (Exception e) {  
                e.printStackTrace();  
                throw new ObjectAccessException("Cannot construct " + obj.getClass(), e);  
            }  
        }  
        String value = reader.getValue();  
        if (null != value && !"".equals(value)) {  
            try {  
                Field field = obj.getClass().getDeclaredField(mapper.realMember(obj.getClass(), textName));  
                if (!field.isAccessible()) {  
                    field.setAccessible(true);  
                }  
                field.set(obj, value);  
            } catch (Exception e) {  
                e.printStackTrace();  
                throw new ObjectAccessException("Cannot construct " + obj.getClass(), e);  
            }  
        }  
        return obj;  
    }  
    /**
     * <li>说明：是否可转换
     * <li>创建人：
     * <li>创建日期：
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public boolean canConvert(Class clazz) {  
        /*Iterator it = convertClass.iterator(); 
        while (it.hasNext()) { 
            Class c = (Class) it.next(); 
            if (c.equals(clazz)) { 
                return true; 
            } 
        }return false;*/  
        return convertClass.contains(clazz);  
    }  
  
    public Iterator getConvertClass() {  
        return convertClass.iterator();  
    }  
  
    public void addConvertClass(Class cc) {  
        this.convertClass.add(cc);  
    }  
  
    public String getTextName() {  
        return textName;  
    }  
    public void setTextName(String textName) {  
        this.textName = textName;  
    }  
}  