package com.yunda.zb.mobile.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.baseapp.upload.entity.Attachment;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.FileUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.jxgc.tpmanage.entity.RepairEmpBean;
import com.yunda.jx.pjwz.partsBase.professionaltype.entity.ProfessionalType;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.mobile.entity.AttachmentRelation;
import com.yunda.zb.mobile.entity.FaultPhoto;
import com.yunda.zb.mobile.manager.AttachmentRelationManager;
import com.yunda.zb.mobile.manager.ZbglTpMobileManager;
import com.yunda.zb.tp.entity.ZbglTp;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 整备移动终端提票请求接口
 * <li>创建人：林欢
 * <li>创建日期：2016-8-3
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class ZbglTpMobileAction extends JXBaseAction<ZbglTp, ZbglTp, ZbglTpMobileManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	
	 /**
     * 附件管理业务对象
     */
    @Resource
    private AttachmentManager attachmentManager;   
    
    /**
     * 附件与业务节点关系对象
     */
    @Resource
    private AttachmentRelationManager attachmentRelationManager; 
	
	
	/**
     * <li>说明：前台获取车型 车号 故障位置 故障现象，后台返回同车同位置同故障现象次数
     * <li>创建人：刘国栋
     * <li>创建日期：2016-8-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException， IOException
     */
	public void findZbglTpByParam() throws JsonMappingException, IOException{
		//map用于转载响应的内容
		Map<String, Object> map = new HashMap<String, Object>();
		try {
            Map<String, Object> mapParam = new HashMap<String, Object>();
            HttpServletRequest reg = getRequest();//得到http请求，通过请求从前台传递参数到后台
            String trainTypeShortNameAndTrainNo = reg.getParameter("trainTypeShortNameAndTrainNo");//获得车型车号
            String faultFixFullCode = reg.getParameter("faultFixFullCode");//获得故障位置
            String faultID = reg.getParameter("faultID");//获得故障现象
                      
            //车型 车号，故障位置，故障现场，从前台获取 
            mapParam.put("trainTypeShortNameAndTrainNo", trainTypeShortNameAndTrainNo);
            mapParam.put("faultFixFullCode", faultFixFullCode);
            mapParam.put("faultID", faultID);
            //状态通过ZbglTp实体自动生成
            mapParam.put("faultNoticeStatus", "'" + ZbglTp.STATUS_OVER + "','" + ZbglTp.STATUS_CHECK + "'");
            
            List<ZbglTp> zbglTpList = this.manager.giveToManager(mapParam);//调用action对应的manager中的giveToManager方法，传递mapParam参数
            //得到同车同位置同故障列表之后，判断列表的大小
            if (zbglTpList != null) {
                map.put("errMsg", String.valueOf(zbglTpList.size()));
                map.put("success", true);
            }else {
                map.put("errMsg", "0");
                map.put("success", true);
            }       
        } catch (Exception e) {
            ExceptionUtil.process(e, logger,map);//捕获到异常后，生成日志，并将异常信息封装到map中
        }finally {
			JSONUtil.write(this.getResponse(), map);//将内容写入响应中
		}
    }
    
    /**
     * <li>说明：分页分组查询车型车号信息
     * <li>创建人：林欢
     * <li>创建日期：2016-8-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void querySameTrainNoAndTrainTypeIDXInfoList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = req.getParameter("searchJson");// 查询条件JSON字符串
            String operatorid = req.getParameter("operatorid");// 操作者ID
            map = this.manager.querySameTrainNoAndTrainTypeIDXInfoList(getStart(), getLimit(), searchJson,operatorid).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据车型车号分页查询提票页面（临修）(待销活/待接活)
     * <li>创建人：林欢
     * <li>创建日期：2016-8-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void queryZbglTpList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = req.getParameter("searchJson");// 查询条件JSON字符串
            String repairClass = req.getParameter("repairClass");// 票活类型（值为2.1.1提票常量中的碎修/临修）
            String faultNoticeStatus = req.getParameter("faultNoticeStatus");// 待接活/待销活（值为2.1.1提票常量中的待接活/待销活）
            String operatorid = req.getParameter("operatorid");// 操作者ID
            
            Map<String, String> params = new HashMap<String, String>();
            params.put("searchJson", searchJson);
            params.put("repairClass", repairClass);
            params.put("faultNoticeStatus", faultNoticeStatus);
            params.put("operatorid", operatorid);

            map = this.manager.queryZbglTpList(getStart(), getLimit(), params).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：领取提票活
     * <li>创建人：林欢
     * <li>创建日期：2016-08-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void receiveTp() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String operatorid = getRequest().getParameter("operatorid");//操作人idx
            String idxs = getRequest().getParameter("idxs");//提票活idx
            String repairClass = getRequest().getParameter("repairClass");//票活类型（值为2.1.1提票常量中的碎修/临修）
            
            // 领取提票活
            this.manager.receiveTp(operatorid, idxs, repairClass);
            map.put(Constants.SUCCESS, true);
        } catch (Exception ex) {
            ExceptionUtil.process(ex, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取故障施修方法分页列表
     * <li>创建人：林欢
     * <li>创建日期：2016-8-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findFaultMethod() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String entity = "com.yunda.jx.base.jcgy.entity.FaultMethod";
            Map queryParamsMap = JSONUtil.read("{}", Map.class);
            map = this.manager.findFaultMethod(entity, null, null, queryParamsMap, getStart(), getLimit(), null);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询碎修提票处理结果列表
     * <li>创建人：林欢
     * <li>创建日期：2016-8-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void queryRepairResultList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = this.manager.queryRepairResultList().extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：销活
     * <li>创建人：林欢
     * <li>创建日期：2016-08-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void handleTp() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String operatorid = getRequest().getParameter("operatorid");//操作人idx
            String idxs = getRequest().getParameter("idxs");//提票活idx
            String tpData = getRequest().getParameter("tpData");//处理提票信息JSON字符串        
            
            ZbglTp tp = JSONUtil.read(StringUtil.nvlTrim(tpData, "{}"), ZbglTp.class);
            // 领取提票活
            this.manager.handleTp(operatorid, idxs, tp);
            
            //销票时上传附件图片
            String jsonPhotos = getRequest().getParameter("faultPhotos");
//          获得提票业务节点
        	String node = ZbConstants.UPLOADPATH_NODE_XP;
           	if(jsonPhotos != null && !"".equals(jsonPhotos)){
           		FaultPhoto[] faultPhotos = JSONUtil.read(jsonPhotos, FaultPhoto[].class);
           		for (FaultPhoto photo : faultPhotos) {
           			copyFaultFile(idxs, photo.getFilePath(), photo.getFilename(), node);
				}
        	}
            map.put(Constants.SUCCESS, true);
        } catch (Exception ex) {
            ExceptionUtil.process(ex, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取专业类型
     * <li>创建人：林欢
     * <li>创建日期：2016-8-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getProfessionalType() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	
        	//添加模糊查询条件
        	String professionalTypeName = getRequest().getParameter("professionalTypeName");//类型名称
        	
        	ProfessionalType type = new ProfessionalType();
            type.setStatus(ProfessionalType.status_start);
            type.setProfessionalTypeName(professionalTypeName);
            SearchEntity<ProfessionalType> searchEntity = new SearchEntity<ProfessionalType>(type, start , limit, null);
            Page page = this.manager.getProfessionalType(searchEntity);
            map = page.extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取提票处理其他作业人员列表
     * <li>创建人：林欢
     * <li>创建日期：2016-8-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getOtherWorkerByTP() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	
        	String operatorid = getRequest().getParameter("operatorid");//操作
        	List<RepairEmpBean> list = this.manager.getOtherWorkerByTP(operatorid);
            map = new Page(list).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：读取故障提票上传的照片
     * <li>创建人：林欢
     * <li>创建日期：2016-08-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void downloadFaultImg() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String attachmentKeyIDX = getRequest().getParameter("attachmentKeyIDX");//提票单IDX
            //获得作业节点 9.2
            String node = getRequest().getParameter("node");
            // 读取故障提票上传的照片
            List<String> base64List = this.manager.downloadFaultImg(attachmentKeyIDX, node);
            if (base64List.isEmpty()) {
            	map.put(Constants.SUCCESS, false);
                map.put("errMsg", "未查询到相关图片！");
			}else {
				map.put(Constants.SUCCESS, true);
	            map.put("errMsg", base64List);
			}
            
        } catch (Exception ex) {
            ExceptionUtil.process(ex, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
    /**
     * <li>说明：复制故障提票图片至附件上传路径并保存附件记录
     * <li>创建人：程锐
     * <li>创建日期：2015-1-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param attachmentKeyIDX 附件业务主键
     * @param attachmentKeyName 附件业务主键名称
     * @param imgFilePath 照片上传路径
     * @param filename 文件名
     * @param node 业务节点
     * @return 
     */
    private void copyFaultFile(String attachmentKeyIDX, String imgFilePath, String filename, String node) {
        try {
            if (StringUtil.isNullOrBlank(imgFilePath))
                return;
            String[] imgFilePaths = StringUtil.tokenizer(imgFilePath, ",");
            for (int i = 0; i < imgFilePaths.length; i++) {
                String extName = "";
                int idx = imgFilePaths[i].lastIndexOf(".");
                if (idx != -1)
                    extName = imgFilePaths[i].substring(idx + 1);
                
                String attachmentKeyName = ZbConstants.UPLOADPATH_TP_IMG;
                if("mp3".equalsIgnoreCase(extName))	attachmentKeyName = ZbConstants.UPLOADPATH_TP_AUDIO;
                
                Attachment att = new Attachment();
                att.setAttachmentKeyIDX(attachmentKeyIDX);
                att.setAttachmentKeyName(attachmentKeyName);
                att.setAttachmentRealName(filename);
                
                Date now = new Date();
                String saveName = att.getAttachmentKeyIDX() + "_" + DateUtil.yyyyMMddHHmmssSSS.format(now) + "." + extName; // 通过对象主键+时间戳生成保存文件名称
                att.setAttachmentSaveName(saveName);
                att.setFileType(extName);
                
                // 获取当前登录操作员
                AcOperator ac = SystemContext.getAcOperator();
                OmEmployee employee = SystemContext.getOmEmployee();
                String personName = ac.getOperatorname();
                if (employee != null && employee.getEmpname() != null) {
                    personName = employee.getEmpname();
                }
                att.setUploadPerson(ac.getOperatorid());
                att.setUploadPersonName(personName);
                att.setUploadTime(now);
                // 设置业务无关的附加信息
                att = EntityUtil.setNoDelete(att);
                att = EntityUtil.setSysinfo(att, now);
                
                String saveDir = AttachmentManager.uploadDir(att.getAttachmentKeyName()); // 获取文件保存目录
                FileUtil.createDir(saveDir); // 如果文件保存目录不存在，自动创建
                String attSaveName = att.getAttachmentSaveName();
                long fileSize = FileUtil.copyFile(imgFilePaths[i], saveDir, attSaveName);
                if (fileSize != 0) {
                    FileUtil.deleteFile(imgFilePaths[i]);
                }
                att.setAttachmentSize(fileSize);
                //保存附件
                attachmentManager.add(att);
                
                //同时，如果上传附件时有业务节点，需要将该业务所属，本次上传的附件和对应节点的关系保存
                if(!StringUtil.isNullOrBlank(node)){
                	AttachmentRelation attachmentRelation = new AttachmentRelation();
                	//业务节点
                	attachmentRelation.setAttachmentNode(node);
                	//所属业务主键
                	attachmentRelation.setAttachmentKeyIDX(attachmentKeyIDX);
                	//上传附件的主键
                	attachmentRelation.setAttachmentIDX(att.getIdx());
                	//保存关系对象
                	attachmentRelationManager.add(attachmentRelation);
                }
            }
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        }
    }
}
