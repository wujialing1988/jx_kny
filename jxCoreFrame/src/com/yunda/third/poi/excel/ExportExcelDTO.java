/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：林欢
 * <li>创建日期：2016-3-8
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

package com.yunda.third.poi.excel;

import com.yunda.frame.common.Page;



/**
 * <li>标题: 导出功能公共封装对象
 * <li>说明: 类的功能描述
 * <li>创建人：林欢
 * <li>创建日期：2016-3-8
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 * @param <T>
 */
public class ExportExcelDTO<T> {
    private Page<T> pages;//导出数据
    private ColumnPattern[] pattern;//
    private String sheetName;//sheet名称
    
    /**
     * <li>说明：导出数据
     * <li>返回值： the pages
     */
    public Page<T> getPages() {
        return pages;
    }
    
    /**
     * <li>说明：导出数据
     * <li>参数： pages
     */
    public void setPages(Page<T> pages) {
        this.pages = pages;
    }
    
    /**
     * <li>说明：
     * <li>返回值： the pattern
     */
    public ColumnPattern[] getPattern() {
        return pattern;
    }
    
    /**
     * <li>说明：
     * <li>参数： pattern
     */
    public void setPattern(ColumnPattern[] pattern) {
        this.pattern = pattern;
    }
    
    /**
     * <li>说明：sheet名称
     * <li>返回值： the sheetName
     */
    public String getSheetName() {
        return sheetName;
    }
    
    /**
     * <li>说明：sheet名称
     * <li>参数： sheetName
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    
}
