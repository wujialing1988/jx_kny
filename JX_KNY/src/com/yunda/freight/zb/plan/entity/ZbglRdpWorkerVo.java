package com.yunda.freight.zb.plan.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpWorkerVo implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    @Id
    private String idx;
    
    /* 作业人员ID */
    private String workPersonIdx ;
    
    /* 作业人员姓名 */
    private String workPersonName ;
    
    /* 作业车辆ID集合 */
    private String recordIdxs ;
    
    /* 作业车辆名称集合 */
    private String recordNames ;
    
    
    /**
     * <li>说明：无参构造函数
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-12
     * <li>修改人： 
     * <li>修改日期：
     */
    public ZbglRdpWorkerVo() {
    }
    
    
    /**
     * <li>说明：带参构造
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-12
     * <li>修改人： 
     * <li>修改日期：
     * @param idx
     * @param workPersonIdx
     * @param workPersonName
     */
    public ZbglRdpWorkerVo(String idx,String workPersonIdx,String workPersonName) {
        this.idx = idx ;
        this.workPersonIdx = workPersonIdx ;
        this.workPersonName = workPersonName ;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getRecordIdxs() {
        return recordIdxs;
    }

    
    public void setRecordIdxs(String recordIdxs) {
        this.recordIdxs = recordIdxs;
    }

    
    public String getRecordNames() {
        return recordNames;
    }

    
    public void setRecordNames(String recordNames) {
        this.recordNames = recordNames;
    }

    
    public String getWorkPersonIdx() {
        return workPersonIdx;
    }

    
    public void setWorkPersonIdx(String workPersonIdx) {
        this.workPersonIdx = workPersonIdx;
    }

    
    public String getWorkPersonName() {
        return workPersonName;
    }

    
    public void setWorkPersonName(String workPersonName) {
        this.workPersonName = workPersonName;
    }
    
    
    
}
