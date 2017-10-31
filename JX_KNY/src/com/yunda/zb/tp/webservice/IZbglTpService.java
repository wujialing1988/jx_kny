package com.yunda.zb.tp.webservice;

import java.io.IOException;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 临碎修提票webservice接口
 * <li>创建人：程锐
 * <li>创建日期：2015-1-26
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface IZbglTpService {
    
    /**
     * <li>说明：获取承修车型
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 承修车型列表JSON字符串
     */
    public String getUndertakeTrainType();
    
    /**
     * <li>说明：根据车型获取车号列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 车号列表JSON字符串
     */
    public String getTrainNoByTrainType(String trainTypeIdx,String trainNo, int start, int limit);
    
    /**
     * <li>说明：根据车型车号获取组成树根节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @param trainNo 车号
     * @return 组成树根节点列表JSON字符串
     */
    public String getBuildUpType(String trainTypeIdx, String trainNo);
    
    /**
     * <li>说明：获取下级组成树节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级父节点idx
     * @param partsBuildUpTypeIdx 所属组成型号idx
     * @param parentPartsAccountIdx 上级安装配件信息主键
     * @param isVirtual 表示下层节点是否是虚拟位置下节点
     * @param buildUpPlaceFullCode 位置编码全名（从树节点获取）
     * @param trainNo 车号
     * @return 下级组成树节点列表JSON字符串
     */
	public String getBuildUpTypeTree(String parentIDX, 
									 String partsBuildUpTypeIdx, 
									 String parentPartsAccountIdx, 
									 String isVirtual, 
									 String buildUpPlaceFullCode, 
									 String trainNo);
	/**
     * <li>说明：通过组成位置主键获取故障现象数据
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpPlaceIdx 组成位置主键
     * @return 故障现象列表JSON字符串
     */
    public String getPlaceFault(String buildUpPlaceIdx);
    
    /**
     * <li>说明：提票录入处理
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param jsonData 提票信息Json集合（需要按照ZbglTp提票实体进行数据组装）
     * @param imgFilePath 上传照片地址，多个地址以","分隔
     * @return 操作结果
     */
    public String saveFaultNoticeFromGrid(Long operatorid, String jsonData, String imgFilePath);
    
    /**
     * <li>说明：获取故障字典码表数据分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param flbm 分类编码 查询条件
     * @param faultName 故障字典名称 查询条件
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 故障字典码表数据分页列表JSON字符串
     */
	public String findfaultList(String flbm, 
								String faultName,
								int start, 
								int limit);
	/**
     * <li>说明：上传故障提票照片
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param imgStr 以Base64编码的图片字符串
     * @return 保存在服务器上的临时文件全路径
     */
    public String uploadFaultImg(String imgStr);
    
    /**
     * <li>说明：获取专业类型分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 专业类型分页列表JSON字符串
     */
    public String getProfessionalType(int start, int limit);
    
    /**
     * 
     * <li>说明：查询提票活分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param repairClass 票活类型
     * @param faultNoticeStatus 待接活/待销活，见ZbglTp常量STATUS_DRAFT/STATUS_OPEN
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 提票活分页列表JSON字符串
     */
    public String queryTpList(String searchJson, 
                              String repairClass, 
                              String faultNoticeStatus,
                              Long operatorid,
                              int start, 
                              int limit);
    
    /**
     * <li>说明：领取提票活
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 提票活idx，多个idx用,分隔
     * @param repairClass 票活类型
     * @return 领取成功与否
     */
    public String receiveTp(Long operatorid, String idxs, String repairClass);
    
    /**
     * <li>说明：获取故障施修方法分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 故障施修方法分页列表JSON字符串
     */
    public String findFaultMethod(int start, int limit);
    
    /**
     * <li>说明：查询处理结果列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 处理结果列表JSON字符串
     */
    public String queryRepairResultList();
    
    /**
     * <li>说明：销活
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 提票活idx，多个idx用,分隔
     * @param tpData 处理提票信息JSON字符串
     * @return 销活成功与否
     */
    public String handleTp(Long operatorid, String idxs, String tpData);
    
    /**
     * <li>说明：撤销领活
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 提票活idx，多个idx用,分隔
     * @param repairClass 票活类型
     * @return 撤销领活成功与否
     */
    public String cancelReceivedTp(Long operatorid, String idxs, String repairClass);
    
    /**
     * <li>说明：读取故障提票上传的照片
     * <li>创建人：程锐
     * <li>创建日期：2015-3-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param attachmentKeyIDX 提票单IDX
     * @return 以Base64编码的图片字符串,如无对应图片则返回空字符串
     */
    public String downloadFaultImg(String attachmentKeyIDX);
    
    /**
     * <li>说明：查询临修处理结果列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 处理结果列表JSON字符串
     */
    public String queryLXRepairResultList();
    
    /**
     * <li>说明：批量保存故障现象
     * <li>创建人：王治龙
     * <li>创建日期：2013-5-4
     * <li>修改人： 王治龙
     * <li>修改日期：2013-12-26
     * <li>修改内容：封装返回值
     * @param operatorid 操作员ID
     * @param jsonData 故障现象Json集合（需要按照PlaceFault提票实体进行数据组装）
     * @return 操作结果
     * @throws IOException 
     */
    public String savePlaceFaultList(Long operatorid, String jsonData) throws IOException;
    
    /**
     * 
     * <li>说明：获取碎修、临修质量检验分页列表
     * <li>创建人：程梅
     * <li>创建日期：2015-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param repairClass 票活类型
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 碎修、临修质量检验分页列表
     */
    public String queryZbglTpQCList(String searchJson, String repairClass, long operatorid, int start, int limit) ;
    
    /**
     * 
     * <li>说明：完成临碎修提票质量检验项（工位终端）
     * <li>创建人：程梅
     * <li>创建日期：2015-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param qcResults 提票质检项列表JSON字符串：[{ idx 质检项IDX accPersonId 验收人编码 accPersonName 验收人员名称 accTime 验收时间}]
     * @param operatorid 操作者id
     * @return 操作成功与否
     */
    public String updateFinishQCResult(String qcResults, long operatorid) ;
    
    /**
     * <li>说明：获取提票处理其他作业人员列表
     * <li>创建人：程锐
     * <li>创建日期：2016-3-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return 提票处理其他作业人员列表
     */
    public String getOtherWorkerByTP(long operatorid);
    
    /**
     * <li>说明：根据工号查询人员信息
     * <li>创建人：林欢
     * <li>创建日期：2016-7-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardID 人员工号
     * @return 人员名词字符串{"empname":"王良廷"}
     */
    public String findOmeployeeByCardID(String workCardID);
    
    /**
     * <li>说明：同车同位置同故障现象次数
     * <li>创建人：林欢
     * <li>创建日期：2016-7-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 查询条件
     * {
           trainTypeShortName:'HXD1',
           trainNo:'0024',
           faultFixFullCode:'040202',
           faultID:'2070'
       }
     * @return 同位置同故障现象次数{"count":"1"}
     */
    public String getSameTrainNONameTPCount(String jsonObject);
    
    /**
     * <li>说明：整备提票检查不通过【提票返修】
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json字符串，返修实体
     * @return 操作成功与否
     */
    public String saveTpRepair(String jsonObject);
    
    
    /**
     * <li>说明：整备提票返修记录列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jt6IDX 提票单主键
     * @return 整备提票返修记录列表JSON字符串
     */
    public String getTpRepairList(String jt6IDX);
    
    /**
     * <li>说明：添加遗留活（工位终端）
     * <li>创建人：张迪
     * <li>创建日期：2015-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 遗留活
     * @param operatorid 操作者id
     * @return 操作成功与否
     */
    public String saveForLwfx(String jsonObject, long operatorid);
    
    /**
     * <li>说明：根据父节点查询专业树
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点
     * @return 专业树列表
     * @throws Exception
     */
    public String findProfessionalTree(String parentIDX) throws Exception;
}
