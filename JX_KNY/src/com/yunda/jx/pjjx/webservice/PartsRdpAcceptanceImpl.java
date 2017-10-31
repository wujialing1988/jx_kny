package com.yunda.jx.pjjx.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpBean;
import com.yunda.jx.pjjx.partsrdp.expendmat.entity.PartsRdpExpendMat;
import com.yunda.jx.pjjx.partsrdp.expendmat.manager.PartsRdpExpendMatManager;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpQueryManager;
import com.yunda.jx.pjjx.partsrdp.rdpnotice.entity.PartsRdpNotice;
import com.yunda.jx.pjjx.partsrdp.rdpnotice.manager.PartsRdpNoticeManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordCard;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordDI;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordRI;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordDIManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordRIManager;
import com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecCard;
import com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecWS;
import com.yunda.jx.pjjx.partsrdp.tecinst.manager.PartsRdpTecCardManager;
import com.yunda.jx.pjjx.partsrdp.tecinst.manager.PartsRdpTecWSManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.wsbean.PartsRdpExpendMatWSBean;
import com.yunda.jx.pjjx.wsbean.PartsRdpNoticeWSBean;
import com.yunda.jx.pjjx.wsbean.PartsRdpRecordCardWSBean;
import com.yunda.jx.pjjx.wsbean.PartsRdpRecordDIWSBean;
import com.yunda.jx.pjjx.wsbean.PartsRdpRecordRIWSBean;
import com.yunda.jx.pjjx.wsbean.PartsRdpTecCardWSBean;
import com.yunda.jx.pjjx.wsbean.PartsRdpTecWSBean;
import com.yunda.jx.pjjx.wsutil.IWSEntityFilter;
import com.yunda.jx.pjjx.wsutil.ValidField;
import com.yunda.jx.pjjx.wsutil.WSEntityFilterImpl;
import com.yunda.jx.pjjx.wsutil.WSParameter;
import com.yunda.util.BeanUtils;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>说明：修竣配件合格验收WS
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-15
 * <li>成都运达科技股份有限公司
 */
@Service("partsRdpAcceptanceWS")
public class PartsRdpAcceptanceImpl implements IPartsRdpAcceptance, IService {

	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 检修记录业务类
	 */
	@Resource(name="partsRdpRecordCardManager")
	private PartsRdpRecordCardManager cardManager;
	/**
	 * 提票业务类
	 */
	@Resource(name="partsRdpNoticeManager")
	private PartsRdpNoticeManager noticeManager;
	/**
	 * 物料消耗业务类
	 */
	@Resource(name="partsRdpExpendMatManager")
	private PartsRdpExpendMatManager expendMatManager;
	/**
	 * ,配件检修检测项实例
	 */
	@Resource(name="partsRdpRecordRIManager")
	private PartsRdpRecordRIManager recordRIManager;
	/**
	 * 配件检修检测数据项
	 */
	@Resource(name="partsRdpRecordDIManager")
	private PartsRdpRecordDIManager recordDIManager;
	/**
	 * 作业工单业务类
	 */
	@Resource(name="partsRdpTecCardManager")
	private PartsRdpTecCardManager tecCardManager;
	/**
	 * 工序业务类
	 */
	@Resource(name="partsRdpTecWSManager")
	private PartsRdpTecWSManager tecWSManager;
    
    /**
     * 配件检修任务单查询业务类
     */
    @Resource
    private PartsRdpQueryManager partsRdpQueryManager;
	
	/**
	 * <li>方法说明：配件修竣合格验收列表查询
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：程锐
	 * <li>修改日期：2015-11-13
	 * <li>修改内容：重构查询hql
     * <li>修改人：何涛
     * <li>修改日期：2016-03-21
     * <li>修改内容：1、增加查询字段：下车车型（unloadTrainType），组合匹配下车车型、下车车号，
                   2、增加查询字段：名称规格型号（specificationModel），组合匹配配件规格型号、配件名称
     * <li>修改人：何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容: 修改不规范的异常处理
     * @param jsonObject {
            start: 1, 
            limit: 50, 
            orders:[{sort: "field" dir: "ASC"}], 
            entityJson: {
                status: "01",
                partsNo: "25202",
                identificationCode："25202"
                unloadTrainType: "HXD10002",
                specificationModel: "TGZS500.221.000\\SS4",
            }
       }
     * @return JSON列表（数组）
	 */
	@Override
	public String acceptanceList(String jsonObject) {
	    JSONObject jo = JSONObject.parseObject(jsonObject);
	    
	    // 查询记录开始索引
	    int start = jo.getIntValue(Constants.START);
	    // 查询记录条数
	    int limit = jo.getIntValue("limit");
	    start = limit * (start - 1);
//	    String entityJson = ;
        String entityJson = StringUtil.nvl(jo.getString("entityJson"), Constants.ENTITY_JSON);
	    PartsRdp rdp;
        try {
            rdp = JSONUtil.read(entityJson, PartsRdp.class);
            rdp.setStatus(PartsRdp.STATUS_DYS);
            JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
            // 排序字段
            Order[] orders = JSONTools.getOrders(jArray);
            Page<PartsRdpBean> page = partsRdpQueryManager.queryPageList(new SearchEntity<PartsRdp>(rdp, start, limit, orders));
            return JSONTools.toJSONList(page);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }            
	}
	
