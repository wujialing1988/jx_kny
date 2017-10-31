package com.yunda.frame.baseapp.message.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.yunda.Application;
import com.yunda.frame.baseapp.message.entity.MessageReceiver;
import com.yunda.frame.baseapp.message.entity.MessageSender;
import com.yunda.frame.baseapp.message.entity.ParticipantEntity;
import com.yunda.frame.baseapp.message.manager.MessageSenderManager;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.frame.yhgl.manager.OrganizationManager;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.util.DaoUtils;

/**
 * <li>标题: 机车检修检修信息系统
 * <li>说明: 消息推送webservice服务
 * <li>创建人：何涛
 * <li>创建日期：2015-3-4
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public class MessageSenderForBPSAction extends JXBaseAction<MessageSender, MessageSender, MessageSenderManager> {
    
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    
    /**
     * <li>说明：获取BPS流程活动参与者对象集合
     * <li>创建人：何涛
     * <li>创建日期：2015-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param messageSenderManager MessageSender业务类,消息记录发送者表
     * @param activityInstID 活动实例ID
     * @return BPS流程活动参与者对象集合
     */
    @SuppressWarnings({ "unchecked", "static-access" })
    private List<ParticipantEntity> getParticipantList(MessageSenderManager messageSenderManager, String activityInstID) {
        // 查询BPS活动参与者
        StringBuffer sb = new StringBuffer("SELECT participanttype, participantid, participantname FROM WFWIParticipant WHERE WORKITEMID IN (");
        sb.append("SELECT WORKITEMID FROM WFWORKITEM  WHERE ACTIVITYINSTID = '").append(activityInstID).append("'");
        sb.append(") ORDER BY participanttype");
        
        
        // 根据流程实例主键及传入的其他参数购置消息实例
        /** ******** 针对流程业务处理可能存在时间延迟的特殊处理 - 开始 ******** */
        List<ParticipantEntity> participantList = null;
        int times = 1;
        do {
            participantList = messageSenderManager.getDaoUtils().executeSqlQueryEntity(sb.toString(), ParticipantEntity.class);
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                ExceptionUtil.process(e, logger);
            }
            if (null != participantList && participantList.size() > 0) {
                break;
            }
        } while (times++ < 2);
        /** ******** 针对流程业务处理可能存在时间延迟的特殊处理 - 结束 ******** */
        return participantList;
    }

    /**
     * <li>说明：获取机车施修任务兑现单车型车号
     * <li>创建人：何涛
     * <li>创建日期：2015-3-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIdx 机车施修任务兑现单主键
     * @param daoUtils 数据库方法工具类实例
     * @return 车型车号
     */
    @SuppressWarnings("unchecked")
    private String getTrainTypeAndNo(String rdpIdx, DaoUtils daoUtils) {
        // 获取车型车号
        String hql = "From TrainWorkPlan Where idx = ?";
        Object object = daoUtils.findSingle(hql, new Object[]{ rdpIdx });
        if (null == object) {
            return null;
        }
        // 强制类型转换为“机车施修任务兑现单”实体对象
        TrainWorkPlan rdp = (TrainWorkPlan) object;
        return rdp.getTrainTypeShortName() + rdp.getTrainNo();
    }
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：何涛
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param messageSender 消息发送者实例
     * @param title 消息标题
     * @param content 消息内容
     * @param sendTime 发送时间
     * @param receiverType 消息接收者类型
     * @return 消息发送者实例
     */
    private MessageSender getMessageSender(MessageSender messageSender, String title, String content, Date sendTime, int receiverType) {
        if (null != messageSender) {
            return messageSender;
        }
        MessageSender entity = new MessageSender();
        entity.setTitle(title);
        entity.setSender(0L);
        entity.setSenderName("BPS流程消息");
        entity.setContent(content);
        entity.setOnlineMode();
        entity.setReceiverType(receiverType);
        entity.setSendTime(sendTime);
        entity.setReceiverArray(new ArrayList<MessageReceiver>());
        return entity;
    }
    
    /**
     * <li>说明：将BPS流程活动参与者根据类型分类存入Map
     * <li>创建人：何涛
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list BPS流程活动参与者实例列表
     * @return map
     */
    private Map<String, List<ParticipantEntity>> putToMap(List<ParticipantEntity> list) {
        Map<String, List<ParticipantEntity>> map = new HashMap<String, List<ParticipantEntity>>();
        for (ParticipantEntity participant : list) {
            if (ParticipantEntity.PARTICIPANT_TYPE_DUTY.equals(participant.getParticipanttype())) {
                this.putToMap(map, participant);
            }
            if (ParticipantEntity.PARTICIPANT_TYPE_ORGANIZATION.equals(participant.getParticipanttype())) {
                this.putToMap(map, participant);
            }
            if (ParticipantEntity.PARTICIPANT_TYPE_PERSON.equals(participant.getParticipanttype())) {
                this.putToMap(map, participant);
            }
            if (ParticipantEntity.PARTICIPANT_TYPE_POSITION.equals(participant.getParticipanttype())) {
                this.putToMap(map, participant);
            }
            if (ParticipantEntity.PARTICIPANT_TYPE_ROLE.equals(participant.getParticipanttype())) {
                this.putToMap(map, participant);
            }
            if (ParticipantEntity.PARTICIPANT_TYPE_WORKGROUP.equals(participant.getParticipanttype())) {
                this.putToMap(map, participant);
            }
        }
        return map;
    }
    
    /**
     * <li>说明：将BPS流程活动参与者根据类型分类存入Map
     * <li>创建人：何涛
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param map Map实例
     * @param participant BPS流程活动参与者实例
     */
    private void putToMap(Map<String, List<ParticipantEntity>> map, ParticipantEntity participant) {
        List<ParticipantEntity> list = map.get(participant.getParticipanttype());
        if (null == list) {
            list = new ArrayList<ParticipantEntity>();
            map.put(participant.getParticipanttype(), list);
        }
        list.add(participant);
    }
    
    /**
     * <li>标题: 机车整备检修信息系统
     * <li>说明: 消息推送webservice服务帮助类
     * <li>创建人：何涛
     * <li>创建日期：2015-3-12
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部整备系统项目组
     * @version 1.0
     */
    private static class MessageSenderForBPSActionHelper {
        
        /** 员工服务类 */
        private static OmEmployeeManager omEmployeeManager =
            (OmEmployeeManager) Application.getSpringApplicationContext().getBean("omEmployeeManager");
        
        /**
         * <li>说明：获取BPS流程参与者实例的ID拼接字符串
         * <li>创建人：何涛
         * <li>创建日期：2015-3-12
         * <li>修改人：
         * <li>修改日期：
         * <li>修改内容：
         * @param participantEntityList BPS流程参与者实例列表
         * @return BPS流程参与者实例的ID拼接字符串
         */
        private static String getParticipantIds(List<ParticipantEntity> participantEntityList) {
            StringBuffer sb = new StringBuffer();
            for (ParticipantEntity entity : participantEntityList) {
                sb.append(",").append(entity.getParticipantid());
            }
            String ids = sb.substring(1).replace(",", "','");
            return ids;
        }
        
        /**
         * <li>说明：构造参与者类型为职务的消息接收者(个人)
         * <li>创建人：何涛
         * <li>创建日期：2015-3-12
         * <li>修改人：
         * <li>修改日期：
         * <li>修改内容：
         * @param participantEntityList BPS流程活动参与者实例列表
         * @return 消息接收者实体列表
         */
//        private static List<MessageReceiver> getReceiverByDuty(List<ParticipantEntity> participantEntityList) {
//            String ids = getParticipantIds(participantEntityList);
//            
//            OmDutyManager omDutyManager = (OmDutyManager) Application.getSpringApplicationContext().getBean("omDutyManager");
//            String hql = "From OmDuty Where dutyid in('" + ids + "')";
//            Collection<OmDuty> collection = omDutyManager.find(hql);
//            if (null == collection || 0 >= collection.size()) {
//                return null;
//            }
//            
//            MessageReceiver mr = null;
//            List<MessageReceiver> mrList = new ArrayList<MessageReceiver>(collection.size());
//            
//            for (OmDuty duty : collection) {
//                // TODO 根据职务查询人员列表（暂时不实现）
//            }
//            return mrList;
//        }
        
        /**
         * <li>说明：构造参与者类型为部门的消息接收者(部门)
         * <li>创建人：何涛
         * <li>创建日期：2015-3-12
         * <li>修改人：
         * <li>修改日期：
         * <li>修改内容：
         * @param participantEntityList BPS流程活动参与者实例列表
         * @return 消息接收者实体列表
         */
        @SuppressWarnings("unchecked")
        private static List<MessageReceiver> getReceiverByOrganization(List<ParticipantEntity> participantEntityList) {
            String ids = getParticipantIds(participantEntityList);
            
            OrganizationManager organizationManager = (OrganizationManager) Application.getSpringApplicationContext().getBean("organizationManager");
            String hql = "From OmOrganization Where orgid in('" + ids + "')";
            Collection<OmOrganization> collection = organizationManager.find(hql);
            if (null == collection || 0 >= collection.size()) {
                return null;
            }
            
            MessageReceiver mr = null;
            List<MessageReceiver> mrList = new ArrayList<MessageReceiver>(collection.size());
            for (OmOrganization org : collection) {
                mr = new MessageReceiver();
                mr.setReceiverName(org.getOrgname());
                mr.setReceiver(org.getOrgseq());
                mrList.add(mr);
            }
            return mrList;
        }
        
        /**
         * <li>说明：构造参与者类型为人员的消息接收者(人员)
         * <li>创建人：何涛
         * <li>创建日期：2015-3-12
         * <li>修改人：
         * <li>修改日期：
         * <li>修改内容：
         * @param participantEntityList BPS流程活动参与者实例列表
         * @return 消息接收者实体列表
         */
        @SuppressWarnings("unchecked")
        private static List<MessageReceiver> getReceiverByPerson(List<ParticipantEntity> participantEntityList) {
            String ids = getParticipantIds(participantEntityList);
            
            String hql = "From OmEmployee Where userid in('" + ids + "')";
            
            List<MessageReceiver> mrList = getMessageReceiverList(hql);
            return mrList;
        }
        
        /**
         * <li>说明：构造参与者类型为岗位的消息接收者(人员)
         * <li>创建人：何涛
         * <li>创建日期：2015-3-12
         * <li>修改人：
         * <li>修改日期：
         * <li>修改内容：
         * @param participantEntityList BPS流程活动参与者实例列表
         * @return 消息接收者实体列表
         */
        private static List<MessageReceiver> getReceiverByPosition(List<ParticipantEntity> participantEntityList) {
            List<MessageReceiver> mrList = new ArrayList<MessageReceiver>();
            for (ParticipantEntity entity : participantEntityList) {
                String participantid = entity.getParticipantid();
                MessageReceiver mr = null;
                List<OmEmployee> collection = omEmployeeManager.findByPosition(Long.valueOf(participantid));
                if (null == collection || collection.size() <= 0) {
                    continue;
                }
                for (OmEmployee emp : collection) {
                    if (null == emp) {
                        continue;
                    }
                    mr = new MessageReceiver();
                    mr.setReceiverName(emp.getEmpname());
                    mr.setReceiver(String.valueOf(emp.getEmpid()));
                    mrList.add(mr);
                }
            }
            return mrList;
        }
        
        /**
         * <li>说明：构造参与者类型为角色的消息接收者(人员)
         * <li>创建人：何涛
         * <li>创建日期：2015-3-12
         * <li>修改人：
         * <li>修改日期：
         * <li>修改内容：
         * @param participantEntityList BPS流程活动参与者实例列表
         * @return 消息接收者实体列表
         */
        @SuppressWarnings("unchecked")
        private static List<MessageReceiver> getReceiverByRole(List<ParticipantEntity> participantEntityList) {
            String ids = getParticipantIds(participantEntityList);
            
            String hql =
                "From OmEmployee Where operatorid In (Select role.id.operatorid From AcOperatorrole role Where role.id.roleid In('" + ids + "'))";
            
            List<MessageReceiver> mrList = getMessageReceiverList(hql);
            return mrList;
            
        }
        
        /**
         * <li>说明：构造参与者类型为工作组的消息接收者(人员)
         * <li>创建人：何涛
         * <li>创建日期：2015-3-12
         * <li>修改人：
         * <li>修改日期：
         * <li>修改内容：
         * @param participantEntityList BPS流程活动参与者实例列表
         * @return 消息接收者实体列表
         */
        @SuppressWarnings("unchecked")
        private static List<MessageReceiver> getReceiverByWorkgroup(List<ParticipantEntity> participantEntityList) {
            String ids = getParticipantIds(participantEntityList);
            
            String hql = "From OmEmployee Where empid In (Select group.id.empid From OmEmpgroup group Where group.id.groupid In('" + ids + "'))";
            
            List<MessageReceiver> mrList = getMessageReceiverList(hql);
            return mrList;
        }

        /**
         * <li>说明：查询人员表，获取消息接收者实体列表
         * <li>创建人：何涛
         * <li>创建日期：2015-3-12
         * <li>修改人： 
         * <li>修改日期：
         * <li>修改内容：
         * @param hql 查询语句
         * @return 消息接收者实体列表
         */
        @SuppressWarnings("unchecked")
        private static List<MessageReceiver> getMessageReceiverList(String hql) {
            Collection<OmEmployee> collection = omEmployeeManager.find(hql);
            if (null == collection || 0 >= collection.size()) {
                return null;
            }
            
            MessageReceiver mr = null;
            List<MessageReceiver> mrList = new ArrayList<MessageReceiver>();
            for (OmEmployee emp : collection) {
                if (null == emp) {
                    continue;
                }
                mr = new MessageReceiver();
                mr.setReceiverName(emp.getEmpname());
                mr.setReceiver(String.valueOf(emp.getEmpid()));
                mrList.add(mr);
            }
            return mrList;
        }
        
    }
    
}
