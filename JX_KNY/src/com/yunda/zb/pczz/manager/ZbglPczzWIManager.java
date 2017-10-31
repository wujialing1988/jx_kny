package com.yunda.zb.pczz.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.pczz.entity.ZbglPczzWI;
import com.yunda.zb.pczz.entity.ZbglPczzWiItem;
import com.yunda.zb.pczz.webservice.ZbglPczzWiBean;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczzWI业务类,普查整治任务单
 * <li>创建人：王利成
 * <li>创建日期：2015-03-04
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbglPczzWIManager")
public class ZbglPczzWIManager extends JXBaseManager<ZbglPczzWI, ZbglPczzWI>{
    
    /** ZbglPczzWiItem业务类,普查整治任务项 */
    @Resource
    private ZbglPczzWiItemManager zbglPczzWiItemManager;
    
    /**
     * <li>说明：获取同一机车整备单的已处理的普查整治任务单数量
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 同一机车整备单的已处理的普查整治任务单数量
     */
    public int getHandledPczzWiCountByRdp(String rdpIDX) {
        List<ZbglPczzWI> list = getHandledPczzWiListByRdp(rdpIDX);
        return CommonUtil.getListSize(list);
    }
    
    /**
     * <li>说明：获取同一机车整备单的已处理的普查整治任务单列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 同一机车整备单的已处理的普查整治任务单列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglPczzWI> getHandledPczzWiListByRdp(String rdpIDX) {
        if (StringUtil.isNullOrBlank(rdpIDX))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("rdpIdx", rdpIDX);
        paramMap.put("wIStatus", ZbglRdpWi.STATUS_HANDLED);
        return getPczzWiList(paramMap);
    }
    
    /**
     * <li>说明：获取同一机车整备单的普查整治任务单数量
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 同一机车整备单的普查整治任务单数量
     */
    public int getAllPczzWiCountByRdp(String rdpIDX) {
        List<ZbglPczzWI> list = getAllPczzWiListByRdp(rdpIDX);
        return CommonUtil.getListSize(list);
    }
    
    /**
     * <li>说明：获取同一机车整备单的普查整治任务单列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 同一机车整备单的普查整治任务单列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglPczzWI> getAllPczzWiListByRdp(String rdpIDX) {
        if (StringUtil.isNullOrBlank(rdpIDX))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("rdpIdx", rdpIDX);
        return getPczzWiList(paramMap);
    }
    
    /**
     * <li>说明：获取普查整治任务单列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 普查整治任务单列表
     */
    @SuppressWarnings("unchecked")
    private List<ZbglPczzWI> getPczzWiList(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from ZbglPczzWI where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：查询普查整治任务单
     * <li>创建人：林欢
     * <li>创建日期：2016-8-19
     * <li>修改人：刘国栋 
     * <li>修改日期：2016-8-25
     * <li>修改内容：添加两个子查询出的字段，普查整治未完成项目和未检查项目
     * @param paramsMap 查询参数map
     * @param operatorid 操作人idx
     * @param start 开始页码
     * @param limit 每页条数
     * @return Page<ZbglPczzWiBean> 返回分页普查整治任务单
     */
    public Page<ZbglPczzWiBean> findZbglPczzPageList(Map<String, Object> paramsMap, Long operatorid, int start, Integer limit) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select a.*, " );
        //在查询普查任务单时，添加两个子查询，将查询出的count数，装载到自定义命名的字段中，（在对应的bean中需要添加对应字段）
        sb.append(" (select count(1) from ZB_ZBGL_PCZZ_wi_Item b where b.item_Status= 0 and b.zbgl_pczz_wi_idx = a.idx) as notFinishCounts, " );
        sb.append(" (select count(1) from ZB_ZBGL_PCZZ_wi_Item b where b.item_Status= 1 and b.zbgl_pczz_wi_idx = a.idx) as notCheckCounts " );
        //组装查询条件
        StringBuffer sbFrom = new StringBuffer();
        sbFrom.append(" from zb_zbgl_pczz_wi a where a.rdp_idx is not null and a.start_date <= sysdate and a.record_status= 0 and a.wi_status <> 'CHECKED' ");
        //车号模糊查询
        String trainTypeShortName = paramsMap.get("trainTypeShortName").toString();
        String trainNo = paramsMap.get("trainNo").toString();
        if (StringUtils.isNotBlank(trainNo)) {
        	sbFrom.append(" and a.train_no like '%").append(trainNo).append("%'");
        }
//      车型模糊查询
        if (StringUtils.isNotBlank(trainTypeShortName)) {
        	sbFrom.append(" and a.train_type_shortname like '%").append(trainTypeShortName).append("%'");
        }
        
        //      此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = " select count(*) as rowcount " + sbFrom.toString();
        return this.queryPageList(totalSql, sb.append(sbFrom.toString()).toString(), start, limit, false, ZbglPczzWiBean.class);
    }

