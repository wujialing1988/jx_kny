/**
 * 
 */
package com.yunda.jx.pjjx.webservice;

import java.io.IOException;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：配件检修质量检验接口
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-14 下午02:28:03
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IPartsRdpQCService extends IService {

    /**
     * <li>说明：待检验工单汇总分页查询
     * <li>创建人：何涛
     * <li>创建日期：2015-01-14
     * <li>修改人: 程锐
     * <li>修改日期： 2015-10-10
     * <li>修改内容：增加配件识别码
     * 
     * @param searchEnityJson {
            entityJson: {
                unloadTrainTypeIdx:"10026",
                unloadTrainType: "HXD10002",
                unloadTrainNo: "256",
                unloadRepairClassIdx: "8a8284f24ab80704014ab891375a0004",
                partsTypeIDX: "8a8284f24ab80704014ab891375a0004",
                specificationModel: "TGZS500.221.000\\SS4",
                partsNo: "25202",
                partsName: "转向架",
                identificationCode："25202"
            },
            start:0,
            limit:50,
            checkWay: "2",
            qcContent: "GZJ,YS,ZJ",
            operatorId: 800109
        }
     * @return JSON列表（数组）
     * @throws IOException
     */
	public String findPartRdpQCItems (String searchEnityJson) throws IOException;
	
    /**
     * <li>说明：签名提交
     * <li>创建人：何涛
     * <li>创建日期：2015-01-14
     * <li>修改人: 程锐
     * <li>修改日期： 2015-10-12
     * <li>修改内容：前台参数传递修改为传递配件质检单的idx
     * 
     * @param jsonObject {
            idxs: ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023"],
            qcItemNo:"GZJ",
            qrResult:"检验合格"
            operatorId: 800109
        }
     * @return JSON列表（数组）
     * @throws IOException
     */
    public String signAndSubmit (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：返修
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
			rdpRecordCardIDXs: ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023"],
			qcItemNo:"GZJ",
			operatorId: 800109
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String updateToBack(String jsonObject) throws IOException;
	
    /**
     * <li>说明：根据配件识别码获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 
     * {
     *   identityCode 配件识别码
     * }  
     * @return 配件作业计划
     * @throws IOException
     */
    public String getPartsRdpByIdentity(String jsonObject) throws IOException;
    
    /**
     * <li>说明：根据配件编号获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 
     * {
     *   partsNo 配件编号
     * }  
     * @return 配件作业计划
     * @throws IOException
     */
    public String getPartsRdpByPartsNo(String jsonObject) throws IOException;
    
    /**
     * <li>说明：待检验工单分页查询
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEnityJson { 
        entityJson:{
            rdpIDX: "772D7520EF5945B695CDB79F2E6CC1E7"
        },
        start:0,
        limit:50,
        operatorId: "10902",
        checkWay: "1"
       }
     * @return 待检验工单分页列表
     * @throws IOException
     */
    public String findPageListForQC(String searchEnityJson) throws IOException;
    
    /**
     * <li>说明：全部签名提交
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人: 
     * <li>修改日期： 
     * <li>修改内容：
     * 
     * @param jsonObject {
            entityJson:{
                rdpIDX: "772D7520EF5945B695CDB79F2E6CC1E7"
            },
            qrResult:"检验合格"
            operatorId: 800109,
            checkWay: "1"
        }
     * @return 操作成功与否
     * @throws IOException
     */
    public String allSignAndSubmit (String jsonObject) throws IOException;
    
    /**
     * <li>说明：根据记录单IDX获取质检人信息列表
     * <li>创建人：程锐
     * <li>创建日期：2015-12-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {            
            rdpRecordCardIDX: "1"
        }
     * @return 质检人信息列表
               [{"qcItemName":"互检",
                 "qcEmpNames":"张三,李四"
                }]
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public String getQREmpInfoByRecordCardIDX(String jsonObject) throws IOException;
    
    /**
     * <li>说明：（新）待检验工单汇总
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String findRdpPageList(String jsonObject) throws IOException;
    
    /**
     * <li>说明：（新）待检验检修记录单汇总
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryRecordPageList(String jsonObject) throws IOException ;
    
    
    /**
     * <li>说明：待检验检修记录卡汇总查询
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryCardList(String jsonObject) throws IOException ;
    
}
