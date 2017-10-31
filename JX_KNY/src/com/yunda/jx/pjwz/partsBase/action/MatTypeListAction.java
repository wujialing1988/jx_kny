package com.yunda.jx.pjwz.partsBase.action; 

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.entity.MatTypeList;
import com.yunda.jx.pjwz.partsBase.manager.MatTypeListManager;
import com.yunda.jx.wlgl.partsBase.entity.WhMatQuota;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatTypeList控制器, 物料清单
 * <li>创建人：程锐
 * <li>创建日期：2013-10-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class MatTypeListAction extends JXBaseAction<MatTypeList, MatTypeList, MatTypeListManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * /jsp/jx/pjwz/partbase/MatTypeList.js调用
	 * <li>说明：下载Excel导入模板
	 * <li>创建人：程锐
	 * <li>创建日期：2013-12-16
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 */
	public void download() {
		HttpServletResponse rsp = getResponse();
		InputStream in = null;
		try {
			String filename = "MatTypeList-Template.xls";
			in = this.getClass().getResourceAsStream(
					"/com/yunda/jx/pjwz/partsBase/tmpl/".concat(filename));
			rsp.setContentType("application/x-msdownload");
			rsp.setHeader("Content-Disposition", "attachment;filename="
					+ StringUtil.toISO("物料清单-导入模板.xls"));
			OutputStream out = rsp.getOutputStream();
			byte[] b = new byte[1024 * 5];
			int len = in.read(b);
			while (len > 0) {
				out.write(b, 0, len);
				len = in.read(b);
			}
		} catch (Exception e) {
			try {
				rsp.getOutputStream().write("文件不存在或已经被删除！".getBytes());
			} catch (IOException ex) {
				ExceptionUtil.process(ex,logger);
			}
			ExceptionUtil.process(e,logger);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					ExceptionUtil.process(e,logger);
				}
				in = null;
			}
		}
	}
	
	/**
	 * <li>说明：根据物料编码查询物料清单信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void getModelByMatCode() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String matCode = getRequest().getParameter("matCode"); 	//物料编码
			String whIdx = getRequest().getParameter("whIdx");
			List list = this.manager.getModelByMatCode(matCode, whIdx);
			if (list != null && list.size() > 0) {
				map.put("list", list);
			} else {
				String str = "没有找到物料编码为【" + matCode + "】的物料信息！";
				map.put("errMsg", str);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：根据物料编码和班组id查询物料清单信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void getModelByMatCodeForExpend() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String matCode = getRequest().getParameter("matCode"); 	//物料编码
			String orgId = getRequest().getParameter("orgId");
			List list = this.manager.getModelByMatCodeForExpend(matCode, orgId);
			if (list != null && list.size() > 0) {
				map.put("list", list);
			} else {
				String str = "没有找到物料编码为【" + matCode + "】的物料信息！";
				map.put("errMsg", str);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
    /**
	 * <li>说明：根据查询实体和库房idx主键查询物料清单信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
     * @throws IOException 
     * @throws JsonMappingException 
	 */
	public void findPageList() throws JsonMappingException, IOException {
		HttpServletRequest req = getRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询实体
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
			// 库房idx主键
			String whIdx = req.getParameter("whIdx");
			entity = (MatTypeList)JSONUtil.read(searchJson, entity.getClass());
			SearchEntity<MatTypeList> searchEntity = new SearchEntity<MatTypeList>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageList(searchEntity, whIdx).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：查询班组用料消耗的物料清单信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void findPageListForExpend() throws JsonMappingException, IOException {
		HttpServletRequest req = getRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询实体
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
			// 库房idx主键
			String orgId = req.getParameter("orgId");
			entity = (MatTypeList)JSONUtil.read(searchJson, entity.getClass());
			SearchEntity<MatTypeList> searchEntity = new SearchEntity<MatTypeList>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageListForExpend(searchEntity, orgId).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
    /**
     * 
     * <li>说明：根据库房、物料编码、物料类型查询库存台账信息和保有量信息
     * <li>创建人：程梅
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getMatInfo() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String matCode = getRequest().getParameter("matCode");  //物料编码
            String whIdx = getRequest().getParameter("whIdx");
            String matType = getRequest().getParameter("matType");//物料类型
            MatStock matStock = this.manager.getMatStockQueryManager().getModelStock(whIdx, matCode, matType);
//            if (null != matStock) {
                map.put("matStock", matStock);
//            }
            WhMatQuota whMatQuota = this.manager.getWhMatQuotaManager().getMatQuota(whIdx, matCode, matType);
//            if (null != whMatQuota) {
                map.put("whMatQuota", whMatQuota);
//            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    public void saveOrUpdate() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            MatTypeList t = (MatTypeList)JSONUtil.read(getRequest(), entity.getClass());
            this.manager.validateSaveOrUpdate(t);
            this.manager.saveOrUpdate(t);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}