package com.yunda.zb.common.webservice;

import java.util.List;

import com.yunda.jx.webservice.stationTerminal.base.entity.EosDictEntryBean;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 工位终端公用接口
 * <li>创建人：程锐
 * <li>创建日期：2015-3-3
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface ITerminalCommonService {
    
    /**
     * <li>说明：获取工位终端首页的机车公用信息
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param accountIDX 机车出入段台账IDX
     * @return 工位终端首页的机车公用信息JSON字符串
     * {    
        trainInfo:{
                trainTypeAndNo: "SS40002",      // 机车号
                trainOrder:”K118”,      // 车次
                planOutTime:”2014-06-04 15:00”// 计划出段时间
        },
        dynamicInfo:[{
                title:”提票活项”,// 题头显示
                order：“1”，// 显示顺序
                item：[{// 显示项目
                    itemName：“总活项数”，// 显示项目名称
                    itemValue：“碎修（3）临修（2）”，// 显示项目值
                    itemOrder：“1”// 显示项目顺序
                },{
                    itemName：“未完成数”，// 显示项目名称
                    itemValue：“7”，// 显示项目值
                    itemOrder：“2”// 显示项目顺序
                },{
                    itemName：“已完成数”，// 显示项目名称
                    itemValue：“2”，// 显示项目值
                    itemOrder：“3”// 显示项目顺序
                }]
                },{
                title:”普查整治活项”,// 题头显示
                order：“2”，// 显示顺序
                item：[{// 显示项目
                    itemName：“总活项数”，// 显示项目名称
                    itemValue：“3”，// 显示项目值
                    itemOrder：“1”// 显示项目顺序
                },{
                    itemName：“未完成数”，// 显示项目名称
                    itemValue：“2”，// 显示项目值
                    itemOrder：“2”// 显示项目顺序
                },{
                    itemName：“已完成数”，// 显示项目名称
                    itemValue：“1”，// 显示项目值
                    itemOrder：“3”// 显示项目顺序
                }]
                },{
                title:”检查活项”,// 题头显示
                order：“3”，// 显示顺序
                item：[{// 显示项目
                    itemName：“总活项数”，// 显示项目名称
                    itemValue：“3”，// 显示项目值
                    itemOrder：“1”// 显示项目顺序
                },{
                    itemName：“未完成数”，// 显示项目名称
                    itemValue：“2”，// 显示项目值
                    itemOrder：“2”// 显示项目顺序
                },{
                    itemName：“已完成数”，// 显示项目名称
                    itemValue：“1”，// 显示项目值
                    itemOrder：“3”// 显示项目顺序
                }]
        }]
    }
     * @throws Exception
     */
    public String getTerminalData(String accountIDX) throws Exception;
    
    /**
     * <li>说明：设置操作员
     * <li>创建人：程锐
     * <li>创建日期：2015-3-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员id
     */
    public void setAcOperatorById(Long operatorid);
    
    /**
     * <li>说明：查询数据字典列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeID 数据字典项
     * @return 数据字典列表
     */
    public String queryEosDictEntryList(String dictTypeID);
    
    /**
     * <li>说明：获取整备任务和提票活数量
     * <li>创建人：程锐
     * <li>创建日期：2015-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param operatorid 操作者ID
     * @return 整备任务和提票活数量
     */
    public String getCount(String searchJson, Long operatorid);
    
    /**
     * <li>方法说明：获取工位终端链接配置文件信息
     * <li>方法名称：getstationTerminalInfo
     * <li>创建人：林欢
     * <li>创建时间：2016-5-20 下午02:08:19
     * <li>修改人：
     * <li>修改内容：
     * @return json字符串
     * 格式:{stationTerminalIp : "www.baidu.com",stationTerminalFunctionName:"linhuanTest"}
     */
     public String getstationTerminalInfo();

     /**
      * <li>说明：查询数据字典列表
      * <li>创建人：林欢
      * <li>创建日期：2016-8-4
      * <li>修改人： 
      * <li>修改日期：
      * <li>修改内容：
      * @param dictTypeID 数据字典项
      * @return 数据字典列表
      */
     public List<EosDictEntryBean> findEosDictEntryBeanByCopy(String string);
}
