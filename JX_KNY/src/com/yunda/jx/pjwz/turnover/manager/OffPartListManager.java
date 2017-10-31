package com.yunda.jx.pjwz.turnover.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPNodeFlowSheetBean;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNode;
import com.yunda.jx.pjwz.turnover.entity.OffPartList;
import com.yunda.jx.pjwz.turnover.entity.OffPartListFlowSheetBean;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 下车配件清单业务处理类
 * <li>创建人：张迪
 * <li>创建日期：2016-7-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="offPartListManager")
public class OffPartListManager extends JXBaseManager<OffPartList, OffPartList>{
    private String ROOT_0 = "ROOT_0";
    /**
     * <li>说明：获取下车配件清单
     * <li>创建人：张迪
     * <li>创建日期：2017-2-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修idx
     * @return 下车配件清单
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<OffPartListFlowSheetBean> getOffPartListByWorkPlanIDX(String workPlanIDX) throws Exception {
        
        String sql = "  select  rownum as row_num, a.* from ( select distinct m.parts_type_idx, m.specification_model, m.WP_IDX,   l.parts_idx,  l.parts_name,  l.off_node_case_idx, "
                   + " l.off_node_name, l.on_node_case_idx,  l.on_node_name,   l.train_type_idx,  l.train_type_shortname, l.train_no,  l.work_plan_idx "
                   + " from (select pt.jcpjbm,   t.unload_place,  t.idx,  t.parts_name,   t.parts_no,  t.rdp_idx,  t.parts_account_idx,   t.specification_model, "
                   + "  t.parts_type_idx, R.WP_IDX   " +
                        "from PJWZ_PARTS_UNLOAD_REGISTER t left join PJWZ_Parts_Type pt on t.parts_type_idx = pt.idx" +
                        " INNER JOIN   PJJX_Parts_Rdp R ON   T.PARTS_ACCOUNT_IDX = R.PARTS_ACCOUNT_IDX   AND T.PARTS_ACCOUNT_IDX IS NOT NULL AND R.PARTS_ACCOUNT_IDX IS NOT NULL AND   R.STATUS != '" + PartsRdp.STATUS_YZZ 
                   + "') m left join jxgc_off_parts_list l on (m.Rdp_Idx = l.work_plan_idx AND (l.wzmc is not null and  m.jcpjbm = l.parts_idx and  m.Unload_Place = l.wzmc) "
                   + " or (m.Rdp_Idx = l.work_plan_idx AND   l.wzmc is null and  m.Unload_Place is null and  m.jcpjbm = l.parts_idx))"
                   + " where   l.on_node_case_idx is not null   and l.off_node_case_idx is not null and   l.Work_Plan_IDX = '" + workPlanIDX + "'  )a ";
        // 查询已经兑现的配件信息
        List<OffPartListFlowSheetBean> entityList = daoUtils.executeSqlQueryEntity(sql, OffPartListFlowSheetBean.class); 
        if(null != entityList && entityList.size() > 0){
            String nodesql = "select N.*, S.Pre_WP_Node_IDX, S1.WP_Node_IDX AS NEXT_WP_Node_IDX FROM PJJX_WP_Node N  left join PJJX_WP_Node_Seq S on N.idx = S.WP_Node_IDX and S.record_status = 0 " +
                    " left join   PJJX_WP_Node_Seq S1 on N.idx = S1.Pre_WP_Node_IDX and S1.record_status = 0 " +
                    " WHERE N.WP_IDX ='#wpIDX#' And (N.PARENT_WP_NODE_IDX Is Null Or N.PARENT_WP_NODE_IDX ='ROOT_0')"+
                    "  AND N.RECORD_STATUS = 0 Order By N.SEQ_NO ASC "; 
            
            String rdpNodesql = "select  N.WP_NODE_IDX, N.STATUS  FROM PJJX_Parts_Rdp_Node N " +
                            " WHERE  N.RDP_IDX in( select idx  from PJJX_Parts_Rdp pr" +
                            " where pr.parts_account_idx in (select pp.parts_account_idx  from pjwz_parts_unload_register pp  " +
                            " where pp.rdp_idx = '" + workPlanIDX + "') and pr.specification_model " +
                            " in(select sm.specification_model  from pjwz_parts_type sm where sm.jcpjbm = '#partsIDX#') AND PR.STATUS !='" + PartsRdp.STATUS_YZZ +
                            "')  AND (N.PARENT_WP_NODE_IDX Is Null   Or N.PARENT_WP_NODE_IDX ='" + ROOT_0 +
                            "') AND N.RECORD_STATUS = 0 Order By  N.PLAN_STARTTIME, N.SEQ_NO ASC ";   
            
            String offOrOnNodesql = "select p.idx　from (select * from jxgc_job_process_node n start with n.idx = '#offOrOnNodeIDX#'"+
                                    " connect by prior n.parent_idx = n.idx) p where p.parent_idx is null or p.parent_idx = '" + ROOT_0 + "'";
            for (OffPartListFlowSheetBean entity : entityList) {
                // 配件上下车节点 如果为子节点，则根据上下车子节点查询父节点idx
                String  offNodesql = offOrOnNodesql.replace("#offOrOnNodeIDX#", entity.getOffNodeCaseIDX());
                String  onNodesql = offOrOnNodesql.replace("#offOrOnNodeIDX#", entity.getOnNodeCaseIDX());
                List<Object> offNodeCaseIDX = daoUtils.executeSqlQuery(offNodesql);
                List<Object> onNodeCaseIDX = daoUtils.executeSqlQuery(onNodesql);
                entity.setOffNodeCaseIDX(offNodeCaseIDX.get(0).toString());
                entity.setOnNodeCaseIDX(onNodeCaseIDX.get(0).toString());
                // 查询配件所对应的流程定义
                String  nodesqlre = nodesql.replace("#wpIDX#", entity.getWpIDX());
                List<WPNodeFlowSheetBean> wPNodeFlowSheets = daoUtils.executeSqlQueryEntity(nodesqlre, WPNodeFlowSheetBean.class);
                entity.setWpNodes(wPNodeFlowSheets); 
                
                // 查询流程节点所对应的状态
                String  rdpNodesqlre = rdpNodesql.replace("#partsIDX#", entity.getPartsIDX());
                List<Object[]> partNodeIDXs = daoUtils.executeSqlQuery(rdpNodesqlre);
                // 匹配流程定义中的节点实际所对应的节点的状态
                for(WPNodeFlowSheetBean wPNodeFlowSheet: wPNodeFlowSheets){
                    int[] counts = new int[3];
                    for(Object[] partNodeIDX: partNodeIDXs){
                        if(wPNodeFlowSheet.getIdx().equals(partNodeIDX[0])){
                            if(partNodeIDX[1].equals(PartsRdpNode.CONST_STR_STATUS_WCL)){
                                counts[0]++;
                                continue;
                            }else if(partNodeIDX[1].equals(PartsRdpNode.CONST_STR_STATUS_CLZ)){
                                counts[1]++;
                                continue;
                            }else if(partNodeIDX[1].equals(PartsRdpNode.CONST_STR_STATUS_YCL)){
                                counts[2]++;
                                continue;
                            }
                        }
                    }
                    String countStr = "<br>未开工数量：" + counts[0] + "<br>已开工数量：" + counts[1] + "<br>完工数量：" + counts[2];
                    wPNodeFlowSheet.setCountStr(countStr);
                    if(counts[0]>0) wPNodeFlowSheet.setStatus(PartsRdpNode.CONST_STR_STATUS_WCL);
                    else if(counts[1] > 0)  wPNodeFlowSheet.setStatus(PartsRdpNode.CONST_STR_STATUS_CLZ);
                    else if(counts[2] > 0)  wPNodeFlowSheet.setStatus(PartsRdpNode.CONST_STR_STATUS_YCL);
                }
            }
        }
        return entityList;
    }


   
}
