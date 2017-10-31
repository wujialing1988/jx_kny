package com.yunda.jx.pjjx.partsrdp.expendmat.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.jx.pjjx.partsrdp.expendmat.entity.PartsRdpExpendMat;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpExpendMat业务类,物料消耗记录
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpExpendMatManager")
public class PartsRdpExpendMatManager extends JXBaseManager<PartsRdpExpendMat, PartsRdpExpendMat>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：只能编辑消耗人为当前用户的记录
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 物料消耗记录实体
	 * @return String[] 验证消息
	 */
	@Override
	public String[] validateUpdate(PartsRdpExpendMat t) {
		// 当前登录用户基本信息
		OmEmployee omEmployee = SystemContext.getOmEmployee();
		if (t.getHandleEmpId().intValue() != omEmployee.getEmpid().intValue()) {
			return new String[]{"操作失败，您不可以修改其他人员的物料消耗！"};
		}
		return super.validateUpdate(t);
	}
	
	/**
	 * <li>说明：只能删除消耗人为当前用户的记录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 主键数组
	 * @return String[] 消息数组
	 */ 
	@Override
	public String[] validateDelete(Serializable... ids) {
		// 当前登录用户基本信息
		OmEmployee omEmployee = SystemContext.getOmEmployee();
		for (Serializable idx : ids) {
			PartsRdpExpendMat entity = this.getModelById(idx);
			if (entity.getHandleEmpId().intValue() != omEmployee.getEmpid().intValue()) {
				return new String[]{"操作失败，您不可以删除其他人员的物料消耗！"};
			}
		}
		return super.validateDelete(ids);
	}
	
	/**
	 * <li>说明：批量添加检修节点所需物料
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-11
	 * <li>修改人： 张迪
	 * <li>修改日期：2016-09-18
	 * <li>修改内容：
	 * 
	 * @param partsRdpExpendMats 选择的物料
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void saveExpendMats(PartsRdpExpendMat[] partsRdpExpendMats) throws BusinessException, NoSuchFieldException {
		// 当前登录用户基本信息
		OmEmployee omEmployee = SystemContext.getOmEmployee();
		// 当前登录用户组织结构信息
		OmOrganization omOrganization = SystemContext.getOmOrganization();
		List<PartsRdpExpendMat> entityList = new ArrayList<PartsRdpExpendMat>();
		// 在后台设置当前用户相关的信息
		for (PartsRdpExpendMat mat : partsRdpExpendMats) {
            // 在指定节点下，如果已经存在某一物料消耗，则只更新数量
            PartsRdpExpendMat partsRdpExpendMat = this.getModel(mat.getRdpNodeIDX(), mat.getMatCode());
            if (null != partsRdpExpendMat) {
//                partsRdpExpendMat.setQty(mat.getQty() + partsRdpExpendMat.getQty());
                partsRdpExpendMat.setQty(mat.getQty());//程锐修改 2015-11-16 物料消耗数量更新为前台传递的值
                partsRdpExpendMat.setHandleEmpId(omEmployee.getEmpid());          // 消耗人
                partsRdpExpendMat.setHandleEmpName(omEmployee.getEmpname());      // 消耗人名称
                
                partsRdpExpendMat.setHandleOrgId(omOrganization.getOrgid());      // 消耗班组
                partsRdpExpendMat.setHandleOrgName(omOrganization.getOrgname());  // 消耗班组名称
                partsRdpExpendMat.setHandleOrgSeq(omOrganization.getOrgseq());    // 消耗班组序列
                entityList.add(partsRdpExpendMat);
                continue;
            }else if(null != mat.getQty()&& 0 < mat.getQty()){
    			mat.setIdx(null);
                mat.setHandleEmpId(omEmployee.getEmpid());          // 消耗人
                mat.setHandleEmpName(omEmployee.getEmpname());      // 消耗人名称
                
                mat.setHandleOrgId(omOrganization.getOrgid());      // 消耗班组
                mat.setHandleOrgName(omOrganization.getOrgname());  // 消耗班组名称
                mat.setHandleOrgSeq(omOrganization.getOrgseq());    // 消耗班组序列			
    			entityList.add(mat);         
            }
		}
		this.saveOrUpdate(entityList);
	}
    
    /**
     * <li>说明：根据“节点主键”和“物料编码”获取【物料消耗记录】实体
     * <li>创建人：何涛
     * <li>创建日期：2015-2-3
     * <li>修改人： 张迪
     * <li>修改日期：2016-09-13
     * <li>修改内容：
     * @param rdpNodeIDX 节点主键
     * @param matCode 物料编码
     * @return PartsRdpExpendMat 物料消耗记录实体
     */
    public PartsRdpExpendMat getModel(String rdpNodeIDX, String matCode) {
        String hql = "From PartsRdpExpendMat Where recordStatus = 0 And rdpNodeIDX = ? And matCode = ?";
        return (PartsRdpExpendMat) this.daoUtils.findSingle(hql, new Object[]{rdpNodeIDX, matCode});
    }
	
	/**
	 * <li>说明：根据工艺工单主主键查询检修工艺工单所需物料集合
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpTecCardIDX 工艺工单主主键
	 * @return List 检修工艺工单所需物料集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpExpendMat> getModelByRdpTecCardIDX (String rdpTecCardIDX) {
		String hql = "From PartsRdpExpendMat Where recordStatus = 0 And rdpTecCardIDX = ?";
		return this.daoUtils.find(hql, new Object[]{rdpTecCardIDX});
	}
    
    /**
     * <li>说明：更新物料消耗数量
     * <li>创建人：何涛
     * <li>创建日期：2015-10-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param mats 物料消耗存储实体数组，形如：
     * [{
     *      idx: 'BBCF8B2A589B4E0D859DABD22B34A308',
     *      qty: 1
     * }]
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void updateExpendMats(PartsRdpExpendMat[] mats) throws BusinessException, NoSuchFieldException {
        List<PartsRdpExpendMat> entityList = new ArrayList<PartsRdpExpendMat>(mats.length);
        PartsRdpExpendMat prem = null;
        for (PartsRdpExpendMat mat : mats) {
            prem = this.getModelById(mat.getIdx());
            // 在原有物料消耗情况上增加消耗
            prem.setQty(prem.getQty() + mat.getQty());
            entityList.add(prem);
        }
        this.saveOrUpdate(entityList);
    }

    /**
     * <li>说明：一键修改物料消耗为默认
     * <li>创建人：何涛
     * <li>创建日期：2015-10-22
     * <li>修改人： 张凡
     * <li>修改日期：2015/10/23
     * <li>修改内容：将方法实现改为HQL更新
     * @param rdpIDX 作业主键
     * @param rdpTecCardIDX 配件检修记录单主键    
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void updateToDefault(String rdpIDX, String rdpTecCardIDX) throws BusinessException, NoSuchFieldException {
        /*List<PartsRdpExpendMat> entityList = this.getModels(rdpIDX, rdpTecCardIDX);
        for (PartsRdpExpendMat mat : entityList) {
            //  需要增加数据表字段，用于记录基础配置中物料消耗的额定数量
            mat.setQty(100d);
        }
        this.saveOrUpdate(entityList);*/
    	
    	String hql = "update PartsRdpExpendMat set qty = numberRated where rdpIDX = ? and rdpNodeIDX = ? and recordStatus = 0";
        daoUtils.execute(hql, rdpIDX, rdpTecCardIDX);
    }
    
    /* *
     * <li>说明：根据“作业主键”和“工艺工单主键”获取【物料消耗记录】实体集合
     * <li>创建人：何涛
     * <li>创建日期：2015-10-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 作业主键
     * @param rdpTecCardIDX 工艺工单主键
     * @return 【物料消耗记录】实体集合
     */
    /*@SuppressWarnings("unchecked")
    public List<PartsRdpExpendMat> getModels(String rdpIDX, String rdpTecCardIDX) {
        String hql = "From PartsRdpExpendMat Where rdpIDX = ? And rdpTecCardIDX = ? And recordStatus = 0";
        return this.daoUtils.find(hql, new Object[]{ rdpIDX, rdpTecCardIDX });
    }*/
	
}