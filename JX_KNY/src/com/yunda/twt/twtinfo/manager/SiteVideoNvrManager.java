package com.yunda.twt.twtinfo.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.twt.twtinfo.entity.SiteVideoNvr;
import com.yunda.twt.twtinfo.entity.SiteVideoNvrChanel;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: SiteVideoNvr业务类，视频监控网络硬盘录像机
 * <li>创建人：何涛
 * <li>创建日期：2015-6-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="siteVideoNvrManager")
public class SiteVideoNvrManager extends JXBaseManager<SiteVideoNvr, SiteVideoNvr> {
    
    /** SiteVideoNvrChanel业务类，网络录像机通道 */
    @Resource
    private SiteVideoNvrChanelManager siteVideoNvrChanelManager;
    
    /**
     * <li>说明：更新前验证NVR名称的唯一性
     * <li>创建人：何涛
     * <li>创建日期：2015-06-01
     * <li>修改人： 
     * <li>修改日期：
     * @param t 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @Override
    public String[] validateUpdate(SiteVideoNvr t) {
        SiteVideoNvr entity = this.getModelByNvrName(t.getNvrName());
        if (null == entity || entity.getIdx().equals(t.getIdx()) || !t.getNvrName().equals(entity.getNvrName())) {
            return null;
        }
        return new String[]{"网络硬盘录像机名称【" + t.getNvrName() + "】已经存在，不可以重复添加！"};
    }
    
    /**
     * <li>说明：根据NVR名称获取视频监控网络硬盘录像机实例对象
     * <li>创建人：何涛
     * <li>创建日期：2015-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nvrName NVR名称
     * @return 视频监控网络硬盘录像机实例对象
     */
    private SiteVideoNvr getModelByNvrName(String nvrName) {
        String hql = "From SiteVideoNvr Where nvrName = ?";
        return (SiteVideoNvr) this.daoUtils.findSingle(hql, new Object[]{nvrName});
    }
    
    /**
     * <li>说明：树
     * <li>创建人：何涛
     * <li>创建日期：2015-06-02
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return List<HashMap<String, Object>> 实体集合
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> tree() {
        String siteID = EntityUtil.findSysSiteId("");
    	StringBuilder sb = new StringBuilder("From SiteVideoNvr Where 0 = 0");
    	if (null != siteID && siteID.trim().length() > 0) {
    		sb.append(" And siteID = '").append(siteID).append("'");
    	}
        sb.append(" Order By nvrName ASC, nvrIP ASC, nvrPort ASC");
        String hql = sb.toString();
        List<SiteVideoNvr> list = (List<SiteVideoNvr>) this.daoUtils.find(hql);
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        StringBuilder sbText = null;
        for (SiteVideoNvr t : list) {
            sbText = new StringBuilder(t.getNvrName()).append("(").append(t.getNvrIP()).append(":").append(t.getNvrPort()).append(")");
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getIdx());  
            nodeMap.put("text", sbText.toString()); 
            nodeMap.put("leaf", true); 
            // Modified by hetao on 2016-06-08 优化视频监控配置功能，增加checked属性，使能批量删除nvr配置信息
            nodeMap.put("checked", false); 
            nodeMap.put("iconCls", "nvrIcon"); 
            nodeMap.put("ip", t.getNvrIP()); 
            nodeMap.put("port", t.getNvrPort()); 
            nodeMap.put("name", t.getNvrName()); 
            nodeMap.put("username", t.getUsername()); 
            nodeMap.put("password", t.getPassword()); 
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：级联删除NVR下属通道号
     * <li>创建人：何涛
     * <li>创建日期：2015-06-03
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 网络硬盘录像机实例idx主键数组
     */
    @Override
    public void deleteByIds(Serializable... ids) throws BusinessException {
        List<String> chanelIdxs = new ArrayList<String>();
        for(Serializable idx : ids) {
            List<SiteVideoNvrChanel> list = this.siteVideoNvrChanelManager.getModelByVideoNvrIDX((String)idx);
            if (null == list || list.size() <= 0) {
                continue;
            }
            for (SiteVideoNvrChanel chanel : list) {
                chanelIdxs.add(chanel.getIdx());
            }
        }
        if (chanelIdxs.size() > 0) {
            this.siteVideoNvrChanelManager.deleteByIds(chanelIdxs.toArray(new Serializable[chanelIdxs.size()]));
        }
        super.deleteByIds(ids);
    }
    
    /**
     * <li>说明：根据IP地址和端口号获取NVR实例对象
     * <li>创建人：何涛
     * <li>创建日期：2015-6-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nvrIP IP地址
     * @param nvrPort 端口号
     * @param siteID 站场id
     * @return SiteVideoNvr NVR实例对象
     */ 
    public SiteVideoNvr getModel(String nvrIP, Integer nvrPort, String siteID) {
        String hql = "From SiteVideoNvr Where nvrIP = ? And nvrPort = ? And siteID = ?";
        return (SiteVideoNvr) this.daoUtils.findSingle(hql, new Object[]{nvrIP, nvrPort, siteID});
    }

}
