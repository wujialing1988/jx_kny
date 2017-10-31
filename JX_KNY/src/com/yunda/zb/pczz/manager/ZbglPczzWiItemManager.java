package com.yunda.zb.pczz.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.zb.pczz.entity.ZbglPczz;
import com.yunda.zb.pczz.entity.ZbglPczzWI;
import com.yunda.zb.pczz.entity.ZbglPczzWiItem;
import com.yunda.zb.pczz.webservice.ZbglPczzWiItemBean;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczzWiItem业务类,普查整治任务项
 * <li>创建人：林欢
 * <li>创建日期：2016-08-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbglPczzWiItemManager")
public class ZbglPczzWiItemManager extends JXBaseManager<ZbglPczzWiItem, ZbglPczzWiItem>{
    
    /** 机构服务类 */
    @Resource
    private OmOrganizationManager omOrganizationManager;
    
    /** ZbglPczzWI业务类,普查整治任务单 */
    @Resource
    private ZbglPczzWIManager zbglPczzWIManager;
    
    /** ZbglPczz业务类,普查整治计划 */
    @Resource
    private ZbglPczzManager zbglPczzManager;

    /**
     * <li>说明：查看单条普查整治任务项
     * <li>创建人：林欢
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param paramsMap 查询参数map
     * 
     * zbglPczzWiIDX 普查整治任务单idxs
     * 
     * @param start 开始页码
     * @param limit 每页条数
     * @return String
     */
    public Page<ZbglPczzWiItemBean> findZbglPczzWiItemPageList(Map<String, Object> paramsMap, int start, Integer limit) {

        StringBuffer sb = new StringBuffer();
        
        String zbglPczzWiIDX = paramsMap.get("zbglPczzWiIDX").toString();

        sb.append(" select a.* from zb_zbgl_pczz_wi_item a where a.zbgl_pczz_wi_idx = '").append(zbglPczzWiIDX).append("'");
        
        String totalSql = " select count(*) as rowcount " + sb.substring(sb.indexOf(" from "));
        return this.queryPageList(totalSql, sb.toString(), start, limit, false, ZbglPczzWiItemBean.class);
    }

