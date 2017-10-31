package com.yunda.sb.base.combo.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.sb.base.combo.ILogicDelete;
import com.yunda.sb.base.combo.IPyFilter;
import com.yunda.util.DaoUtils;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: DynamicCombo，Ext-js 动态组件combo查询辅助类
 * <li>创建人：张凡
 * <li>创建日期：2015年7月20日
 * <li>修改人：何涛
 * <li>修改日期：2016年10月14日
 * <li>修改内容：增加动态下拉框控件[xtype : singlefieldcombo]，对应数据源方法：singleFieldDataSource()
 * <li>修改内容：增加动态下拉框控件[xtype : simpleEntitycombo]，对应数据源方法：simpleEntityDataSource()
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@SuppressWarnings("serial")
public class DynamicComboAction{

	/** 框架中的数据库Hibernate访问操作工具，可供所有的业务服务对象都调用此对象完成数据库的操作 */
	@Resource
	private DaoUtils daoUtils;

	/**
	 * <li>说明：分页查询下拉框数据源，获取指定实体的所有表记录，如果查询的实体entity实现了IPyFilter.java接口，该接口还支持拼音首字母过滤的功能
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月14日
	 * <li>修改人：黄杨
	 * <li>修改内容：由设备系统移植转为Struts2请求方式
	 * <li>修改日期：2017年5月3日
	 * @param req http请求对象
	 * @param entity 指定实体的全路径类名称，如：【com.yunda.sbgl.certificate.entity.CertificateClass】
	 * @param start 分页开始索引
	 * @param limit 分页结束索引
	 * @return 分页查询结果集，例如：
	 * {
	 * 	"id":"idx",
	 * 	"totalProperty":6,
	 * 	"root":[{"idx":"8a8284c957b2bb6c0157b2bc86700000","certificateNamePY":"cyzgz","awardInstitutionPY":"cdyd","validPeriod":4,"certificateType":"","certificateName":"从业资格证","awardInstitution":"成都运达"}]
	 * } 
	 * @throws Exception 
	 */
	@SuppressWarnings("all")
	public void simpleEntityDataSource() throws Exception {
		HttpServletRequest req = ServletActionContext.getRequest();
		String entity = req.getParameter("entity");
		int start = Integer.parseInt(req.getParameter("start"));
		int limit = Integer.parseInt(req.getParameter("limit"));
		// 构造查询语句
		StringBuilder sb = new StringBuilder();
		sb.append("From ").append(entity).append(" Where 0 = 0 ");

		// 动态过滤查询
		String query = req.getParameter("query");
		// 页面显示字段即为查询条件过滤目标字段
		String[] displayFields = JSONUtil.read(req.getParameter("displayFields"), String[].class);
		// 因为此处用到反射，所以需要入参entity需为类的全路径名称
		Class<?> clazz = Class.forName(entity);
		if (!StringUtil.isNullOrBlank(query)) {
			sb.append(" And (");
			for (int i = 0; i < displayFields.length; i++) {
				// 验证类型是否实现了IPyFilter接口
				if (IPyFilter.class.isAssignableFrom(clazz)) {
					String fieldName4PY = ((IPyFilter) clazz.newInstance()).getFieldName4PY(displayFields[i]);
					if (null != fieldName4PY && isCharacter(query)) {
						sb.append("(");
						sb.append(displayFields[i]).append(" Like '%").append(query).append("%'");
						sb.append(" Or Upper(");
						sb.append(fieldName4PY).append(") Like '%").append(query.toUpperCase()).append("%'");
						sb.append(")");
					} else {
						sb.append(displayFields[i]).append(" Like '%").append(query).append("%'");
					}
				} else {
					sb.append(displayFields[i]).append(" Like '%").append(query).append("%'");
				}
				if (i != displayFields.length - 1) {
					sb.append(" Or ");
				}
			}
			sb.append(")");
		}
		// 如果查询对象有逻辑删除标识，则不查询逻辑删除的对象
		if (ILogicDelete.class.isAssignableFrom(clazz) || EntityUtil.contains(clazz, EntityUtil.RECORD_STATUS)) {
			sb.append(" And recordStatus =").append(Constants.NO_DELETE);
		}
		// 拼接whereListJson查询条件
		this.processWhereList(sb, clazz, req);

		final int beginIdx = start < 0 ? 0 : start;
		final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;
		final String hql = sb.toString();
		final String totalHql = "Select Count(*) As rowcount " + sb.substring(sb.indexOf("From"));
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
		// 分页查询
		template.execute(new HibernateCallback() {
			public String doInHibernate(Session s) {
				Query q = null;
				try {
					q = s.createQuery(totalHql);
					q.setCacheable(false); // 缓存开关
					int total = ((Long) q.uniqueResult()).intValue();
					q.setCacheable(false);
					int begin = beginIdx > total ? total : beginIdx;
					q = s.createQuery(hql).setFirstResult(begin).setMaxResults(pageSize);
					q.setCacheable(false); // 缓存开关
					List list = q.list();
					// 构造查询结果集
					Page page = new Page(total, list);
					// 返回查询结果集
					JSONUtil.write(ServletActionContext.getResponse(), page.extjsStore());
				} catch (HibernateException e) {
					throw new BusinessException(e);
				} catch (IOException e) {
					throw new BusinessException(e);
				} finally {
					if (q != null) {
						q.setCacheable(false);
					}
				}
				return "1";
			}
		});
	}

