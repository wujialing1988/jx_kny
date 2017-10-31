/**
 * 
 */
package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictType;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 业务字典分类管理-业务层
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 2.0
 */
@Service(value="sysEosDictTypeManager")
public class SysEosDictTypeManager extends JXBaseManager<EosDictType,EosDictType>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 业务字典分类查询接口 */
	@Resource(name="eosDictTypeManager")
	private IEosDictTypeManager eosDictTypeManager;
	
	/**
	 * 
	 * <li>说明：获取当前业务字典类别的下级
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param nodeid 业务字典ID
	 * @return 下级业务字典
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public List <Map> getChildNodes(String nodeid) throws BusinessException{
		List <EosDictType> list = null;
		if(StringUtil.isNullOrBlank(nodeid)||"ROOT_0".equals(nodeid)){
			list = eosDictTypeManager.findRoots();  //查询根
		} else {
			list = eosDictTypeManager.findChildsByIds(nodeid); //查询下层
		}
		Map node = null;
		List<Map> children = new ArrayList<Map>();
		for(EosDictType type : list){
			node = new LinkedHashMap();
			node.put("id", 		type.getDicttypeid()); //node.id
			node.put("text", 	type.getDicttypename()); //node.text
			List <EosDictType> _t = eosDictTypeManager.findChildsByIds(type.getDicttypeid()); //查询下层是否还有数据
			if(_t!=null && _t.size()>0){
				node.put("leaf", false); //不是叶子节点
			} else {
				node.put("leaf", true);  //是叶子节点
			}
			node.put("dicttypeid", 		type.getDicttypeid()); //业务字典分类ID
			node.put("dicttypename", 	type.getDicttypename()); //业务字典分类名称
			node.put("rank", 			type.getRank()); //业务字典层级
			node.put("parentid", 		type.getParentid()); //上层业务字典ID
			node.put("seqno", 			type.getSeqno()); //业务字典SEQ
			children.add(node);
		}
		return children;
	}
	
	/**
	 * <li>说明： 验证是否存在相同的字典类型ID
	 * <li>创建人： 谭诚
	 * <li>创建日期：2013-12-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param type 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	public String[] validateUpdate(EosDictType type){
		if(type == null) return new String[]{"验证出错，参数未输入!"};
		EosDictType exist = eosDictTypeManager.getModelById(type.getDicttypeid());
		if(exist != null){
			return new String[]{"字典类型代码 ["+type.getDicttypeid()+"] 已存在， 请重新输入!"};
		}
		return null;
	}
	
	/**
	 * <li>说明：新增、编辑业务字典类型
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-13
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param type 实体对象
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void saveOrUpdate(EosDictType type) throws BusinessException, NoSuchFieldException {
		if(type == null) return;
		boolean isAdd = false;
		if(eosDictTypeManager.getModelById(type.getDicttypeid())==null) isAdd = true;
		if(isAdd){ //如果当前是新增操作
			//根据dicttypeid生成seqno
			if(StringUtil.isNullOrBlank(type.getSeqno())) type.setSeqno("."+type.getDicttypeid()+"."); 
			else type.setSeqno(type.getSeqno()+type.getDicttypeid()+".");
			//rank层级+1
			type.setRank(type.getRank()+1);
			this.daoUtils.saveOrUpdate(type); //保存实体，并产生dicttypeid
		} else {
			String sql = "UPDATE eos_dict_type set dicttypename = '" + type.getDicttypename() + "' where dicttypeid = '"+ type.getDicttypeid()+"'";
			this.daoUtils.executeSql(sql);
		} 
	}
	
	/**
	 * <li>说明：覆盖基类方法，执行选中业务字典分类的删除方法，并级联删除子项数据，包括外键关联的业务字典项
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-13
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param type 实体对象
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void deleteByIds(Serializable... ids) throws BusinessException {
		if(ids == null) return;
		try {
			//循环删除选中机构
			for (Serializable id : ids) {
				if(StringUtil.isNullOrBlank(String.valueOf(id))) continue;
				EosDictType type = eosDictTypeManager.getModelById(String.valueOf(id)); //根据dicttypeid获取待删除的业务字典类型实体
				if(type == null) continue;
				iterationDeleteChildType(type); //调用递归方法删除子项
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * <li>说明：递归方法，根据入参的业务字典，迭代删除其下属所有子项及对应的字典项明细
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param type 业务字典实体
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private void iterationDeleteChildType(EosDictType type) throws BusinessException{
		SysEosDictEntryManager sysEosDictEntryManager = (SysEosDictEntryManager)Application.getSpringApplicationContext().getBean("sysEosDictEntryManager"); //获取业务字典项业务处理类的实例
		List <EosDictType> childType = eosDictTypeManager.findChildsByIds(type.getDicttypeid()); //获取下级业务字典类型列表
		if(childType==null||childType.size()<1) {
			sysEosDictEntryManager.deleteByTypeIds(type.getDicttypeid()); //【调用方法删除目标分类下的字典项】
			this.daoUtils.getHibernateTemplate().delete(type); //【执行字典分类】
			return; //如果未找到子项，跳出递归
		}
		for(EosDictType _o : childType){
			iterationDeleteChildType(_o); //递归调用，继续向下层查找
		}
		sysEosDictEntryManager.deleteByTypeIds(type.getDicttypeid());//【调用方法删除目标分类下的字典项】
		this.daoUtils.getHibernateTemplate().delete(type); //【执行删除字典分类】
	}
}
