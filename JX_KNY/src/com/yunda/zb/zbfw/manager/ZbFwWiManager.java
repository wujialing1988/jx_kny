package com.yunda.zb.zbfw.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.zbfw.entity.ZbFwWi;
import com.yunda.zb.zbfw.entity.ZbFwWidi;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbFwWi业务类,整备作业项目
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbFwWiManager")
public class ZbFwWiManager extends AbstractOrderManager<ZbFwWi, ZbFwWi>{
    /** ZbFwWidiManager业务类，整备作业项目数据项 */
    @Resource
    private ZbFwWidiManager zbFwWidiManager ;
	/**
	 * <li>说明：整备范围作业项模糊查询
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity	查询封装实体
	 * @return 分页列表
	 */
   public Page<ZbFwWi> getZbFwWiList(SearchEntity<ZbFwWi> searchEntity){
		 String selectHql = "select new ZbFwWi(a.idx, a.zbfwIDX, a.wICode, a.wIName, a.wIDesc, a.seqNo)";
			String fromHql = " from ZbFwWi a, ZbFw b where a.zbfwIDX = '"+searchEntity.getEntity().getFwidx()+"' and a.recordStatus = 0 and b.recordStatus = 0";
		    StringBuffer awhere =  new StringBuffer(); 
			if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getWIName())){
	            awhere.append(" and a.wIName like '%").append(searchEntity.getEntity().getWIName()).append("%' ");
	        }
			if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getWIDesc())){
	            awhere.append(" and a.wIDesc like '%").append(searchEntity.getEntity().getWIDesc()).append("%' ");
	        }
			String totalHql = "select count(*) " + fromHql;		
			totalHql += awhere;
			String hql = selectHql + fromHql + awhere;
			return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	 } 
   
	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 实体对象
	 * @return int
	 */
	public int count(ZbFwWi t) {
		String hql = "Select Count(*) From ZbFwWi Where recordStatus = 0 And zbfwIDX = ? and nodeIDX = ? ";
		return this.daoUtils.getCount(hql, new Object[]{t.getZbfwIDX(),t.getNodeIDX()});
	}
    
	/**
	 * <li>说明：获取排序范围内的同类型的所有记录
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 实体对象
	 * @return  List<ZbFwWi>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ZbFwWi> findAll(ZbFwWi t) {
		String hql = "From ZbFwWi Where recordStatus = 0 And zbfwIDX = ? and nodeIDX = ? ";
		return this.daoUtils.find(hql, new Object[]{t.getZbfwIDX(), t.getNodeIDX()});
	}
	
	/**
	 * <li>说明：置底
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx			被排序记录的idx主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveBottom(String idx) throws Exception {
		ZbFwWi entity = this.getModelById(idx);
		int count = this.count(entity);
		// 获取被【置底】记录被置底前，在其后的所有记录
		String hql = "From ZbFwWi Where recordStatus = 0 And seqNo > ? And nodeIDX = ?";
		List<ZbFwWi> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getNodeIDX()});
		// 设置被【置底】记录的排序号为当前记录总数
		entity.setSeqNo(count);
		List<ZbFwWi> entityList = new ArrayList<ZbFwWi>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后依次减一
		for (ZbFwWi recordCard : list) {
			recordCard.setSeqNo(recordCard.getSeqNo() - 1);
			entityList.add(recordCard);
		}
		this.saveOrUpdate(entityList);
	}
    
	/**
	 * <li>说明：下移
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx			被排序记录的idx主键
	 * @throws Exception
	 */
	public void updateMoveDown(String idx) throws Exception {
		ZbFwWi entity = this.getModelById(idx);
		String hql = "From ZbFwWi Where recordStatus = 0 And seqNo = ? And nodeIDX = ?";
		// 获取被【下移】记录被下移前，紧随其后的记录
		ZbFwWi nextEntity = (ZbFwWi)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() + 1, entity.getNodeIDX()});
		List<ZbFwWi> entityList = new ArrayList<ZbFwWi>(2);
		// 设置被【下移】记录的排序号+1
		entity.setSeqNo(entity.getSeqNo() + 1);
		entityList.add(entity);
		// 设置被【下移】记录后的记录的排序号-1
		nextEntity.setSeqNo(nextEntity.getSeqNo() - 1);
		entityList.add(nextEntity);
		this.saveOrUpdate(entityList);
	}
     
	/**
	 * <li>说明：置顶
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx			被排序记录的idx主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveTop(String idx) throws Exception {
		ZbFwWi entity = this.getModelById(idx);
		// 获取被【置顶】记录被置顶前，在其前的所有记录
		String hql = "From ZbFwWi Where recordStatus = 0 And seqNo < ? And nodeIDX = ?";
		List<ZbFwWi> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getNodeIDX()});
		// 设置被【置顶】记录的排序号为1
		entity.setSeqNo(1);
		List<ZbFwWi> entityList = new ArrayList<ZbFwWi>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后一次加一
		for (ZbFwWi recordCard : list) {
			recordCard.setSeqNo(recordCard.getSeqNo() + 1);
			entityList.add(recordCard);
		}
		this.saveOrUpdate(entityList);
	}
    
	/**
	 * <li>说明：上移
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx			被排序记录的idx主键
	 * @throws Exception
	 */
	public void updateMoveUp(String idx) throws Exception {
		ZbFwWi entity = this.getModelById(idx);
		String hql = "From ZbFwWi Where recordStatus = 0 And seqNo = ? And nodeIDX = ?";
		// 获取被【上移】记录被上移移前，紧随其前的记录
		ZbFwWi nextEntity = (ZbFwWi)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() - 1, entity.getNodeIDX()});
		List<ZbFwWi> entityList = new ArrayList<ZbFwWi>(2);
		// 设置被【上移】记录的排序号-1
		entity.setSeqNo(entity.getSeqNo() - 1);
		entityList.add(entity);
		// 设置被【上移】记录前的记录的排序号+1
		nextEntity.setSeqNo(nextEntity.getSeqNo() + 1);
		entityList.add(nextEntity);
		this.saveOrUpdate(entityList);
	}
    
	/**
	 * <li>说明：获取指定顺序号(包含)后的所有实体，即：如参数t的顺序号为n，则获取该实体排序范围内，所有顺序号>=n的实体列表
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 指定顺序号的实体
	 * @return List<ZbFwWi>
	 * @throws Exception TODO
	 */
	@SuppressWarnings("unchecked")
	public List<ZbFwWi> findAllBySN(ZbFwWi t) throws Exception {
		String hql = "From ZbFwWi Where recordStatus = 0 And zbfwIDX = ? And seqNo >= ?";
		return this.daoUtils.find(hql, new Object[]{t.getZbfwIDX(), t.getSeqNo()});
	

	}
     /**
     * <li>说明：根据作业流程节点主键获取作业项目实体对象集
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 作业流程节点主键
     * @return 实体对象
     */
    @SuppressWarnings("unchecked")
    public List<ZbFwWi> getModelByNodeIDX(Serializable nodeIDX) {
        String hql = "From ZbFwWi Where recordStatus = 0 And nodeIDX = ?";
        return this.daoUtils.find(hql, new Object[]{nodeIDX});
    }
    /**
     * <li>说明：逻辑删除作业项目和数据项
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityList 要删除的实体集
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @Override
    public void logicDelete(List<ZbFwWi> entityList) throws BusinessException, NoSuchFieldException {
        
        for (ZbFwWi project : entityList) {
            // 级联逻辑删除关联的数据项
            List<ZbFwWidi> diList = this.zbFwWidiManager.getModelByZbfwwiIDX(project.getIdx());
            if (null != diList && diList.size() > 0) {
                this.zbFwWidiManager.logicDelete(diList);
            }
        }
        super.logicDelete(entityList);
    }
    /**
     * <li>说明：逻辑删除作业项目和数据项
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 要删除的实体主键数组
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @Override
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        for (Serializable idx : ids) {
            //级联逻辑删除关联的数据项
            List<ZbFwWidi> diList = this.zbFwWidiManager.getModelByZbfwwiIDX(idx);
            if (null != diList && diList.size() > 0) {
                this.zbFwWidiManager.logicDelete(diList);
            }
            // 逻辑删除
            this.logicDelete(idx);
        }
    }
    
    
	/**
	 * <li>说明：通过整备范围ID查询 专业
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 实体对象
	 * @return  List<ZbFwWi>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ZbFwWi> findWiByFw(String zbfwIdx) {
		String hql = "From ZbFwWi Where recordStatus = 0 And zbfwIDX = ? ";
		return this.daoUtils.find(hql, new Object[]{zbfwIdx});
	}
}