	/**
	 * <li>说明：分页查询下拉框数据源，获取指定实体的某一个字段值，如果查询的实体entity实现了IPyFilter.java接口，该接口还支持拼音首字母过滤的功能
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月13日
	 * <li>修改人：黄杨
	 * <li>修改内容：由设备系统移植转为Struts2请求方式
	 * <li>修改日期：2017年5月3日
	 * @param req http请求对象
	 * @param entity 指定实体的全路径类名称，如：【com.yunda.sbgl.certificate.entity.CertificateClass】
	 * @param xfield 指定的查询的字段名称，如：“awardInstitution”
	 * @param start 分页开始索引
	 * @param limit 分页结束索引
	 * @return 分页查询结果集，例如：
	 * {"id":"awardInstitution","totalProperty":3,"root":[{"awardInstitution":"四川人事"},{"awardInstitution":"成都人事考试机构"}]}
	 * @throws Exception 
	 */
	@SuppressWarnings("all")
	public void singleFieldDataSource() throws Exception {
		HttpServletRequest req = ServletActionContext.getRequest();
		String entity = req.getParameter("entity");
		final String  xfield = req.getParameter("xfield");
		int start = Integer.parseInt(req.getParameter("start"));
		int limit = Integer.parseInt(req.getParameter("limit"));
		// 构造查询语句
		StringBuilder sb = new StringBuilder();
		sb.append(" Select Distinct ").append(xfield).append(" From ").append(entity).append(" Where ").append(xfield).append(" Is Not Null And Length(Trim(").append(xfield)
				.append(")) <> 0 ");

		// 动态过滤查询
		String query = req.getParameter("query");
		Class<?> clazz = Class.forName(entity);
		if (!StringUtil.isNullOrBlank(query)) {
			// 因为此处用到反射，所以需要入参entity需为类的全路径名称
			// 验证类型是否实现了IPyFilter接口
			if (IPyFilter.class.isAssignableFrom(clazz)) {
				String fieldName4PY = ((IPyFilter) clazz.newInstance()).getFieldName4PY(xfield);
				if (null != fieldName4PY && isCharacter(query)) {
					sb.append(" And Upper(").append(fieldName4PY).append(") Like '%").append(query.toUpperCase()).append("%'");
				} else {
					sb.append(" And ").append(xfield).append(" Like '%").append(query).append("%'");
				}
			} else {
				sb.append(" And ").append(xfield).append(" Like '%").append(query).append("%'");
			}
		}
		// 如果查询对象有逻辑删除标识，则不查询逻辑删除的对象
		if (ILogicDelete.class.isAssignableFrom(clazz) || EntityUtil.contains(clazz, EntityUtil.RECORD_STATUS)) {
			sb.append(" And recordStatus =").append(Constants.NO_DELETE);
		}
		// 拼接whereListJson查询条件
		this.processWhereList(sb, clazz, req);
		sb.append(" Order By ").append(xfield);

		final int beginIdx = start < 0 ? 0 : start;
		final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;
		final String hql = sb.toString();
		final String totalHql = "Select Count(distinct " + xfield + ") As rowcount " + sb.substring(sb.indexOf("From"));
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
		// 分页查询
		template.execute(new HibernateCallback() {
			public String doInHibernate(Session s) {
				Query q = null;
				try {
					q = s.createQuery(totalHql);
					q.setCacheable(false); // 缓存开关
					int total = ((Long) q.uniqueResult()).intValue();
					q.setCacheable(false);
					int begin = beginIdx > total ? total : beginIdx;
					q = s.createQuery(hql).setFirstResult(begin).setMaxResults(pageSize);
					q.setCacheable(false); // 缓存开关
					List list = q.list();

					// 构造查询结果集
					List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
					Map<String, Object> map = null;
					for (Object o : list) {
						map = new HashMap<String, Object>();
						map.put(xfield, o);
						result.add(map);
					}
					Page page = new Page(total, result);
					// 返回查询结果集
					Map extjsStore = page.extjsStore();
					extjsStore.put("id", xfield);
					JSONUtil.write(ServletActionContext.getResponse(), extjsStore);
				} catch (HibernateException e) {
					throw new BusinessException(e);
				} catch (IOException e) {
					throw new BusinessException(e);
				} finally {
					if (q != null) {
						q.setCacheable(false);
					}
				}
				return "1";
			}
		});
	}

