package com.yunda.jx.pjjx.webservice;

import java.io.IOException;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：配件检修物料消耗接口
 * <li>http://localhost:8080/CoreFrame/ydservices/PartsRdpNodeMatService?wsdl
 * <li>创建人： 张迪
 * <li>创建日期： 2016-9-14 
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IPartsRdpNodeMatService extends IService{

    /**
     * <li>说明：所需物料查询列表
     * <li>创建人：何涛
     * <li>创建日期：2015-01-12
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
     * @param searchEnityJson 查询参数
     * @return 所需物料查询列表
     */
    public String findListForMat(String jsonObject) throws IOException;
    /**
     * <li>说明：新增物料消耗
     * <li>创建人：张迪
     * <li>创建日期：2016-09-14
     * <li>修改人: 
     * <li>修改日期：1
     * <li>修改内容：
     * @param jsonObject {
            partsRdpExpendMats: [{ 
                rdpIDX:"8a8284f24ae27846014ae27ca8340002",
                rdpNodeIDX:"9E2273FF52EB49B790269A14017F7573",
                matCode:"20003",
                matDesc:"20003",
                qty:12,
                unit:"20003",
                price:999.9
            }],
            operatorId: 800109
        }
     * @return 
     * <li>"{'flag':'true','message':'操作成功！'}";
     * <li>"{'flag':'false','message':'操作失败！'}"
     */
	public String saveExpendMats(String jsonObject) throws IOException;
    /**
     * <li>说明：消耗物料分页查询
     * <li>创建人：张迪
     * <li>创建日期：2016-09-14
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEnityJson {
            entityJson: {
                rdpNodeIDX:"3E15424C5A0943BA9E195108949A188C"
            },
            start:1,
            limit:50, 
            orders:[{
                sort: "idx",
                dir: "ASC"
            }]
        }
     * @return 
     * <li>"{'flag':'true','message':'操作成功！'}";
     * <li>"{'flag':'false','message':'操作失败！'}"
     */
    public String pageListForExpendMat(String searchEnityJson) throws IOException;
    
   
    /**
     * <li>说明：删除物料消耗
     * <li>创建人：张迪
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 提示信息
     * @throws IOException
     */
    public String deleteNodeMats(String jsonObject) throws IOException;
    /**
     * <li>说明：查询物料信息列表
     * <li>创建人：张迪
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEnityJson 查询参数
     * @return 物料信息列表
     * @throws IOException
     */
    public String pageListForMatList(String searchEnityJson) throws IOException;
    
    /**
     * <li>说明：保存节点物料信息
     * <li>创建人：张迪
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 提示信息
     * @throws IOException
     */
    public String saveNodeMatList(String jsonObject) throws IOException;

}
