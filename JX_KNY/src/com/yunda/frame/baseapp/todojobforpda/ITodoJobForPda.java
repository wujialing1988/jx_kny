package com.yunda.frame.baseapp.todojobforpda;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: pda引用功能权限接口
 * <li>创建人：程锐
 * <li>创建日期：2015-11-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface ITodoJobForPda {
	/** 良好配件登记 */
    String FUNC_LHPJ = "lhpj";  
    
    /** 修竣配件合格验收 */
    String FUNC_HGYS = "hgys";  
    
    /** 配件报废登记 */
    String FUNC_PJBF = "pjbf";  
        
    /** 配件出库 */
    String FUNC_PJCK = "pjck";
    
    /** 配件调出登记 */
    String FUNC_PJDC = "pjdc";  
    
    /** 配件识别码绑定 */
    String FUNC_PJSBM = "pjsbm";  
    
    /** 配件上车登记 */
    String FUNC_PJSC = "pjsc";  
    
    /** 配件委外登记 */
    String FUNC_PJWY = "pjwy";  
    
    /** 配件信息查询 */
    String FUNC_PJXXCX = "pjxxcx";  
    
    /** 下车配件登记 */
    String FUNC_XCPJ = "xcpj";  
    
    /** 修竣配件入库 */
    String FUNC_XJPJRK = "xjpjrk";  
    
    /** 配件检修质量检验 */
    String FUNC_ZLJY = "zljy"; 
}
