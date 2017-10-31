package com.yunda.jx.jcll.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jcll.entity.TrainRecordAttachment;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.mobile.entity.FaultPhoto;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车履历附件业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value="trainRecordAttachmentManager")
public class TrainRecordAttachmentManager extends JXBaseManager<TrainRecordAttachment, TrainRecordAttachment> {
    
    
    @Resource
    private AttachmentManager attachmentManager ;
    
    /**
     * <li>说明：复制图片并进行相应关联
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonPhotos 图片信息
     * @param recordIdx 机车履历主键
     * @param instanceIdx 机车部件实例主键
     * @throws Exception 
     */
    public void copyRecordFile(String jsonPhotos,String recordIdx,String instanceIdx) throws Exception{
        if(jsonPhotos != null && !"".equals(jsonPhotos)){
            FaultPhoto[] faultPhotos = JSONUtil.read(jsonPhotos, FaultPhoto[].class);
            for (FaultPhoto photo : faultPhotos) {
                TrainRecordAttachment attachment = new TrainRecordAttachment();
                attachment.setAttachmentName(photo.getFilename());
                attachment.setTrainRecordIdx(recordIdx);
                attachment.setTrainRecordInstanceIdx(instanceIdx);
                this.saveOrUpdate(attachment);
                this.daoUtils.flush();
                //attachmentManager.copyFaultFile(attachment.getIdx(), photo.getFilePath(), photo.getFilename(),ZbConstants.UPLOADPATH_TRAINRECORD_IMG);
            }
        }
    }
    
    
    /**
     * <li>说明：重写履历附件查询方法
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 分页查询参数实体
     * @return Page<TrainRecordAttachment> 履历附件
     * @throws BusinessException
     */
    public Page<TrainRecordAttachment> findPageList(SearchEntity<TrainRecordAttachment> searchEntity) throws BusinessException {
        TrainRecordAttachment entity = searchEntity.getEntity();
        if(StringUtil.isNullOrBlank(entity.getTrainRecordIdx()) || StringUtil.isNullOrBlank(entity.getTrainRecordInstanceIdx())){
            return null ;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(" select new TrainRecordAttachment(ra.idx,ra.trainRecordIdx,ra.trainRecordInstanceIdx,ra.attachmentName,ra.seqNo,");
        sb.append(" ra.createTime,ra.updateTime,a.attachmentSize,a.attachmentClass,a.attachmentIdx)");
        sb.append(" From TrainRecordAttachment ra,com.yunda.frame.baseapp.upload.entity.Attachment a ");
        sb.append(" where ra.recordStatus = 0 and a.recordStatus = 0 and ra.idx = a.attachmentKeyIDX ");
        sb.append(" and ra.trainRecordIdx = '").append(entity.getTrainRecordIdx()).append("' ");
        sb.append(" and ra.trainRecordInstanceIdx = '").append(entity.getTrainRecordInstanceIdx()).append("' ");
        String hql = sb.toString();
        String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
        return findPageList(hql, totalHql, searchEntity.getStart(), searchEntity.getLimit());
    }
    
    
}
