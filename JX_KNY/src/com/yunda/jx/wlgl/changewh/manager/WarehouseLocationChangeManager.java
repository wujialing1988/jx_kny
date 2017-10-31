package com.yunda.jx.wlgl.changewh.manager;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation;
import com.yunda.jx.pjwz.partsBase.warehouse.manager.WarehouseLocationManager;
import com.yunda.jx.wlgl.changewh.entity.WarehouseLocationChange;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockManager;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockQueryManager;

@Service(value="warehouseLocationChangeManager")
public class WarehouseLocationChangeManager extends JXBaseManager<WarehouseLocationChange,WarehouseLocationChange> {
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    @Resource
    private MatStockQueryManager matStockQueryManager;
    @Resource
    private MatStockManager matStockManager;
    //库位业务类
    @Resource
    private WarehouseLocationManager warehouseLocationManager ;
    /**
     * <li>说明：调整库位数量方法
     * <li>创建人：张迪
     * <li>创建日期：2016-5-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param whlChange 库位调整实体
     * @throws BusinessException
     * @throws Exception
     */
    public String[] changeWhl(WarehouseLocationChange whlChange) throws BusinessException, Exception {       
        String locationName = whlChange.getLocationName();
        String locationNameCh = whlChange.getLocationNameChange();
        String matCode = whlChange.getMatCode();
        String matType = whlChange.getMatType();
        String whIdx = whlChange.getWhIDX();
        isChangeSatus(whlChange.getLocationIdx(), whlChange.getStatus()); //判断是否更新了原库位的状态
        isChangeSatus(whlChange.getChangeLocationIdx(), whlChange.getChangeStatus()); //判断是否更新了调整库位的状态
        //查询对应的台账信息
        MatStock mats = matStockQueryManager.getModelStockByLocation(whIdx, matCode, matType, locationName);
        MatStock matsCh = matStockQueryManager.getModelStockByLocation(whIdx, matCode, matType, locationNameCh);
        if(null == mats){
            return new String[]{"原库位没有该物料，不能调整！"};
        } 
        if(mats.getQty()< whlChange.getChangeQty()){
            return new String[]{"大于原库位物料现有数量，不能调整！"};
        }
        if(null == matsCh || matsCh.getIdx().isEmpty()){
            matsCh = new MatStock();
            matsCh.setLocationName(locationNameCh);
            matsCh.setMatCode(matCode);
            matsCh.setMatDesc(whlChange.getMatDesc());
            matsCh.setMatType(matType);
            matsCh.setQty(whlChange.getChangeQty());
            matsCh.setUnit(whlChange.getUnit());
            matsCh.setWhIdx(whIdx);
            matsCh.setWhName(whlChange.getWhName());
            matStockManager.insert(matsCh);
        }
        else {
            matsCh.setQty(matsCh.getQty()+ whlChange.getChangeQty());
        }
        // 修改原库位的数量
        mats.setQty(mats.getQty()- whlChange.getChangeQty());
        matStockManager.update(matsCh);
        matStockManager.update(mats);
        //保存修改信息
        whlChange.setChangeDate(new Date());
        super.saveOrUpdate(whlChange);
        return null;
    }
    
    /**
     * <li>说明：判断是否更新了库位状态
     * <li>创建人：张迪
     * <li>创建日期：2016-5-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param locationIdx 库位id
     * @param status 库位状态
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void isChangeSatus(String locationIdx, String status) throws BusinessException, NoSuchFieldException{
        if(null != locationIdx){
            WarehouseLocation location = warehouseLocationManager.getModelById(locationIdx);
            //如果库位状态发生了改变，则更新库位信息
            if(null != location && !location.getStatus().equals(status)){
                location.setStatus(status);
                warehouseLocationManager.saveOrUpdate(location);
            }
        }
    }
    
}