	/**
	 * <li>说明：分页查询数据字典下拉框数据源
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月9日
	 * <li>修改人：黄杨
	 * <li>修改内容：2017年5月3日
	 * <li>修改日期：由设备系统移植转为Struts2请求方式
	 * @param req http请求对象
	 * @param start 分页开始索引
	 * @param limit 分页结束索引
	 * @param dictTypeID 数据字典类型id
	 * @return 分页查询结果集，例如：
	 * {"id":"idx","totalProperty":15,"root":[{"dictid":"e_sbjx_unit_1","dictname":"MΩ"}]}
	 */
	@SuppressWarnings("all")
	public void dictEntryDataSource() {
		HttpServletRequest req = ServletActionContext.getRequest();
		String  dictTypeID = req.getParameter("dictTypeID");
		int start = Integer.parseInt(req.getParameter("start"));
		int limit = Integer.parseInt(req.getParameter("limit"));
		StringBuilder sb = new StringBuilder("From EosDictEntry");
		sb.append(" Where dicttypeid = '").append(dictTypeID).append("'");
		// 动态过滤查询
		String query = req.getParameter("query");
		if (!StringUtil.isNullOrBlank(query)) {
			sb.append(" And dictname Like '%").append(query).append("%'");
		}
		sb.append(" Order By sortno");

		final int beginIdx = start < 0 ? 0 : start;
		final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;
		final String hql = sb.toString();
		final String totalHql = "Select Count(*) As rowcount " + sb.substring(sb.indexOf("From"));
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
		// 分页查询
		template.execute(new HibernateCallback() {
			public String doInHibernate(Session s) {
				Query q = null;
				try {
					q = s.createQuery(totalHql);
					q.setCacheable(false); // 缓存开关
					int total = ((Long) q.uniqueResult()).intValue();
					q.setCacheable(false);
					int begin = beginIdx > total ? total : beginIdx;
					q = s.createQuery(hql).setFirstResult(begin).setMaxResults(pageSize);
					q.setCacheable(false); // 缓存开关
					List list = q.list();

					List<Map> result = new ArrayList<Map>(list.size());
					Map map = null;
					for (Object o : list) {
						EosDictEntry ede = (EosDictEntry) o;
						map = new HashMap<String, String>();
						map.put("dictid", ede.getId().getDictid());
						map.put("dictname", ede.getDictname());
						result.add(map);
					}
					Page page = new Page(total, result);
					JSONUtil.write(ServletActionContext.getResponse(), page.extjsStore());
				} catch (HibernateException e) {
					throw new BusinessException(e);
				} catch (IOException e) {
					throw new BusinessException(e);
				} finally {
					if (q != null) {
						q.setCacheable(false);
					}
				}
				return "1";
			}
		});
	}

