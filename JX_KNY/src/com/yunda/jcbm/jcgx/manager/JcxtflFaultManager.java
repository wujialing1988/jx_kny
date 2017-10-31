package com.yunda.jcbm.jcgx.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yunda.common.BusinessException;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.jcbm.jcgx.entity.JcxtflFault;
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;

/**
 * <li>类型名称：分类编码故障现象业务类
 * <li>说明：
 * <li>创建人： 何东
 * <li>创建日期：2016-5-16
 * <li>修改人： 
 * <li>修改日期：
 */
@Service(value="jcxtflFaultManager")
public class JcxtflFaultManager  extends AbstractOrderManager<JcxtflFault, JcxtflFault> {
    
    /** CodeRuleConfig业务类,业务编码规则配置 */
    @Resource
    private CodeRuleConfigManager codeRuleConfigManager;
	/**
	 * <li>方法名：保存分类编码的故障现象
	 * <li>@param flbm 分类编码
	 * <li>@param faultId 故障编号
	 * <li>@param faultName 故障名称
	 * <li>@return
	 * <li>返回类型：Map<String,Object>
	 * <li>说明：
	 * <li>创建人：何东
	 * <li>创建日期：2016-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveFlbmFault(JcxtflFault[] faultList) throws BusinessException, NoSuchFieldException{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		
		for (JcxtflFault fault : faultList) {
			String flbm = fault.getFlbm();
		    String faultName = fault.getFaultName();
		    
		    if (StringUtils.isNotBlank(flbm) && StringUtils.isNotBlank(faultName)) {
				String hql = " from JcxtflFault where flbm = ? and faultName = ?";
			
				// 查询数据
		        List<JcxtflFault> list = (List<JcxtflFault>) this.daoUtils.find(hql, new Object[]{flbm, faultName});
		        if (!CollectionUtils.isEmpty(list)) {
		        	map.put("success", false);
		        	map.put("errMsg", "该分类编码的“" + faultName + "”已经存在");
		        	return map;
		        }
			} else {
				map.put("success", false);
	        	map.put("errMsg", "保存所需参数不完整");
	        	return map;
			}
		}
		
		for (JcxtflFault fault : faultList) {
            this.validateUpdate(fault);
//			this.daoUtils.saveOrUpdate(fault);
            super.saveOrUpdate(fault);
		}
        
        return map;
	}
    
     /**
     * <li>说明：删除
     * <li>创建人：张迪
     * <li>创建日期：2016-09-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容： 
     * @param Serializable... ids id集合
     * @throws BusinessException 
     */
    public void deleteByIds(Serializable... ids) throws BusinessException {       
        for (Serializable idx : ids) {
            JcxtflFault entity = this.getModelById(idx);        
            try {
                super.deleteByIds(idx);
                this.updateSort(this.findAll(entity));
            } catch (NoSuchFieldException e) {
                throw new BusinessException(e.getMessage());
            }
        }
    }
      /**
     * <li>说明：查询当前对象在数据库中的记录总数
     * <li>创建人：张迪
     * <li>创建日期：2016-09-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param t 实体对象
     * @return int 记录总数
     */
    @Override
    public int count(JcxtflFault entity) {
        StringBuilder sb = new StringBuilder(" from JcxtflFault WHERE flbm = '");
        sb.append(entity.getFlbm()).append("' ");   
        return this.daoUtils.getCount(sb.toString());
    }
  