    /**
     * <li>说明：普查整治任务项完工/普查整治任务项检查完毕
     * <li>创建人：林欢
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param paramsMap 查询参数map
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void finishOrCheckedZbglPczzWiItem(Map<String, Object> paramsMap) throws BusinessException, NoSuchFieldException {
        String operatStatus = paramsMap.get("operatStatus").toString();//操作动作（finished == 普查整治任务项完工 checked == 普查整治任务项检查完毕）
        Long operatorid = Long.valueOf(paramsMap.get("operatorid").toString());//操作人员
        String zbglPczzWiItemIDX = paramsMap.get("zbglPczzWiItemIDX").toString();//普查整治任务项idx
        String itemResualt = paramsMap.get("itemResualt").toString();//普查整治任务项完成结果
        
        //判断当前操作是完工还是普查整治
        if ("finished".equals(operatStatus)) {
            finishZbglPczzWiItem(operatorid,zbglPczzWiItemIDX,itemResualt);
//          判断当前操作是完工还是普查整治
        }else if ("checked".equals(operatStatus)) {
            checkedZbglPczzWiItem(operatorid,zbglPczzWiItemIDX,itemResualt);
        }else {
            throw new BusinessException("操作失败！");
        }
    }

    /**
     * <li>说明：普查整治任务项检查完毕
     * <li>创建人：林欢
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人员
     * @param zbglPczzWiItemIDX 普查整治任务项idx
     * @param itemResualt 普查整治任务项结果
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    private void checkedZbglPczzWiItem(Long operatorid,String zbglPczzWiItemIDX,String itemResualt) throws BusinessException, NoSuchFieldException {
//      通过普查整治任务项idx
        ZbglPczzWiItem zbglPczzWiItem = this.getModelById(zbglPczzWiItemIDX);
        
//      通过普查整治任务项idx查询数据中的状态是否已经修改
        if (zbglPczzWiItem.getItemStatus() > 1 ) {
            throw new BusinessException("当条数据正在处理中，请刷新确认！");
        }
        
        zbglPczzWiItem.setItemStatus(2);//设置普查整治任务项完成
        
        OmEmployee omEmployee = SystemContext.getOmEmployee();
        zbglPczzWiItem.setCheckPersonIDX(omEmployee.getOperatorid());//检查人idx
        zbglPczzWiItem.setCheckPersonName(omEmployee.getEmpname());//检查人名称
        zbglPczzWiItem.setCheckDate(new Date());//检查时间
        
        this.saveOrUpdate(zbglPczzWiItem);
        
        //当所有的普查整治任务项均为已检查，修改普查整治任务单位已检查
//      当不存在未完毕的普查整治任务项，但是有完毕的普查整治任务项时候，修改普查整治任务单状态为已完成
        
//      封装查询参数
        ZbglPczzWiItem params = new ZbglPczzWiItem();
        params.setZbglPczzWiIDX(zbglPczzWiItem.getZbglPczzWiIDX());
        
        Integer todoCount = 0;//普查整治项未完毕个数
        Integer finishCount = 0;//普查整治项完毕个数
        List<ZbglPczzWiItem> zbglPczzWiItemList = findZbglPczzWiItemListByStatus(params);
        for (ZbglPczzWiItem item : zbglPczzWiItemList) {
            if (item.getItemStatus() == 1) {
                finishCount++;
            }
            if (item.getItemStatus() == 0) {
                todoCount++;
            }
        }
        
        //
        if (todoCount == 0 && finishCount == 0) {
            //通过普查整治任务单idx查询普查整治任务单对象
            ZbglPczzWI zbglPczzWI = zbglPczzWIManager.getModelById(zbglPczzWiItem.getZbglPczzWiIDX());
            zbglPczzWI.setWIStatus(ZbglPczzWI.STATUS_CHECKED);
            zbglPczzWIManager.saveOrUpdate(zbglPczzWI);
            
//          断该普查整治任务单是否完成，完成的话，修改普查整治计划
            ZbglPczz zbglPczz = zbglPczzManager.getModelById(zbglPczzWI.getZbglPczzIDX());
            zbglPczz.setWorkStatus(1);//普查整治计划为已完成
            zbglPczzManager.saveOrUpdate(zbglPczz);
        }
    }

    /**
     * <li>说明：普查整治任务项完工
     * <li>创建人：林欢
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人员
     * @param zbglPczzWiItemIDX 普查整治任务项idx
     * @param itemResualt 普查整治任务项结果
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    private void finishZbglPczzWiItem(Long operatorid,String zbglPczzWiItemIDX,String itemResualt) throws BusinessException, NoSuchFieldException {
        //通过普查整治任务项idx
        ZbglPczzWiItem zbglPczzWiItem = this.getModelById(zbglPczzWiItemIDX);
        
//      通过普查整治任务项idx查询数据中的状态是否已经修改
        if (zbglPczzWiItem.getItemStatus() > 0 ) {
            throw new BusinessException("当条数据正在处理中，请刷新确认！");
        }
        
        zbglPczzWiItem.setItemResualt(itemResualt);//普查整治任务项结果
        zbglPczzWiItem.setItemStatus(1);//设置普查整治任务项完成
        zbglPczzWiItem.setItemTime(new Date());//设置普查整治任务项完成时间
        //通过操作人员idx查询班组信息
        OmOrganization org = omOrganizationManager.findByOperator(operatorid);
        zbglPczzWiItem.setHandleOrgID(org.getOrgid());//班组idx
        zbglPczzWiItem.setHandleOrgName(org.getOrgname());//班组名称
        
        OmEmployee omEmployee = SystemContext.getOmEmployee();
        zbglPczzWiItem.setHandlePersonIDX(omEmployee.getOperatorid());//作业人idx
        zbglPczzWiItem.setHandlePersonName(omEmployee.getEmpname());//作业人名称
        
        this.saveOrUpdate(zbglPczzWiItem);
        
        Boolean flag = true;
        //当不存在未完毕的普查整治任务项，但是有完毕的普查整治任务项时候，修改普查整治任务单状态为已处理
        //封装查询参数
        ZbglPczzWiItem params = new ZbglPczzWiItem();
        params.setZbglPczzWiIDX(zbglPczzWiItem.getZbglPczzWiIDX());
        
        List<ZbglPczzWiItem> zbglPczzWiItemList = findZbglPczzWiItemListByStatus(params);
        for (ZbglPczzWiItem item : zbglPczzWiItemList) {
            if (item.getItemStatus() != 2) {
                //不需要跟新普查整治任务单状态
                flag = false;
            }
        }
        
        ZbglPczzWI zbglPczzWI = zbglPczzWIManager.getModelById(zbglPczzWiItem.getZbglPczzWiIDX());
        //如果是待处理状态，修改为处理中
        if (ZbglPczzWI.STATUS_TODO.equals(zbglPczzWI.getWIStatus())) {
            zbglPczzWI.setWIStatus(ZbglPczzWI.STATUS_HANDLING);
        }
        if (flag) {
            //通过普查整治任务单idx查询普查整治任务单对象
            zbglPczzWI.setWIStatus(ZbglPczzWI.STATUS_HANDLED);
        }
        zbglPczzWIManager.saveOrUpdate(zbglPczzWI);
    }

    /**
     * <li>说明：通过普查整治任务单idx和传递状态，查询普查整治任务项
     * <li>创建人：林欢
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglPczzWiIDX 普查整治任务单idx
     * @return List<ZbglPczzWiItem> 普查整治任务项list
     */
    @SuppressWarnings("unchecked")
    public List<ZbglPczzWiItem> findZbglPczzWiItemListByStatus(ZbglPczzWiItem zbglPczzWiItem) {
        
        StringBuffer sb = new StringBuffer();
        
        sb.append(" from ZbglPczzWiItem a where a.zbglPczzWiIDX = '").append(zbglPczzWiItem.getZbglPczzWiIDX()).append("'");
        
        return (List<ZbglPczzWiItem>) this.find(sb.toString());
    }
    
}