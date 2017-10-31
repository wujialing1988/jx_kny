package com.yunda.jx.pjjx.partsrdp.qcinst.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsQRBean;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQR;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQRManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQRQueryManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecord;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpQR控制器, 质量检查结果
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
public class PartsRdpQRAction extends JXBaseAction<PartsRdpQR, PartsRdpQR, PartsRdpQRManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /** PartsRdpQRQueryManager业务类,配件质检查询业务类 */
    @Resource
    private PartsRdpQRQueryManager partsRdpQRQueryManager;
    
    @Resource
    private  PartsRdpRecordCardManager partsRdpRecordCardManager;
	
	
	/**
	 * <li>说明：签名提交
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void signAndSubmit() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		try {
			String[] rdpRecordCardIDXs = JSONUtil.read(req, String[].class); 
			String qCItemNo = req.getParameter("qCItemNo");		// 质量检查项编码
			String qRResult = req.getParameter("qRResult");		// 质量检查结果
			this.manager.signAndSubmit(rdpRecordCardIDXs, qCItemNo, qRResult);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
	/**
	 * <li>说明：回退
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void updateToBack() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		try {
			String[] rdpRecordCardIDXs = JSONUtil.read(req, String[].class); 
			String qCItemNo = req.getParameter("qCItemNo");		// 质量检查项编码
			this.manager.updateToBack(rdpRecordCardIDXs, qCItemNo);
            map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
	/**
	 * 
	 * <li>说明：根据“记录卡实例主键”查询质量检查结果
	 * <li>创建人：程梅
	 * <li>创建日期：2015-1-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @throws 抛出异常列表
	 */
	public void getModelByRdpRecordCardIDX() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 记录卡实例主键
			String rdpRecordCardIDX = getRequest().getParameter("rdpRecordCardIDX");
			
			List<PartsRdpQR> list = this.manager.getModel(rdpRecordCardIDX);
			
			map.put("qrList", list);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
    
    /**
     * <li>说明：签名提交（手持端）
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容： 
     * @throws JsonMappingException
     * @throws IOException
     */
    public void sign() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String[] idxs = JSONUtil.read(req, String[].class); 
            String qrResult = req.getParameter("qrResult");     // 质量检查结果
            this.manager.signAndSubmitByIDX(idxs, qrResult);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    /**
     * <li>说明：全部签名提交（手持端）
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容： 
     * @throws JsonMappingException
     * @throws IOException
     */
    public void allSignAndSubmit() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String entityJson = StringUtil.nvl(getRequest().getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            PartsQRBean entity = JSONUtil.read(entityJson, PartsQRBean.class);
            
            SearchEntity<PartsQRBean> searchEntity = new SearchEntity<PartsQRBean>(entity, null, null, null);
            String qrResult = req.getParameter("qrResult");     // 质量检查结果
            String checkWay = req.getParameter("checkWay");     
            this.manager.allSignAndSubmit(checkWay, searchEntity, qrResult);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：（新）待检验工单汇总
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findRdpPageList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            PartsRdp entity = JSONUtil.read(entityJson, PartsRdp.class);
            SearchEntity<PartsRdp> searchEntity = new SearchEntity<PartsRdp>(entity, getStart(), getLimit(), getOrders());
            map = partsRdpQRQueryManager.findRdpPageList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：（新）待检验工单汇总
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void queryRecordPageList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            PartsRdpRecord entity = JSONUtil.read(entityJson, PartsRdpRecord.class);
            SearchEntity<PartsRdpRecord> searchEntity = new SearchEntity<PartsRdpRecord>(entity, getStart(), getLimit(), getOrders());
            // 是否查询全部
            boolean isAll = StringUtil.isNullOrBlank(req.getParameter("isAll")) ? false : Boolean.valueOf(req.getParameter("isAll"));
            map = partsRdpQRQueryManager.queryRecordPageList(searchEntity,isAll).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：（新）待检验检修记录卡汇总查询
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void queryCardList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String  rdpRecordIDX = req.getParameter("rdpRecordIDX");
            String partsRdpRecordCardList = partsRdpRecordCardManager.integrateQueryCardList(rdpRecordIDX);
            map.put("cardList", partsRdpRecordCardList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
}