package com.yunda.zb.mobile.manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Encoder;

import com.yunda.frame.baseapp.upload.entity.Attachment;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.component.manager.BaseComboManager;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.tpmanage.entity.RepairEmpBean;
import com.yunda.jx.pjwz.partsBase.professionaltype.entity.ProfessionalType;
import com.yunda.jx.pjwz.partsBase.professionaltype.manager.ProfessionalTypeManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.EosDictEntryBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.FaultMethodBean;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.util.BeanUtils;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.common.webservice.ITerminalCommonService;
import com.yunda.zb.mobile.entity.ZbglTpTrainNoAndTrainTypeIDXBean;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.manager.ZbglTpManager;
import com.yunda.zb.tp.webservice.ZbglTpBean;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 整备移动终端提票请求业务类
 * <li>创建人：林欢
 * <li>创建日期：2016-8-3
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */

@Service(value = "zbglTpMobileManager")
public class ZbglTpMobileManager extends JXBaseManager<ZbglTp, ZbglTp>{
    
    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());

    /** ZbglTp业务类,JT6提票 */
	@Resource 
	private ZbglTpManager zbglTpManager;
    
	/** TrainAccessAccount业务类,机车出入段台账 */
	@Resource 
	private TrainAccessAccountManager trainAccessAccountManager;
    
    /**
     * 通用下拉列表业务对象
     */
    @Resource
    private BaseComboManager baseComboManager;
    
    /**
     * 工位终端共有接口
     */
    @Resource
    private ITerminalCommonService terminalCommonService;
    
    /**
     * 附件管理业务对象
     */
    @Resource
    private AttachmentManager attachmentManager;
    
    /**
     * 专业类型业务对象
     */
    @Resource
    private ProfessionalTypeManager professionalTypeManager;
	
    /**
     * 人员选择业务对象
     */
    @Resource
    private OmEmployeeSelectManager omEmployeeSelectManager ;
    
	public List<ZbglTp> giveToManager(Map<String, Object> mapParam){
		return zbglTpManager.findZbglTpByParam(mapParam);
		
	}
    
    /**
     * <li>说明：分页查询提票页面
     * <li>创建人：林欢
     * <li>创建日期：2016-8-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始页码
     * @param limit 每页条数
     * @param params 查询参数
     * params.put("searchJson", searchJson);//查询条件JSON字符串
        params.put("repairClass", repairClass);// 票活类型（值为2.1.1提票常量中的碎修/临修）
        params.put("faultNoticeStatus", faultNoticeStatus);// 待接活/待销活（值为2.1.1提票常量中的待接活/待销活）
        params.put("operatorid", operatorid);// 操作者ID
     * @throws Exception 
     * @throws NumberFormatException 
     */
    public Page<ZbglTp> queryZbglTpList(Integer start, Integer limit, Map<String, String> params) throws NumberFormatException, Exception {
        
        String searchJson = params.get("searchJson");// 查询条件JSON字符串
        String repairClass = params.get("repairClass");// 票活类型（值为2.1.1提票常量中的碎修/临修）
        String faultNoticeStatus = params.get("faultNoticeStatus");// 待接活/待销活（值为2.1.1提票常量中的待接活/待销活）
        String operatorid = params.get("operatorid") == null? "0" : params.get("operatorid");// 操作者ID
        
        Page<ZbglTp> page = zbglTpManager.queryTpList(searchJson, repairClass, faultNoticeStatus, Long.valueOf(operatorid), start, limit);
        List<ZbglTpBean> list = new ArrayList<ZbglTpBean>();
        for (ZbglTp tp : page.getList()) {
            ZbglTpBean bean = new ZbglTpBean();
            BeanUtils.copyProperties(bean, tp);
            bean.setNoticeTime(tp.getNoticeTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(tp.getNoticeTime()) : "");
            list.add(bean);
        }
        return page;
    }

    /**
     * <li>说明：分页分组查询车型车号信息
     * <li>创建人：林欢
     * <li>创建日期：2016-8-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始页码
     * @param limit 每页显示条数
     * @param searchJson 查询条件
     * @return Page<ZbglTpTrainNoAndTrainTypeIDXBean> 返回查询结果
     * @throws ParseException 
     */
    public Page<ZbglTpTrainNoAndTrainTypeIDXBean> querySameTrainNoAndTrainTypeIDXInfoList(Integer start, Integer limit, String searchJson,String operatorid) throws ParseException {
    	
    	StringBuilder sb = new StringBuilder();
        
        sb.append(" select * from (select sys_guid() as idx,t.train_no, ");
        sb.append(" t.train_type_shortname, ");
        sb.append(" (select count(1) ");
        sb.append(" from zb_Zbgl_jt6 x ");
        sb.append(" where x.record_status = 0 ");
        sb.append(" and x.repair_class = '10' ");
        sb.append(" and x.rev_person_id is null ");
        sb.append(" and (x.rdp_idx in ");
        sb.append(" (select k.idx ");
        sb.append(" from zb_zbgl_rdp k ");
        sb.append(" where k.Rdp_Status = 'ONGOING' ");
        sb.append(" and k.record_status = 0 ");
        sb.append(" and k.siteid = '").append(JXConfig.getInstance().getSynSiteID());
        sb.append("')) ");
        sb.append(" and x.train_no = t.train_no ");
        sb.append(" and x.train_type_shortname = t.train_type_shortname ");
        sb.append(" and x.fault_notice_status = 'TODO') as toDoCount, ");
        sb.append(" (select count(1) ");
        sb.append(" from zb_Zbgl_jt6 y ");
        sb.append(" where y.record_status = 0 ");
        sb.append(" and y.Repair_Class = '10' ");
        sb.append(" and (y.RDP_IDX in ");
        sb.append(" (select m.idx ");
        sb.append(" from ZB_ZBGL_RDP m ");
        sb.append(" where m.Rdp_Status = 'ONGOING' ");
        sb.append(" and m.Record_Status = 0 ");
        sb.append(" and m.siteid = '").append(JXConfig.getInstance().getSynSiteID());
        sb.append("')) ");
        sb.append(" and y.Fault_Notice_Status = 'ONGOING' ");
        
        //通过操作人员查询员工信息表
        OmEmployee omEmployee = omEmployeeSelectManager.findEmpByOperator(Long.valueOf(operatorid));
        
        sb.append(" and y.Rev_Person_Id = '").append(omEmployee.getEmpid()).append("'");//TODO
        sb.append(" and (y.Handle_Person_Id is null) ");
        sb.append(" and y.train_no = t.train_no ");
        sb.append(" and y.train_type_shortname = t.train_type_shortname ");
        sb.append(" and y.fault_notice_status = 'ONGOING') as onGoingCount ");
        sb.append(" from (select a.train_no, a.train_type_shortname ");
        sb.append(" from zb_Zbgl_jt6 a ");
        sb.append(" where a.record_status = 0 ");
        sb.append(" and a.fault_notice_status in ('TODO', 'ONGOING') ");
        sb.append(" group by a.train_no, a.train_type_shortname) t where 1=1 ");
        //添加查询条件
        if (StringUtils.isNotBlank(searchJson)) {
        	sb.append(" and (t.train_no like '%").append(searchJson).append("%'");
        	sb.append(" or t.train_type_shortname like '%").append(searchJson).append("%'");
        	sb.append(" ) ");
		}
        sb.append(" ) t1 where (t1.toDoCount != 0 ");
        sb.append(" or t1.onGoingCount != 0) ");
        
        String totalSql = "select count(1) as rowcount from (" +sb.toString() + ")";

        Page<ZbglTpTrainNoAndTrainTypeIDXBean> page = this.queryPageList(totalSql, sb.toString(), start, limit, false, ZbglTpTrainNoAndTrainTypeIDXBean.class);
        List<ZbglTpTrainNoAndTrainTypeIDXBean> zbglTpTrainNoAndTrainTypeIDXBeanList = page.getList();
        //判断list是否为null,如果不为null通过车型车号，查询出入段台账最新的入段时间
        if (zbglTpTrainNoAndTrainTypeIDXBeanList != null && zbglTpTrainNoAndTrainTypeIDXBeanList.size() > 0) {
            for (ZbglTpTrainNoAndTrainTypeIDXBean bean : zbglTpTrainNoAndTrainTypeIDXBeanList) {
//					封装查询条件
                TrainAccessAccount entity = new TrainAccessAccount();
                entity.setTrainNo(bean.getTrainNo());
                entity.setTrainTypeShortName(bean.getTrainTypeShortName());
                //通过车型车号查询List，根据更新时间排序
                List<TrainAccessAccount> trainAccessAccountList = trainAccessAccountManager.getTrainAccessAccountListByEntity(entity);
                if (trainAccessAccountList != null && trainAccessAccountList.size() > 0) {
                	TrainAccessAccount trainAccessAccount = trainAccessAccountList.get(0);
                	bean.setInTime(DateUtil.yyyy_MM_dd_HH_mm_ss.format(trainAccessAccount.getInTime()));
				}
            }
            
            //根据出入段时间排序
//            冒泡
            ZbglTpTrainNoAndTrainTypeIDXBean tb = null;
            for (int i = 0; i < zbglTpTrainNoAndTrainTypeIDXBeanList.size(); i++) {
                for (int j = i + 1; j < zbglTpTrainNoAndTrainTypeIDXBeanList.size(); j++) {
                    ZbglTpTrainNoAndTrainTypeIDXBean z1 = zbglTpTrainNoAndTrainTypeIDXBeanList.get(i);
                    ZbglTpTrainNoAndTrainTypeIDXBean z2 = zbglTpTrainNoAndTrainTypeIDXBeanList.get(j);
                    if (DateUtil.yyyy_MM_dd_HH_mm_ss.parse(z1.getInTime()).getTime() < DateUtil.yyyy_MM_dd_HH_mm_ss.parse(z2.getInTime()).getTime()) {
                        tb = z1;
                        zbglTpTrainNoAndTrainTypeIDXBeanList.set(i, z2);
                        zbglTpTrainNoAndTrainTypeIDXBeanList.set(j, tb);
                    }
                }
            }
            
        }
        
        return page;
    }

    /**
     * <li>说明：领取提票活
     * <li>创建人：林欢
     * <li>创建日期：2016-08-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人idx
     * @param idxs 提票活id
     * @param repairClass 票活类型（值为2.1.1提票常量中的碎修/临修）
     * @throws Exception 
     * @throws NumberFormatException 
     * @throws JsonMappingException
     * @throws IOException
     */
    public void receiveTp(String operatorid, String idxs, String repairClass) throws NumberFormatException, Exception {
        zbglTpManager.receiveTp(Long.valueOf(operatorid), idxs, repairClass);
    }

    /**
     * <li>说明：获取故障施修方法分页列表
     * <li>创建人：林欢
     * <li>创建日期：2016-8-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> findFaultMethod(String entity, Object object, Object object2, Map queryParamsMap, int start, Integer limit, Object object3) throws Exception {
        //准备一个返回map
        Map<String, Object> map = new HashMap<String, Object>();
        //调用方法查询独享
        map = baseComboManager.page(entity, null, null, queryParamsMap, start, limit, null);
        //将查询到的list对象通过拷贝放入新的list中
        List<FaultMethodBean> beanList = new ArrayList<FaultMethodBean>();
        beanList = BeanUtils.copyListToList(FaultMethodBean.class, (List) map.get("root"));
        Page pageList = new Page((Integer) (map.get("totalProperty")), beanList);
        return pageList.extjsStore();
    }

    /**
     * <li>说明：查询碎修提票处理结果列表
     * <li>创建人：林欢
     * <li>创建日期：2016-8-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public Page<EosDictEntryBean> queryRepairResultList() {
        return new Page<EosDictEntryBean>(terminalCommonService.findEosDictEntryBeanByCopy("JCZB_TP_REPAIRRESULT"));
    }

    /**
     * <li>说明：销活
     * <li>创建人：林欢
     * <li>创建日期：2016-08-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人idx
     * @param idxs 提票活idx
     * @param tp 处理提票信息JSON字符串
     * @throws Exception 
     * @throws NumberFormatException 
     * @throws JsonMappingException
     * @throws IOException
     */
    public void handleTp(String operatorid, String idxs, ZbglTp tp) throws NumberFormatException, Exception {
        zbglTpManager.handleTp(Long.valueOf(operatorid), idxs, tp);
    }

    /**
     * <li>说明：读取故障提票上传的照片
     * <li>创建人：林欢
     * <li>创建日期：2016-08-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param attachmentKeyIDX 提票单IDX
     * @param node 作业节点标识
     * @return 以Base64编码的图片字符串
     */
    public List<String> downloadFaultImg(String attachmentKeyIDX, String node) {
    	
    	List<String> returnList = new ArrayList<String>();
    	
        List<Attachment> list = this.findListByKeyAndNode(attachmentKeyIDX, ZbConstants.UPLOADPATH_TP_IMG, node);
        if (list != null && list.size() > 0) {
        	
        	for (Attachment att : list) {
                String imgPath = AttachmentManager.uploadFilepath(att);
                
                byte[] data = null;
                try {
                    InputStream in = new FileInputStream(imgPath);
                    data = new byte[in.available()];
                    in.read(data);
                    in.close();
                } catch (IOException e) {
                    ExceptionUtil.process(e, logger);
                }
                sun.misc.BASE64Encoder encoder = new BASE64Encoder();
                returnList.add(encoder.encode(data));
			}
		}
       
        return returnList;
    }

    /**
     * <li>说明：获取专业类型
     * <li>创建人：林欢
     * <li>创建日期：2016-8-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询参数
     * @return Page 返回专业类型对象
     */
	public Page getProfessionalType(SearchEntity<ProfessionalType> searchEntity) {
		return professionalTypeManager.findPageList(searchEntity);
	}

	/**
     * <li>说明：获取提票处理其他作业人员列表
     * <li>创建人：林欢
     * <li>创建日期：2016-8-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return List<RepairEmpBean> 提票处理其他作业人员列表
     */
	public List<RepairEmpBean> getOtherWorkerByTP(String operatorid) {
		OmEmployee emp = omEmployeeSelectManager.getByOperatorid(Long.valueOf(operatorid));
		return zbglTpManager.getOtherWorkerByTP(emp.getEmpid());
	}
	
	
	  /**
     * <li>说明：根据附件所属的业务键值和业务节点获取附件列表
     * <li>创建人：刘国栋
     * <li>创建日期：2016-9-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param attachmentKeyIDX 附件所属的关键值
     * @param attachmentKeyName 附件所属的业务表单，记录表名
     * @param node 业务节点 关系表名
     * @return 附件列表
     */
    @SuppressWarnings("unchecked")
    public List<Attachment> findListByKeyAndNode(String attachmentKeyIDX, String attachmentKeyName, String node) {

    	StringBuffer sb = new StringBuffer();
    	sb.append(" select a.* " );
    	sb.append(" from JXPZ_Attachment_Manage a , jxpz_attachment_relation b ");
    	sb.append(" where a.ATTACHMENT_KEY_IDX = '").append(attachmentKeyIDX).append("'");
    	sb.append("  and a.ATTACHMENT_KEY_NAME = '") .append(attachmentKeyName).append("'");
    	sb.append("  and b.ATTACHMENT_IDX = a.idx" );
    	sb.append("  and b.ATTACHMENT_NODE = '").append(node).append("'");
    	 
    	String sql = sb.toString();
    	return this.getDaoUtils().executeSqlQueryEntity(sql , Attachment.class);
    }
}
