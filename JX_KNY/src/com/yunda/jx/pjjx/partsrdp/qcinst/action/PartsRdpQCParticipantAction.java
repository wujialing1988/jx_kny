package com.yunda.jx.pjjx.partsrdp.qcinst.action; 

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
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQCParticipant;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQCParticipant.QCEmp;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQCParticipantManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpQCParticipant控制器, 质量可检查人员
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
public class PartsRdpQCParticipantAction extends JXBaseAction<PartsRdpQCParticipant, PartsRdpQCParticipant, PartsRdpQCParticipantManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：根据“记录卡实例主键”查询【质量可检查人员】
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 *
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void getModelByRdpRecordCardIDX() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 记录卡实例主键
			String rdpRecordCardIDX = getRequest().getParameter("rdpRecordCardIDX");
			
			List<PartsRdpQCParticipant> list = this.manager.getModelByRdpRecordCardIDX(rdpRecordCardIDX);
            Map<String, List<String>> hashMap = new HashMap<String, List<String>>();
            for (PartsRdpQCParticipant t : list) {
                List<String> tempList = hashMap.get(t.getQCItemName());
                if (null == tempList) {
                    tempList = new ArrayList<String>();
                    hashMap.put(t.getQCItemName(), tempList);
                }
                tempList.add(t.getQCEmpName());
            }
            if (hashMap.size() > 0) {
                map.put("qcInfo", hashMap);
            }
			map.put("qcParticipantList", list);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
    
    /**
     * <li>说明：获取记录工单指派的质量检查人员
     * <li>创建人：程锐
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getQCEmpsForAssign() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        map.put(Constants.SUCCESS, false);      
        try {
            String rdpRecordCardIDX = getRequest().getParameter("rdpRecordCardIDX");
            List<QCEmp> beanlist = this.manager.getQCEmpsForAssign(rdpRecordCardIDX);
            map = Page.extjsStore("qCItemNo", beanlist);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}