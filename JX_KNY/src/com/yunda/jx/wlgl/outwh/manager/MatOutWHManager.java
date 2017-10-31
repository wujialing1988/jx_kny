package com.yunda.jx.wlgl.outwh.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation;
import com.yunda.jx.pjwz.partsBase.warehouse.manager.WarehouseLocationManager;
import com.yunda.jx.wlgl.expend.entity.MatTrainExpendAccount;
import com.yunda.jx.wlgl.expend.manager.MatTrainExpendAccountManager;
import com.yunda.jx.wlgl.inwh.entity.MatInWHNew;
import com.yunda.jx.wlgl.inwh.manager.MatInWHNewManager;
import com.yunda.jx.wlgl.movewh.entity.MatMoveWH;
import com.yunda.jx.wlgl.movewh.manager.MatMoveWHManager;
import com.yunda.jx.wlgl.outwh.entity.MatOutWH;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockManager;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockQueryManager;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatOutWH业务类,出库单
 * <li>创建人：程梅
 * <li>创建日期：2016-05-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="matOutWHManager")
public class MatOutWHManager extends JXBaseManager<MatOutWH, MatOutWH>{
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
    //移库单业务类
    @Resource
    private MatMoveWHManager matMoveWHManager ;
    //入库单单业务类
    @Resource
    private MatInWHNewManager matInWHNewManager;

