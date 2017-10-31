package com.yunda.zb.rdp.zbtaskbill.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWidi;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglRdpWi控制器, 机车整备任务单
 * <li>创建人：程锐
 * <li>创建日期：2015-01-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbglRdpWiAction extends JXBaseAction<ZbglRdpWi, ZbglRdpWi, ZbglRdpWiManager> {
    
    /** 日志工具 */
    Logger logger = Logger.getLogger(getClass());
    
    /**
     * 附件管理业务对象
     */
    @Resource
    private AttachmentManager attachmentManager; 
    
    /**
     * <li>说明：整备作业工单分页查询（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    @SuppressWarnings("unchecked")
    public void findPageQuery() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        Long operatorid = SystemContext.getAcOperator().getOperatorid();
        HttpServletRequest req = getRequest();
        try {
            // 任务单状态（必需）
            String wiStatus = req.getParameter("wiStatus");
            // 查询条件
            String searchJson = req.getParameter("entityJson");
            Page page = this.manager.queryRdpWiList(searchJson, wiStatus, operatorid, getStart(), getLimit());
            map = page.extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findPageQueryForWeb() throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String,Object>();
		try {
			QueryCriteria<ZbglRdpWi> query = new QueryCriteria<ZbglRdpWi>(getQueryClass(),getWhereList(), getOrderList(), getStart(), getLimit());
			Page<ZbglRdpWi> pages = (Page<ZbglRdpWi>)this.manager.findPageList(query);
			for (ZbglRdpWi wi : pages.getList()) {
				List atts = attachmentManager.findListByKey(wi.getIdx(), ZbConstants.UPLOADPATH_TP_IMG);
				if(wi.getIsCheckPicture() != null && wi.getIsCheckPicture() == 1){
					if(atts != null && atts.size() > 0){
						wi.setCheckPictureStatus(2);
					}else{
						wi.setCheckPictureStatus(1);
					}
				}else{
					wi.setCheckPictureStatus(0);
				}
			}
			map = pages.extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
    }
    
    /**
     * <li>说明：整备作业工单领取（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void updateForReceive() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            // 当前操作员信息
            OmEmployee employee = SystemContext.getOmEmployee(); 
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ids.length; i++) {
                sb.append(",").append(ids[i]);
            }
            this.manager.receiveRdp(employee.getOperatorid(), sb.substring(1));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：整备作业工单撤销领取（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void updateForRevoke() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            // 当前操作员信息
            OmEmployee employee = SystemContext.getOmEmployee(); 
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ids.length; i++) {
                sb.append(",").append(ids[i]);
            }
            this.manager.cancelReceivedRdp(employee.getOperatorid(), sb.substring(1));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：整备作业工单暂存（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void updateForRdpWidi() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            // 整备任务单idx
            String rdpWiIDX = req.getParameter("rdpWiIDX");
            // 机车整备任务单数据项实体数组JSON字符串
            String widiDatas = req.getParameter("widiDatas");
            ZbglRdpWidi[] widiArray = null;
            if (!StringUtil.isNullOrBlank(widiDatas)) {
                widiArray = JSONUtil.read(widiDatas, ZbglRdpWidi[].class);
            }
            this.manager.updateForRdpWidi(rdpWiIDX, widiArray);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：整备作业工单销活（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void handleRdp() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
			// 整备任务单idx
            String rdpWiIDX = req.getParameter("rdpWiIDX");
            // 机车整备任务单数据项实体数组JSON字符串
            String widiDatas = req.getParameter("widiDatas");
            // 是否合格
            String isHg = req.getParameter("isHg");
            
            // 联合作业人员
            String worker = req.getParameter("worker");
            ZbglRdpWidi[] widiArray = null;
            if (!StringUtil.isNullOrBlank(widiDatas)) {
                widiArray = JSONUtil.read(widiDatas, ZbglRdpWidi[].class);
            }
            // 当前操作员信息
            OmEmployee employee = SystemContext.getOmEmployee(); 
            this.manager.updateForHandleRdp(employee.getOperatorid(), rdpWiIDX, isHg, widiArray,worker);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：查询机车过滤信息（iPad应用）
     * <li>创建人：姚凯
     * <li>创建日期：2015-7-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getFilterModels() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 任务单状态（必需）
            String wiStatus = getRequest().getParameter("wiStatus");
            // 获取查询条件
            String searchParam = getRequest().getParameter("searchParam");
            // 查询机车过滤信息
            List models = this.manager.getFilterModels(wiStatus, searchParam);
            // 组装页面Store所要求的的数据源格式
            map.put("id", EntityUtil.IDX);
            map.put("root", models);
            map.put("totalProperty", models.size());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * 获取附件图片列表
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getRdpWiDiImgs() throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String,Object>();
    	List atts = null;
    	try {
    		String wiIdx = getRequest().getParameter("wiIdx");
    		atts = attachmentManager.findListByKey(wiIdx, ZbConstants.UPLOADPATH_TP_IMG);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), atts);
		}
    }
    
    /**
     * 获取附件录音列表
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getRdpWiDiAudio() throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String,Object>();
    	List atts = null;
    	try {
    		String wiIdx = getRequest().getParameter("wiIdx");
    		atts = attachmentManager.findListByKey(wiIdx, ZbConstants.UPLOADPATH_TP_AUDIO);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), atts);
		}
    }
}
