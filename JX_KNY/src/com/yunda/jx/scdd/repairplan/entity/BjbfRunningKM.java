package com.yunda.jx.scdd.repairplan.entity;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.yunda.common.BusinessException;
import com.yunda.frame.util.StringUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: BjbfRunningKM实体类, 数据表：北京博飞走行公里接口数据存储表
 * <li>创建人：何涛
 * <li>创建日期：2016-4-22
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class BjbfRunningKM implements Serializable {
    
    /** 默认序列号 */
    private static final long serialVersionUID = 1L;
    
    private static final int TRAIN_NO_LENGTH = 4;
    
    /** 车型简称例如（SS4） */
    private String cx;
    
    /** 车号：4位字符串（0001） */
    private String ch;
    
    /** 辅修公里 */
    private Float fxgl;
    
    /** 小修公里 */
    private Float xxgl;
    
    /** 中修公里 */
    private Float zxgl;
    
    /** 大修公里 */
    private Float dxgl;
    
    /** 累计公里 */
    private Float ljgl;
    
    /** 核算公里 */
    private Float hsgl;
    
    /**
     * <li>说明：设置车型
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param cx 车型
     * @return 走行公里据对象
     */
    public BjbfRunningKM cx(String cx) {
        this.cx = cx;
        return this;
    }
    
    /**
     * <li>说明：设置车号
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ch 车号
     * @return 走行公里据对象
     */
    public BjbfRunningKM ch(String ch) {
        this.ch = ch;
        return this;
    }
    /**
     * <li>说明：设置辅修公里
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fxgl 辅修公里
     * @return 走行公里据对象
     */
    public BjbfRunningKM fxgl(Float fxgl) {
        this.fxgl = fxgl;
        return this;
    }
    /**
     * <li>说明：设置车型
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param xxgl 车型
     * @return 走行公里据对象
     */
    public BjbfRunningKM xxgl(Float xxgl) {
        this.xxgl = xxgl;
        return this;
    }
    /**
     * <li>说明：设置中修公里
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zxgl 中修公里
     * @return 走行公里据对象
     */
    public BjbfRunningKM zxgl(Float zxgl) {
        this.zxgl = zxgl;
        return this;
    }
    /**
     * <li>说明：设置大修公里
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dxgl 大修公里
     * @return 走行公里据对象
     */
    public BjbfRunningKM dxgl(Float dxgl) {
        this.dxgl = dxgl;
        return this;
    }
    /**
     * <li>说明：设置累计公里
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ljgl 累计公里
     * @return 走行公里据对象
     */
    public BjbfRunningKM ljgl(Float ljgl) {
        this.ljgl = ljgl;
        return this;
    }
    
    /**
     * <li>说明：设置核算公里
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param hsgl 核算公里
     * @return 走行公里据对象
     */
    public BjbfRunningKM hsgl(Float hsgl) {
        this.hsgl = hsgl;
        return this;
    }
    
    /**
     * <li>说明：验证车号是否为4位，如果不足四位，则在左侧补零（0）
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public void checkCh() {
        if (StringUtil.isNullOrBlank(ch)) {
            throw new BusinessException("北京博飞数据接口异常，获取的走行公里数据【车号】为空！");
        }
        int length = ch.length();
        if (TRAIN_NO_LENGTH == length) {
            return;
        }
        int num = TRAIN_NO_LENGTH - length;
        for (int i = 0; i < num; i++) {
            ch = 0 + ch;
        }
    }
    
    /**
     * @return 获取车号
     */
    public String getCh() {
        return ch;
    }
    
    /**
     * @param ch 设置车号
     */
    public void setCh(String ch) {
        this.ch = ch;
    }
    
    /**
     * @return 获取车型
     */
    public String getCx() {
        return cx;
    }
    
    /**
     * @param cx 设置车型
     */
    public void setCx(String cx) {
        this.cx = cx;
    }
    
    /**
     * @return 获取大修公里
     */
    public Float getDxgl() {
        return dxgl;
    }
    
    /**
     * @param dxgl 设置大修公里
     */
    public void setDxgl(Float dxgl) {
        this.dxgl = dxgl;
    }
    
    /**
     * @return 获取辅修公里
     */
    public Float getFxgl() {
        return fxgl;
    }
    
    /**
     * @param fxgl 设置辅修公里
     */
    public void setFxgl(Float fxgl) {
        this.fxgl = fxgl;
    }
    
    /**
     * @return 获取核算公里
     */
    public Float getHsgl() {
        return hsgl;
    }
    
    /**
     * @param hsgl 设置核算公里
     */
    public void setHsgl(Float hsgl) {
        this.hsgl = hsgl;
    }
    
    /**
     * @return 获取累计公里
     */
    public Float getLjgl() {
        return ljgl;
    }
    
    /**
     * @param ljgl 设置累计公里
     */
    public void setLjgl(Float ljgl) {
        this.ljgl = ljgl;
    }
    
    /**
     * @return 获取小修公里
     */
    public Float getXxgl() {
        return xxgl;
    }
    
    /**
     * @param xxgl 设置小修公里
     */
    public void setXxgl(Float xxgl) {
        this.xxgl = xxgl;
    }
    
    /**
     * @return 获取中修公里
     */
    public Float getZxgl() {
        return zxgl;
    }
    
    /**
     * @param zxgl 设置中修公里
     */
    public void setZxgl(Float zxgl) {
        this.zxgl = zxgl;
    }
    
}