    /**
     * 
     * <li>说明：新增物料出库
     * <li>创建人：程梅
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param outWh 出库信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void saveMatOutWH(MatOutWH outWh) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException{
        OmEmployee emp = SystemContext.getOmEmployee();
        if (null != emp){
            outWh.setExWhEmpID(emp.getEmpid());
            outWh.setExWhEmp(emp.getEmpname());
        }
        outWh.setWhDate(new Date());
        super.saveOrUpdate(outWh);//保存出库信息
        WarehouseLocation location = warehouseLocationManager.getModelById(outWh.getLocationIdx());
        //如果库位状态发生了改变，则更新库位信息
        if(null != location && !location.getStatus().equals(outWh.getStatus())){
            location.setStatus(outWh.getStatus());
            warehouseLocationManager.saveOrUpdate(location);
        }
        //根据库房id、物料编码、物料类型、库位查询库存台账信息
        MatStock matStock = matStockQueryManager.getModelStockByLocation(outWh.getWhIDX(), outWh.getMatCode(), outWh.getMatType(), outWh.getLocationName());
        if(null != matStock){
            matStock.setQty(matStock.getQty() - outWh.getQty()) ;//库存数量减少
            //更新库存数量
            matStockManager.saveOrUpdate(matStock) ;
        }else{
            throw new BusinessException("出库数量超出了库存数量，请重新录入！");
        }
        //出库类型为【出库到机车】，则新增消耗记录
        if(MatOutWH.TYPE_CKDJC.equals(outWh.getWHType())){
            MatTrainExpendAccount account = new MatTrainExpendAccount();
            BeanUtils.copyProperties(account, outWh) ;
            account.setXcId(outWh.getXcID());
            account.setRtId(outWh.getRtID());
            account.setExpendOrgId(outWh.getGetOrgID());
            account.setExpendOrgSeq(outWh.getGetOrgSeq());
            account.setExpendOrg(outWh.getGetOrg());
            account.setExpendDate(outWh.getWhDate());
            account.setIdx(null);
            account.setDataSource(MatTrainExpendAccount.SOURCE_OUTWH) ;//数据来源 - 出库到机车
            account.setSourceIx(outWh.getIdx());//数据来源id--出库单id
            if (null != emp){
                account.setRegistEmpID(emp.getEmpid());
                account.setRegistEmp(emp.getEmpname());
            }
            account.setRegistDate(new Date());
            matTrainExpendAccountManager.saveOrUpdate(account);
        }else if(MatOutWH.TYPE_YK.equals(outWh.getWHType())){  //出库类型为【移库】，则新增移库记录
                MatMoveWH moveWH = new MatMoveWH();
                moveWH.setOutWhIdx(outWh.getIdx());
                moveWH.setGetWhIDX(outWh.getGetWhIDX());
                moveWH.setGetWhName(outWh.getGetWhName());
                moveWH.setMovestatus(MatMoveWH.STATUS_WAITCHECK);//移库状态--待确认移入
                matMoveWHManager.saveOrUpdate(moveWH) ;//新增移库单记录
            }
    }
    
    /**
     * <li>说明：查找待移入确认的列表
     * 
     * <li>创建人：张迪
     * <li>创建日期：2016-5-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity
     * @return
     */
    public Page<MatOutWH> findMoveInList(SearchEntity<MatOutWH> searchEntity) {
        StringBuilder sb = new StringBuilder();
        sb.append("select new MatOutWH(m.idx, m.getWhIDX, m.getWhName, o.matType, o.matCode, o.matDesc, o.unit, o.qty, o.whIDX, o.whName, o.locationName, o.whDate, o.exWhEmp, m.movestatus) "
                +"from MatMoveWH m, MatOutWH o Where m.outWhIdx = o.idx "
                +"and m.movestatus = '").append(MatMoveWH.STATUS_WAITCHECK).append("'");  
        // 排序字段
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            sb.append(" Order By ");
            sb.append(orders[0].toString());
            for (int i = 1; i < orders.length; i++) {
                sb.append(", ");
                sb.append(orders[i].toString());
            }
        }
        String hql = sb.toString();
        String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("from"));
        Page<MatOutWH> page = findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
        return page;
    }

    /**
     * <li>说明：获取待退回原库的列表信息
     * <li>创建人：张迪
     * <li>创建日期：2016-5-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity
     * @return
     */
    public Page<MatOutWH> findBackMoveInList(SearchEntity<MatOutWH> searchEntity) {
        StringBuilder sb = new StringBuilder();
        sb.append("select new MatOutWH(m.idx, m.getWhIDX, m.getWhName, o.matType, o.matCode, o.matDesc, o.unit, o.qty, o.whIDX, o.whName, o.locationName, o.whDate, o.exWhEmp, m.movestatus) "
                +"from MatMoveWH m, MatOutWH o Where m.outWhIdx = o.idx "
                +"and m.movestatus = '").append(MatMoveWH.STATUS_BACKWAITCHECK).append("'"); 
        // 排序字段
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            sb.append(" Order By ");
            sb.append(orders[0].toString());
            for (int i = 1; i < orders.length; i++) {
                sb.append(", ");
                sb.append(orders[i].toString());
            }
        }
        String hql = sb.toString();
        String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("from"));
        return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }

    /**
     * <li>说明：称库中状态为待退回原库的入库确认
     * <li>创建人：张迪
     * <li>创建日期：2016-5-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param matOutWH 入库单
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void saveMoveBackInMat(MatOutWH matOutWH) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
            OmEmployee emp = SystemContext.getOmEmployee();    
            //封装入库数据
            MatInWHNew matInWHNew = new MatInWHNew();
            matInWHNew.setMatType(matOutWH.getMatType());
            matInWHNew.setMatCode(matOutWH.getMatCode());
            matInWHNew.setWhIDX(matOutWH.getWhIDX());
            matInWHNew.setWhName(matOutWH.getWhName());
            matInWHNew.setLocationIdx(null);
            matInWHNew.setQty(matOutWH.getQty());
            matInWHNew.setLocationName(matOutWH.getLocationName());
            matInWHNew.setInWhType(MatInWHNew.TYPE_YKTH);
            // 保存入库信息
            matInWHNewManager.saveMatInWHNew(matInWHNew);
           //  修改移库单状态
            MatMoveWH matMoveWH = new MatMoveWH();
            matMoveWH = matMoveWHManager.getModelById(matOutWH.getMoveIdx());
            if(null != matMoveWH){
                matMoveWH.setMovestatus(MatMoveWH.STATUS_BACKCHECKED);
                matMoveWH.setCheckDate(new Date());
                if (null != emp){
                    matMoveWH.setCheckEmp(emp.getEmpname());
                    matMoveWH.setCheckEmpID(emp.getEmpid());
                }
                matMoveWHManager.saveOrUpdate(matMoveWH);
            }      
            
        }
           
    
}