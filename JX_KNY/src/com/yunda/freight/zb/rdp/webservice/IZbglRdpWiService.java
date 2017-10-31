package com.yunda.freight.zb.rdp.webservice;

import java.io.IOException;

import com.yunda.jx.pjjx.webservice.IService;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检任务卡处理接口
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-4-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IZbglRdpWiService extends IService {
    
    /**
     * <li>说明：查询任务及其下的数据项
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                rdpIDX:"8a8284c35b276ca1015b2789390c0010",
                nodeIDX:"8a8284c35b276ca1015b278939280011",
                wiName:""
            },
            start:1,
            limit:50, 
            orders:[{
                sort: "seqNo",
                dir: "ASC"
            }]
       }
     * @return 
     * @throws IOException
     */
    public String findZbglRdpWiDiList(String jsonObject) throws IOException;
    
    /**
     * <li>说明：保存数据项
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson : [{
                idx:"D89F538E6A9E4C7689670165A3500E77",
                rdpWiIDX:"A1323DBA169B47309A132B9289DF6C9E",
                diResult:""
            }],
            operatorId: "800109"
       }
     * @return 
     * @throws IOException
     */
    public String saveZbglRdpDiDatas(String jsonObject) throws IOException;
    
}
