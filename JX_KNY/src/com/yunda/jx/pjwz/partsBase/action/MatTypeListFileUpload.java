package com.yunda.jx.pjwz.partsBase.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjwz.partsBase.manager.MatTypeListManager;

@SuppressWarnings(value = "serial")
public class MatTypeListFileUpload extends ActionSupport{
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());

    private File image; //上传的文件

    private String imageFileName; //文件名称

    private String imageContentType; //文件类型

    private Boolean success = true; //json中返回的结果  

    private String resultMsg = "ok"; //上传失败后，返回的错误结果  
    
    private MatTypeListManager matTypeListManager;
    
    
    /**
	 * 
	 * <li>说明：执行导入-重写方法
	 * <li>创建人：程锐
	 * <li>创建日期：2014-2-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 错误验证信息
	 */
    public String execute() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (image != null) {
                //如果上传的文件不是Excel2003格式的
                if (!imageFileName.endsWith(".xls")) {
                    success = false;
                    map.put("errMsg", "该功能仅支持 Excel2003 版本文件！");
                } else {
                    String filePath = image.getPath();
                    String sheetName = null;
                    String startCellX = "5";
                    String startCellY = "A";
                    List<String> list = new ArrayList<String>();
                    sheetName = "物料清单";                        
                    list = matTypeListManager.importData(filePath,sheetName, startCellX, startCellY);
                    map.put("errMsg", list);
                }
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            success = false;
        } finally {
            String a = JSONUtil.write(map);
            String msg = "{success:" + success + ",resultMsg:" + a + "}";
            ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
            ServletActionContext.getResponse().setContentType("text/html");
            ServletActionContext.getResponse().getWriter().write(msg);
        }
        return NONE;
    }

    
    public File getImage() {
        return image;
    }

    
    public void setImage(File image) {
        this.image = image;
    }

    
    public String getImageContentType() {
        return imageContentType;
    }

    
    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    
    public String getImageFileName() {
        return imageFileName;
    }

    
    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    
    public MatTypeListManager getMatTypeListManager() {
        return matTypeListManager;
    }

    
    public void setMatTypeListManager(MatTypeListManager matTypeListManager) {
        this.matTypeListManager = matTypeListManager;
    }

    
    public String getResultMsg() {
        return resultMsg;
    }

    
    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    
    public Boolean getSuccess() {
        return success;
    }

    
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    
    
}
