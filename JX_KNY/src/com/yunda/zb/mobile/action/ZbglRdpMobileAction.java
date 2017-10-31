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
import org.springframework.beans.factory.annotation.Autowired;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.baseapp.upload.entity.Attachment;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.FileUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.jxgc.dispatchmanage.manager.ZbglWorkStationBindingManager;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.mobile.entity.FaultPhoto;
import com.yunda.zb.mobile.entity.ZbglRdpWiMobileDTO;
import com.yunda.zb.mobile.manager.AttachmentRelationManager;
import com.yunda.zb.mobile.manager.ZbglRdpMobileManager;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 整备范围活
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-8-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public class ZbglRdpMobileAction extends JXBaseAction<ZbglRdp,ZbglRdp,ZbglRdpMobileManager> {
    
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

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
     * 工位绑定服务
     */
    @Resource
    private ZbglWorkStationBindingManager zbglWorkStationBindingManager ;
    
    /**
     * 机车整备任务单业务对象
     */
    @Autowired
    private ZbglRdpWiManager zbglRdpWiManager;

	/**
     * <li>说明：分页查询当前登录人机车整备单列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-7-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void queryZbglRdpList() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String operatIDX = req.getParameter("operatIDX");//登陆人idx
            String queryStr = req.getParameter("queryStr");//查询条件
            String workStationIDX = req.getParameter("workStationIDX");
            List bangs =  zbglWorkStationBindingManager.getBindingWorkStationByOperatIDX(operatIDX);
            // 如果没有绑定工位则查询全部整备单
            if(bangs == null || bangs.size() == 0){
            	map = this.manager.queryZbglRdpAllList(getStart(),getLimit(),queryStr,workStationIDX).extjsStore();
            }else{
            	map = this.manager.queryZbglRdpList(getStart(),getLimit(),operatIDX,queryStr,workStationIDX).extjsStore();
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明:根据整备单和工位查询整备活项
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void queryZbglRdpWiListByRDPIDandWSid() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String operatIDX = req.getParameter("operatIDX");//登陆人idx
            String zbRdpId = req.getParameter("zbRdpId");//整备任务单id
            String workStationIDX = req.getParameter("workStationIDX"); // 工位id
            List bangs =  zbglWorkStationBindingManager.getBindingWorkStationByOperatIDX(operatIDX);
            // 如果没有绑定工位则查询全部整备单
            if(bangs == null || bangs.size() == 0){
            	map = this.manager.queryZbglRdpWiListByRDPIDandWSidAll(getStart(),getLimit(),zbRdpId,workStationIDX).extjsStore();
            }else{
            	map = this.manager.queryZbglRdpWiListByRDPIDandWSid(getStart(),getLimit(),operatIDX,zbRdpId,workStationIDX).extjsStore();
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：机车整备任务单完工
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void completZbglRdpWi() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            // 任务单
            String jsonObject = req.getParameter("jsonObject");
            ZbglRdpWiMobileDTO entity = JSONUtil.read(jsonObject, ZbglRdpWiMobileDTO.class);
            if(entity != null){
                this.manager.completZbglRdpWi(entity);
                this.manager.getDaoUtils().flush();
                // 附件
                String jsonPhotos = req.getParameter("faultPhotos");
                if(jsonPhotos != null && !"".equals(jsonPhotos)){
                    FaultPhoto[] faultPhotos = JSONUtil.read(jsonPhotos, FaultPhoto[].class);
                    for (FaultPhoto photo : faultPhotos) {
                        copyFaultFile(entity.getZbglRdpWiIDX(), photo.getFilePath(), photo.getFilename());
                    }
                }
                map.put(Constants.SUCCESS, true);
            } else {
                map.put(Constants.ERRMSG, "解析参数值jsonObject生成的ZbglRdpWiMobileDTO为空");
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：复制整备范围活图片至附件上传路径并保存附件记录
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-07-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param attachmentKeyIDX 附件业务主键
     * @param attachmentKeyName 附件业务主键名称
     * @param imgFilePath 照片上传路径
     * @param filename 文件名
     * @return 
     */
    private void copyFaultFile(String attachmentKeyIDX, String imgFilePath, String filename) {
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
                if("mp3".equalsIgnoreCase(extName)) attachmentKeyName = ZbConstants.UPLOADPATH_TP_AUDIO;
                
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
                //保存附件到附件管理表
                att.setAttachmentSize(fileSize);
                attachmentManager.add(att);
            }
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        }
    }
    
    
    /**
     * <li>说明：领活
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-7-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void receiveRdp() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            // 任务单
            String operatorid = req.getParameter("operatorid");
            String idxs = req.getParameter("idxs");
            if(!StringUtil.isNullOrBlank(operatorid)){
            	zbglRdpWiManager.receiveRdp(Long.parseLong(operatorid), idxs);
            	map.put(Constants.SUCCESS, true);
            }else{
            	map.put(Constants.SUCCESS, false);
            	map.put(Constants.ERRMSG, "操作人员不能为空！");
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
}
