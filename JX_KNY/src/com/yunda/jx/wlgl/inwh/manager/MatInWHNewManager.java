package com.yunda.jx.wlgl.inwh.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation;
import com.yunda.jx.pjwz.partsBase.warehouse.manager.WarehouseLocationManager;
import com.yunda.jx.wlgl.expend.entity.MatTrainExpendAccount;
import com.yunda.jx.wlgl.expend.manager.MatTrainExpendAccountManager;
import com.yunda.jx.wlgl.inwh.entity.MatInWHNew;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockManager;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockQueryManager;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatInWHNew业务类,入库单
 * <li>创建人：程梅
 * <li>创建日期：2016-05-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="matInWHNewManager")
public class MatInWHNewManager extends JXBaseManager<MatInWHNew, MatInWHNew>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    //库位业务类
    @Resource
    private WarehouseLocationManager warehouseLocationManager ;
    //库存台账查询业务类
    @Resource
    private MatStockQueryManager matStockQueryManager ;
    //库存台账业务类
    @Resource
    private MatStockManager matStockManager ;
    //机车用料消耗记录业务类
    @Resource
    private MatTrainExpendAccountManager matTrainExpendAccountManager ;
    /**
     * 
     * <li>说明：新增物料入库
     * <li>创建人：程梅
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param inWh 入库信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void saveMatInWHNew(MatInWHNew inWh) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException{
        OmEmployee emp = SystemContext.getOmEmployee();
        if (null != emp){
            inWh.setInWhEmpID(emp.getEmpid());
            inWh.setInWhEmp(emp.getEmpname());
        }
        inWh.setInWhDate(new Date());
        super.saveOrUpdate(inWh);//保存入库信息
        if(null!=inWh.getLocationIdx()){
            WarehouseLocation location = warehouseLocationManager.getModelById(inWh.getLocationIdx());
            //如果库位状态发生了改变，则更新库位信息
            if(null != location && !location.getStatus().equals(inWh.getStatus())){
                location.setStatus(inWh.getStatus());
                warehouseLocationManager.saveOrUpdate(location);
            }
        }
        MatStock matStock = matStockQueryManager.getModelStockByLocation(inWh.getWhIDX(), inWh.getMatCode(), inWh.getMatType(), inWh.getLocationName());
        if(null != matStock){
            matStock.setQty(matStock.getQty() + inWh.getQty()) ;//库存数据增加
        }else{
            //新增一条库存台账信息
            matStock = new MatStock();
            BeanUtils.copyProperties(matStock, inWh);
            matStock.setWhIdx(inWh.getWhIDX());
            matStock.setIdx(null) ;
        }
        matStockManager.saveOrUpdate(matStock) ;
        //入库类型为【机车退回入库】，则新增消耗记录
        if(MatInWHNew.TYPE_JCTH.equals(inWh.getInWhType())){
            MatTrainExpendAccount account = new MatTrainExpendAccount();
            BeanUtils.copyProperties(account, inWh) ;
            account.setXcId(inWh.getXcID());
            account.setRtId(inWh.getRtID());
            account.setExpendOrgId(inWh.getHandOverOrgID());
            account.setExpendOrgSeq(inWh.getHandOverOrgSeq());
            account.setExpendOrg(inWh.getHandOverOrg());
            account.setExpendDate(inWh.getInWhDate());
            account.setIdx(null);
            account.setDataSource(MatTrainExpendAccount.SOURCE_INWH) ;//数据来源 - 机车退回入库
            account.setSourceIx(inWh.getIdx());//数据来源id--入库单id
            if (null != emp){
                account.setRegistEmpID(emp.getEmpid());
                account.setRegistEmp(emp.getEmpname());
            }
            account.setRegistDate(new Date());
            int qty = account.getQty();
            qty = -qty ;
            account.setQty(qty) ;//数量为负数
            matTrainExpendAccountManager.saveOrUpdate(account);
        }
    }
}