package com.yunda.zb.mobile.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.baseapp.upload.entity.Attachment;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.FileUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.EmployeeManager;
import com.yunda.frame.yhgl.manager.IEosDictEntryManager;
import com.yunda.jx.base.jcgy.entity.EquipFault;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jx.base.jcgy.manager.EquipFaultManager;
import com.yunda.jx.base.jcgy.manager.TrainTypeManager;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault;
import com.yunda.jx.jxgc.buildupmanage.manager.BuildUpTypeQueryManager;
import com.yunda.jx.jxgc.buildupmanage.manager.PlaceFaultManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.EosDictEntryBean;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessInAndOutManager;
import com.yunda.util.MediaUtil;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.mobile.entity.AttachmentRelation;
import com.yunda.zb.mobile.entity.FaultPhoto;
import com.yunda.zb.mobile.manager.AttachmentRelationManager;
import com.yunda.zb.mobile.manager.MobileManager;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpDTO;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpNodeManager;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;
import com.yunda.zb.tecorder.manager.ZbglTecOrderManager;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.manager.ZbglTpManager;
import com.yunda.zb.trainclean.entity.ZbglCleaning;
import com.yunda.zb.trainclean.manager.ZbglCleaningManager;
import com.yunda.zb.trainhandover.entity.ZbglHoCase;
import com.yunda.zb.trainhandover.entity.ZbglHoCaseItem;
import com.yunda.zb.trainhandover.entity.ZbglHoModelItem;
import com.yunda.zb.trainhandover.manager.ZbglHoCaseManager;
import com.yunda.zb.trainhandover.manager.ZbglHoModelItemManager;
import com.yunda.zb.trainonsand.entity.ZbglSandingBean;
import com.yunda.zb.trainonsand.manager.ZbglSandingManager;
import com.yunda.zb.zbfw.entity.ZbfwTrainCenter;
import com.yunda.zb.zbfw.manager.ZbFwManager;
import com.yunda.zb.zbfw.manager.ZbfwTrainCenterManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 整备移动终端请求接口
 * <li>创建人：刘晓斌
 * <li>创建日期：2015-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class MobileAction extends JXBaseAction<Object,Object,MobileManager> {
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/** 上传文件对象 */
	private File file;
	
    /** 车型业务类 */
    @Resource
    private TrainTypeManager trainTypeManager;
    /** 机车出入段台账业务类 */
    @Resource
    private TrainAccessAccountManager trainAccessAccountManager;
    /** 业务字典项组件实例对象 */
    @Resource
    private IEosDictEntryManager eosDictEntryManager;
    /**
     * 组成查询业务对象
     */
    @Resource
    private BuildUpTypeQueryManager buildUpTypeQueryManager;
    /**
     * 故障现象业务对象
     */
    @Resource
    private PlaceFaultManager placeFaultManager;  
    /**
     * 故障现象编码业务对象
     */
    @Resource
    private EquipFaultManager equipFaultManager;
    /**
     * 临碎修提票业务对象
     */
    @Resource
    private ZbglTpManager zbglTpManager;
    /**
     * 机车整备单业务对象
     */
    @Resource
    private ZbglRdpManager zbglRdpManager;    
    /**
     * 机车交接项模板业务对象
     */
    @Resource
    private ZbglHoModelItemManager zbglHoModelItemManager;  
    /**
     * 机车交接记录业务类
     */
    @Resource
    private ZbglHoCaseManager zbglHoCaseManager;
    /**
     * 人员信息业务类
     */
    @Resource
    private EmployeeManager employeeManager;
    /**
     * 数据字典接口查询功能
     */
    @Resource
    private IEosDictEntryManager iEosDictEntryManager;   
    /**
     * 机车保洁记录业务类
     */
    @Resource
    private ZbglCleaningManager zbglCleaningManager;     
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
     * 机车整备 机车上砂管理
     */
    @Autowired
    private ZbglSandingManager zbglSandingManager;
    
    /**
     * ZbglTecOrder业务类,机车技术指令措施
     */
    @Autowired
    private ZbglTecOrderManager zbglTecOrderManager;
    
    /**
     * ZbglRdpWi业务类,机车整备任务单
     */
    @Autowired
    private ZbglRdpWiManager zbglRdpWiManager;
    
    /** 整备范围业务类 */
    @Autowired
    private ZbFwManager zbFwManager;

    /** 整备节点业务类 */
    @Autowired
    private ZbglRdpNodeManager zbglRdpNodeManager;
    
    /** ZbfwTrianCenterManager业务类,范围活和车型车号绑定的中间表  */
    @Resource    
    private ZbfwTrainCenterManager zbfwTrainCenterManager;
    
    /** TrainAccessAccount业务类,机车出入段业务类  */
    @Resource    
    private TrainAccessInAndOutManager trainAccessInAndOutManager;
    
    
	/**
	 * <li>说明：手工入段机车
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2015-4-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void intoWareHouseForPDA() throws JsonMappingException, IOException{
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);
		try {
			HttpServletRequest req = getRequest();
			String trainTypeIdx = req.getParameter("trainTypeIdx");
			String trainno = req.getParameter("trainno");
			String toGo = req.getParameter("toGo");
	        TrainType trainType = trainTypeManager.getModelById(trainTypeIdx);
	        
	        if (trainType == null) {
	        	map.put(Constants.ERRMSG, "车型为空");
	            return;
	        }
            
	        TrainAccessAccount trainAccessAccount = new TrainAccessAccount();
            trainAccessAccount.setTrainTypeShortName(trainType.getShortName());
	        trainAccessAccountManager.setInEntityProperty(trainno, trainType, toGo, trainAccessAccount);
	        //由于业务逻辑修改，入段调用新接口 by 林欢
            trainAccessInAndOutManager.saveOrUpdateTrainAccessIn(trainAccessAccount);
//            trainAccessAccountManager.saveOrUpdateInPDA(trainno, trainType, toGo);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {				
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	/**
	 * <li>说明：获取承修车型列表
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2015-4-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	@SuppressWarnings("unchecked")
    public void getUndertakeTrainType() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);		
        try {
            OmOrganization org = OmOrganizationSelectManager.getOrgByOrgcode(JXSystemProperties.OVERSEA_ORGCODE);
            Map datas = trainTypeManager.page("", JSONUtil.read("{}", Map.class), 0, 100, "", "yes", org.getOrgseq());
            List<TrainType> list = (List<TrainType>) datas.get("root");
            map = Page.extjsStore("typeID", list);
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger);
        	map.put(Constants.ERRMSG, "读取车型数据出错，原因：" + e.getMessage());
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }
	
   /**
    * <li>说明：获取机车入段去向数据字典列表
    * <li>创建人：刘晓斌
    * <li>创建日期：2015-4-17
    * <li>修改人： 
    * <li>修改日期：
    * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
    */
   public void getTrainToGo() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);		
        try {
			String hql = "from EosDictEntry where status = '1' and id.dicttypeid='TWT_TRAIN_ACCESS_ACCOUNT_TOGO' and parentid is not null order by id.dictid, sortno";
			List<EosDictEntry> eosdyList = eosDictEntryManager.findToList(hql);
			List<EosDictEntryBean> beanlist = new ArrayList<EosDictEntryBean>();
			for (EosDictEntry eos : eosdyList) {
				EosDictEntryBean bean = new EosDictEntryBean();
				bean.setDictid(eos.getId().getDictid());
				bean.setDictname(eos.getDictname());
				beanlist.add(bean);
			}
            map = Page.extjsStore("dictid", beanlist);
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger);
        	map.put(Constants.ERRMSG, "读取入段去向出错，原因：" + e.getMessage());
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	   
	}
   /**
    * <li>说明：根据车型车号获取组成树根节点列表
    * <li>创建人：刘晓斌
    * <li>创建日期：2015-4-20
    * <li>修改人： 
    * <li>修改日期：
    * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
    */
   @SuppressWarnings("unchecked")
   public void getBuildUpType() throws JsonMappingException, IOException {
	   Map<String,Object> map = new HashMap<String,Object>();
	   map.put(Constants.SUCCESS, false);
	   String trainTypeIdx = getRequest().getParameter("trainTypeIdx");
	   String trainNo = getRequest().getParameter("trainNo");
		try {
			List<HashMap> list = buildUpTypeQueryManager.getRootBuildUp(trainTypeIdx, trainNo);
			if (list != null && list.size() > 0) {
				map = list.get(0);
			} else {
				map.put(Constants.ERRMSG, "当前机车无组成");
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
   	/**
   	 * <li>说明：获取下级组成树节点列表
   	 * <li>创建人：刘晓斌
   	 * <li>创建日期：2015-4-20
   	 * <li>修改人： 
   	 * <li>修改日期：
   	 * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
   	 */
	public void getBuildUpTypeTree() throws JsonMappingException, IOException {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		HttpServletRequest req = getRequest();
		String parentIDX = req.getParameter("parentIDX");
		String partsBuildUpTypeIdx = req.getParameter("partsBuildUpTypeIdx");
		String isVirtual = req.getParameter("isVirtual");
		try {
			list = buildUpTypeQueryManager.allTree(parentIDX, partsBuildUpTypeIdx, "", isVirtual);
			Collections.reverse(list);
			map.put("root", list);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
   	/**
   	 * <li>说明：通过组成位置主键获取故障现象数据
   	 * <li>创建人：刘晓斌
   	 * <li>创建日期：2015-4-20
   	 * <li>修改人： 
   	 * <li>修改日期：
   	 * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
   	 */
    public void getPlaceFault() throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put(Constants.SUCCESS, false);
		try {
			String buildUpPlaceIdx = getRequest().getParameter("buildUpPlaceIdx");
			List<PlaceFault> list = placeFaultManager.findFaultList(buildUpPlaceIdx);
			PlaceFault other = new PlaceFault();
			other.setFaultId(PlaceFault.OTHERID);
			other.setFaultName("其它");
			list.add(list.size(), other);
			Page<PlaceFault> page = new Page<PlaceFault>();
			page.setList(list);
			map = page.extjsResult();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
    /**
     * <li>说明：上传故障提票照片
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 
     */
    public void uploadFaultImg()throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);
//        以Base64编码的图片字符串
		String photoBase64Str = getRequest().getParameter("photoBase64Str");
		String extname = getRequest().getParameter("extname");
        if (StringUtil.isNullOrBlank(photoBase64Str)){
        	map.put(Constants.ERRMSG, "上传故障提票照片失败，参数imgStr为空");
        	return;
        }
        OutputStream out = null;
        try {
        	sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            byte[] bytes = decoder.decodeBuffer(photoBase64Str);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            String saveDir = AttachmentManager.uploadDir(ZbConstants.UPLOADPATH_TP_IMG) + "tmp";
            FileUtil.createDir(saveDir);
//            保存在服务器上的临时文件全路径
            String filePath = saveDir + File.separator + DateUtil.yyyyMMddHHmmssSSS.format(new Date()) + extname;
            out = new FileOutputStream(filePath);
            out.write(bytes);
            out.flush();
            map.put(Constants.SUCCESS, true);
            map.put("filePath", filePath);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } finally {
    		JSONUtil.write(this.getResponse(), map);
			if(out != null)	out.close();
        }
    }   
    /**
     * <li>说明：上传故障提票语音
     * <li>创建人：刘晓斌
     * <li>创建日期：2016-3-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 
	 * @throws IOException 
	 * @throws JsonMappingException
     */
	public void uploadFaultAudio() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);
        try {
            String saveDir = AttachmentManager.uploadDir(ZbConstants.UPLOADPATH_TP_IMG) + "tmp";
            FileUtil.createDir(saveDir);
//            保存在服务器上的临时文件全路径
            String filePath = saveDir + File.separator + DateUtil.yyyyMMddHHmmssSSS.format(new Date()) + ".mp3";
			File des = new File(filePath);
			MediaUtil.toMp3(file, des);
			map.put("filePath", filePath);
			map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
		} finally {
			boolean success = (Boolean)map.get(Constants.SUCCESS);
			if(!success)	map.put(Constants.ERRMSG, "上传语音文件失败，请尝试重新操作或联系管理员。");
			JSONUtil.write(this.getResponse(), map);
		}
	}
   	/**
   	 * <li>说明：获取故障字典码表数据列表
   	 * <li>创建人：刘晓斌
   	 * <li>创建日期：2015-4-20
   	 * <li>修改人： 
   	 * <li>修改日期：
   	 * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
   	 */	
	public void findfaultList() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);	
//		String fixPlaceIdx = getRequest().getParameter("fixPlaceIdx");
		String faultName = getRequest().getParameter("faultName");
        try {
            EquipFault entity = new EquipFault();
            if (!StringUtil.isNullOrBlank(faultName)) {
                entity.setFaultName(faultName);
            }
            SearchEntity<EquipFault> searchEntity = new SearchEntity<EquipFault>(entity, 0, 100, null);
            Page<EquipFault> page = equipFaultManager.faultList(searchEntity, null);
            page.setId("FaultID");
            map = page.extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}	
	
   	/**
   	 * <li>说明：提票录入处理
   	 * <li>创建人：刘晓斌
   	 * <li>创建日期：2015-4-20
   	 * <li>修改人： 
   	 * <li>修改日期：
   	 * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
   	 */	
    public void saveFaultNoticeFromGrid() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);
        try {
        	HttpServletRequest req = getRequest();
        	String jsonData = req.getParameter("jsonData");
            
        	//获得提票业务节点
        	String node = ZbConstants.UPLOADPATH_NODE_TP;
        	
            ZbglTp[] entities = JSONUtil.read(jsonData, ZbglTp[].class);
            ZbglTp entity = entities.length > 0 ? entities[0] : null;
            if (entity != null) {
                zbglTpManager.saveTpAndInst(entities, entity);
                zbglTpManager.getDaoUtils().flush();
                String jsonPhotos = req.getParameter("faultPhotos");
               	if(jsonPhotos != null && !"".equals(jsonPhotos)){
               		FaultPhoto[] faultPhotos = JSONUtil.read(jsonPhotos, FaultPhoto[].class);
               		for (FaultPhoto photo : faultPhotos) {
               			copyFaultFile(entity.getIdx(), photo.getFilePath(), photo.getFilename(), node);
					}
            	}
				map.put(Constants.SUCCESS, true);
            } else {
            	map.put(Constants.ERRMSG, "解析参数值jsonData生成的ZbglTp为空");
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
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
   	/**
   	 * <li>说明：取得机车交接任务列表
   	 * <li>创建人：刘晓斌
   	 * <li>创建日期：2015-4-20
   	 * <li>修改人： 林欢
   	 * <li>修改日期：2015-8-15
   	 * <li>修改内容：由于需求变动，产品化不使用该限制，只有郑州方面在使用,同步涉及修改，移除产品化isStartUsingNum，checkZbglRdpNum，isStartUsingJt6 3个配置项的配置
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
   	 */	    
    public void getTrainHandOverTasks() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);    
        try {
        	String searchJson = getRequest().getParameter("searchJson");
        	String sort = getRequest().getParameter("sort");
        	String dir = getRequest().getParameter("dir");
            Page<ZbglRdpDTO> page = zbglRdpManager.queryRdpListForHO(searchJson, getStart(), getLimit() ,sort,dir);
            
            map = page.extjsResult();
//            //判断字段赋值（1.是否该车型车号下有处理中的jt6提票。2.在前N次任务单中，是否做过范围活）
//            map = zbglRdpManager.zbglRdpFlagSet(page).extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }    
   	/**
   	 * <li>说明：取得交接项
   	 * <li>创建人：刘晓斌
   	 * <li>创建日期：2015-4-20
   	 * <li>修改人： 
   	 * <li>修改日期：
   	 * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
   	 */    
    public void getHoModelItem() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);      
        try {
            Page<ZbglHoModelItem> page = zbglHoModelItemManager.getHoModelItemList();
            map = page.extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }
    
   	/**
   	 * <li>说明：完成机车交接工作
   	 * <li>创建人：刘晓斌
   	 * <li>创建日期：2015-4-20
   	 * <li>修改人： 
   	 * <li>修改日期：
   	 * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
   	 */    
	public void finishHoTask() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);  	
		try {
			
//	没有业务节点
        	String node = null;
			
			HttpServletRequest req = getRequest();
			String rdpJson = req.getParameter("rdpJson");
			String itemJson = req.getParameter("itemJson");
            //获取是否做范围活 0不做，1做
            String isDoZbglRdpWi = req.getParameter("isDoZbglRdpWi");
            
			ZbglHoCase hoCase = (ZbglHoCase) JSONUtil.read(rdpJson,	ZbglHoCase.class);
			ZbglHoCaseItem[] items = (ZbglHoCaseItem[]) JSONUtil.read(itemJson, ZbglHoCaseItem[].class);
            
            //获取整备单对象
            ZbglRdp rdp = zbglRdpManager.getModelById(hoCase.getRdpIdx());
            //整备单idx
			String[] param = { rdp.getIdx() };
            
            if (StringUtils.isNotBlank(isDoZbglRdpWi) && Integer.valueOf(isDoZbglRdpWi) == 1) {
                
                //如果要做范围活，新增范围，绑定到对应的整备单上
//              通过车型车号查新范围流程主键
                ZbfwTrainCenter zbfwTrainCenter = zbfwTrainCenterManager.findZbfwIDXByTrainNoAndTrainTypeIDX(rdp.getTrainNo(),rdp.getTrainTypeIDX());
                if (zbfwTrainCenter != null) {
                    rdp.setZbfwIDX(zbfwTrainCenter.getZbfwIDX());
//                  反写是否做范围活到整备单字段中
                    rdp.setIsDoZbglRdpWi(Integer.valueOf(isDoZbglRdpWi));
                    zbglRdpManager.saveOrUpdate(rdp);
                    zbglRdpNodeManager.saveNodeAndSeq(rdp);
                }
                
                //林欢  获取范围活定义idx方式改变
//                ZbFw zbfw = zbFwManager.getZbFwByTrain(rdp.getTrainTypeIDX());
//                if (zbfw != null) {
//                    rdp.setZbfwIDX(zbfw.getIdx());
////                  反写是否做范围活到整备单字段中
//                    rdp.setIsDoZbglRdpWi(Integer.valueOf(isDoZbglRdpWi));
//                    zbglRdpManager.saveOrUpdate(rdp);
//                    zbglRdpNodeManager.saveNodeAndSeq(rdp);
//                } 
                
                
                // 调用存储过程，生产整备范围
                zbglRdpWiManager.saveZbglRdpWiByProc(param);
                // 要做范围活，生成技术指令的范围活
                zbglTecOrderManager.saveZbglTecOrderByProc(param);
                // 对范围活进行排序
                zbglRdpWiManager.sortZbglRdpWi(rdp.getIdx());
            }
            zbglHoCaseManager.saveHoCaseAndItems(hoCase, items);
            
            // 附件
            String jsonPhotos = req.getParameter("faultPhotos");
            if(jsonPhotos != null && !"".equals(jsonPhotos)){
                FaultPhoto[] faultPhotos = JSONUtil.read(jsonPhotos, FaultPhoto[].class);
                for (FaultPhoto photo : faultPhotos) {
                    copyFaultFile(hoCase.getIdx(), photo.getFilePath(), photo.getFilename(), node);
                }
            }
            
//			if (null != hoCase.getFromPersonName()) {
//				if (!zbglHoCaseManager.isNumeric(hoCase.getFromPersonName())) {
//					zbglHoCaseManager.saveHoCaseAndItems(hoCase, items);
//					map.put(Constants.SUCCESS, true);
//					return;
//				} else {
//					String hql = "from OmEmployee as o where o.userid = '"
////							+ hoCase.getFromPersonId() + "'";
//						+ hoCase.getFromPersonName() + "'";
//					OmEmployee ome = employeeManager.findSingle(hql);
//					if (ome != null) {
//						hoCase.setFromPersonName(ome.getEmpname());
//						zbglHoCaseManager.saveHoCaseAndItems(hoCase, items);
//						map.put(Constants.SUCCESS, true);
//						return;
//					} else {
//						map.put(Constants.ERRMSG, "该工号不存在对应工作人员！");
//						return;					
//					}
//				}
//			} else {
//				zbglHoCaseManager.saveHoCaseAndItems(hoCase, items);
//			}
			map.put(Constants.SUCCESS, true);  
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}    
   	/**
   	 * <li>说明：获取保洁任务列表
   	 * <li>创建人：刘晓斌
   	 * <li>创建日期：2015-4-20
   	 * <li>修改人： 
   	 * <li>修改日期：
   	 * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
   	 */	
    public void getTrainCleaningTasks() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);     
        try {
        	String searchJson = getRequest().getParameter("searchJson");
        	String sort = getRequest().getParameter("sort");
        	String dir = getRequest().getParameter("dir");
            Page<?> page = zbglRdpManager.queryRdpListForClean(searchJson, getStart(), getLimit(),sort,dir);
            map = page.extjsResult();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }	
   	/**
   	 * <li>说明：获取机车等级
   	 * <li>创建人：刘晓斌
   	 * <li>创建日期：2015-4-20
   	 * <li>修改人： 
   	 * <li>修改日期：
   	 * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
   	 */    
    public void getTrainLevel() throws JsonMappingException, IOException{
    	findDictList("JCZB_JC_LEVEL");
    }    
   	/**
   	 * <li>说明：获取保洁等级
   	 * <li>创建人：刘晓斌
   	 * <li>创建日期：2015-4-20
   	 * <li>修改人： 
   	 * <li>修改日期：
   	 * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
   	 */        
    public void getCleaningLevel() throws JsonMappingException, IOException {
    	findDictList("JCZB_CLEAN_LEVEL");
    }
   	/**
   	 * <li>说明：完成保洁任务
   	 * <li>创建人：刘晓斌
   	 * <li>创建日期：2015-4-20
   	 * <li>修改人： 
   	 * <li>修改日期：
   	 * <li>修改内容：
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
   	 */     
    public void finishTrainCleaningTask() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);     
        try {
        	String json = getRequest().getParameter("json");
            ZbglCleaning clean = (ZbglCleaning) JSONUtil.read(json, ZbglCleaning.class);
            zbglCleaningManager.savaTrainCleaning(clean);
            map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }    
   	/**
   	 * <li>说明：根据id获取数据字典项
   	 * <li>创建人：刘晓斌
   	 * <li>创建日期：2015-4-20
   	 * <li>修改人： 
   	 * <li>修改日期：
   	 * <li>修改内容：
     * @param dicttypeid 数据字典项id
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
   	 */    
    private void findDictList(String dicttypeid) throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, false);      
        try {
            List<EosDictEntry> eosdyList = iEosDictEntryManager.findCacheEntry(dicttypeid);
			List<EosDictEntryBean> beanlist = new ArrayList<EosDictEntryBean>();
			for (EosDictEntry eos : eosdyList) {
				EosDictEntryBean bean = new EosDictEntryBean();
				bean.setDictid(eos.getId().getDictid());
				bean.setDictname(eos.getDictname());
				beanlist.add(bean);
			}
            map = Page.extjsStore("dictid", beanlist);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
     * <li>说明：查询整备上砂分页列表
     * <li>创建人：何东
     * <li>创建日期：2016-5-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 整备上砂分页列表JSON字符串
     */
	@SuppressWarnings("unchecked")
    public void querySandingList() throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, true);
        try {
        	String searchJson = StringUtil.nvlTrim(getRequest().getParameter("searchJson"), "{}");
            ZbglSandingBean sandingBean = JSONUtil.read(searchJson, ZbglSandingBean.class);
            Page page = zbglSandingManager.querySandingList(sandingBean, getStart(), getLimit());
            map = page.extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            map.put(Constants.SUCCESS, false);
            map.put("errMsg", "操作失败！");
        } finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }
    
    /**
     * <li>说明：开始整备上砂任务
     * <li>创建人：何东
     * <li>创建日期：2016-5-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 操作成功与否
     */
    public void startSanding() throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, true);
    	try {
        	HttpServletRequest req = getRequest();
        	// 机车出入库台账主键
        	String trainAccessIdx = req.getParameter("trainAccessIdx");
        	// 操作者ID
        	String operatorid = req.getParameter("operatorid");
        	
            String sandingIdx = zbglSandingManager.startSanding(trainAccessIdx, StringUtils.isNotBlank(operatorid) ? Long.valueOf(operatorid) : null);
            if (StringUtils.isNotBlank(sandingIdx)) {
            	map.put("id", sandingIdx);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            map.put(Constants.SUCCESS, false);
            map.put("errMsg", "操作失败！");
        } finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }

    /**
     * <li>说明：结束整备上砂任务
     * <li>创建人：何东
     * <li>创建日期：2016-5-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 操作成功与否
     */
    public void endSanding() throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String,Object>();
		map.put(Constants.SUCCESS, true);
    	try {
        	HttpServletRequest req = getRequest();
        	// 上砂记录主键
        	String sandingIdx = req.getParameter("sandingIdx");
        	// 上砂量
        	String sandNum = req.getParameter("sandNum");
        	
            zbglSandingManager.endSanding(sandingIdx, sandNum);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            map.put(Constants.SUCCESS, false);
            map.put("errMsg", "操作失败！");
        } finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }
}
