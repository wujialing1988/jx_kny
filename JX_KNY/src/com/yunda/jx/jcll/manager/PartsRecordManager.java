package com.yunda.jx.jcll.manager;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jcll.entity.PartsRecord;
import com.yunda.jx.jcll.entity.TrainRecordAttachment;
import com.yunda.zb.mobile.entity.ZbglRdpWiMobileDTO;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件履历业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value="partsRecordManager")
public class PartsRecordManager extends JXBaseManager<PartsRecord, PartsRecord> {
    
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page<PartsRecord> findPartsRecords(SearchEntity<PartsRecord> searchEntity) throws BusinessException{
         PartsRecord entity = searchEntity.getEntity();
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from ( select rdp.parts_no , rdp.parts_name , count(1) as rdp_count from PJJX_Parts_Rdp rdp where rdp.record_status = 0 and rdp.parts_no is not null and rdp.status = '0401' ");
        sb.append(" group by rdp.parts_no , rdp.parts_name ) t where 1=1  ");
        if(!StringUtil.isNullOrBlank(entity.getPartsNo())){
            sb.append(" and (t.parts_no like '%"+entity.getPartsNo()+"%' or t.parts_name like '%"+entity.getPartsNo()+"%' )");
        }
        String sql = sb.toString();
        String totalSql = "Select count(*) as rowcount " + sql.substring(sql.indexOf("from"));
        return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, PartsRecord.class);
        
    }
    
    
}
