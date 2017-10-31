package com.yunda.jx.pjjx.partsrdp.recordinst.action; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordDI;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordDIManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpRecordDI控制器, 配件检修检测数据项
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsRdpRecordDIAction extends JXBaseAction<PartsRdpRecordDI, PartsRdpRecordDI, PartsRdpRecordDIManager>{
	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
	
    /**
     * <li>说明：检测项查询
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void findListForDI() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String rdpRecordRIIDX = getRequest().getParameter("rdpRecordRIIDX");            
            List<PartsRdpRecordDI> list = manager.getModelByRdpRecordRIIDX(rdpRecordRIIDX);
            
            map = Page.extjsStore("idx", list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取检修检测结果的数据字典项
     * <li>创建人：程锐
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void getDicForRIRepairResult() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        map.put(Constants.SUCCESS, false);      
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("from EosDictEntry where status = 1");
            sb.append("and id.dicttypeid='").append("PJJX_RECORD_RI_RESULT").append("'");
            List<EosDictEntry> list = this.manager.getDaoUtils().find(sb.toString());
            List<String> tempList = new ArrayList<String>(list.size());
            for (EosDictEntry entry : list) {
                tempList.add(entry.getDictname());
            }
            map = Page.extjsStore("dictname", tempList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}