	/**
	 * <li>说明：处理拼接whereListJson查询条件
	 * <li>创建人：何涛
	 * <li>创建日期：2016年12月13日
	 * <li>修改人：何涛
	 * <li>修改内容：优化查询条件，对传人查询条件不被包含在实体对象时，不予处理
	 * <li>修改日期：2017年2月15日
	 * @param sb 查询语句hql
	 * @param clazz Class对象
	 * @param req http请求对象
	 */
	private void processWhereList(StringBuilder sb, Class<?> clazz, HttpServletRequest req) {
		Condition[] conditions = null;
		try {
			conditions = JSONUtil.read(req.getParameter("whereListJson"), Condition[].class);
		} catch (IOException e) {
			return;
		}
		if (null == conditions || 0 >= conditions.length) {
			return;
		}
		for (Condition condition : conditions) {
			// Modified by hetao on 2017-02-15 优化查询条件，对传人查询条件不被包含在实体对象时，不予处理
			if ((null != condition.getPropName() && !EntityUtil.contains(clazz, condition.getPropName()))
					|| (null == condition.getPropValue() || 0 >= condition.getPropValue().toString().trim().length())
					&& (Condition.SQL != condition.getCompare() && Condition.IN != condition.getCompare())) {
				continue;
			}
			// 等于
			if (Condition.EQ == condition.getCompare()) {
				// 模糊匹配
				if (condition.isStringLike()) {
					sb.append(" And ").append(condition.getPropName()).append(" Like '%").append(condition.getPropValue()).append("%'");
					// 精确匹配
				} else {
					sb.append(" And ").append(condition.getPropName()).append(" = '").append(condition.getPropValue()).append("'");
				}
				continue;
				// 大于
			}
			if (Condition.GT == condition.getCompare()) {
				sb.append(" And ").append(condition.getPropName()).append(Condition.GT_STR).append(condition.getPropValue());
				continue;
				// 大于等于
			}
			if (Condition.GE == condition.getCompare()) {
				sb.append(" And ").append(condition.getPropName()).append(Condition.GE_STR).append(condition.getPropValue());
				continue;
				// 小于
			}
			if (Condition.LT == condition.getCompare()) {
				sb.append(" And ").append(condition.getPropName()).append(Condition.LT_STR).append(condition.getPropValue());
				continue;
				// 小于等于
			}
			if (Condition.LE == condition.getCompare()) {
				sb.append(" And ").append(condition.getPropName()).append(Condition.LE_STR).append(condition.getPropValue());
				continue;
				// hql查询条件
			}
			if (Condition.SQL == condition.getCompare()) {
				// 实质上是hql拼接
				sb.append(" And ").append(condition.getSql());
				continue;
				// 条件范围
			}
			if (Condition.IN == condition.getCompare()) {
				Object[] propValues = condition.getPropValues();
				if (null == propValues || 0 >= propValues.length) {
					continue;
				}
				StringBuilder temp = new StringBuilder();
				for (Object o : propValues) {
					temp.append(',').append(o.toString());
				}
				sb.append(" And ").append(condition.getPropName()).append(" " + Condition.IN_STR).append("('").append(temp.substring(1).replace(",", "','")).append("')");
			}
		}
	}

	/**
	 * <li>说明：验证给定的参数是否是纯字母的字符串
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月14日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param str 字符串
	 * @return true：参数是纯字母的字符串，否则返回false
	 */
	private boolean isCharacter(String str) {
		Pattern p = Pattern.compile("^[A-za-z]+$");
		Matcher m = p.matcher(str);
		return m.matches();
	}

}
