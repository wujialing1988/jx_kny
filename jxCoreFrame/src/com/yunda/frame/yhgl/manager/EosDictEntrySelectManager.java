package com.yunda.frame.yhgl.manager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.EosDictEntryBean;
import com.yunda.frame.yhgl.entity.EosDictEntryId;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 业务字典选择控件业务类
 * <li>创建人：程锐
 * <li>创建日期：2012-9-5
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "eosDictEntrySelectManager")
public class EosDictEntrySelectManager extends JXBaseManager<EosDictEntry, EosDictEntry> {
    
    @Autowired
    private IEosDictEntryManager dictManager;
    
    /**
     * <li>说明：根据业务字典类型ID查找并返回对应的业务字典实体项记录
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param dicTypeID 业务字典类型ID
     * @return List<EosDictEntry>
     */
    @SuppressWarnings("unchecked")
    public List<EosDictEntry> findByDicTypeID(String dicTypeID) {
        String hql = "from EosDictEntry where id.dicttypeid = ? order by seqno ";
        return daoUtils.getHibernateTemplate().find(hql, dicTypeID);
    }
    
    /**
     * <li>说明：根据类型及字典名称获取对象
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dicTypeID 类型ID
     * @param dictName 字典名称
     * @return
     */
    public EosDictEntry getEosDictEntryByTypeIDAndName(String dicTypeID,String dictName){
        String hql = "from EosDictEntry where status = 1 and id.dicttypeid = ? and dictname = ?";
        return (EosDictEntry)daoUtils.findSingle(hql, new Object[]{dicTypeID,dictName});
    }
    
    /**
     * <li>说明：返回EOS业务字典项所有记录，客户端使用Ext.Store存放所有记录
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-9-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 以json字符串形式返回所有业务字典项记录，用于客户端store的data配置
     * @throws Exception
     */
    public static String getAllRecordJSON() throws Exception {
        EosDictEntrySelectManager manager =
            (EosDictEntrySelectManager) Application.getSpringApplicationContext().getBean("eosDictEntrySelectManager");
        List<EosDictEntry> entryList = manager.getAll();
        if (entryList == null || entryList.size() < 1) {
            return "[]";
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (EosDictEntry entry : entryList) {
            Map<String, Object> entryMap = new HashMap<String, Object>();
            EosDictEntryId entryID = entry.getId();
            entryMap.put("idx", entryID.getDicttypeid() + "_" + entryID.getDictid());
            entryMap.put("dicttypeid", entryID.getDicttypeid());
            entryMap.put("dictid", entryID.getDictid());
            entryMap.put("dictname", entry.getDictname());
            entryMap.put("status", entry.getStatus());
            entryMap.put("sortno", entry.getSortno());
            entryMap.put("rank", entry.getRank());
            entryMap.put("parentid", entry.getParentid());
            entryMap.put("seqno", entry.getSeqno());
            entryMap.put("filter1", entry.getFilter1());
            entryMap.put("filter2", entry.getFilter2());
            list.add(entryMap);
        }
        return JSONUtil.write(list);
    }
    
    /**
     * <li>说明：通过ID获取数据字典业务名称
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeId 业务字典类型标示
     * @param dictid 业务字典ID，
     * @return 返回数据字典实体EosDictEntry
     * @throws BusinessException
     */
    public EosDictEntry getEosDictEntry(String dictTypeId, String dictid) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append(" from EosDictEntry where status = 1 ");
        if (!StringUtil.isNullOrBlank(dictTypeId)) {
            hql.append(" and id.dicttypeid='" + dictTypeId + "'");
        }
        if (!StringUtil.isNullOrBlank(dictid)) {
            hql.append(" and id.dictid='" + dictid + "'");
        }
        return (EosDictEntry) this.daoUtils.findSingle(hql.toString());
    }
    
