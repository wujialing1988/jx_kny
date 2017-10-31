package com.yunda.zb.rdp.zbtaskbill.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWidi;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWidiVo;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglRdpWidi业务类,机车整备任务单数据项
 * <li>创建人：程锐
 * <li>创建日期：2015-01-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglRdpWidiManager")
public class ZbglRdpWidiManager extends JXBaseManager<ZbglRdpWidi, ZbglRdpWidi> {
    
    /**
     * <li>说明：查询机车整备任务单数据项分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 机车整备任务单数据项分页列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Page queryRdpWiList(String searchJson, int start, int limit) throws Exception {
        QueryCriteria<ZbglRdpWidi> query = new QueryCriteria<ZbglRdpWidi>();
        query.setEntityClass(ZbglRdpWidi.class);
        List<Condition> whereList = new ArrayList<Condition>();
        Map<String, String> queryMap = new HashMap<String, String>();
        if (StringUtil.isNullOrBlank(searchJson))
            throw new BusinessException("任务单IDX为空");
        queryMap = JSONUtil.read(searchJson, Map.class);
        if (!queryMap.containsKey("rdpWiIDX"))
            throw new BusinessException("任务单IDX为空");
        String rdpWiIDX = String.valueOf(queryMap.get("rdpWiIDX"));
        if (StringUtil.isNullOrBlank(rdpWiIDX))
            throw new BusinessException("任务单IDX为空");
        whereList.add(new Condition("rdpWiIDX", Condition.EQ, rdpWiIDX));
        query.setWhereList(whereList);
        query.setStart(start);
        query.setLimit(limit);
        return findPageList(query);
    }
    
    /**
     * <li>说明：销活时更新机车整备任务单数据项
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param widiArray 机车整备任务单数据项实体数组
     * @throws Exception
     */
    public void updateForHandleRdp(ZbglRdpWidi[] widiArray) throws Exception {
        for (ZbglRdpWidi widi : widiArray) {
            ZbglRdpWidi oldWidi = getModelById(widi.getIdx());
            oldWidi.setDiResult(widi.getDiResult());
            saveOrUpdate(oldWidi);
        }
    }
    
    /**
     * <li>说明：机车整备任务活处理-撤销领活-删除暂存数据
     * <li>创建人：程锐
     * <li>创建日期：2015-3-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpWiIDXS 以,号分隔的任务单IDX的sql字符串
     */
    public void updateForCancelReceivedRdp(String rdpWiIDXS) {
        String sql = SqlMapUtil.getSql("zb-rdp:cancelReceivedRdpWidi")
                               .replace(ZbConstants.IDXS, rdpWiIDXS);
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：根据任务单ID查询任务单数据项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpWiIDX 任务单ID
     * @return 任务单数据项集合
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpWidi> getModels(String rdpWiIDX) {
        String hql = "From ZbglRdpWidi Where rdpWiIDX = ? Order By seqNo ASC";
        return this.daoUtils.find(hql, new Object[]{rdpWiIDX});
    }

    /**
     * <li>说明：查询车辆列检详情
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIdx 车辆列检实例ID
     * @return
     */
    public List<ZbglRdpWidiVo> findZbglRdpWidisByRdpIdx(String rdpIdx) {
        StringBuffer hql = new StringBuffer(" select new ZbglRdpWidiVo(d.idx,d.diCode,d.diName,d.diStandard,d.diClass,d.isBlank,d.seqNo,d.diResult,w.wiIDX,w.wiName,w.handlePersonID,w.handlePersonName,w.wiStatus,w.isHg,w.handleTime,n.idx,n.nodeName) " +
                "from ZbglRdpWidi d,ZbglRdpWi w,ZbglRdpNode n ");
                hql.append("where d.rdpWiIDX = w.idx and w.nodeIDX = n.idx and w.rdpIDX = ? and n.rdpIDX = ? Order By n.seqNo ,w.seqNo, d.seqNo ASC");
        List<ZbglRdpWidiVo> list = this.daoUtils.find(hql.toString(), new Object[]{rdpIdx,rdpIdx});
        return list;
    }
    
}
