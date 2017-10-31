package com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.manager;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.entity.Taskins;
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: Taskins业务类, 立体仓库_作业任务表
 * <li>创建人：何涛
 * <li>创建日期：2015-12-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2.4
 */
@Service(value="taskinsManager")
public class TaskinsManager extends JXBaseManager<Taskins, Taskins> {
    
    /** CodeRuleConfig业务类,业务编码规则配置 */
    @Resource
    private CodeRuleConfigManager codeRuleConfigManager;
    
    @Override
    public Page<Taskins> findPageList(SearchEntity<Taskins> searchEntity) throws BusinessException{
        Taskins entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder("From Taskins ");
        if (!StringUtil.isNullOrBlank(entity.getExecFlag())) {
             sb.append("  where execFlag In ('").append(entity.getExecFlag().replace(",", "','")).append("')");
        }     
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            sb.append( HqlUtil.getOrderHql(orders));
        }else{
            sb.append(" order by inscreateTime DESC");
        } 
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        return super.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());      
    }
    /**
     * <li>说明：生成立方体仓库指令
     * <li>创建人： 张迪
     * <li>创建日期： 2016-5-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 立体仓库作业任务表实体对象
     * @return 已存储的立体仓库作业任务表实体对象
     * @throws BusinessException
     */ 
    public Taskins insert(Taskins entity) throws BusinessException {     
        // 根据业务编码规则自动生成“任务号”
        entity.setTaskNo(Integer.parseInt(this.codeRuleConfigManager.makeConfigRule(Taskins.CODE_RULE_TASK_NO)));
        // 计算输送机号
//        Integer deckId= Integer.parseInt(entity.getGat()) *2 -(2-Integer.parseInt(entity.getRow()))  ;   
//        entity.setDeckId(String.valueOf(deckId*100+1)); 
        entity.setDeckId(entity.getDeckId()+"01"); 
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");      
        entity.setInscreateTime(parse.format(new Date()));
        entity.setInsType(Taskins.CONST_STR_INS_TYPE);
        entity.setExecFlag(Taskins.CONST_STR_EXEC_FLAG_WZX);
        return super.insert(entity);

    }
  
    /**
     * <li>说明：验证货位号是否被占用
     * <li>创建人：张迪
     * <li>创建日期：2016-5-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 立体体仓库实体
     * @return 返回验证信息
     */
    @SuppressWarnings("unchecked")
    public String[] validateInsert(Taskins entity){
        try {
            String deckId = entity.getDeckId();
            Integer gat = (Integer.parseInt(deckId)+1)/2;
            Integer row = (Integer.parseInt(deckId)+1)%2 + 1;
            String cargoNo = gat.toString() + "-" + row.toString() +"-" +  entity.getColumn() + "-" + entity.getLevel();
            String str = "From Taskins Where execFlag in ('"+ Taskins.CONST_STR_EXEC_FLAG_WZX + "', '" + Taskins.CONST_STR_EXEC_FLAG_ZTZX + "', '" 
                        +  Taskins.CONST_STR_EXEC_FLAG_ZX + "') And cargoNo = '"+ cargoNo + "'";
            List<Taskins> taskinsList = this.daoUtils.find(str);
            if(null == taskinsList || taskinsList.isEmpty()){
                entity.setCargoNo(cargoNo);
                entity.setLaneWay(gat.toString());    
                return null;
            }
            else return new String[]{"此货位号:"+ cargoNo + "正在执行，请选择其它货位号"};
        } catch (Exception ex) {
             throw new BusinessException(ex);
         }
    }
    
    /**
     * <li>说明：验证货位号是否是未执行
     * <li>创建人：张迪
     * <li>创建日期：2016-5-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 文体仓库实体
     * @return 返回验证信息
     */
   @Override
   public String[] validateDelete(Serializable... ids){
       try {
           String hql = "From Taskins Where execFlag != '" + Taskins.CONST_STR_EXEC_FLAG_WZX + "' and taskNo = ?";
           List<Taskins> taskinsList = new ArrayList<Taskins>();
           for (Serializable id : ids) {
                 Taskins  item =  (Taskins)this.daoUtils.findSingle(hql, new Object[]{Integer.parseInt(id.toString())});
                 if(null != item)  {
                     taskinsList.add(item);
                 }
            }
           if(null != taskinsList && !taskinsList.isEmpty()){
               return new String[]{"执行标志为【未执行】的才能删除"};    
           }
           return null;
       } catch (Exception ex) {
            throw new BusinessException(ex);
        }
   }
        
}
