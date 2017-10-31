package com.yunda.jxpz.coderule.manager;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.coderule.entity.CodeRuleConfigProp;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：CodeRuleConfigProp业务类,业务编码规则配置属性
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-10-09
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="codeRuleConfigPropManager")
public class CodeRuleConfigPropManager extends JXBaseManager<CodeRuleConfigProp, CodeRuleConfigProp>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * <li>说明：判定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return true使用查询缓存，false不使用
	 */
	@Override
	protected boolean enableCache(){
		return true;
	}	
	/**
	 * <li>说明：查询符合条件的业务编码规则属性信息
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-17
	 * <li>修改人： 
	 * <li>修改日期：2013-11-17
	 * <li>修改内容：增加查询缓存
	 * @return true使用查询缓存，false不使用
	 */	
	@SuppressWarnings({"unchecked" })
	public Page<CodeRuleConfigProp> findPageList(final SearchEntity<CodeRuleConfigProp> searchEntity) throws BusinessException{
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
		return (Page<CodeRuleConfigProp>)template.execute(new HibernateCallback(){
			public Page<CodeRuleConfigProp> doInHibernate(Session s){
				try {
					CodeRuleConfigProp entity = searchEntity.getEntity();
					//过滤掉idx的查询条件
					Example exp = Example.create(entity)
						.excludeProperty(EntityUtil.IDX);
					Criteria criteria = s.createCriteria(entity.getClass())
						.add(exp);
					criteria.setCacheable(enableCache());
					//查询总记录数
					int total = ((Integer)criteria
						.setProjection(Projections.rowCount())
						.uniqueResult())
						.intValue();
					//分页列表
					criteria = s.createCriteria(entity.getClass()).add(exp)
						.setFirstResult(searchEntity.getStart())
						.setMaxResults(searchEntity.getLimit());
					//设置排序规则
					Order[] orders = searchEntity.getOrders();
					if(orders != null){
						for (Order order : orders) {
							criteria.addOrder(order);
						}					
					}
					criteria.addOrder(Order.asc("orderNo"));
					criteria.setCacheable(enableCache());
					return new Page<CodeRuleConfigProp>(total, criteria.list());
				} catch (Exception e) {
					ExceptionUtil.process(e,logger);
				}
				return null;
			}
		});
    }
	/**
	 * 保存记录
	 */
	public void saveOrUpdate(CodeRuleConfigProp configpro) throws BusinessException, NoSuchFieldException {
//		判断实体类idx主键，若为“”空白字符串，设置实体类主键idx为null
		String idx = (String)BeanUtils.forceGetProperty(configpro, "idx");
		idx = StringUtil.nvlTrim(idx, null);
		BeanUtils.forceSetProperty(configpro, "idx", idx);
		this.daoUtils.getHibernateTemplate().saveOrUpdate(configpro);
	}

}