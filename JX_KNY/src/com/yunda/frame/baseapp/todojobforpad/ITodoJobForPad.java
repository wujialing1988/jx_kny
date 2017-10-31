package com.yunda.frame.baseapp.todojobforpad;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: iPad应用功能权限接口
 * <li>创建人：何涛
 * <li>创建日期：2015-7-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface ITodoJobForPad {
    
    /** 临碎修提票 - ID */
    String FUNC_LSXTP_MID = "pad_lsxtp";            
    /** 临碎修提票 - 图标 */
    String FUNC_LSXTP_ICON = "R";            
    /** 临碎修提票 - 功能名称 */
    String FUNC_LSXTP_NAME = "临碎修提票";     
    
    /** 临修活处理 - ID */
    String FUNC_LXHCL_MID = "pad_lxhcl";
    /** 临修活处理 - 图标 */
    String FUNC_LXHCL_ICON = "W";
    /** 临修活处理 - 功能名称 */
    String FUNC_LXHCL_NAME = "临修活处理";
    
    /** 碎修活处理 - ID */
    String FUNC_SXHCL_MID = "pad_sxhcl";
    /** 碎修活处理 - 图标 */
    String FUNC_SXHCL_ICON = "W";
    /** 碎修活处理 - 功能名称 */
    String FUNC_SXHCL_NAME = "碎修活处理";
    
    /** 临碎修质检处理 - ID */
    String FUNC_LSXJY_MID = "pad_lsxjy";
    /** 临碎修质检处理 - 图标 */
    String FUNC_LSXJY_ICON = "p";
    /** 临碎修质检处理 - 功能名称 */
    String FUNC_LSXJY_NAME = "临碎修检验";
    
    /** 整备作业工单处理 - ID */
    String FUNC_ZBZYGD_MID = "pad_zbzygd";
    /** 整备作业工单处理 - 图标 */
    String FUNC_ZBZYGD_ICON = "I";
    /** 整备作业工单处理 - 功能名称 */
    String FUNC_ZBZYGD_NAME = "范围活处理";
    
    /** 机车检修作业工单处理 - ID */
    String FUNC_JXZYGD_MID = "pad_jxzygd";
    /** 机车检修作业工单处理 - 图标 */
    String FUNC_JXZYGD_ICON = "y";
    /** 机车检修作业工单处理 - 功能名称 */
    String FUNC_JXZYGD_NAME = "作业工单";
    
    /** 质量检查处理 - ID */
    String FUNC_ZLJC_MID = "pad_zljc";
    /** 质量检查处理 - 图标 */
    String FUNC_ZLJC_ICON = "p";
    /** 质量检查处理 - 功能名称 */
    String FUNC_ZLJC_NAME = "质量检查";
    
    /** 检查提票 - ID */
    String FUNC_JCTP_MID = "pad_jctp";
    /** 检查提票 - 图标 */
    String FUNC_JCTP_ICON = "R";
    /** 检查提票 - 功能名称 */
    String FUNC_JCTP_NAME = "检查提票";
    
    /** 提票调度派工 - ID */
    String FUNC_TPDDPG_MID = "pad_tpddpg";
    /** 提票调度派工 - 图标 */
    String FUNC_TPDDPG_ICON = "g";
    /** 提票调度派工 - 功能名称 */
    String FUNC_TPDDPG_NAME = "提票调度派工";
    
    /** 提票工长派工 - ID */
    String FUNC_TPGZPG_MID = "pad_tpgzpg";
    /** 提票工长派工 - 图标 */
    String FUNC_TPGZPG_ICON = "g";
    /** 提票工长派工 - 功能名称 */
    String FUNC_TPGZPG_NAME = "提票工长派工";
    
    /** 检查提票处理 - ID */
    String FUNC_JCTPCL_MID = "pad_jctpcl";
    /** 检查提票处理 - 图标 */
    String FUNC_JCTPCL_ICON = "W";
    /** 检查提票处理 - 功能名称 */
    String FUNC_JCTPCL_NAME = "检查提票处理";
    
    /** 检查提票质量检验 - ID */
    String FUNC_JCTPZJ_MID = "pad_jctpzj";
    /** 检查提票质量检验 - 图标 */
    String FUNC_JCTPZJ_ICON = "p";
    /** 检查提票质量检验 - 功能名称 */
    String FUNC_JCTPZJ_NAME = "提票质量检验";
    
    /** 配件检修处理 - ID */
    String FUNC_PJJXCL_MID = "pad_pjjxcl";
    /** 配件检修处理 - 图标 */
    String FUNC_PJJXCL_ICON = "x";
    /** 配件检修处理 - 功能名称 */
    String FUNC_PJJXCL_NAME = "配件检修处理";
    
    /** 配件检修质检 - ID */
    String FUNC_PJJXZJ_MID = "pad_pjjxzj";
    /** 配件检修质检 - 图标 */
    String FUNC_PJJXZJ_ICON = "x";
    /** 配件检修质检 - 功能名称 */
    String FUNC_PJJXZJ_NAME = "配件检修质检";
    
    /** 配件检修质检（新） - ID */
    String FUNC_PJJXZJN_MID = "pad_pjjxzjn";
    /** 配件检修质检（新） - 图标 */
    String FUNC_PJJXZJN_ICON = "x";
    /** 配件检修质检（新） - 功能名称 */
    String FUNC_PJJXZJN_NAME = "配件检修质检";
    
    /** 机车检修质检（新） - ID */
    String FUNC_JCZLJC_MID = "pad_jczljc";
    /** 机车检修质检（新） - 图标 */
    String FUNC_JCZLJC_ICON = "x";
    /** 机车检修质检（新） - 功能名称 */
    String FUNC_JCZLJC_NAME = "机车检修质检";
    
    /** 配件检修验收 - ID */
    String FUNC_PJJXYS_MID = "pad_pjjxys";
    /** 配件检修验收 - 图标 */
    String FUNC_PJJXYS_ICON = "x";
    /** 配件检修验收 - 功能名称 */
    String FUNC_PJJXYS_NAME = "配件检修验收";
    
    /** 配件检修进度 - ID */
    String FUNC_PJJXJD_MID = "pad_pjjxjd";
    /** 配件检修进度 - 图标 */
    String FUNC_PJJXJD_ICON = "x";
    /** 配件检修进度 - 功能名称 */
    String FUNC_PJJXJD_NAME = "配件检修进度";
    
    /** 下车配件登记 - ID */
    String FUNC_XCPJDJ_MID = "pad_xcpjdj";
    /** 下车配件登记 - 图标 */
    String FUNC_XCPJDJ_ICON = "n";
    /** 下车配件登记 - 功能名称 */
    String FUNC_XCPJDJ_NAME = "下车配件登记";
    
    /** 上车配件登记 - ID */
    String FUNC_SCPJDJ_MID = "pad_scpjdj";
    /** 上车配件登记 - 图标 */
    String FUNC_SCPJDJ_ICON = "n";
    /** 上车配件登记 - 功能名称 */
    String FUNC_SCPJDJ_NAME = "上车配件登记";
    
    /** 整备下车配件登记 - ID */
    String FUNC_ZBXCPJDJ_MID = "pad_zbxcpjdj";
    /** 整备下车配件登记 - 图标 */
    String FUNC_ZBXCPJDJ_ICON = "n";
    /** 整备下车配件登记 - 功能名称 */
    String FUNC_ZBXCPJDJ_NAME = "整备下车配件登记";
    
    /** 整备上车配件登记 - ID */
    String FUNC_ZBSCPJDJ_MID = "pad_zbscpjdj";
    /** 整备上车配件登记 - 图标 */
    String FUNC_ZBSCPJDJ_ICON = "n";
    /** 整备上车配件登记 - 功能名称 */
    String FUNC_ZBSCPJDJ_NAME = "整备上车配件登记";
    
    /** 良好配件登记 - ID */
    String FUNC_LHPJDJ_MID = "pad_lhpjdj";
    /** 良好配件登记 - 图标 */
    String FUNC_LHPJDJ_ICON = "n";
    /** 良好配件登记 - 功能名称 */
    String FUNC_LHPJDJ_NAME = "良好配件登记";
    
    /** 配件委外登记 - ID */
    String FUNC_PJWWDJ_MID = "pad_pjwwdjzxjc";
    /** 配件外登记 - 图标 */
    String FUNC_PJWWDJ_ICON = "n";
    /** 配件委外登记 - 功能名称 */
    String FUNC_PJWWDJ_NAME = "配件委外登记";
    
    /** 配件入库 - ID */
    String FUNC_XJPJRK_MID = "pad_xjpjrk";
    /** 配件入库 - 图标 */
    String FUNC_XJPJRK_ICON = "n";
    /** 配件入库 - 功能名称 */
    String FUNC_XJPJRK_NAME = "配件入库";
    
    /** 配件出库登记 - ID */
    String FUNC_PJCKDJ_MID = "pad_pjckdj";
    /** 配件出库登记 - 图标 */
    String FUNC_PJCKDJ_ICON = "n";
    /** 配件出登记库 - 功能名称 */
    String FUNC_PJCKDJ_NAME = "配件出库登记";
    
    /** 配件报废登记 - ID */
    String FUNC_PJBFDJ_MID = "pad_pjbfdj";
    /** 配件报废登记 - 图标 */
    String FUNC_PJBFDJ_ICON = "n";
    /** 配件报废登记 - 功能名称 */
    String FUNC_PJBFDJ_NAME = "配件报废登记";
    
    /** 配件调出登记 - ID */
    String FUNC_PJDCDJ_MID = "pad_pjdcdj";
    /** 配件调出登记 - 图标 */
    String FUNC_PJDCDJ_ICON = "n";
    /** 配件调出登记 - 功能名称 */
    String FUNC_PJDCDJ_NAME = "配件调出登记";
    
    /** 配件信息查询 - ID */
    String FUNC_PJXXCX_MID = "pad_pjxxcx";
    /** 配件信息查询 - 图标 */
    String FUNC_PJXXCX_ICON = "n";
    /** 配件信息查询 - 功能名称 */
    String FUNC_PJXXCX_NAME = "配件信息查询";
    
    /** 配件识别码绑定 - ID */
    String FUNC_PJSBM_MID = "pad_pjsbm";
    /** 配件识别码绑定 - 图标 */
    String FUNC_PJSBM_ICON = "n";
    /** 配件识别码绑定 - 功能名称 */
    String FUNC_PJSBM_NAME = "配件识别码绑定";
    
    /** 配件退库登记 - ID */
    String FUNC_PJTKDJ_MID = "pad_pjtkdj";
    /** 配件退库登记 - 图标 */
    String FUNC_PJTKDJ_ICON = "n";
    /** 配件退库登记 - 功能名称 */
    String FUNC_PJTKDJ_NAME = "配件退库登记";
    
    /** 配件销账登记 - ID */
    String FUNC_PJXZDJ_MID = "pad_pjxzdj";
    /** 配件销账登记 - 图标 */
    String FUNC_PJXZDJ_ICON = "n";
    /** 配件销账登记 - 功能名称 */
    String FUNC_PJXZDJ_NAME = "配件销账登记";
    
    /** 配件拆卸登记 - ID */
    String FUNC_PJCXDJ_MID = "pad_pjcxdj";
    /** 配件拆卸登记 - 图标 */
    String FUNC_PJCXDJ_ICON = "n";
    /** 配件拆卸登记 - 功能名称 */
    String FUNC_PJCXDJ_NAME = "配件拆卸登记";
    
    /** 配件安装登记 - ID */
    String FUNC_PJAZDJ_MID = "pad_pjazdj";
    /** 配件安装登记 - 图标 */
    String FUNC_PJAZDJ_ICON = "n";
    /** 配件拆安装登记 - 功能名称 */
    String FUNC_PJAZDJ_NAME = "配件安装登记";
    
    /** 配件检修记录单识别码绑定 - ID */
    String FUNC_JXSBM_MID = "pad_jxsbm";
    /** 配件检修记录单识别码绑定 - 图标 */
    String FUNC_JXSBM_ICON = "n";
    /** 配件检修记录单识别码绑定 - 功能名称 */
    String FUNC_JXSBM_NAME = "配件检修记录单识别码绑定";
    
    /** 配件委外回段 - ID */
    String FUNC_PJWWHD_MID = "pad_pjwwhdzxjc";
    /** 配件委外回段 - 图标 */
    String FUNC_PJWWHD_ICON = "n";
    /** 配件委外回段 - 功能名称 */
    String FUNC_PJWWHD_NAME = "配件委外回段";
    
    /** 配件移库登记 - ID */
    String FUNC_PJYKDJ_MID = "pad_pjykdj";
    /** 配件移库登记 - 图标 */
    String FUNC_PJYKDJ_ICON = "n";
    /** 配件移库登记 - 功能名称 */
    String FUNC_PJYKDJ_NAME = "配件移库登记";
    
    /** 配件校验 - ID */
    String FUNC_PJJY_MID = "pad_pjjy";
    /** 配件校验 - 图标 */
    String FUNC_PJJY_ICON = "n";
    /** 配件校验 - 功能名称 */
    String FUNC_PJJY_NAME = "配件校验";
    
    /** 配件交接 - ID */
    String FUNC_PJJJ_MID = "pad_pjjj";
    /** 配件交接 - 图标 */
    String FUNC_PJJJ_ICON = "n";
    /** 配件交接 - 功能名称 */
    String FUNC_PJJJ_NAME = "配件交接";
    
    /** 配件售后入段 - ID */
    String FUNC_PJSHRD_MID = "pad_pjshrd";
    /** 配件售后入段 - 图标 */
    String FUNC_PJSHRD_ICON = "n";
    /** 配件售后入段 - 功能名称 */
    String FUNC_PJSHRD_NAME = "配件售后入段";
    
    /** 配件售后出段 - ID */
    String FUNC_PJSHCD_MID = "pad_pjshcd";
    /** 配件售后出段 - 图标 */
    String FUNC_PJSHCD_ICON = "n";
    /** 配件售后出段 - 功能名称 */
    String FUNC_PJSHCD_NAME = "配件售后出段";
    
}
