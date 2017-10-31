package com.yunda.jxpz.coderule.manager;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.jxpz.coderule.entity.CodeRuleConfig;
import com.yunda.jxpz.coderule.entity.CodeRuleConfigProp;
import com.yunda.jxpz.coderule.entity.CodeRuleConfigRandom;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：CodeRuleConfig业务类,业务编码规则配置
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-10-09
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="codeRuleConfigManager")
public class CodeRuleConfigManager extends JXBaseManager<CodeRuleConfig, CodeRuleConfig>{
	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    private String ZERO = "0";
	/**
	 * <li>说明：确定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return true使用查询缓存，false不使用
	 */
	@Override
	protected boolean enableCache(){
		return true;
	}	
	/**
	 * <li>说明：根据编码规则配置信息生成编号，用于后台调用生成编码供存入数据库，并更新流水号信息
	 * <li>创建人：程梅
	 * <li>创建日期：2012-10-11
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2012-11-17
	 * <li>修改内容：增减查询缓存方式
	 * @param ruleFunction 功能点
	 * @return 返回值为String类型；返回根据编码规则拼接好的字符串
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public String makeConfigRule(String ruleFunction){
		String numb = "";
		try {
			String hql = "select p From CodeRuleConfig c,CodeRuleConfigProp p " +
					"where c.idx=p.ruleIDX and c.ruleFunction='"+ruleFunction+"' order by p.orderNo";
			List<CodeRuleConfigProp> list = (List<CodeRuleConfigProp>)daoUtils.find(enableCache(), hql);
//			List<CodeRuleConfigProp> list = (List<CodeRuleConfigProp>)daoUtils.find(hql);
            //有制定的规则就生成相应的编码
			if(list!=null&&list.size()>0){
				for(CodeRuleConfigProp pro : list){
					if(pro.getPropertyType() == 1){//流水号
						String hqlRan = "from CodeRuleConfigRandom where ruleFunction='"+ruleFunction+"'";
						CodeRuleConfigRandom random = (CodeRuleConfigRandom) daoUtils.findSingle(hqlRan);
			            String randomNum = "";
			            if(random!=null){
                            randomNum = random.getRandomNum();
			                if("".equals(randomNum)){
                                randomNum = "1";
								random.setRandomNum(randomNum);
								for(int i=1;i<Integer.parseInt(pro.getPropertyValue());i++){
                                    randomNum = ZERO+randomNum;
								}
							}else{
                                randomNum = Integer.parseInt(randomNum)+1+"";
								int num = Integer.parseInt(pro.getPropertyValue())-randomNum.length();
								//流水号值已经达到上限
								if(num < 0){
                                    randomNum = "1";
									random.setRandomNum(randomNum);
									for(int i=1;i<Integer.parseInt(pro.getPropertyValue());i++){
                                        randomNum = ZERO+randomNum;
									}
								}else{
									random.setRandomNum(randomNum);
									for(int i=0;i<num;i++){
                                        randomNum = ZERO+randomNum;
									}
								}
								
							}
//			                random.setRandomNum(Integer.parseInt(random_num)+1+"");
			                daoUtils.update(random);
			            }else{
			                CodeRuleConfigRandom ran = new CodeRuleConfigRandom();
			                ran.setRuleFunction(ruleFunction);
                            randomNum = "1";
			                ran.setRandomNum(randomNum);
							for(int i=1;i<Integer.parseInt(pro.getPropertyValue());i++){
                                randomNum = ZERO+randomNum;
							}
			                daoUtils.insert(ran);
			            }
						
						numb = numb+randomNum;
					}else if(pro.getPropertyType() == 2){//日期
						SimpleDateFormat df=new SimpleDateFormat(pro.getPropertyValue());
						String systime = df.format(new Date());
						numb = numb+systime;
					}else{
						numb = numb+pro.getPropertyValue();
					}
				}
			}else{  //没有特定的规则就根据默认规则生成编码
				SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
				String systimeV = df.format(new Date());
				String strV = (Math.random()+"").toString().substring(3,9);
				numb = systimeV+"_"+strV;
			}
		} catch (Exception e) {
			ExceptionUtil.process(e,logger);
		}
		return numb;
	}
	
	/**
	 * 
	 */
    /**
     * <li>说明：物理删除业务编码规则及其属性信息和流水号信息
     * <li>创建人：程梅
     * <li>创建日期：2012-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 主键
     */
	public void deleteByIds(Serializable... ids) throws BusinessException {
		try {
			String idxs="";
			for(int i=0;i<ids.length;i++){
				idxs+="'"+ids[i]+"'";
				if(i<ids.length-1){
					idxs+=",";
				}
			}
			String hql = "delete from JXPZ_Code_Rule_Config_Prop where rule_IDX in("+idxs+")";
			String hqlRa = "delete from JXPZ_Code_Rule_Config_random where Rule_Function in ( select c.Rule_Function from JXPZ_Code_Rule_Config c where c.idx in ("+idxs+"))";
			daoUtils.executeSql(hql);
			daoUtils.executeSql(hqlRa);
			daoUtils.removeByIds(ids, entityClass, getModelIdName(entityClass));// 按ID删除规则主表信息
			
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
    /**
     * <li>说明：批量生成编号
     * <li>创建人：程锐
     * <li>创建日期：2016-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ruleFunction 功能点
     * @param num 生成数量
     * @return 数组 {html字符串，编号}
     */
	public String[] batchMakeConfigRule(String ruleFunction, int num) {
		String[] array = new String[2];
		StringBuilder sb = new StringBuilder();
		StringBuilder rules = new StringBuilder();
		sb.append("<table class=\"tdlContextTab\" cellpadding=\"0\" cellspacing=\"0\" border=\"1\">");        
		for (int i = 0; i < num; i++) {
			String newRule = makeConfigRule(ruleFunction);
			sb.append("<tr>");
			sb.append("<td width=\"*\" style=\"background-color: #ffffff; text-align:center\" >" );
			sb.append(newRule);
			sb.append("</td>");
			sb.append("</tr>");
			rules.append(newRule).append(Constants.JOINSTR);			
		}
		sb.append("</table>");
		if (rules.toString().endsWith(Constants.JOINSTR))
			rules.deleteCharAt(rules.length() - 1);
		array[0] = sb.toString();
		array[1] = rules.toString();
		return array;
	}
}