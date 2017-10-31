package com.yunda.zb.pczz.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.zb.pczz.entity.ZbglPczzItem;
import com.yunda.zb.pczz.entity.ZbglPczzItemToTraininfo;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczzItem业务类,普查整治计划项
 * <li>创建人：王利成
 * <li>创建日期：2015-03-04
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbglPczzItemManager")
public class ZbglPczzItemManager extends JXBaseManager<ZbglPczzItem, ZbglPczzItem>{
    
    /**ZbglPczzItemToTraininfoManager业务类,普查整治项中保存的机车信息**/
    @Resource
    private ZbglPczzItemToTraininfoManager zbglPczzItemToTraininfoManager;

    /**
     * <li>说明：逻辑删除普查整治任务项
     * <li>创建人：林欢
     * <li>创建日期：2016-6-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 批量删除普查整治项idx数组
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws Exception
     */
	public void deleteZbglPczzItem(String[] ids) throws BusinessException, NoSuchFieldException {
		for (String idx : ids) {
            
            Map<String, String> map = new HashMap<String, String>();
            map.put("idx", idx);
			//通过普查整治项idx查询与其对应的车号
            List<ZbglPczzItemToTraininfo> zbglPczzItemToTraininfoList = zbglPczzItemToTraininfoManager.findZbglPczzItemToTraininfoListByPczzItemID(map);
            
            //清空这些关联车号
            for (ZbglPczzItemToTraininfo traininfo : zbglPczzItemToTraininfoList) {
				this.daoUtils.remove(traininfo);
			}
            
            //逻辑删除普查整治项
            this.logicDelete(idx);
        }
	}

    /**
     * <li>说明：查询整备普查整治计划项
     * <li>创建人：林欢
     * <li>创建日期：2016-8-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTpyeShortName 车型
     * @return List<String> 整备普查整治计划项list
     */
    public List<String> findAllZbglPczzItem(String trainTpyeShortName) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select b.item_name ");
        sb.append(" from zb_zbgl_pczz_item_to_traininfo a, zb_zbgl_pczz_item b ");
        sb.append(" where a.zbgl_pczz_item_idx = b.idx ");
        sb.append(" and a.train_type_shortname = '").append(trainTpyeShortName).append("'");
        sb.append(" group by b.item_name ");
        return this.getDaoUtils().executeSqlQuery(sb.toString());
    }

    /**
     * <li>说明：查询整备普查整治导出数据
     * <li>创建人：林欢
     * <li>创建日期：2016-8-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglPczzItemNameList 动态项list
     * @param trainTpyeShortName 车型
     * @param zbglPczzID 普查整治idx
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, String>> findDynamicZbglPczzWiItemList(List<String> zbglPczzItemNameList, String trainTpyeShortName, String zbglPczzID) {
        
        List<Map<String, String>> returnList = new ArrayList<Map<String,String>>();
        
        //结果集
        ScrollableResults srs = null;
        StringBuffer sb = new StringBuffer();
        sb.append(" select a.train_no, ");
        
        //动态拼接普查整治项idx
        for (String itemName : zbglPczzItemNameList) {
            sb.append(" ( ");
            sb.append(" select y.item_resualt ");
            sb.append(" from zb_zbgl_pczz_wi x, zb_zbgl_pczz_wi_item y ");
            sb.append(" where x.record_status = 0 ");
            sb.append(" and x.idx = y.zbgl_pczz_wi_idx ");
            sb.append(" and y.idx = b.idx ");
            sb.append(" and y.item_name is not null ");
            sb.append(" and y.item_name = '").append(itemName).append("'");
            sb.append(" ) as \"").append(itemName).append("\",");
        }
        
        sb.append(" to_char(b.item_time,'yyyy-mm-dd hh24:mi:ss'),b.handle_orgname,b.handle_person_name,b.check_person_name ");
        sb.append(" from zb_zbgl_pczz_wi a, zb_zbgl_pczz_wi_item b ");
        sb.append(" where a.record_status = 0 ");
        sb.append(" and a.idx = b.zbgl_pczz_wi_idx ");
        sb.append(" and a.idx = b.zbgl_pczz_wi_idx ");
        sb.append(" and a.train_type_shortname = '").append(trainTpyeShortName).append("'");
        sb.append(" and a.zbgl_pczz_idx = '").append(zbglPczzID).append("'");
        
        //定义变量
        Integer number = 0;
        Query query = this.getDaoUtils().getSessionFactory().openSession().createSQLQuery(sb.toString());
        srs = query.scroll();
        
        Map<String, String> map = null;
        while (srs.next()){
            
            //判断是否为null
            if (!returnList.isEmpty()) {
                Boolean flag = true;
                for (Map<String, String> listMap : returnList) {
                    //判断list中的map是否有该车号
                    if (srs.get(number).toString().equals(listMap.get("trainNo"))) {
                        //如果相等，取出该map对象
                        map = listMap;
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    map = new HashMap<String, String>();
                    returnList.add(map);
                }
            }else {
                map = new HashMap<String, String>();
                returnList.add(map);
            }
            
            map.put("trainTypeShortname", trainTpyeShortName);
            map.put("trainNo", srs.get(number).toString());
            number++;
            
            //动态获取值
            for (String itemName : zbglPczzItemNameList) {
                if (map.get(itemName) != null && !" ".equals(map.get(itemName))) {
                    number++;
                }else {
                    map.put(itemName, srs.get(number) == null ? " " : srs.get(number).toString());
                    number++;
                }
            }
            map.put("itemTime", srs.get(number) == null ? " " : srs.get(number).toString());
            number++;
            map.put("handleOrgname", srs.get(number) == null ? " " : srs.get(number).toString());
            number++;
            map.put("handlePersonName", srs.get(number) == null ? " " : srs.get(number).toString());
            number++;
            map.put("checkPersonName", srs.get(number) == null ? " " : srs.get(number).toString());
            number = 0;
        }

        return returnList;
    }
}