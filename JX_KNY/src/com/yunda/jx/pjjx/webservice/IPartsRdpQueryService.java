package com.yunda.jx.pjjx.webservice;

import java.io.IOException;

import org.codehaus.jackson.map.JsonMappingException;


public interface IPartsRdpQueryService extends IService {

    /**
     * <li>说明：通过工位，下车车型车号，配件名称查询配件检修情况
     * <li>创建人：张迪
     * <li>创建日期：2016-7-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 查询参数json对象
     * @return 配件检修记录
     * @throws IOException
     */
    public String queryPartsListByWorkStation(String jsonObject) throws IOException;
    
    
    /**
     * <li>说明：查询检修记录单
     * <li>创建人：张迪
     * <li>创建日期：2016-8-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 
     * {
     *   rdpRecordIDX 检修作业兑现单idx
     * }
     * @return 检修记录单集合
     * @throws IOException 
     * @throws JsonMappingException  
     */
    public String  queryRecordPageList(String jsonObject) throws JsonMappingException, IOException;
    
    /**
     * <li>说明：查询检修记录卡集合
     * <li>创建人：张迪
     * <li>创建日期：2016-8-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 
     * {
     *   rdpRecordIDX 检修记录单idx
     * }  
     * @return 檢修记录卡详情列表
     * @throws IOException
     */
    public String queryCardList(String jsonObject) throws IOException;
}