	/**
	 * <li>方法说明：作业工单列表
	 * <li>方法名：jobOrderList
	 * @param jsonObject {start: x, limit: x, orders:[{sort: "field" dir: "ASC"}], entityJson: {}}
	 * @param rdpIdx 兑现单主键
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@Override
	public String jobOrderList(String jsonObject, String rdpIdx) {
		ValidField.nullPointer(rdpIdx);
		WSEntityFilterImpl<PartsRdpTecCard> filter = new WSEntityFilterImpl<PartsRdpTecCard>(){
			
			@Override
			public void handle(PartsRdpTecCard t) {
				t.setRdpIDX(params[0].toString());
			}
		};
		filter.params = new Object[] { rdpIdx };
		return findPageList(jsonObject, tecCardManager, PartsRdpTecCard.class, filter, PartsRdpTecCardWSBean.class);
	}
	
	/**
	 * <li>方法说明：作业任务列表
	 * <li>方法名：jobTaskList
	 * @param jsonObject {start: x, limit: x, orders:[{sort: "field" dir: "ASC"}], entityJson: {}}
	 * @param rdpTecCardIdx 作业工单主键
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@Override
	public String jobTaskList(String jsonObject, String rdpTecCardIdx) {
		ValidField.nullPointer(rdpTecCardIdx);
		WSEntityFilterImpl<PartsRdpTecWS> filter = new WSEntityFilterImpl<PartsRdpTecWS>(){
			
			@Override
			public void handle(PartsRdpTecWS t) {
				t.setRdpTecCardIDX(params[0].toString());
			}
		};
		filter.params = new Object[] { rdpTecCardIdx };
		
		return findPageList(jsonObject, tecWSManager, PartsRdpTecWS.class, filter, PartsRdpTecWSBean.class);
	}

	/**
	 * <li>方法说明：物料消耗列表
	 * <li>方法名：materialConsumptionList
	 * @param jsonObject {start: x, limit: x, orders:[{sort: "field" dir: "ASC"}], entityJson: {}}
	 * @param rdpIdx 配件兑现单主键
	 * @param rdpTecCardIdx 工单主键
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@Override
	public String materialConsumptionList(String jsonObject, String rdpIdx, String rdpTecCardIdx) {		
		
		WSEntityFilterImpl<PartsRdpExpendMat> filter = new WSEntityFilterImpl<PartsRdpExpendMat>(){
			
			@Override
			public void handle(PartsRdpExpendMat t) {
				if(params[0] != null)
					t.setRdpIDX(params[0].toString());
				else{
					ValidField.nullPointer(params[1]); //不能两个参数为空，其中一个必须有值
					t.setRdpNodeIDX(params[1].toString());
				}
			}
		};
		filter.params = new Object[] { rdpIdx, rdpTecCardIdx };
		return findPageList(jsonObject, expendMatManager, PartsRdpExpendMat.class, filter, PartsRdpExpendMatWSBean.class);
	}

	/**
	 * <li>方法说明：检修/检测项列表
	 * <li>方法名：repairItemList
	 * @param jsonObject {start: x, limit: x, orders:[{sort: "field" dir: "ASC"}], entityJson: {}}
	 * @param rdpRecordCardIDX 记录卡实例主键
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@Override
	public String repairItemList(String jsonObject, String rdpRecordCardIDX) {
		ValidField.nullPointer(rdpRecordCardIDX);
		
		WSEntityFilterImpl<PartsRdpRecordRI> filter = new WSEntityFilterImpl<PartsRdpRecordRI>(){
			
			@Override
			public void handle(PartsRdpRecordRI t) {
				t.setRdpRecordCardIDX(params[0].toString());
			}
		};
		filter.params = new Object[] { rdpRecordCardIDX };
		return findPageList(jsonObject, recordRIManager, PartsRdpRecordRI.class, filter, PartsRdpRecordRIWSBean.class);
	}

	/**
	 * <li>方法说明：检测项结果列表
	 * <li>方法名：repairItemResultList
	 * @param jsonObject 查询JSON
	 * @param rdpRecordRIIDX 检测项主键
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-15
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public String repairItemResultList(String jsonObject, String rdpRecordRIIDX){
		ValidField.nullPointer(rdpRecordRIIDX);
		
		WSEntityFilterImpl<PartsRdpRecordDI> filter = new WSEntityFilterImpl<PartsRdpRecordDI>(){
			
			@Override
			public void handle(PartsRdpRecordDI t) {
				t.setRdpRecordRIIDX(params[0].toString());
			}
		};
		filter.params = new Object[] { rdpRecordRIIDX };
		return findPageList(jsonObject, recordDIManager, PartsRdpRecordDI.class, filter, PartsRdpRecordDIWSBean.class);
	}
	
	/**
	 * <li>方法说明：检修记录列表
	 * <li>方法名：repairRecordList
	 * @param jsonObject {start: x, limit: x, orders:[{sort: "field" dir: "ASC"}], entityJson: {}}
	 * @param rdpIdx 配件修任务主键
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@Override
	public String repairRecordList(String jsonObject, String rdpIdx) {
		
		ValidField.nullPointer(rdpIdx);
		
		WSEntityFilterImpl<PartsRdpRecordCard> filter = new WSEntityFilterImpl<PartsRdpRecordCard>(){
			
			@Override
			public void handle(PartsRdpRecordCard t) {
				t.setRdpIDX(params[0].toString());
			}
		};
		filter.params = new Object[] { rdpIdx };
		
		return findPageList(jsonObject, cardManager, PartsRdpRecordCard.class, filter, PartsRdpRecordCardWSBean.class);
	}

	/**
	 * <li>方法说明：回修提票列表
	 * <li>方法名：returnRepairFaultList
	 * @param jsonObject {start: x, limit: x, orders:[{sort: "field" dir: "ASC"}], entityJson: {}}
	 * @param rdpIdx 配件任务单主键
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@Override
	public String returnRepairFaultList(String jsonObject, String rdpIdx) {
		
		ValidField.nullPointer(rdpIdx);
		
		WSEntityFilterImpl<PartsRdpNotice> filter = new WSEntityFilterImpl<PartsRdpNotice>(){
			
			@Override
			public void handle(PartsRdpNotice t) {
				t.setRdpIDX(params[0].toString());
			}
		};
		filter.params = new Object[] { rdpIdx };
		
		return findPageList(jsonObject, noticeManager, PartsRdpNotice.class, filter, PartsRdpNoticeWSBean.class);
	}

	/**
     * <li>方法说明： 统一查询
     * <li>方法名：findPageList
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-14
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @param <T> 泛型
     * @param jsonObject json字符串
     * @param service 业务类
     * @param clazz 实体类
     * @param filter 默认查询过滤
     * @param copyBean 复制到bean集合
     * @return 查询结果JSON字符串
     */
    @SuppressWarnings("unchecked")
    private <T> String findPageList(String jsonObject, JXBaseManager<T, T> service, Class<T> clazz, IWSEntityFilter<T> filter, Class<?> copyBean) {
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            WSParameter<T> request = new WSParameter<T>(jsonObject, clazz);
            
            if (filter != null) {
                filter.handle(request.getEntity());
            }
            SearchEntity<T> searchEntity = new SearchEntity<T>(request.getEntity(), request.getStart(), request.getLimit(), request.getOrders());
            Page<T> page = service.findPageList(searchEntity);
            if (page.getTotal() == 0) {
                msg.setFaildFlag(IService.MSG_RESULT_IS_EMPTY);
            } else if (copyBean != null) {
                List list = page.getList();
                List newList = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    Object obj = list.get(i);
                    if (obj instanceof PartsRdpTecCard) {
                        PartsRdpTecCard tec = (PartsRdpTecCard) obj;
                        tec.setStatus(PartsRdpRecordCardService.convertPartsRdpCardStatus(tec.getStatus()));
                        newList.add(tec);
                    } else if (obj instanceof PartsRdpRecordCard) {
                        PartsRdpRecordCard record = (PartsRdpRecordCard) obj;
                        record.setStatus(PartsRdpRecordCardService.convertPartsRdpCardStatus(record.getStatus()));
                        newList.add(record);
                    } else if (obj instanceof PartsRdpNotice) {
                        PartsRdpNotice notice = (PartsRdpNotice) obj;
                        notice.setStatus(PartsRdpRecordCardService.convertPartsRdpCardStatus(notice.getStatus()));
                        newList.add(notice);
                    } else
                        newList.add(obj);
                }
                return JSONTools.toJSONList(page.getTotal(), BeanUtils.copyListToList(copyBean, newList));
            } else {
                return JSONTools.toJSONList(page.getTotal(), page.getList());
            }
        } catch (Exception e) {
            msg.setFaildFlag(e.getMessage());
            logger.error("配件修竣合格验收WS接口异常", e);
        }
        return JSONObject.toJSONString(msg);
    }
    
}
