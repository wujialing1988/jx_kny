package com.yunda.jx.pjjx.partsrdp.recordinst.manager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.base.recorddefine.entity.PartsStepResult;
import com.yunda.jx.pjjx.base.recorddefine.manager.PartsStepResultManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQCParticipantManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordDI;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordRI;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordRIAndDI;
import com.yunda.jx.pjjx.partsrdp.tecinst.manager.PartsRdpTecWSManager;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpRecordRI业务类,配件检修检测项实例
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpRecordRIManager")
public class PartsRdpRecordRIManager extends JXBaseManager<PartsRdpRecordRI, PartsRdpRecordRI>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** PartsRdpRecordDI业务类,配件检修检测数据项 */
	@Resource
	private PartsRdpRecordDIManager partsRdpRecordDIManager;
	
	/** PartsRdpQCParticipant实体类, 数据表：质量可检查人员 */
	@Resource
	private PartsRdpQCParticipantManager partsRdpQCParticipantManager;
    
    /** 配件默认检测检修结果 */
    @Resource
    private PartsStepResultManager partsStepResultManager;
	
	/**
	 * <li>说明：根据“记录卡实例主键”获取配件检修检测项实例
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 *  
	 * @param rdpRecordCardIDX 记录卡实例主键
	 * @return List<PartsRdpRecordRI> 配件检修检测项实例集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpRecordRI> getModels(String rdpRecordCardIDX) {
		String hql = "From PartsRdpRecordRI Where recordStatus = 0 And rdpRecordCardIDX = ? Order By seqNo";
		return this.daoUtils.find(hql, new Object[]{rdpRecordCardIDX});
	}
	
	/**
	 * <li>说明：批量处理
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 配件检修检测项实例主键数组
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void finishBatchRI(String[] ids) throws BusinessException, NoSuchFieldException {
		List<PartsRdpRecordRI> entityList = new ArrayList<PartsRdpRecordRI>(ids.length);
		for (String idx : ids) {
			entityList.add(this.getModelById(idx));
		}
		this.finishBatchRI(entityList);
	}
	
	/**
	 * <li>说明：批量处理
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 配件检修检测项实例集合
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void finishBatchRI(List<PartsRdpRecordRI> entityList) throws BusinessException, NoSuchFieldException {
		for (PartsRdpRecordRI entity : entityList) {
			if (entity.getStatus().equals(PartsRdpRecordRI.CONST_STR_STATUS_WCL)) {
				// 设置记录状态为“已处理”
				entity.setStatus(PartsRdpRecordRI.CONST_STR_STATUS_YCL);
			}
		}
		this.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明：功能操作前的记录状态验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 配件检修检测项实例集合
	 * @param status 记录的预期状态
	 * @return String[] 验证消息
	 */
	public String[] validateStatus(List<PartsRdpRecordRI> entityList, String status) {
		List<String> errMsgs = new ArrayList<String>(entityList.size());
		String validateMsg = null;
		for (PartsRdpRecordRI entity : entityList) {
			validateMsg = this.validateStatus(entity, status);
			if (null == validateMsg) {
				continue;
			}
			errMsgs.add(validateMsg);
		}
		if (errMsgs.size() > 0) {
			return errMsgs.toArray(new String[errMsgs.size()]);
		}
		return null;
	}

	/**
	 * <li>说明：功能操作前的记录状态验证，如果操作发生时，记录的状态不是预期的状态，则返回相应的状态的提示信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 配件检修检测项实例
	 * @param status 记录的预期状态
	 * @return String 验证消息
	 */
	public String validateStatus(PartsRdpRecordRI entity, String status) {
        // 验证记录的状态
        String errMsg = PartsRdpTecWSManager.checkEntityStatus(status, entity.getStatus());
        if (null == errMsg) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getRepairItemName()).append(Constants.BRACKET_L).append(entity.getRepairItemNo()).append(Constants.BRACKET_R);
        return sb.append(errMsg).toString();
	}
	
	/**
	 * <li>说明：功能操作前的记录状态验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 配件检修检测项实例 idx主键数组
	 * @param status 记录的预期状态
	 * @return String[] 验证消息
	 */
	public String[] validateStatus(String[] ids, String status) {
		List<String> errMsgs = new ArrayList<String>(ids.length);
		PartsRdpRecordRI entity = null;
		String validateMsg = null;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			validateMsg = this.validateStatus(entity, status);
			if (null == validateMsg) {
				continue;
			}
			errMsgs.add(validateMsg);
		}
		if (errMsgs.size() > 0) {
			return errMsgs.toArray(new String[errMsgs.size()]);
		}
		return null;
	}
	
	/**
	 * <li>说明：暂存
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param partsRdpRecordRI 配件检修检测项实例
	 * @param partsRdpRecordDIs  配件检修检测数据项对象数组
	 * @param isTemporary 是否是暂存
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveTemporary(PartsRdpRecordRI partsRdpRecordRI, PartsRdpRecordDI[] partsRdpRecordDIs, boolean isTemporary) throws BusinessException, NoSuchFieldException {
        PartsRdpRecordRI entity = this.getModelById(partsRdpRecordRI.getIdx());
        entity.setRepairResult(partsRdpRecordRI.getRepairResult());         // 检测结果
        entity.setRemarks(partsRdpRecordRI.getRemarks());                   // 备注
        entity.setHandleResult(partsRdpRecordRI.getHandleResult());                   // 备注
        // 如果不是【暂存】操作，则设置【配件检修检测项实例】的状态为“已处理”
        if (!isTemporary) {
            entity.setStatus(PartsRdpRecordRI.CONST_STR_STATUS_YCL);
        }
        // 暂存配件检修检测数据项
        PartsRdpRecordDI recordDI;
        if (partsRdpRecordDIs != null && partsRdpRecordDIs.length > 0) {
            for (PartsRdpRecordDI di : partsRdpRecordDIs) {
                recordDI = this.partsRdpRecordDIManager.getModelById(di.getIdx());

                String diDataItemResult = di.getDataItemResult();
                String recordDIDataItemResult = recordDI.getDataItemResult();
                
                //判断是否是可视化数据
                if (recordDI.getDataSource() != null && recordDI.getDataSource() == 1) {
                    //判断传递过来的数据是否和数据中的数据不一致，如果一致修改数据库中的dataSource为0
                    if (StringUtils.isNotBlank(diDataItemResult) && !diDataItemResult.equals(recordDIDataItemResult)) {
                        recordDI.setDataSource(PartsRdpRecordDI.HANDINPUT);         // 检测结果数据来源
                    }
                }
                recordDI.setDataItemResult(diDataItemResult);         // 检测结果
                this.partsRdpRecordDIManager.saveOrUpdate(recordDI);                
            }
        }
        this.saveOrUpdate(entity);
	}
	
	/**
	 * <li>说明：检验所有记录是否都已处理完成
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 配件检修检测项实例集合
	 * @param msgList 验证消息集合
	 * @throws BusinessException
	 */
	public void checkFinishStatus(List<PartsRdpRecordRI> entityList, List<String> msgList) throws BusinessException {
		for (PartsRdpRecordRI entity : entityList) {
			if (!entity.getStatus().equals(PartsRdpRecordRI.CONST_STR_STATUS_YCL)) {
				msgList.add("检修检测项：" + entity.getRepairItemNo() + "（" + entity.getRepairItemName() + "）还未处理！");
				return;
			}
		}
	}
	
	/**
	 * <li>说明：撤销检修检测项的处理历史记录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 检修检测项实例
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void giveUpJob(PartsRdpRecordRI entity) throws BusinessException, NoSuchFieldException {
		entity.setRepairResult(null);			// 清空检测结果
		entity.setRemarks(null);				// 清空备注
		entity.setStatus(PartsRdpRecordRI.CONST_STR_STATUS_WCL);				// 设置状态为未处理
		this.saveOrUpdate(entity);
		
		// 撤销检修检测项的处理历史记录
		List<PartsRdpRecordDI> diList = this.partsRdpRecordDIManager.getModelByRdpRecordRIIDX(entity.getIdx());
		if (null != diList && diList.size() > 0) {
			for (PartsRdpRecordDI di : diList) {
				this.partsRdpRecordDIManager.giveUpJob(di);
			}
		}
	}
	
	/**
	 * <li>说明：撤销检修检测项的处理历史记录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 检修检测项实例列表
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void giveUpJob(List<PartsRdpRecordRI> entityList) throws BusinessException, NoSuchFieldException {
		for (PartsRdpRecordRI entity : entityList) {
			this.giveUpJob(entity);
		}
	}

	/**
	 * <li>说明：返修
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 配件检修检测项实例 
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	@Deprecated
	public void updateToBack(PartsRdpRecordRI entity) throws BusinessException, NoSuchFieldException {
		entity.setStatus(PartsRdpRecordRI.CONST_STR_STATUS_WCL);
		this.saveOrUpdate(entity);
	}

	/**
	 * <li>说明：返修
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 配件检修检测项实例列表
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	@Deprecated
	public void updateToBack(List<PartsRdpRecordRI> entityList) throws BusinessException, NoSuchFieldException {
		for (PartsRdpRecordRI entity : entityList) {
			this.updateToBack(entity);
		}
	}

    /**
     * <li>说明：检修检测项查询，并将查询结果以HTML形式进行返回，以返回结果在页面进行显示
     * <li>创建人：何涛
     * <li>创建日期：2016-1-21
     * <li>修改人：何涛
     * <li>修改日期：2016-03-04
     * <li>修改内容：优化代码，调整展示表格的宽度
     * @param rdpRecordCardIDX 记录卡实例主键
     * @return 以HTML表格形式显示的检修检测项处理详情
     */
    public String queryInHTML(String rdpRecordCardIDX) {
        List<PartsRdpRecordRI> entityList = this.getModels(rdpRecordCardIDX);
        if (null == entityList || 0 >= entityList.size()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<table cellpadding=\"0\" cellspacing=\"0\">");
        sb.append("<thead>");
        sb.append("<tr>");
//        sb.append("<td width=\"20px\">").append("&nbsp;&nbsp;&nbsp;&nbsp;").append("</td>");
        sb.append("<td width=\"13%\">").append("编号").append("</td>");
        sb.append("<td width=\"20%\">").append("名称").append("</td>");
        sb.append("<td width=\"28%\">").append("技术要求").append("</td>");
        sb.append("<td width=\"24%\" colspan=\"2\">").append("检测结果").append("</td>");
        sb.append("<td width=\"15%\">").append("备注").append("</td>");
        sb.append("</tr>");
        sb.append("</thead>");
//        int index = 1;
        for (PartsRdpRecordRI ri : entityList) {
            // 查询数据项
            int rowspan = 1;            // 表格默认的【行】合并数量
            List<PartsRdpRecordDI> diList = partsRdpRecordDIManager.getModelByRdpRecordRIIDX(ri.getIdx());
            if (null != diList && 0 < diList.size()) {
                rowspan += diList.size();
            }
            sb.append("<tr>");
            // 序号
//            sb.append("<td style=\"text-align:center; padding:0;\" rowspan =\"").append(rowspan).append("\">").append(index++).append("</td>");
            sb.append("<td rowspan =\"").append(rowspan).append("\">").append(ri.getRepairItemNo()).append("</td>");       // 编号
            sb.append("<td rowspan =\"").append(rowspan).append("\">").append(ri.getRepairItemName()).append("</td>");     // 名称
            sb.append("<td rowspan =\"").append(rowspan).append("\">").append(ri.getRepairStandard()).append("</td>");     // 技术要求
            if (1 == rowspan) {
                sb.append("<td colspan=\"2\">").append(null == ri.getRepairResult() ? "&nbsp;" : ri.getRepairResult()).append("</td>");
                sb.append("<td>").append(null == ri.getRemarks() ? "&nbsp;" : ri.getRemarks()).append("</td>");
            } else {
                // 加载数据项
                for (PartsRdpRecordDI di : diList) {
                    sb.append("<tr>");
                    // 数据项名称 + 检测值
                    // 检验输入的数据项是否超出了预设的范围值
                    sb.append("<td style=\"text-align:right;").append(di.isOutOfRange() ? "color:red;" : "").append("\">").append(di.getDataItemName()).append("：").append(di.getIsBlank() == 1 ? "*" : "").append("</td>");
                    
                    sb.append("<td style=\"width:55px;").append(di.isOutOfRange() ? "color:red;" : "").append("\">");
                    sb.append(null == di.getDataItemResult()? "&nbsp" : di.getDataItemResult());
                    sb.append("</td>");
                    
                    StringBuilder temp = new StringBuilder();
                    // 备注（值域）
                    if (null != di.getMinResult()) {
                        temp.append("大于等于&nbsp;").append(di.getMinResult()).append("<br/>");
                    }
                    if (null != di.getMaxResult()) {
                        temp.append("小于等于&nbsp;").append(di.getMaxResult());
                    }
                    sb.append("<td>").append(temp.length() <= 0 ? "&nbsp;" : temp.toString()).append("</td>");
                    sb.append("</tr>");
                }
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
    
    /**
     * <li>说明：动态获取检修检测项的页面输入组件
     * <li>创建人：何涛
     * <li>创建日期：2016-2-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordCardIDX 记录卡实例主键
     * @return 动态html代码片段和检修数据项数据模型 {
     *      html: "<table>...</table>",
     *      data: [{
     *          idx:"E1AC1965F5F647C3A98F1C261366F5F5",
     *          status:"01",
     *          repairResult:"合格",
     *          remarks:"备注",           
     *          diList:[{
     *                  idx:"9069B8786F7842B5BC9DC6E0023214CE",
     *                  dataItemResult:"合格"
     *           }]
     *      }]
     * }
     * @throws IOException 
     */
    public Map<String, Object> createInHTML(String rdpRecordCardIDX) throws IOException {
        List<PartsRdpRecordRI> entityList = this.getModels(rdpRecordCardIDX);
        // Modified by hetao on 2016-04-19 优化界面显示，在无检修检测项时不显示空提示信息
        if (null == entityList || 0 >= entityList.size()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<table cellpadding=\"0\" cellspacing=\"0\">");
        sb.append("<thead>");
        sb.append("<tr>");
        //sb.append("<td width=\"10%\">").append("编号").append("</td>");
        sb.append("<td width=\"10%\">").append("名称").append("</td>");
        sb.append("<td width=\"25%\">").append("技术要求").append("</td>");
        sb.append("<td width=\"15%\">").append("处理情况").append("</td>");
        sb.append("<td width=\"30%\" colspan=\"2\">").append("检测结果（数据项）").append("</td>");
        sb.append("<td width=\"10%\">").append("数据范围").append("</td>");
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        
        // 检修数据项数据模型
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(entityList.size());
        Map<String, Object> riJson = null;
        for (PartsRdpRecordRI ri : entityList) {
            riJson = new HashMap<String, Object>();
            riJson.put("idx", ri.getIdx());
            riJson.put("status", ri.getStatus());
            riJson.put("repairResult", ri.getRepairResult());
            riJson.put("remarks", ri.getRemarks());
            list.add(riJson);
            // 查询数据项
            int rowspan = 1;            // 表格默认的【行】合并数量
            List<PartsRdpRecordDI> diList = partsRdpRecordDIManager.getModelByRdpRecordRIIDX(ri.getIdx());
            if (null != diList && 0 < diList.size()) {
                rowspan += diList.size();
            }
            sb.append("<tr>");
            //sb.append("<td rowspan =\"").append(rowspan).append("\">").append(ri.getRepairItemNo()).append("</td>");               // 编号
            sb.append("<td rowspan =\"").append(rowspan).append("\">").append(ri.getRepairItemName()).append("</td>");             // 名称
            sb.append("<td rowspan =\"").append(rowspan).append("\">").append(ri.getRepairStandard()).append("</td>");             // 技术要求
            sb.append("<td align=\"center\" rowspan =\"").append(rowspan).append("\">");             // 处理情况
            sb.append("<textarea id=\""+ ri.getIdx() +"_clqk\" name=\""+ ri.getHandleResult()+"\" style=\"height:100px;width:200px;\" \">");
            sb.append(null == ri.getHandleResult()? "" : ri.getHandleResult());
            sb.append("</textarea>");
            sb.append("</td>");  
            if (1 == rowspan) {
                sb.append("<td align=\"center\" colspan=\"2\">");
                // 可输入的下拉框开始
                sb.append("<div style=\"position:relative;\">");
                sb.append("<span style=\"margin-left:100px;width:18px;overflow:hidden;\">");
                sb.append("<select style=\"height:30px;width:158px;margin-left:-100px;\" onchange=\"document.getElementById('" + ri.getIdx() + "').value=this.value\">");
                // Modefied by hetao on 2016-05-09 增加一个空选项，规避select默认选择第一个下拉元素，导致在选择第一个选项不能触发change事件的缺陷
                sb.append("<option></option>");
                List<PartsStepResult> resultList = this.partsStepResultManager.getModelsByRiIDX(ri.getIdx());
                // 设置默认的检修检测项处理结果
                String defaultResult = "";
                if (null != resultList && !resultList.isEmpty()) {
                    for (PartsStepResult result : resultList) {
                        sb.append("<option vlaue=\"" + result.getResultName() + "\">" + result.getResultName() + "</option>");
                        if (PartsStepResult.ISDEFAULT_YES.equals(result.getIsDefault())) {
                            if (0 >= defaultResult.length()) defaultResult = result.getResultName();
                        }
                    }
                }
//                sb.append("<option vlaue=\"良好\">良好</option>");
//                sb.append("<option vlaue=\"不合格\">不合格</option>");
                sb.append("</select>");
                sb.append("<input id=\""+ ri.getIdx() +"\" name=\""+ ri.getRepairItemName() +"\" style=\"height:30px;width:140px;margin-left:-158px;\" value=\"" + (null == ri.getRepairResult()? defaultResult : ri.getRepairResult()) + "\"/>");
                sb.append("</span>");
                sb.append("</div>");
                // 可输入的下拉框结束
                sb.append("</td>");
                // 备注
                sb.append("<td>");
//                sb.append("<textarea style=\"height:60px;width:140px;\" id=\""+ ri.getIdx() + "_bz" + "\">");
                sb.append(null == ri.getRemarks()? "" : ri.getRemarks());
//                sb.append("</textarea>");
                sb.append("</td>");
            } else {
                List<Map<String, Object>> innerList = new ArrayList<Map<String,Object>>();
                Map<String, Object> diJson = null;
                // 加载数据项
                for (PartsRdpRecordDI di : diList) {
                    diJson = new HashMap<String, Object>();
                    diJson.put("idx", di.getIdx());
                    // Modified by hetao on 2016-03-04 22:30 增加数据项的必填项验证功能
                    diJson.put("required", (1 == di.getIsBlank() ? true : false));
                    diJson.put("dataItemResult", di.getDataItemResult());
                    innerList.add(diJson);
                    sb.append("<tr>");
                    // 数据项名称 + 检测值
                    // 检验输入的数据项是否超出了预设的范围值
                    // 如果输入值超出预设的范围，则以红色字体进行显示
                    sb.append("<td style=\"text-align:right;").append(di.isOutOfRange() ? "color:red;" : "").append("\">").append(di.getDataItemName()).append("：").append(di.getIsBlank() == 1 ? "*" : "").append("</td>");
                    sb.append("<td>");
                    // jxm.view.pjjxcl.PartsRdpRecordCardEditView.validate() 函数用于验证输入的数据项是否超出预设的范围
                    sb.append("<input onchange=\"jxm.view.pjjxcl.PartsRdpRecordCardEditView.validate('" + di.getIdx() + "')\" ");
                    sb.append("min=\"" + di.getMinResult() + "\" max=\"" + di.getMaxResult() + "\" ");
                    sb.append("style=\"height:30px;width:135px;border:0;border-bottom:1px solid gray;border-radius:0;").append(di.isOutOfRange() ? "color:red;" : "").append("\" ");
                    sb.append("type=\"text\" id=\""+ di.getIdx() +"\" ");
                    sb.append("title=\"").append(di.getDataItemName()).append("\"");
                    sb.append("name=\"").append(di.getDataItemName()).append("\"");
                    sb.append("required=\"").append(1 == di.getIsBlank() ? true : false).append("\" ");
                    sb.append("value=\"").append(null == di.getDataItemResult()? "" : di.getDataItemResult()).append("\" ");
                    sb.append("/>");
                    // 添加是否为可视化数据来源
                    if(di.getDataSource() != null && di.getDataSource().intValue() == 1){
                        sb.append("&nbsp;&nbsp;&nbsp;&nbsp; <span style=\"color:blue\">（可视化）</span>");
                    }
                    sb.append("</td>");
                    // html的input输入框提示信息 <仅用于备忘，于实际业务无关 hetao on 2016-03-04 23:50>
                    // <input type="text" name="textfield" value="请输入..." onclick="if(value==defaultValue){value='';this.style.color='#000'}" onBlur="if(!value){value=defaultValue;this.style.color='#999'}" style="color:#999"/>
                    StringBuilder temp = new StringBuilder();
                    // 备注（值域）
                    if (null != di.getMinResult()) {
                        temp.append("大于等于&nbsp;").append(di.getMinResult()).append("<br/>");
                    }
                    if (null != di.getMaxResult()) {
                        temp.append("小于等于&nbsp;").append(di.getMaxResult());
                    }
                    sb.append("<td>").append(temp.length() <= 0 ? "&nbsp;" : temp.toString()).append("</td>");
                    sb.append("</tr>");
                }
                riJson.put("diList", innerList);
            }
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        sb.append("</table>");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("html", sb.toString());                 // 动态html代码片段
        map.put("data", JSONUtil.write(list));          // 检修检测数据项数据模型
        return map;
    }
    
	/**
	 * <li>说明：获取配件检修检测项及数据项列表
	 * <li>创建人：程锐
	 * <li>创建日期：2016-1-21
	 * <li>修改人：林欢
	 * <li>修改日期：2016-5-9
	 * <li>修改内容：默认结果通过pjjx_parts_step_resutl获取
	 * @param rdpRecordCardIDX 配件检修记录单IDX
	 * @return 配件检修检测项及数据项列表
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpRecordRIAndDI> getListForRIAndDI(String rdpRecordCardIDX) throws IllegalAccessException, InvocationTargetException {
		List<PartsRdpRecordRI> riList = getModels(rdpRecordCardIDX);
		List<PartsRdpRecordRIAndDI> riDiList = new ArrayList<PartsRdpRecordRIAndDI>();
        
        //注释部分为通过数据字段获取默认结果
//		StringBuilder sb = new StringBuilder();
//        sb.append("from EosDictEntry where status = 1");
//        sb.append("and id.dicttypeid = '").append("PJJX_RECORD_RI_RESULT").append("'");
//        List<EosDictEntry> eosList = partsRdpQCParticipantManager.getDaoUtils().find(sb.toString());
//        List<String> resultList = new ArrayList<String>(eosList.size());
//        for (EosDictEntry entry : eosList) {
//            resultList.add(entry.getDictname());
//        }
       
       
		for (PartsRdpRecordRI ri : riList) {
			PartsRdpRecordRIAndDI riDi = new PartsRdpRecordRIAndDI();
			BeanUtils.copyProperties(riDi, ri);
			List<PartsRdpRecordDI> diList = partsRdpRecordDIManager.getModelByRdpRecordRIIDX(ri.getIdx());
			List<RecordDIBean> diBeanList = new ArrayList<RecordDIBean>();
			if (diList != null && diList.size() > 0) {
				for (PartsRdpRecordDI recordDI : diList) {
					RecordDIBean bean = new RecordDIBean();
					BeanUtils.copyProperties(bean, recordDI);
					StringBuilder remark = new StringBuilder();
					Double minResult = recordDI.getMinResult();
					Double maxResult = recordDI.getMaxResult();
					if (minResult != null)
						remark.append("大于等于").append(minResult);
					if (maxResult != null)
						remark.append("小于等于").append(maxResult);
					bean.setRemark(remark.toString());
					diBeanList.add(bean);
				}
				riDi.setDiList(diBeanList);
				riDi.setHasDI(true);
			} else {
                
                //如果没有检测项，获取默认的结果
                List<PartsStepResult> eosList = partsStepResultManager.getPartsStepResultByRdpRecordCardIDX(riDi);
                List<String> resultList = new ArrayList<String>(eosList.size());
                for (PartsStepResult entry : eosList) {
                    resultList.add(entry.getResultName());
                }
                
				riDi.setResultList(resultList);
				riDi.setHasDI(false);
			}
			riDiList.add(riDi);
		}
		return riDiList;
	}
	
	/**
	 * <li>标题: 机车检修管理信息系统
	 * <li>说明：配件检修数据项包装类
	 * <li>创建人：程锐
	 * <li>创建日期：2016-1-21
	 * <li>修改人: 
	 * <li>修改日期：
	 * <li>修改内容：
	 * <li>版权: Copyright (c) 2008 运达科技公司
	 * @author 信息系统事业部检修系统项目组
	 * @version 3.2
	 */
	@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecordDIBean {
		private String idx;
		private String dataItemName;
		private Integer isBlank;
		private String dataItemResult;
	    private String remark;
	    /* 最小范围值 */
	    private Double minResult;
	    /* 最大范围值 */
	    private Double maxResult; 
		public Double getMaxResult() {
			return maxResult;
		}
		public void setMaxResult(Double maxResult) {
			this.maxResult = maxResult;
		}
		public Double getMinResult() {
			return minResult;
		}
		public void setMinResult(Double minResult) {
			this.minResult = minResult;
		}
		public String getDataItemName() {
			return dataItemName;
		}
		public void setDataItemName(String dataItemName) {
			this.dataItemName = dataItemName;
		}
		public String getDataItemResult() {
			return dataItemResult;
		}
		public void setDataItemResult(String dataItemResult) {
			this.dataItemResult = dataItemResult;
		}
		public String getIdx() {
			return idx;
		}
		public void setIdx(String idx) {
			this.idx = idx;
		}
		public Integer getIsBlank() {
			return isBlank;
		}
		public void setIsBlank(Integer isBlank) {
			this.isBlank = isBlank;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
	}
}