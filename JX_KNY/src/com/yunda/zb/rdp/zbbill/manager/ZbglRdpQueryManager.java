package com.yunda.zb.rdp.zbbill.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车整备单查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2016-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@Service(value = "zbglRdpQueryManager")
public class ZbglRdpQueryManager extends JXBaseManager<ZbglRdp, ZbglRdp>{
    
    @Resource
    private ZbglRdpNodeQueryManager zbglRdpNodeQueryManager;
    
    /**
     * <li>说明：分页查询
     * <li>创建人：程锐
     * <li>创建日期：2016-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 包装了实体类查询条件的对象
     * @return Page<ZbglRdp> 分页查询列表
     * @throws BusinessException
     */
    @Override
    public Page<ZbglRdp> findPageList(SearchEntity<ZbglRdp> searchEntity) throws BusinessException {
        ZbglRdp entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder("From ZbglRdp Where recordStatus = 0");
        if (!StringUtil.isNullOrBlank(entity.getRdpStatus())) {
            sb.append(" And rdpStatus In ('").append(entity.getRdpStatus().replace(",", "','")).append("')");
        }
        // 根据“车型”“车号”模糊匹配查询
        String trainNo = StringUtil.nvl(entity.getTrainNo(), "").trim();
        if (!StringUtil.isNullOrBlank(trainNo)) {
            sb.append(" And trainTypeShortName || trainNo Like '%").append(trainNo.toUpperCase()).append("%'");
        }
        // 以“整备开始时间”进行升序排序
        sb.append(" Order By rdpStatus DESC,rdpStartTime DESC");
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        Page<ZbglRdp> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
        List<ZbglRdp> list = page.getList();
        for (ZbglRdp rdp : list) {
            // 查询机车整备单下属第一层级的作业节点实体
            try {
                rdp.setZbglRdpNodes(zbglRdpNodeQueryManager.getFirstNodeList(rdp.getIdx()));                
            } catch (Exception e) {
                throw new BusinessException(e);
            }            
        }
        return page;
    }
    
    /**
     * <li>说明：通过机车查询整备作业编辑
     * <li>创建人：张迪
     * <li>创建日期：2016-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 包装了实体类查询条件的对象
     * @return 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
	public Page<ZbglRdp> findListByTrain(SearchEntity<ZbglRdp> searchEntity) throws BusinessException {
        ZbglRdp entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder("From ZbglRdp Where recordStatus = 0");
        if (!StringUtil.isNullOrBlank(entity.getRdpStatus())) {
            sb.append(" And rdpStatus ='").append(entity.getRdpStatus()).append("'");
        }
        // 根据“车型”“车号”模糊匹配查询
        String trainNo = StringUtil.nvl(entity.getTrainNo(), "").trim();
        if (!StringUtil.isNullOrBlank(trainNo)) {
            sb.append(" And trainTypeShortName || trainNo Like '%").append(trainNo.toUpperCase()).append("%'");
        }
        // 以“整备开始时间”进行升序排序
        sb.append(" Order By rdpStartTime DESC");
        String hql = sb.toString();
        List<ZbglRdp> list = this.daoUtils.find(hql);
        
        Page<ZbglRdp> page = new Page<ZbglRdp>(0, list);
        List<ZbglRdp> entityList = new ArrayList<ZbglRdp>();
        if (null == list || list.isEmpty()) {
        	return page;
        }
        // 只取最近的一条记录
        ZbglRdp rdp = list.get(0);
        try {
			rdp.setZbglRdpNodes(zbglRdpNodeQueryManager.getFirstNodeList(rdp.getIdx()));
		} catch (Exception e) {
			throw new BusinessException(e);
		}         
        entityList.add(rdp);
        page.setList(entityList);
        page.setTotal(1);
        return page;
    }
  
}
