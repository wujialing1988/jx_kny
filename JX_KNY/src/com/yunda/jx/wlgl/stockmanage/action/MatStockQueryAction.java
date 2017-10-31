/**
 * 
 */
package com.yunda.jx.wlgl.stockmanage.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;
import com.yunda.jx.wlgl.stockmanage.entity.MatStockQuery;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockQueryManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatStockQuery控制器, 物料库存查询
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-9 下午04:46:04
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class MatStockQueryAction extends JXBaseAction<MatStockQuery, MatStockQuery, MatStockQueryManager>  {
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：根据库房、物料编码、物料类型、库位查询库存台账信息
     * <li>创建人：程梅
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getModelStockByLocation() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String matCode = getRequest().getParameter("matCode");  //物料编码
            String whIdx = getRequest().getParameter("whIdx");
            String matType = getRequest().getParameter("matType");//物料类型
            String locationName = getRequest().getParameter("locationName");//库位
            MatStock matStock = this.manager.getModelStockByLocation(whIdx, matCode, matType, locationName);
            if (null != matStock) {
                map.put("matStock", matStock);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