    /**
     * <li>说明：获取排序范围内的同类型的所有记录
     * <li>创建人：张迪
     * <li>创建日期：2016-09-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 实体对象
     * @return List<UnionNode> 结果集
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<JcxtflFault> findAll(JcxtflFault t) {
        String hql = " From JcxtflFault Where  flbm = ? ";
      return this.daoUtils.find(hql, new Object[] { t.getFlbm()});
    }
    
    /**
     * <li>说明：置底
      * <li>创建人：张迪
     * <li>创建日期：2016-09-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveBottom(String idx) throws Exception {
        JcxtflFault entity = this.getModelById(idx);
        int count = this.count(entity);
        // 获取被【置底】记录被置底前，在其后的所有记录
        String hql = "From JcxtflFault Where  seqNo > ? And flbm = ? ";
        List<JcxtflFault> list = this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getFlbm()});
        // 设置被【置底】记录的排序号为当前记录总数
        entity.setSeqNo(count);
        List<JcxtflFault> entityList = new ArrayList<JcxtflFault>();
        entityList.add(entity);
        // 设置其后的所有记录的排序后依次减一
        for (JcxtflFault jcxtflFault : list) {
            jcxtflFault.setSeqNo(jcxtflFault.getSeqNo() - 1);
            entityList.add(jcxtflFault);
        }
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：下移
      * <li>创建人：张迪
     * <li>创建日期：2016-09-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    public void updateMoveDown(String idx) throws Exception {
        JcxtflFault entity = this.getModelById(idx);
        String hql = "From JcxtflFault Where  seqNo = ? And flbm = ? ";
        // 获取被【下移】记录被下移前，紧随其后的记录
        JcxtflFault nextEntity =
            (JcxtflFault) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() + 1, entity.getFlbm()});
        List<JcxtflFault> entityList = new ArrayList<JcxtflFault>(2);
        // 设置被【下移】记录的排序号+1
        entity.setSeqNo(entity.getSeqNo() + 1);
        entityList.add(entity);
        // 设置被【下移】记录后的记录的排序号-1
        nextEntity.setSeqNo(nextEntity.getSeqNo() - 1);
        entityList.add(nextEntity);
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：置顶
      * <li>创建人：张迪
     * <li>创建日期：2016-09-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveTop(String idx) throws Exception {
        JcxtflFault entity = this.getModelById(idx);
        // 获取被【置顶】记录被置顶前，在其前的所有记录
        String hql = "From JcxtflFault Where  seqNo < ? And flbm = ? ";
        List<JcxtflFault> list = this.daoUtils.find(hql, new Object[] { null == entity.getSeqNo()?1:entity.getSeqNo(), entity.getFlbm()});
        // 设置被【置顶】记录的排序号为1
        entity.setSeqNo(1);
        List<JcxtflFault> entityList = new ArrayList<JcxtflFault>();
        entityList.add(entity);
        // 设置其后的所有记录的排序后一次加一
        for (JcxtflFault jcxtflFault : list) {
            jcxtflFault.setSeqNo(jcxtflFault.getSeqNo() + 1);
            entityList.add(jcxtflFault);
        }
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：上移
      * <li>创建人：张迪
     * <li>创建日期：2016-09-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    public void updateMoveUp(String idx) throws Exception {
        JcxtflFault entity = this.getModelById(idx);
        String hql = "From JcxtflFault Where seqNo = ? And flbm = ? ";
        // 获取被【上移】记录被上移移前，紧随其前的记录
        JcxtflFault nextEntity =
            (JcxtflFault) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() - 1, entity.getFlbm()});
        List<JcxtflFault> entityList = new ArrayList<JcxtflFault>(2);
        // 设置被【上移】记录的排序号-1
        entity.setSeqNo(entity.getSeqNo() - 1);
        entityList.add(entity);
        // 设置被【上移】记录前的记录的排序号+1
        nextEntity.setSeqNo(nextEntity.getSeqNo() + 1);
        entityList.add(nextEntity);
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>获取已知作业节点之后的所有同级作业节点
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 作业节点实体对象
     * @return 实体集合
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<JcxtflFault> findAllBySN(JcxtflFault t) throws Exception {
        String hql = "From JcxtflFault Where  seqNo >= ? And flbm = ?";
        return this.daoUtils.find(hql, new Object[] { t.getSeqNo(), t.getFlbm()});
    }

    /**
     * <li>说明：验证故障名称是否重复，生成故障现象编码
     * <li>创建人：张迪
     * <li>创建日期：2016-9-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 故障现象封装实体
     * @return 提示信息
     */
    @SuppressWarnings("unchecked")
    public String[] validateInsert(JcxtflFault entity) {
        String hql = " from JcxtflFault where flbm = ? and faultName = ?";      
        // 查询数据
        List<JcxtflFault> list = (List<JcxtflFault>) this.daoUtils.find(hql, new Object[]{entity.getFlbm(), entity.getFaultName()});
        if (!CollectionUtils.isEmpty(list)) {
            return new String[]{ "该分类编码的【" + entity.getFaultName() + "】已经存在"};
        } else{
            //  根据业务编码规则自动生成“故障编码”
            entity.setFaultId(codeRuleConfigManager.makeConfigRule(JcxtflFault.FAULT_ID_DEF));      
            return null;
        }
        
    }
    

}
