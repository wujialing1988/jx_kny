package com.yunda.freight.base.classOrganization.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.freight.base.classOrganization.entity.ClassOrganization;
import com.yunda.freight.base.classOrganization.entity.ClassOrganizationUser;
import com.yunda.freight.base.classOrganization.entity.ClassOrganizationUserVo;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 分队维护业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-13 16:44:12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
@Service("classOrganizationUserManager")
public class ClassOrganizationUserManager extends JXBaseManager<ClassOrganizationUser, ClassOrganizationUser> implements IbaseCombo, IbaseComboTree {
	
   
}
