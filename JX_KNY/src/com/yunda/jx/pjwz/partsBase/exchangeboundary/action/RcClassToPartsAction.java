package com.yunda.jx.pjwz.partsBase.exchangeboundary.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjwz.partsBase.exchangeboundary.entity.RcClassToParts;
import com.yunda.jx.pjwz.partsBase.exchangeboundary.manager.RcClassToPartsManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RcClassToParts控制器, 修程配件分类对应车型规格型号
 * <li>创建人：王治龙
 * <li>创建日期：2013-05-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class RcClassToPartsAction extends JXBaseAction<RcClassToParts, RcClassToParts, RcClassToPartsManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * /jsp/jx/pjwz/base/PartsClassNew.js调用
     * <li>说明：选择规格型号批量生成修程配件分类对应车型规格型号
     * <li>创建人：王治龙
     * <li>创建日期：2013-5-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */ 
    public void saveOrUpdateList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	RcClassToParts[] objList = (RcClassToParts[])JSONUtil.read(getRequest(), RcClassToParts[].class);
            String[] errMsg = this.manager.saveOrUpdateList(objList);
            if (errMsg == null || errMsg.length < 1) {
                map.put("success", "true");
            }else{
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * 
     * <li>说明：配件互换配件维护对应修程的配给互换页面加载时调用
     * <li>创建人：王斌
     * <li>创建日期：2014-5-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
	public void findPageQuery() throws Exception{
		Map<String,Object> map=new HashMap<String, Object>();
		try {
			map=this.manager.findPageQueryForRcClass(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}finally{
			JSONUtil.write(getResponse(), map);
		}
	}
}