    /**
     * <li>说明：业务字典选择控件下拉列表
     * <li>创建人：程锐
     * <li>创建日期：2012-9-5
     * <li>修改人： 程锐
     * <li>修改日期：2012-9-9
     * <li>修改内容：添加查询过滤条件
     * @param status 状态
     * @param dictTypeId 业务字典类型编号
     * @param queryWhere 查询条件字符串
     * @param hasEmpty 是否插入一条空记录
     * @return 业务字典选择控件下拉列表JSON数据源
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public String combolist(String status, String dictTypeId, String queryWhere, boolean hasEmpty) throws IOException {
        StringBuilder queryString = new StringBuilder();
        queryString.append("From EosDictEntry Where status = '" + status + "' and id.dicttypeid='" + dictTypeId + "' ");
        if (!StringUtil.isNullOrBlank(queryWhere)) {
            queryString.append(" and " + queryWhere);
        }
        queryString.append(" Order By id.dictid, sortno");
        
        List<EosDictEntry> list = getDaoUtils().find(queryString.toString());
        if (null == list || 0 >= list.size()) {
            return null;
        }
        
        EosDict dict = null;
        List<EosDict> jsonList = new ArrayList<EosDict>();
        if (hasEmpty) {
            jsonList.add(EosDict.getInstance());
        }
    
        for (EosDictEntry eosDictEntry : list) {
            dict = new EosDict();
            dict.dictname = eosDictEntry.getDictname();
            dict.dictid = eosDictEntry.getId().getDictid();
            dict.status = eosDictEntry.getStatus();
            dict.sortno = eosDictEntry.getSortno();
            dict.rank = eosDictEntry.getRank();
            dict.parentid = eosDictEntry.getParentid();
            dict.seqno = eosDictEntry.getSeqno();
            dict.dicttypeid = eosDictEntry.getId().getDicttypeid();
            jsonList.add(dict);
        }
        return JSONUtil.write(jsonList);
    }
    
    /**
     * <li>说明：数据字典下拉树
     * <li>创建人：程锐
     * <li>创建日期：2014-5-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param req 请求对象
     * @return List<HashMap>
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> findEosDictTreeData(HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException {
        String parentIDX = StringUtil.nvlTrim(req.getParameter("parentIDX"), "ROOT_0");
        String isChecked = req.getParameter("isChecked");
        Map<String, String> map = JSONUtil.read(req.getParameter("queryParams"), Map.class);
        String dicttypeid = "";
        if (map != null && !map.isEmpty() && map.containsKey("dicttypeid")) {
            dicttypeid = map.get("dicttypeid");
        }
        List<EosDictEntry> list = new ArrayList<EosDictEntry>();
        List<HashMap> children = new ArrayList<HashMap>();
        if (parentIDX.equals("ROOT_0")) {
            list = dictManager.findRoots(dicttypeid);
        } else {
            list = dictManager.findChildsByIds(dicttypeid, parentIDX);
        }
        for (EosDictEntry dict : list) {
            Boolean isLeaf = isLeaf(dict.getId().getDictid(), dicttypeid);
            HashMap nodeMap = new HashMap();
            nodeMap.put("id", dict.getId().getDictid());
            nodeMap.put("text", dict.getDictname());
            nodeMap.put("leaf", isLeaf);
            nodeMap.put("parentid", dict.getParentid());
            if (!StringUtil.isNullOrBlank(isChecked))
                nodeMap.put("checked", false);
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：获取数据字典列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeID 数据字典类型
     * @return 分页对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Page queryDictList(String dictTypeID) throws Exception {
        List<EosDictEntry> list = findByDicTypeID(dictTypeID);
        List<EosDictEntryBean> beanlist = new ArrayList<EosDictEntryBean>();
        for (EosDictEntry eos : list) {
            EosDictEntryBean bean = new EosDictEntryBean();
            bean.setDictid(eos.getId().getDictid());
            bean.setDictname(eos.getDictname());
            beanlist.add(bean);
        }
        return new Page(beanlist.size(), beanlist);
    }
    
    /**
     * <li>说明：数据字典下拉树 （重写父节点不能被选中）
     * <li>创建人：张迪
     * <li>创建日期：2016-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param req 请求对象
     * @return 节点列表
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> findEosDictTreeChildData(HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException {
        String parentIDX = StringUtil.nvlTrim(req.getParameter("parentIDX"), "ROOT_0");
        Map<String, String> map = JSONUtil.read(req.getParameter("queryParams"), Map.class);
        String dicttypeid = "";
        if (map != null && !map.isEmpty() && map.containsKey("dicttypeid")) {
            dicttypeid = map.get("dicttypeid");
        }
        List<EosDictEntry> list = new ArrayList<EosDictEntry>();
        List<HashMap> children = new ArrayList<HashMap>();
        if (parentIDX.equals("ROOT_0")) {
            list = dictManager.findRoots(dicttypeid);
        } else {
            list = dictManager.findChildsByIds(dicttypeid, parentIDX);
        }
        for (EosDictEntry dict : list) {
            Boolean isLeaf = isLeaf(dict.getId().getDictid(), dicttypeid);
            HashMap nodeMap = new HashMap();
            nodeMap.put("id", dict.getId().getDictid());
            nodeMap.put("text", dict.getDictname());
            nodeMap.put("leaf", isLeaf);
            nodeMap.put("parentid", dict.getParentid());
            if(isLeaf) {
                //nodeMap.put("disabled", true);  
                nodeMap.put("checked", false);
             }
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：是否为子节点
     * <li>创建人：程锐
     * <li>创建日期：2014-5-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 上级主键
     * @param dicttypeid 数据字典类型主键
     * @return true 是子节点，false 非子节点
     * @throws BusinessException
     */
    public boolean isLeaf(String idx, String dicttypeid) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append("select count(*) From EosDictEntry t where t.status = 1 and dicttypeid = '").append(dicttypeid).append("'");
        if (!StringUtil.isNullOrBlank(idx)) {
            hql.append(" and t.parentid='").append(idx).append("'");
        }
        int count = daoUtils.getInt(enableCache(), hql.toString());
        return count == 0 ? true : false;
    }
    
    /**
     * <li>标题: 机车检修管理信息系统
     * <li>说明: 数据字典查询json格式封装类
     * <li>创建人：何涛
     * <li>创建日期：2015-9-24
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部整备系统项目组
     * @version 1.0
     */
    @JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
    private static class EosDict implements Serializable {
        
        /** default */
        private static final long serialVersionUID = 1L;
        
        protected String dictname;
        
        protected String dictid;
        
        protected Long status;
        
        protected Long sortno;
        
        protected Long rank;
        
        protected String parentid;
        
        protected String seqno;
        
        protected String dicttypeid;
        
        /**
         * <li>说明：空数据字典记录“请选择...”
         * <li>创建人：何涛
         * <li>创建日期：2015-9-24
         * <li>修改人： 
         * <li>修改日期：
         * <li>修改内容：
         * @return EosDict
         */
        protected static EosDict getInstance() {
            EosDict dict = new EosDict();
            dict.dictname = "请选择...";
            return dict;
        }
    }

    /**
     * <li>说明：获取数据字典中的默认值（默认排序选序号最小的）[只能支持非树形结构的数据字典]
     * <li>创建人：林欢
     * <li>创建日期：2017-3-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数
     * @return Map<String, String> 默认对象
     * @throws Exception
     */
	public Map<String, String> getDeafultValue(Map<String, String> paramMap) {
//		结果集
        ScrollableResults srs = null;
        StringBuffer sb = new StringBuffer();
        sb.append(" select a.dictid,a.dictname from eos_dict_entry a where 1=1 ");
        
        //过滤字典idx
        if (StringUtils.isNotBlank(paramMap.get("dicttypeid"))) {
        	sb.append(" and a.dicttypeid = '").append(paramMap.get("dicttypeid")).append("' ");
		}
        
        sb.append(" and a.parentid is null ");
        sb.append(" order by a.seqno ");
        
//      定义变量
        Integer number = 0;
        Query query = this.getDaoUtils().getSessionFactory().openSession().createSQLQuery(sb.toString());
        srs = query.scroll();
        
        Map<String, String> map = null;
        while (srs.first()){
        	map = new HashMap<String, String>();
        	
        	map.put("dictid", srs.get(number) == null ? " " : srs.get(number).toString());
            number++;
            map.put("dictname", srs.get(number) == null ? " " : srs.get(number).toString());
            number = 0;
            return map;
        }
		return map;
	}

    
}