    /**
     * <li>说明：查询普查整治任务单对象
     * <li>创建人：林欢
     * <li>创建日期：2016-8-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglPczzIDX 普查整治计划idx
     * 
     * zbglPczzIDX 普查整治计划idx
     * wiStatus 普查整治任务单状态
     * rdpIdx 整备单idx
     * 
     * ZbglPczzWI 普查整治任务单对像
     */
    public ZbglPczzWI findZbglPczzWIByParams(Map<String, Object> params) {
        StringBuffer sb = new StringBuffer();
        
        sb.append(" from ZbglPczzWI a where a.recordStatus = 0 ");
        
        //过滤普查整治计划idx
        if (params.get("zbglPczzIDX") != null) {
            sb.append(" and a.zbglPczzIDX = '").append(params.get("zbglPczzIDX").toString()).append("'");
        }
        
        //过滤普查整治任务单状态
        if (params.get("wiStatus") != null) {
            sb.append(" and a.wIStatus in ('").append(params.get("wiStatus").toString().replace(",", "','")).append("')");
        }
        
        //过滤整备单idx
        if (params.get("rdpIdx") != null) {
            sb.append(" and a.rdpIdx = '").append(params.get("rdpIdx").toString()).append("'");
        }
        
//      过滤车号
        if (params.get("trainNo") != null) {
            sb.append(" and a.trainNo = '").append(params.get("trainNo").toString()).append("'");
        }
        
//      过滤车型
        if (params.get("trainTypeShortName") != null) {
            sb.append(" and a.trainTypeShortName = '").append(params.get("trainTypeShortName").toString()).append("'");
        }
        
        return this.findSingle(sb.toString());
    }

    /**
     * <li>说明：级联删除普查整治任务单以及下面的任务项根据整备单idx
     * <li>创建人：林欢
     * <li>创建日期：2016-8-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglRdpIDX 整备单idx
     */
    public void deleteZbglPczzWiInfo(String zbglRdpIDX) {
//      普查整治对象删除（普查整治任务单，普查整治任务项）
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("rdpIdx", zbglRdpIDX);
        ZbglPczzWI zbglPczzWI = this.findZbglPczzWIByParams(params);
        //删除普查整治任务项
        //通过普查整治任务单idx查询普查整治任务项list
        ZbglPczzWiItem zbglPczzWiItem = new ZbglPczzWiItem();
        zbglPczzWiItem.setZbglPczzWiIDX(zbglPczzWI.getIdx());    
        List<ZbglPczzWiItem> zbglPczzWiItemList = zbglPczzWiItemManager.findZbglPczzWiItemListByStatus(zbglPczzWiItem );
        for (ZbglPczzWiItem item : zbglPczzWiItemList) {
            zbglPczzWiItemManager.getDaoUtils().remove(item);
        }
        
        //删除普查整治任务单
        this.getDaoUtils().remove(zbglPczzWI);
    }
}
