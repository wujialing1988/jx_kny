package com.yunda.jx.pjjx.partsrdp.wpinst.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsNodeRe;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 返修节点业务类
 * <li>创建人：程锐
 * <li>创建日期：2016-2-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@Service(value="partsNodeReManager")
public class PartsNodeReManager extends JXBaseManager<PartsNodeRe, PartsNodeRe>{
	
    /**
     * <li>说明：根据检修作业流程节点主键获取节点返修原因集合
     * <li>创建人：何涛
     * <li>创建日期：2016-3-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpNodeIDX 检修作业流程节点主键
     * @return 配件检修节点返修原因集合
     */
    @SuppressWarnings("unchecked")
    private List<PartsNodeRe> getModelsByRdpNodeIDX(String rdpNodeIDX) {
        String hql = "From PartsNodeRe Where recordStatus = 0 And rdpNodeIDX = ? Order By updateTime Desc";
        return daoUtils.find(hql, new Object[] {rdpNodeIDX});
    }
    
	/**
     * <li>说明：获取返修节点的返修原因
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpNodeIDX 返修节点IDX
     * @return 返修原因
	 */
	@SuppressWarnings("unchecked")
	public String getCausesByNode(String rdpNodeIDX) {
        List<PartsNodeRe> list = this.getModelsByRdpNodeIDX(rdpNodeIDX);
		StringBuilder sb = new StringBuilder();
        for (PartsNodeRe re : list) {
			sb.append(StringUtil.nvlTrim(re.getRebackCause())).append(Constants.JOINSTR);
		}
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : null;
	}

    /**
     * <li>说明：获取返修节点的返修原因集合
     * <li>创建人：何涛
     * <li>创建日期：2016-2-23
     * <li>修改人：何涛
     * <li>修改日期：2016-03-04
     * <li>修改内容：没有返修原因时，仍然返回该条返修原因记录
     * @param rdpNodeIDX 返修节点IDX
     * @return 返修原因
     */
    @SuppressWarnings("unchecked")
    public List<String> getRebackCause(String rdpNodeIDX) {
        List<PartsNodeRe> list = this.getModelsByRdpNodeIDX(rdpNodeIDX);
        if (null == list || list.isEmpty()) {
            return null;
        }
        List<String> rebackCauseList = new ArrayList<String>(list.size());
        String rebackCause = null;      // 返修原因
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (PartsNodeRe entity : list) {
            rebackCause = entity.getRebackCause();
            // Modified by hetao on 2016-03-04 没有返修原因时，仍然返回该条返修原因记录
//            if (StringUtil.isNullOrBlank(rebackCause)) {
//                continue;
//            }
            rebackCauseList.add((null == rebackCause ? "" : rebackCause) + " (" + df.format(entity.getUpdateTime()) + ")");
        }
        return rebackCauseList;
    }
    
    /**
     * <li>说明：重新保存方法，在保存时自动设置返修提交人信息为当前登陆人员
     * <li>创建人：何涛
     * <li>创建日期：2016-2-23
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容:
     * @param t 返修节点原因
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveOrUpdate(PartsNodeRe t) throws BusinessException, NoSuchFieldException  {
        OmEmployee emp = SystemContext.getOmEmployee();
        t.setSubmitEmpID(String.valueOf(emp.getEmpid()));
        t.setSubmitEmpName(emp.getEmpname());
        super.saveOrUpdate(t);
    }
    
}
