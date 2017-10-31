package com.yunda.jxpz.orgdic.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.partsBase.warehouse.entity.Warehouse;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jxpz.orgdic.entity.OrgDicItem;
import com.yunda.jxpz.orgdic.entity.PartsTakeOverOrg;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsTakeOverOrg业务类,接收部门
 * <li>创建人：程梅
 * <li>创建日期：2016年6月2日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsTakeOverOrgManager")
public class PartsTakeOverOrgManager extends JXBaseManager<PartsTakeOverOrg, PartsTakeOverOrg>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /** 常用部门字典项业务类 */
    @Resource
    private OrgDicItemManager orgDicItemManager ;
    /**
     * 
     * <li>说明：查询接收部门列表【包含配件周转常用部门和库房列表】
     * <li>创建人：程梅
     * <li>创建日期：2016-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 分页查询开始行数
     * @param limit 该页最大记录总数
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<PartsTakeOverOrg> findTakeOverOrgList(Integer start, Integer limit){
        List<PartsTakeOverOrg> orgList = new ArrayList<PartsTakeOverOrg>();
        PartsTakeOverOrg org ;
        //获取当前登录操作员
        OmEmployee emp = SystemContext.getOmEmployee();
        //查询库房列表
        StringBuffer whHql = new StringBuffer("From Warehouse where recordStatus=0 and status=").append(Warehouse.STATUS_USE)
        .append(" and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='").append(emp.getEmpid()).append("')");
        List<Warehouse> whList = (List<Warehouse>)daoUtils.find(whHql.toString());
        if(null != whList && whList.size() > 0){
            for(Warehouse wh : whList){
                org = new PartsTakeOverOrg();
                org.setOrgId(wh.getIdx());
                org.setOrgName(wh.getWareHouseName());
                org.setOrgSeq("");
                org.setOrgType(PartsAccount.MANAGE_DEPT_TYPE_WH);//接收部门类型为库房
                orgList.add(org);
            }
        }
        //查询配件周转常用部门
        SearchEntity<OrgDicItem> searchEntity = new SearchEntity<OrgDicItem>(new OrgDicItem(), start, limit, null);
        List<OrgDicItem> itemList = this.orgDicItemManager.getAccountOrgList(searchEntity).getList();
        if(null != itemList && itemList.size() > 0){
            for(OrgDicItem item : itemList){
                org = new PartsTakeOverOrg();
                org.setOrgId(item.getId().getOrgId());
                org.setOrgName(item.getOrgName());
                org.setOrgSeq(item.getOrgSeq());
                org.setOrgType(PartsAccount.MANAGE_DEPT_TYPE_ORG);//接收部门类型为机构
                orgList.add(org);
            }
        }
        if(null != orgList && orgList.size() > 0) return orgList ;
        else return null ;
    }
}