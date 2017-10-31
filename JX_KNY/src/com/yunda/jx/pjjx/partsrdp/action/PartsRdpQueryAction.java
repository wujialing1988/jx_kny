package com.yunda.jx.pjjx.partsrdp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpQueryManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：任务单查询控制器
 * <li>创建人：程梅
 * <li>创建日期：2014-12-05
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class PartsRdpQueryAction extends JXBaseAction<PartsRdp, PartsRdp, PartsRdpQueryManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：查询任务单列表【派工情况】
     * <li>创建人：程梅
     * <li>创建日期：2014-12-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findPartsRdpList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            PartsRdp entity = JSONUtil.read(searchJson, PartsRdp.class);
            SearchEntity<PartsRdp> searchEntity = new SearchEntity<PartsRdp>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPartsRdpList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：配件检修质量检验-根据识别码获取配件作业计划信息
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void getPartsRdpByIdentity() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String entityJson = StringUtil.nvl(getRequest().getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            PartsRdp partsRdp = JSONUtil.read(entityJson, PartsRdp.class);
            PartsRdp rdp = this.manager.getRdpForQC(partsRdp);
            JSONUtil.write(getResponse(), rdp);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        }
    }
    
    /**
     * <li>说明：配件检修质量检验-根据配件编号获取配件作业计划信息
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void getPartsRdpByPartsNo() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String entityJson = StringUtil.nvl(getRequest().getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            PartsRdp partsRdp = JSONUtil.read(entityJson, PartsRdp.class);
            PartsRdp rdp = this.manager.getRdpForQC(partsRdp);
            JSONUtil.write(getResponse(), rdp);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        }
    }
    
    /**
     * <li>说明：根据识别码获取当前操作类型
     * <li>创建人：程锐
     * <li>创建日期：2015-10-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void getOpTypeByIdentity() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String identificationCode = getRequest().getParameter("identificationCode");
            map = this.manager.findPartsProMap(identificationCode);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据配件编号获取当前操作类型
     * <li>创建人：程锐
     * <li>创建日期：2015-10-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void getOpTypeByPartsNo() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String partsNo = getRequest().getParameter("partsNo");
            PartsRdp partsRdp = new PartsRdp();
            partsRdp.setPartsNo(partsNo);
            Map<String, Object> proMap = this.manager.findPartsProMap(partsRdp);
            map.put("type", proMap.get("type"));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：配件检修合格验收-根据配件编号获取配件作业计划信息
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void getHgysPartsRdpByPartsNo() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String partsNo = StringUtil.nvl(getRequest().getParameter("partsNo"), "");
            PartsRdp rdp = this.manager.getHgysRdpByPartsNo(partsNo);
            if (rdp == null) {
            	map.put("success", false);
                map.put("errMsg", "未查询到相应结果");
                JSONUtil.write(getResponse(), map);
            } else
            	JSONUtil.write(getResponse(), rdp);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：配件检修合格验收-根据配件识别码获取配件作业计划信息
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void getHgysRdpByIdentityCode() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String identityCode = StringUtil.nvl(getRequest().getParameter("identityCode"), "");
            PartsRdp rdp = this.manager.getHgysRdpByIdentityCode(identityCode);
            if (rdp == null) {
            	map.put("success", false);
                map.put("errMsg", "未查询到相应结果");
                JSONUtil.write(getResponse(), map);
            } else
            	JSONUtil.write(getResponse(), rdp);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(getResponse(), map);
        }
    }    
}
