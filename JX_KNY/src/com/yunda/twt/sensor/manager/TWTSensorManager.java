package com.yunda.twt.sensor.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.twt.sensor.entity.TWTSensor;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TWTSensor业务类,传感器注册
 * <li>创建人：程梅
 * <li>创建日期：2015-05-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="tWTSensorManager")
public class TWTSensorManager extends AbstractOrderManager<TWTSensor, TWTSensor> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
        
    /**
     * <li>说明： 集线盒编号和传感器编号唯一验证
     * <li>创建人：程梅
     * <li>创建日期：2015-5-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 传感器实体
     * @return String[] 错误提示
     * @throws BusinessException BusinessException
     */
    public String[] validateUpdate(TWTSensor entity) throws BusinessException {
        List<String> errMsg = new ArrayList<String>();
        TWTSensor sensor = this.getModelById(entity.getIdx()); // 获取当前对象在数据库中的值
        if ("".equals(entity.getIdx()) || !sensor.getBoxCode().equals(entity.getBoxCode()) || !sensor.getSensorCode().equals(entity.getSensorCode())) { // 新增或修改时验证
            StringBuilder sb = new StringBuilder(" Select count(*) From TWTSensor where boxCode='").append(entity.getBoxCode()).append("'");
            sb.append(" and sensorCode='").append(entity.getSensorCode()).append("'");
            String hql = sb.toString();
            int count = this.daoUtils.getCount(hql);
            if (count > 0) {
                errMsg.add("集线盒编号【" + entity.getBoxCode() + "】,传感器编号【" + entity.getSensorCode() + "】已存在！");
            }
        }
        if (errMsg.size() > 0) {
            String[] errArray = new String[errMsg.size()];
            errMsg.toArray(errArray);
            return errArray;
        }
        return null;
    }
    
    /**
     * <li>说明：物理删除传感器注册信息【注销】
     * <li>创建人：程梅
     * <li>创建日期：2015-5-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 需注销的传感器注册id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        daoUtils.removeByIds(ids, entityClass, getModelIdName(entityClass));// 按ID删除工种信息
    }
    
    /**
     * <li>说明：传感器门限设置
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param twtSensor 传感器实体
     * @param ids 需要设置门限的ids
     * @throws NoSuchFieldException
     * @throws BusinessException
     */
    public void twtSensorUpdate(TWTSensor twtSensor, Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<TWTSensor> sensorList = new ArrayList<TWTSensor>();
        TWTSensor sensor = null;
        for (Serializable id : ids) {
            sensor = getModelById(id);
            sensor.setMaxLimit(twtSensor.getMaxLimit());
            sensor.setMinLimit(twtSensor.getMinLimit());
            sensorList.add(sensor);
        }
        this.saveOrUpdate(sensorList);
    }
    
    /**
     * <li>说明：设置传感器的检查周期
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param twtSensor 传感器实体
     * @param ids 需要设置检查周期的ids
     * @throws NoSuchFieldException
     * @throws BusinessException
     */
    public void twtaddcycle(TWTSensor twtSensor, Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<TWTSensor> sensorList = new ArrayList<TWTSensor>();
        TWTSensor sensor = null;
        for (Serializable id : ids) {
            sensor = getModelById(id);
            sensor.setCheckCycle(twtSensor.getCheckCycle());
            sensorList.add(sensor);
        }
        this.saveOrUpdate(sensorList);
    }
    
    /**
     * <li>说明：修改传感器的集线盒号
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param twtSensor 传感器实体
     * @param ids 需要设置集线盒号的ids
     * @return String[] 错误提示
     * @throws NoSuchFieldException
     * @throws BusinessException
     */
    public String[] twtupdatebox(TWTSensor twtSensor, Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<TWTSensor> sensorList = new ArrayList<TWTSensor>();
        List<String> errMsg = new ArrayList<String>();
        TWTSensor sensor = null;
        for (Serializable id : ids) {
            sensor = getModelById(id);
            sensor.setBoxCode(twtSensor.getBoxCode());
            String[] box = validateUpdate(sensor);
            if (null == box || box.length < 1) {
                sensorList.add(sensor);
            } else {
                errMsg.add(box[0]);
            }
        }
        if (errMsg.size() > 0) {
            String[] errArray = new String[errMsg.size()];
            errMsg.toArray(errArray);
            return errArray;
        } else if (null != sensorList && sensorList.size() > 0) {
            this.saveOrUpdate(sensorList);
        }
        return null;
    }
    
    /**
     * <li>说明：解除传感器的绑定
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 需要解除绑定的传感器ids
     * @throws NoSuchFieldException
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public void twtRelieveBin(Serializable... ids) throws BusinessException, NoSuchFieldException {
        TWTSensor sensor = null;
        for (Serializable id : ids) {
            sensor = getModelById(id);
            List<TWTSensor> maxList = getMaxSeqNo(sensor.getStationCode());
            if(null != maxList && maxList.size() > 1){
                // 获取顺序号比此记录大的数据
                String hql = "From TWTSensor Where seqNo > ? and stationCode = ?";
                List<TWTSensor> list = this.daoUtils.find(hql, new Object[] { sensor.getSeqNo() ,sensor.getStationCode()});
                List<TWTSensor> entityList = new ArrayList<TWTSensor>();
                // 设置其后的所有记录的排序后依次减一
                for (TWTSensor recordCard : list) {
                    recordCard.setSeqNo(recordCard.getSeqNo() - 1);
                    entityList.add(recordCard);
                }
                super.saveOrUpdate(entityList);
            }
            sensor.setSeqNo(null);
            sensor.setStationName("");
            sensor.setStationCode("");
            this.saveOrUpdate(sensor);
        }
    }
    
    /**
     * <li>说明：根据传感器编号 查询传感器信息
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param boxCodeId 集线盒编号
     * @param sensorCodeId 传感器编号
     * @return List<TWTSensor> 传感器列表信息
     */
    @SuppressWarnings("unchecked")
    public List<TWTSensor> getSensorStationByCode(String boxCodeId, String sensorCodeId) {
        // 查询的是没有被绑定的传感器
        StringBuilder sb = new StringBuilder(" From TWTSensor where  stationCode is null and stationName is null ");
        if (!StringUtil.isNullOrBlank(boxCodeId) && !StringUtil.isNullOrBlank(sensorCodeId)) {
            sb.append(" and boxCode='").append(boxCodeId).append("' and sensorCode='").append(sensorCodeId).append("'");
        }
        String hql = sb.toString();
        List<TWTSensor> list = (List<TWTSensor>) daoUtils.find(hql);
        return list;
    }
    
    /**
     * <li>说明：绑定选择的传感器
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param deskCode 台位图编号
     * @param deskName 台位图名称
     * @param mapcode 站场
     * @param stationList 传感器实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveStation(String deskCode, String deskName, String mapcode, TWTSensor[] stationList) throws BusinessException, NoSuchFieldException {
        List<TWTSensor> list = new ArrayList<TWTSensor>(stationList.length);
        List<TWTSensor> maxList = getMaxSeqNo(deskCode);
        Integer maxSeqNo = 1;
        
        if(null != maxList && maxList.size() > 0 && null != maxList.get(0)){
            maxSeqNo = maxList.get(0).getSeqNo() + 1;
        }
        for (TWTSensor station : stationList) {
            station.setStationCode(deskCode); // 台位图编号
            station.setStationName(deskName); // 台位图名称
            station.setSiteId(mapcode);// 站场
            station.setSeqNo(maxSeqNo);
            list.add(station);
            maxSeqNo +=1 ;
        }
        this.saveOrUpdate(list);
    }
    
    /**
     * <li>说明：根据输入的集线盒号和传感器编号实现台位绑定
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param deskCode 台位图编号
     * @param deskName 台位图名称
     * @param mapcode 站场
     * @param boxCodeId 集线盒编号
     * @param sensorCodeId 传感器编号
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @return List
     */
    public String[] bindSensorSave(String deskCode, String deskName, String mapcode, String boxCodeId, String sensorCodeId) throws BusinessException,
        NoSuchFieldException {
        // 根据传感器编号查询传感器信息
        String[] errMsg = null;
        List<TWTSensor> entityList = getSensorStationByCode(boxCodeId, sensorCodeId); // 查询结果
        List<TWTSensor> maxList = getMaxSeqNo(deskCode);
        Integer maxSeqNo = 1;
        if(null != maxList && maxList.size() >0 ){
            maxSeqNo = maxList.get(0).getSeqNo() + 1;
        }
        if (null != entityList && entityList.size() > 0) {
            for (TWTSensor sensor : entityList) {
                sensor.setStationCode(deskCode); // 台位图编号
                sensor.setStationName(deskName); // 台位图名称
                sensor.setSiteId(mapcode);// 站场
                sensor.setSeqNo(maxSeqNo);
                maxSeqNo +=1 ;
            }
            this.saveOrUpdate(entityList);
            return null;
        }
        errMsg = new String[1];
        errMsg[0] = "没有找到集线盒编号为【" + boxCodeId + "】,传感器编号为【" + sensorCodeId + "】的传感器</br> 或者 </br> 该传感器已绑定了台位！";
        return errMsg;
    }
    /**
     * 
     * <li>说明：查询台位信息列表
     * <li>创建人：程梅
     * <li>创建日期：2015-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param unBindSensor unBindSensor
     * @param sensor sensor
     * @param searchJson searchJson
     * @param start start
     * @param limit limit
     * @param order order
     * @return Page<TWTSensor>
     */
    public Page<TWTSensor> getSiteStationList(String unBindSensor, TWTSensor sensor, String searchJson, int start, int limit,Order[] order){
        StringBuffer selectSql = new StringBuffer("select t.station_code as \"stationCode\",");
        selectSql.append("t.station_name \"stationName\",");
        selectSql.append("(select wmsys.wm_concat(s.box_code || s.sensor_code) from TWT_SENSOR s where t.station_code = s.station_code) as \"boxCode\" ");
        String fromSql = " from twt_site_station t ";
        StringBuffer awhere =  new StringBuffer(); 
        awhere.append(" where 1=1 ");
        SearchEntity<TWTSensor> searchEntity = new SearchEntity<TWTSensor>(sensor, start,limit, order);
        if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getStationCode())){
              awhere.append(" and t.station_code like '%").append(searchEntity.getEntity().getStationCode()).append("%' ");
          }
        if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getStationName())){
              awhere.append(" and t.station_name like '%").append(searchEntity.getEntity().getStationName()).append("%' ");
          }
        if("true".equals(unBindSensor)){
              awhere.append(" and (select wmsys.wm_concat(s.box_code || s.sensor_code) from TWT_SENSOR s where t.station_code = s.station_code) is null ");
         }
       
        String totalSql = "select count(*) " + fromSql;   
        totalSql += awhere;
        String querySql = selectSql + fromSql + awhere;
        return super.findPageList(totalSql, querySql, start , limit, null, order);
    }
    /**
     * 
     * <li>说明：获取同一台位绑定的传感器列表
     * <li>创建人：程梅
     * <li>创建日期：2015-7-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param stationCode 台位编码
     * @return 传感器列表
     */
    @SuppressWarnings("unchecked")
    public List<TWTSensor> getMaxSeqNo(String stationCode){
        StringBuffer sql = new StringBuffer(" From TWTSensor Where seqNo is not null and stationCode='").append(stationCode)
        .append("' order by seqNo desc");
        List<TWTSensor> list = daoUtils.find(sql.toString());
        return list ;
    }
    /**
     * <li>说明：置底
     * <li>创建人：程梅
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveBottom(String idx) throws Exception {
        TWTSensor entity = this.getModelById(idx);
        List<TWTSensor> maxList = getMaxSeqNo(entity.getStationCode());
        if(null != maxList && maxList.size() > 1){
            int count = maxList.size();
            // 获取被【置底】记录被置底前，在其后的所有记录
            String hql = "From TWTSensor Where seqNo > ? and stationCode = ?";
            List<TWTSensor> list = this.daoUtils.find(hql, new Object[] { entity.getSeqNo(),entity.getStationCode() });
            // 设置被【置底】记录的排序号为当前记录总数
            entity.setSeqNo(count);
            List<TWTSensor> entityList = new ArrayList<TWTSensor>();
            entityList.add(entity);
            // 设置其后的所有记录的排序后依次减一
            for (TWTSensor recordCard : list) {
                recordCard.setSeqNo(recordCard.getSeqNo() - 1);
                entityList.add(recordCard);
            }
            super.saveOrUpdate(entityList);
        }
    }
    
    /**
     * <li>说明：下移
     * <li>创建人：程梅
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    public void updateMoveDown(String idx) throws Exception {
        TWTSensor entity = this.getModelById(idx);
        String hql = "From TWTSensor Where seqNo = ? and stationCode = ?";
        // 获取被【下移】记录被下移前，紧随其后的记录
        TWTSensor nextEntity =
            (TWTSensor) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() + 1,entity.getStationCode() });
        List<TWTSensor> entityList = new ArrayList<TWTSensor>(2);
        // 设置被【下移】记录的排序号+1
        entity.setSeqNo(entity.getSeqNo() + 1);
        entityList.add(entity);
        // 设置被【下移】记录后的记录的排序号-1
        nextEntity.setSeqNo(nextEntity.getSeqNo() - 1);
        entityList.add(nextEntity);
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：置顶
     * <li>创建人：程梅
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveTop(String idx) throws Exception {
        TWTSensor entity = this.getModelById(idx);
        // 获取被【置顶】记录被置顶前，在其前的所有记录
        String hql = "From TWTSensor Where seqNo < ? and stationCode = ?";
        List<TWTSensor> list = this.daoUtils.find(hql, new Object[] { entity.getSeqNo() ,entity.getStationCode()});
        // 设置被【置顶】记录的排序号为1
        entity.setSeqNo(1);
        List<TWTSensor> entityList = new ArrayList<TWTSensor>();
        entityList.add(entity);
        // 设置其后的所有记录的排序后一次加一
        for (TWTSensor recordCard : list) {
            recordCard.setSeqNo(recordCard.getSeqNo() + 1);
            entityList.add(recordCard);
        }
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：上移
     * <li>创建人：程梅
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    public void updateMoveUp(String idx) throws Exception {
        TWTSensor entity = this.getModelById(idx);
        String hql = "From TWTSensor Where seqNo = ? and stationCode = ?";
        // 获取被【上移】记录被上移移前，紧随其前的记录
        TWTSensor nextEntity =
            (TWTSensor) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() - 1 ,entity.getStationCode()});
        List<TWTSensor> entityList = new ArrayList<TWTSensor>(2);
        // 设置被【上移】记录的排序号-1
        entity.setSeqNo(entity.getSeqNo() - 1);
        entityList.add(entity);
        // 设置被【上移】记录前的记录的排序号+1
        nextEntity.setSeqNo(nextEntity.getSeqNo() + 1);
        entityList.add(nextEntity);
        super.saveOrUpdate(entityList);
    }
}