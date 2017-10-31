package com.yunda.jx.scdd.enforceplan.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jcgy.entity.JgyjcBureau;
import com.yunda.jcgy.entity.JgyjcDeport;
import com.yunda.jx.base.jcgy.entity.RT;
import com.yunda.jx.base.jcgy.entity.XC;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.jczl.attachmanage.manager.JczlTrainManager;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetail;
import com.yunda.third.poi.excel.ExcelReader;
import com.yunda.util.BeanUtils;
/**
 * 
 * <li>标题：机车月计划检修上传管理
 * <li>说明：处理月计划机修报表的导入
 * <li>创建人：王利成
 * <li>创建日期：2014-4-22
 * <li>修改人： 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权：Copyright(c) 2008 运达科技公司
 * @author 测控部检修项目组
 * @version 2.0
 */

@Service(value="trainEnforceUploadManager")
public class TrainEnforceUploadManager extends JXBaseManager<TrainEnforcePlanDetail, TrainEnforcePlanDetail> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    @Resource
    private JczlTrainManager jczlTrainManager;
    
	public List<String> importData(String filePath, String sheetName, String startCellX, String startCellY,TrainEnforcePlanDetail tpdDetail) throws Exception{
		 List<String> errInfo = null;
	        //解析Excel文件，获取数据
	        String[][] datas = analysisExcel(filePath, sheetName, startCellX,startCellY);
	        if(datas==null){
	            errInfo = new ArrayList<String>();
	            errInfo.add("未找到名为『"+sheetName+"』的工作簿内的数据，导入失败！");
	            return errInfo;
	        }
          errInfo = dataVerification(datas, startCellX, startCellY ,tpdDetail);
           //验证未通过，返回错误信息
	        if(errInfo!=null){
	            return errInfo;
	        }
          //验证已通过，进行数据存储
	        else{
	        	if(datas != null && datas.length > 0){
	        		List<TrainEnforcePlanDetail> saveList = new ArrayList<TrainEnforcePlanDetail>();
	        		List<JczlTrain> saveJczlTrainList = new ArrayList<JczlTrain>();
	        		for(int i = 0;i < datas.length;i++){
	        			TrainEnforcePlanDetail tpdList = new TrainEnforcePlanDetail();
                        JczlTrain jczlTrain = new JczlTrain();
	        			BeanUtils.copyProperties(tpdList,tpdDetail);     //拷贝含有机车年检计划id的对象
	        			for(int j = 0;j < datas[i].length-1; j++){
	        				 switch(j){
	  					     case 0://车型简称和ID
	  					    	tpdList.setTrainTypeShortName(datas[i][j]);
	  					    	String trShortName = datas[i][j];
	  					    	tpdList.setTrainTypeIDX(getTrainTypeIdx(trShortName));//赋值车型主键
                                 break;
	  					     case 1://车号
	  					    	tpdList.setTrainNo(datas[i][j]);                             
	  					    	break;
	  					     case 2://配属局和ID
	  					    	if("".equals(datas[i][j]) || null == datas[i][j])
	  					    		break;
	  					    	tpdList.setBName(datas[i][j]);
	  					    	String bname = datas[i][j];
	  					    	tpdList.setBid(getJBureauId(bname));
	  					    	tpdList.setBShortName(getJBureauShortName(bname));
	  					    	 break;
	  					     case 3://配属段和ID
	  					    	 if("".equals(datas[i][j])|| null == datas[i][j])
	  					    		 break;
	  					    	 tpdList.setDNAME(datas[i][j]);	  					    	
	  					    	 String dname = datas[i][j];	  					    	
	  					    	 tpdList.setDid(getJDeportId(dname));
	  					    	break;
	  					     case 4://委修单位
	  					    	 tpdList.setUsedDName(datas[i][j]);
                                 break; 					    	 
	  					     case 5://修程名称和ID
	  					    	tpdList.setRepairClassName(datas[i][j]);
	  					    	tpdList.setRepairClassIDX(getXcCode(datas[i][j]));
	  					    	 break; 
	  					     case 6://修次名称和ID
	  					    	tpdList.setRepairtimeName(datas[i][j]); 
	  					    	tpdList.setRepairtimeIDX(getRtCode(datas[i][j]));
	  					    	tpdList.setPlanStatus(10);//设置编制状态
	  					    	 break;  
	  					     case 7://走行公里
	  					    	 if(StringUtils.isNotBlank(datas[i][j])){
	  					    		 tpdList.setRunningKM(Double.valueOf(datas[i][j])); 
	  					    	 }
	  					      break;
	  					     case 8://计划修车时间
	  					    	tpdList.setPlanStartDate(DateUtil.parse(datas[i][j], "yyyy-MM-dd"));
	  					    	 break; 
	  					    case 9://计划交车时间
	  					    	tpdList.setPlanEndDate(DateUtil.parse(datas[i][j], "yyyy-MM-dd"));
	  					    	 break; 
	        				   }
	        			  }
                        BeanUtils.copyProperties(jczlTrain,tpdList);     //拷贝含有机车年检计划id的对象
                        jczlTrain.setIdx(null);
                        if (null == jczlTrainManager.validateUpdate(jczlTrain)) {
                            jczlTrain.setDId(tpdList.getDid());
                            jczlTrain.setBId(tpdList.getBid());
                            jczlTrain.setIdx(null);
                            OmEmployee emp = SystemContext.getOmEmployee();
                            jczlTrain.setRegisterPerson(emp.getEmpid());
                            jczlTrain.setRegisterPersonName(emp.getEmpname());
                            jczlTrain.setRegisterTime(new Date());
                            jczlTrain.setAssetState(JczlTrain.TRAIN_ASSET_STATE_USE);
                            saveJczlTrainList.add(jczlTrain);
                        } else {
                        	List<JczlTrain> trains = jczlTrainManager.getModelList(tpdList.getTrainTypeIDX(), tpdList.getTrainNo(),null);
                        	JczlTrain jczlTrainE = trains.get(0);
                            if(null==tpdList.getDNAME()|| "".equals(tpdList.getDNAME())){
                                tpdList.setDid(jczlTrainE.getDId());
                                tpdList.setDNAME(getDnameByDId(jczlTrainE.getDId()));
                            }
                           if(null==tpdList.getBName()|| "".equals(tpdList.getBName())){
                               tpdList.setBid(jczlTrainE.getBId());
                               tpdList.setBName(getBnameByBId(jczlTrainE.getBId()));
                           }
	        			saveList.add(tpdList);
	        			}
	        		saveOrUpdate(saveList);
                    jczlTrainManager.saveOrUpdate(saveJczlTrainList);
	        		}
	        	}
            }
		return null;	
	}
	/**
	 * 
	 * <li>方法说明：获取车型主键
	 * <li>方法名称：getTrainTypeIdx
	 * <li>@param trShortName
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：
	 * <li>创建人：王利成
	 * <li>创建日期：2014-4-22 下午07:03:45
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	 public String getTrainTypeIdx(String trShortName ){
		 String  trid = "";
		 String hql =" from JczlTrain as jt where jt.recordStatus=0 and jt.trainTypeShortName = '"+trShortName+"'";
		 JczlTrain entity =(JczlTrain)daoUtils.findSingle(hql);
		 trid = entity.getTrainTypeIDX();
		 return trid;
	 }
	/**
	 * 
	 * <li>方法说明：获取配属局ID
	 * <li>方法名称：getJBureauId
	 * <li>@param bname
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：
	 * <li>创建人：王利成
	 * <li>创建日期：2014-4-22 下午07:04:07
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */	 
	 public String getJBureauId(String bname){
		 String bid = ""; 		 
		 JgyjcBureau entity = getJBureau(bname);
		 if(entity != null) bid = entity.getBId();
		 return bid;
	 }
	 /**
	  * 
	  * <li>方法说明：获得配属局名称
	  * <li>方法名称：getJBureauShortName
	  * <li>@param bname
	  * <li>@return
	  * <li>返回类型：String
	  * <li>说明：
	  * <li>创建人：王利成
	  * <li>创建日期：2014-4-22 下午07:04:30
	  * <li>修改人： 
	  * <li>修改日期：
	  * <li>修改内容：
	  */
	 public String getJBureauShortName(String bname){
		 String bsname = ""; 		 
		 JgyjcBureau entity = getJBureau(bname);
		 if(entity != null) bsname = entity.getShortName();
		 return bsname;
	 }
	 /**
	  * 
	  * <li>方法说明：获得配属局实体
	  * <li>方法名称：getJBureau
	  * <li>@param bname
	  * <li>@return
	  * <li>返回类型：JgyjcBureau
	  * <li>说明：
	  * <li>创建人：王利成
	  * <li>创建日期：2014-4-22 下午07:05:12
	  * <li>修改人： 
	  * <li>修改日期：
	  * <li>修改内容：
	  */
	 public JgyjcBureau getJBureau(String bname){
		 String hql = "from JgyjcBureau as jb where jb.bName ='"+bname+"'";
		 return (JgyjcBureau)daoUtils.findSingle(hql);
	 }
	 /**
	  * 
	  * <li>方法说明：获取配属段ID
	  * <li>方法名称：getJDeportId
	  * <li>@param dname
	  * <li>@return
	  * <li>返回类型：String
	  * <li>说明：
	  * <li>创建人：王利成
	  * <li>创建日期：2014-4-22 下午07:05:32
	  * <li>修改人： 
	  * <li>修改日期：
	  * <li>修改内容：
	  */
	 public String getJDeportId(String dname){
		 String did = ""; 
		 String hql = "from JgyjcDeport as jd where jd.dName ='"+dname+"'";
		 JgyjcDeport entity =(JgyjcDeport)daoUtils.findSingle(hql);
		 did = entity.getDId();
		 return did;
	 }	
	 /**
	  * 
	  * <li>方法说明：获取修程XCID
	  * <li>方法名称：getXcCode
	  * <li>@param xcName
	  * <li>@return
	  * <li>返回类型：String
	  * <li>说明：
	  * <li>创建人：王利成
	  * <li>创建日期：2014-4-22 下午07:05:43
	  * <li>修改人： 
	  * <li>修改日期：
	  * <li>修改内容：
	  */
	 public String getXcCode(String xcName){
		 String xcCode = "";
		 String hql ="from XC as c where c.xcName ='"+xcName+"' ";
		 XC entity = (XC)daoUtils.findSingle(hql);
		 xcCode = entity.getXcID();
		 return xcCode;
	 }
	 /**
	  * 
	  * <li>方法说明：获得修次编码RTID
	  * <li>方法名称：getRtCode
	  * <li>@param rtName
	  * <li>@return
	  * <li>返回类型：String
	  * <li>说明：
	  * <li>创建人：王利成
	  * <li>创建日期：2014-4-22 下午07:05:58
	  * <li>修改人： 
	  * <li>修改日期：
	  * <li>修改内容：
	  */
	 
	 public String getRtCode(String rtName){
		 String rtCode = "";
		 String hql ="from RT as r where r.rtName ='"+rtName+"' ";
		 RT entity = (RT)daoUtils.findSingle(hql);
		 rtCode = entity.getRtID();
		 return rtCode;
	 }	
     /**
      * 
      * <li>方法说明：验证excel中数据
      * <li>方法名称：dataVerification
      * <li>@param datas
      * <li>@param startCellX
      * <li>@param startCellY
      * <li>@return
      * <li>返回类型：List<String>
      * <li>说明：
      * <li>创建人：王利成
      * <li>创建日期：2014-4-22 下午07:06:11
      * <li>修改人： 
      * <li>修改日期：
      * <li>修改内容：
      */	
	 public List<String> dataVerification(String[][] datas, String startCellX, String startCellY,TrainEnforcePlanDetail tpdDetail){
		 String [] _t = new String []{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R",
	                "S","T","U","V","W","X","Y","Z"}; 
		 List<String> errInfo = null;
		 String key = "";     //设置报错信息
		 if(datas != null && datas.length > 0){
			 for(int i = 0; i<datas.length; i++){
                int count =0;
				 for(int j = 0; j<datas[ i ].length-1; j++){                 
					 switch(j){
					   case 0://车型英文简称
						   String trainTypeShortName = datas[i][j];
						   tpdDetail.setTrainTypeShortName(trainTypeShortName);
						   if(StringUtil.isNullOrBlank(trainTypeShortName) || trainTypeShortName.length()>8){
                               count++;
                               if(errInfo == null) errInfo = new ArrayList<String>();
                               key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,车型不能为空！";
                               errInfo.add(key);
                             
                           }else{
                        	   if((validate(trainTypeShortName,"JczlTrain","trainTypeShortName"))){
                        	    if(errInfo == null) errInfo = new ArrayList<String>();
                        	    key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,车型数据不正确！";
                        		errInfo.add(key); 
                        	   }
                              }   
						   break;
					   case 1://车号
						   String trainNo = datas[i][j];
						   tpdDetail.setTrainNo(trainNo);
						   if(StringUtil.isNullOrBlank(trainNo) || trainNo.length()>50){
                               count++;
                               if(errInfo == null) errInfo = new ArrayList<String>();
                               key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,车号不能为空！";
                               errInfo.add(key);
                              }
						   else{
//							   if(validate(trainNo,"JczlTrain","trainNo")){
//								   if(errInfo == null) errInfo = new ArrayList<String>();
//	                               key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,"+trainNo+"车号没有登记,请先在机车信息维护中登记！";
//	                               errInfo.add(key);
//							      }
//							      else
//							       {
							    	  if(vilidateTrno(tpdDetail)){
							    		  key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,"+trainNo+"车号已经在本月计划中存在,不能重复添加！"; 
                                          if(errInfo == null) errInfo = new ArrayList<String>();
                                          errInfo.add(key);  
							    	  }  
//							       }
						       }
						   break;
					   case 2://配属局名称
						   String bName = datas[i][j];
						   if(StringUtil.isNullOrBlank(bName) || bName.length()>50){
                               count++;
//                               if(errInfo == null) errInfo = new ArrayList<String>();
//                               key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,配属局不能为空！";
//                               errInfo.add(key);
							//   trainNo = datas[i][1];//获取当前车号
							//   getBnameByTrainNo(trainNo);//配属局若为空，获取当前车号对用的配属局名称
                           }else{
                               if((validate(bName, "JgyjcBureau", "bName"))){ 
                            	 if(errInfo == null) errInfo = new ArrayList<String>();
                            	 key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,配属局数据不正确！";
                                 errInfo.add(key);	
                               }   
                           } 
						   break;   
					   case 3://配属段名称
						   String dName = datas[i][j];
							  if( StringUtil.isNullOrBlank(dName) || dName.length()>50){
                                  count++;
//								  if(errInfo == null) errInfo = new ArrayList<String>();
//	                               key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,配属段名称不能为空！";
//	                               errInfo.add(key);
								//  trainNo = datas[i][1];
								//  getDnameByTrainNo(trainNo);//配属段为空，获取当前车号对用的配属段名称
								 }
							      else{
							    	   if(validate(dName,"JgyjcDeport","dName")){ 
			                            	  if(errInfo == null) errInfo = new ArrayList<String>();
			                            	  key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,配属段名称不正确！";
			                                  errInfo.add(key);	
			                              }
								     }	
						   break;
					   case 4://委修段名称
						    String ddname = datas[i][j];
						    if( !(StringUtil.isNullOrBlank(ddname)) && validate(ddname,"JgyjcDeport","dName")){
                                
						    	 if(errInfo == null) errInfo = new ArrayList<String>();
                           	     key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,委修段名称不正确！";
                                 errInfo.add(key);	
						      }else   count++;
	                         break;
					   case 5://*修程名称
						   String xcName = datas[i][j];
							  if(StringUtil.isNullOrBlank(xcName) || xcName.length()>50){
                                  count++;
	                              if(errInfo == null) errInfo = new ArrayList<String>();
	                              key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,修程不能为空！";
	                              errInfo.add(key);
	                          }
							  else{
								  if(validate(xcName,"XC","xcName")){
									  if(errInfo == null) errInfo = new ArrayList<String>();
									   key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,修程填写不正确！";
		                               errInfo.add(key);	 
								  }
							  }
						   break;
					   case 6://*修次名称
						   String rcName = datas[i][j];
							  if(StringUtil.isNullOrBlank(rcName) || rcName.length()>50){
                                  count++;
	                              if(errInfo == null) errInfo = new ArrayList<String>();
	                              key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,修次不能为空！";
	                              errInfo.add(key);
	                          }
							  else{
								  if(validate(rcName,"RT","rtName")){
									  if(errInfo == null) errInfo = new ArrayList<String>();
		                              key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,修次填写不正确！";
		                              errInfo.add(key); 
								  }
							  }
						   break;
					   case 7://走行公里
						   String runningKM=datas[i][j];
						    if(StringUtils.isNotBlank(runningKM)){
						    	  if(runningKM.length()>8){
						    		  if(errInfo == null) errInfo = new ArrayList<String>();
		                              key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,长度不能超过8位！";
		                              errInfo.add(key);
						    	  }else{
						    		  //判断是否为数字或小数
						    		  boolean flag=Pattern.compile("^([0-9]*)+(\\.[0-9]*)?$").matcher(runningKM).matches(); 
						    		  if(!flag){
						    			  if(errInfo == null) errInfo = new ArrayList<String>();
			                              key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,走行公里填写的格式不正确！";
			                              errInfo.add(key);
						    		  }
						    	  }
						    }else   count++;
						    break;
					   case 8://计划修车时间
						   String startDate = datas[i][j];
						   if(StringUtil.isNullOrBlank(startDate) || startDate.length()>50){
                               count++;
                               if(errInfo == null) errInfo = new ArrayList<String>();
                               key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,计划修车时间不能为空！";
                               errInfo.add(key);
                             }
						   break; 
					   case 9://计划交车日期
						   String endDate = datas[i][j];
						   if(StringUtil.isNullOrBlank(endDate) || endDate.length()>50){
                               count++;
                               if(errInfo == null) errInfo = new ArrayList<String>();
                               key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,计划交车日期不能为空！";
                               errInfo.add(key);
                             } 
						   break; 
					 }
				 } 
                 if(count  == datas[i].length-1){
                     key = "第 " + (Integer.parseInt(startCellX)+i)+"行,请删除该行后再导入！";
                     errInfo.add(key);
                 }
			 }	 
		 }
		 return errInfo;
		 
	 }
	 /**
	  * 
	  * <li>方法说明：通过配属局ID获取配属局名称
	  * <li>方法名称：getBnameByBId
	  * <li>@param bid
	  * <li>@return name
	  * <li>返回类型：String
	  * <li>说明：
	  * <li>创建人：陈志刚
	  * <li>创建日期：2016-8-23 上午11:50:01
	  * <li>修改人： 
	  * <li>修改日期：
	  * <li>修改内容：
	  */
	 public String getBnameByBId(String bid){
		 String name = "";
		 String hql = "  from JgyjcBureau  where bId = '" + bid + "'";	 
		 JgyjcBureau entity = (JgyjcBureau)daoUtils.findSingle(hql);
		 if(entity != null) name = entity.getBName();
		 return name;
	 }
	 /**
	  * 
	  * <li>方法说明：通过配属段ID获取配属段名称
	  * <li>方法名称：getDnameByDId
	  * <li>@param dId 
	  * <li>@return name
	  * <li>返回类型：String
	  * <li>说明：
	  * <li>创建人：陈志刚
	  * <li>创建日期：2016-8-23 上午14:11:23
	  * <li>修改人： 
	  * <li>修改日期：
	  * <li>修改内容：
	  */
	 public String getDnameByDId(String dId){
		 String name = "";		
		 String hql = "  from JgyjcDeport  where dId = '" + dId + "'";	 
		 JgyjcDeport entity = (JgyjcDeport)daoUtils.findSingle(hql);
		 if(entity != null) name = entity.getDName();
		 return name;
	 }
	/**
	 * 
	 * <li>方法说明：简析excel
	 * <li>方法名称：analysisExcel
	 * <li>@param filePath
	 * <li>@param sheetName
	 * <li>@param startCellX
	 * <li>@param startCellY
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：String[][]
	 * <li>说明：
	 * <li>创建人：王利成
	 * <li>创建日期：2014-4-22 下午07:06:24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	 public String[][] analysisExcel(String filePath, String sheetName, String startCellX,String startCellY) throws Exception{
	        try{
	            ExcelReader excel = new ExcelReader(filePath);
	            String[][] datas = excel.getTableValue(sheetName, startCellX.concat(startCellY));
	            excel.close();
	            return datas;
	        }catch(Exception ex){
	            return null;
	        }
	    }
      /**
       * 
       * <li>方法说明：判断数据是否正确
       * <li>方法名称：validate
       * <li>@param Name
       * <li>@param tableName
       * <li>@param fieldName
       * <li>@return
       * <li>返回类型：boolean
       * <li>说明：
       * <li>创建人：王利成
       * <li>创建日期：2014-4-22 下午07:06:37
       * <li>修改人： 
       * <li>修改日期：
       * <li>修改内容：
       */
	 public boolean validate(String Name, String tableName, String fieldName){
		 StringBuilder sb = new StringBuilder("Select count(*) from  ");
		 sb.append(tableName).append(" where ").append(fieldName).append(" = ").append("'" +Name+ "'"); 
		 int count = this.daoUtils.getCount(sb.toString());
		 if(count < 1){
			 return true;
		 }
		 else
			return false;
	 }
	 /**
	  * 
	  * <li>方法说明：验证机车号是否在年检计划表中重复
	  * <li>方法名称：vilidateTrno
	  * <li>@param trainNo
	  * <li>@return
	  * <li>返回类型：boolean
	  * <li>说明：
	  * <li>创建人：王利成
	  * <li>创建日期：2014-4-22 下午07:06:51
	  * <li>修改人： 
	  * <li>修改日期：
	  * <li>修改内容：
	  */ 
	 public boolean vilidateTrno(TrainEnforcePlanDetail tpdDetail){
		 String hql = "Select count(*) from TrainEnforcePlanDetail where recordStatus=0 and trainEnforcePlanIDX = '"+tpdDetail.getTrainEnforcePlanIDX()+"' and trainTypeShortName = '"+tpdDetail.getTrainTypeShortName()+"' and trainNo = '"+tpdDetail.getTrainNo()+"'"; 
		 int count = this.daoUtils.getCount(hql);
		 if(count > 0){
			 return true;
		 }
		 else
			return false;
	 }
	 

}
