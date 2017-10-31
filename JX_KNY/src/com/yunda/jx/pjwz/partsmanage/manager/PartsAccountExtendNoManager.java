package com.yunda.jx.pjwz.partsmanage.manager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsExtendNoBean;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccountExtendNo;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsAccountExtendNo业务类,配件信息扩展编号
 * <li>创建人：程锐
 * <li>创建日期：2014-08-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsAccountExtendNoManager")
public class PartsAccountExtendNoManager extends JXBaseManager<PartsAccountExtendNo, PartsAccountExtendNo>{
    
	/**
     * <li>说明：保存配件信息扩展编号
     * <li>创建人：程锐
     * <li>创建日期：2014-8-26
     * <li>修改人： 何涛
     * <li>修改日期：2014-08-28
     * <li>修改内容：增加对extendNoJson参数的空值验证
     * <li>修改人： 何涛
     * <li>修改日期：2014-04-08
     * <li>修改内容：代码审查，使用规范的异常处理方式
     * @param partsAccountIDX
     * @param extendNoJson
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public void saveExtendNo(String partsAccountIDX, String extendNoJson) throws BusinessException, NoSuchFieldException {
        if (StringUtil.isNullOrBlank(extendNoJson)) {
            return;
        }
        PartsAccountExtendNo partsAccountExtendNo = new PartsAccountExtendNo();
        partsAccountExtendNo.setPartsAccountIDX(partsAccountIDX);
        List<PartsExtendNoBean> partsExtendNoBeanList = new ArrayList<PartsExtendNoBean>();
        PartsExtendNoBean[] partsExtendNoBeans;
        try {
            partsExtendNoBeans = JSONUtil.read(extendNoJson, PartsExtendNoBean[].class);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        partsExtendNoBeanList = Arrays.asList(partsExtendNoBeans);
        if (partsExtendNoBeanList != null && partsExtendNoBeanList.size() > 0) {
            for (PartsExtendNoBean bean : partsExtendNoBeanList) {
                try {
                    Field field = PartsAccountExtendNo.class.getDeclaredField(bean.getExtendNoField());
                    if (field != null) {
                        field.setAccessible(true); // 设置属性为可访问
                        try {
                            field.set(partsAccountExtendNo, bean.getValue());
                        } catch (Exception e) {
                            throw new BusinessException(e);
                        }
                    }
                } catch (SecurityException e) {
                } catch (NoSuchFieldException e) {
                }
            }
        }
        saveOrUpdate(partsAccountExtendNo);
    }
	
	/**
     * <li>说明：根据“扩展编号json”构造“扩展编号”显示字符串
     * <li>创建人：何涛
     * <li>创建日期：2014-08-28
     * <li>修改人：何涛
     * <li>修改日期：2016-04-08
     * <li>修改内容：代码优化
     * @param extendNoJson 扩展编号json
     * @return 扩展编号
     */
    public String getExtendNoFromJson(String extendNoJson) {
        if (StringUtil.isNullOrBlank(extendNoJson)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        PartsExtendNoBean[] beans = null;
        try {
            beans = JSONUtil.read(extendNoJson, PartsExtendNoBean[].class);
            if (null == beans || 0 >= beans.length) {
                return null;
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        for (int i = 1; i < beans.length; i++) {
            String value = beans[i].getValue();
            if (StringUtil.isNullOrBlank(value)) {
                continue;
            }
            sb.append("|");
            sb.append(value);
        }
        return sb.substring(1);
    }
    
}