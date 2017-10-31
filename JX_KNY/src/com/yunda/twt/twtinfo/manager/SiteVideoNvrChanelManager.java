package com.yunda.twt.twtinfo.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.twt.twtinfo.entity.SiteVideoNvr;
import com.yunda.twt.twtinfo.entity.SiteVideoNvrChanel;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: SiteVideoNvrChanel业务类，网络硬盘录像机通道
 * <li>创建人：何涛
 * <li>创建日期：2015-6-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="siteVideoNvrChanelManager")
public class SiteVideoNvrChanelManager extends JXBaseManager<SiteVideoNvrChanel, SiteVideoNvrChanel> {
    
	/** SiteVideoNvr业务类，视频监控网络硬盘录像机 */
	@Resource
	private SiteVideoNvrManager siteVideoNvrManager;
	
    /**
     * <li>说明：更新前验证通道号的唯一性
     * <li>创建人：何涛
     * <li>创建日期：2015-06-01
     * <li>修改人： 
     * <li>修改日期：
     * @param t 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @Override
    public String[] validateUpdate(SiteVideoNvrChanel t) {
        SiteVideoNvrChanel entity = this.getModelByChanelID(t.getVideoNvrIDX(), t.getChanelID());
        // 通道号唯一性验证
        if (null != entity && !entity.getIdx().equals(t.getIdx()) && entity.getChanelID().equals(t.getChanelID())) {
        	return new String[]{"通道号【" + t.getChanelID() + "】已经存在，不可以重复添加！"};
        }
        // 重复绑定验证，如果重复绑定，则解除之前的绑定
        if (null != t.getVideoCode() && t.getVideoCode().trim().length() > 0 && null != t.getVideoName() && t.getVideoName().trim().length() > 0) {
        	entity = this.getModel(t.getSiteID(), t.getVideoCode(), t.getVideoName());
        	if (null != entity && !entity.getIdx().equals(t.getIdx())) {
        		try {
					this.unBind(entity.getIdx());
				} catch (Exception e) {
					throw new BusinessException(e);
				}
        	}
        }
        return null;
    }
    
    /**
     * <li>说明：根据通道号获取网络硬盘录像机通道实例对象
     * <li>创建人：何涛
     * <li>创建日期：2015-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param videoNvrIDX 网络硬盘录像机idx主键
     * @param chanelID 通道号
     * @return 网络硬盘录像机通道实例对象
     */
    private SiteVideoNvrChanel getModelByChanelID(String videoNvrIDX, Integer chanelID) {
        String hql = "From SiteVideoNvrChanel Where videoNvrIDX = ? And chanelID = ?";
        return (SiteVideoNvrChanel)this.daoUtils.findSingle(hql, new Object[]{videoNvrIDX, chanelID});
    }
    
    /**
     * <li>说明：获取单个网络硬盘录像机下属的所有通道
     * <li>创建人：何涛
     * <li>创建日期：2015-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param videoNvrIDX 网络硬盘录像机idx主键
     * @return List<SiteVideoNvrChanel>
     */
    @SuppressWarnings("unchecked")
    public List<SiteVideoNvrChanel> getModelByVideoNvrIDX(String videoNvrIDX) {
        String hql = "From SiteVideoNvrChanel Where videoNvrIDX = ? ";
        return this.daoUtils.find(hql, new Object[]{videoNvrIDX});
    }

    /**
     * <li>说明：获取单个NVR下属的最大通道号
     * <li>创建人：何涛
     * <li>创建日期：2015-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param videoNvrIDX 网络硬盘录像机idx主键
     * @return 单个NVR下属的最大通道号
     */
    public Integer getMaxChanelID(String videoNvrIDX) {
        String hql = "Select Max(chanelID) From SiteVideoNvrChanel Where videoNvrIDX = ?";
        return (Integer) this.daoUtils.findSingle(hql, videoNvrIDX);
    }

    /**
     * <li>说明：初始化通道
     * <li>创建人：何涛
     * <li>创建日期：2015-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param initNum 初始化通道数量
     * @param videoNvrIDX 网络硬盘录像机idx主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void init(String initNum, String videoNvrIDX) throws BusinessException, NoSuchFieldException {
        // NVR下属的最大通道号
        int mChanelID = 0;
        Integer maxChanelID = this.getMaxChanelID(videoNvrIDX);
        if (null != maxChanelID) {
            mChanelID = maxChanelID.intValue();
        }
        int nInitNum = Integer.parseInt(initNum);
        SiteVideoNvrChanel entity = null;
        List<SiteVideoNvrChanel> entityList = new ArrayList<SiteVideoNvrChanel>(nInitNum);
        for (int i = 0; i < nInitNum; i++) {
            int chanelID = mChanelID + i + 1;
            entity = new SiteVideoNvrChanel();
            entity.setVideoNvrIDX(videoNvrIDX);
            entity.setChanelID(chanelID);
            entity.setChanelName("通道" + chanelID);
            entityList.add(entity);
        }
        this.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：获取网络录像机通道实例对象
     * <li>创建人：何涛
     * <li>创建日期：2015-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param siteID 站场ID
     * @param videoCode 视频点编码
     * @param videoName 视频点名称
     * @return SiteVideoNvrChanel
     */
    public SiteVideoNvrChanel getModel(String siteID, String videoCode, String videoName) {
    	String hql ="Select new SiteVideoNvrChanel(a.idx, a.videoNvrIDX, b.nvrName, a.chanelID, a.chanelName, a.siteID, a.videoCode, a.videoName) From SiteVideoNvrChanel a, SiteVideoNvr b Where a.videoNvrIDX = b.idx And a.siteID = ? And a.videoCode = ? And a.videoName = ?";
    	return (SiteVideoNvrChanel) this.daoUtils.findSingle(hql, new Object[]{siteID, videoCode, videoName});
    }
    
    /**
     * <li>说明：获取网络录像机通道实例对象，其中包含了摄像机所属NVR的实例对象
     * <li>创建人：何涛
     * <li>创建日期：2015-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param siteID 站场ID
     * @param videoCode 视频点编码
     * @return SiteVideoNvrChanel
     */
    public SiteVideoNvrChanel getModel(String siteID, String videoCode) {
    	String hql ="From SiteVideoNvrChanel Where siteID = ? And videoCode = ?";
    	SiteVideoNvrChanel entity = (SiteVideoNvrChanel) this.daoUtils.findSingle(hql, new Object[]{siteID, videoCode});
    	if (null == entity) {
    		return null;
    	}
    	SiteVideoNvr nvr = siteVideoNvrManager.getModelById(entity.getVideoNvrIDX());
    	entity.setSiteVideoNvr(nvr);
    	return entity;
    }
    
    /**
     * <li>说明：解除网络录像机通道与台位图上视频监控点的绑定
     * <li>创建人：何涛
     * <li>创建日期：2015-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx idx主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void unBind(Serializable idx) throws BusinessException, NoSuchFieldException {
    	SiteVideoNvrChanel entity = this.getModelById(idx);
    	entity.setVideoCode(null);
    	entity.setVideoName(null);
    	this.saveOrUpdate(entity);
    }
    
    
    /**
     * <li>说明：批量解除网络录像机通道与台位图上视频监控点的绑定
     * <li>创建人：何涛
     * <li>创建日期：2015-6-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids idx主键数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void unBind(String[] ids) throws BusinessException, NoSuchFieldException {
        List<SiteVideoNvrChanel> entityList = new ArrayList<SiteVideoNvrChanel>(ids.length);
        SiteVideoNvrChanel entity = null;
        for (String idx : ids) {
            entity = this.getModelById(idx);
            entity.setVideoCode(null);
            entity.setVideoName(null);
            entityList.add(entity);
        }
        this.saveOrUpdate(entityList);
    }

    /**
     * <li>说明：导入
     * <li>创建人：何涛
     * <li>创建日期：2015-6-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param impStencil 导入模板文件
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws Exception 
     */
    public void saveUpload(File impStencil) throws FileNotFoundException, IOException, BusinessException, NoSuchFieldException {
//        POIFSFileSystem poi = new POIFSFileSystem(new FileInputStream(impStencil));
//        HSSFWorkbook workBook = new HSSFWorkbook(poi); 
//        int sheetIndex = 0;
//        HSSFSheet sheet = workBook.getSheetAt(sheetIndex);
//        while (null != sheet) {
//            try {
//                saveByStencil(sheet);
//                sheet = workBook.getSheetAt(++sheetIndex);
//            } catch (IndexOutOfBoundsException e) {
//                break;
//            }
//        }
    }    
    
    /**
     * <li>说明：导入单个sheet中的视频监控配置信息
     * <li>创建人：何涛
     * <li>创建日期：2015-6-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param sheet Excel工作薄中的单个sheet页实例
     * @param validList 用于验证网络硬盘录像机名称的全局唯一性
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws Exception
     */
//    private void saveByStencil(HSSFSheet sheet) throws BusinessException, NoSuchFieldException {
//        String[][] tableValues = ExcelUtil.getTableValue(sheet, "5A");
//        Map<String, SiteVideoNvr> map = parseTableValues(tableValues);
//        Collection<SiteVideoNvr> collection = map.values();
//        if (null == collection || collection.size() <= 0) {
//            return;
//        }
//        for (SiteVideoNvr nvr : collection) {
//            this.saveByStencil(nvr);
//        }
//    }

    /**
     * <li>说明：解析sheet单元格数据为java对象实体集合
     * <li>创建人：何涛
     * <li>创建日期：2015-6-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param tableValues sheet单元格数据
     * @return Map<String, SiteVideoNvr> 
     */
    @SuppressWarnings("unused")
    private Map<String, SiteVideoNvr> parseTableValues(String[][] tableValues) {
        Map<String, SiteVideoNvr> map = new HashMap<String, SiteVideoNvr>();
        SiteVideoNvr nvr = null;
        SiteVideoNvrChanel nvrChanel = null;
        for (int i = 0; i < tableValues.length; i++) {
            String[] values = tableValues[i];
            boolean isValid = true;
            for (int j = 0; j < 7; j++) {
                if (null == values[j] || 0 >= values[j].trim().length()) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid) {
                continue;
            }
            String nvrIP = values[1].trim();
            String nvrPort = values[2].trim();
            if (map.containsKey(nvrIP + nvrPort)) {
                nvr = map.get(nvrIP + nvrPort);
            } else {
                nvr = new SiteVideoNvr();
                nvr.setNvrName(values[0].trim());          // NVR名称
                nvr.setNvrIP(nvrIP);            // IP
                nvr.setNvrPort(Integer.valueOf(nvrPort));          // Port
                nvr.setUsername(values[3].trim());         // 用户名
                nvr.setPassword(values[4].trim());         // 密码
                nvr.setSiteID(JXConfig.getInstance().getSynSiteID());
                nvr.setChanelList(new ArrayList<SiteVideoNvrChanel>());
                map.put(nvrIP + nvrPort, nvr);
            }
            nvrChanel = new SiteVideoNvrChanel();
            nvrChanel.setChanelID(Integer.valueOf(values[5].trim()));
            nvrChanel.setChanelName(values[6].trim());
            if (!nvr.getChanelList().contains(nvrChanel)) {
                nvr.getChanelList().add(nvrChanel);
            }
        }
        return map;
    }    
    
    /**
     * <li>说明：导入
     * <li>创建人：何涛
     * <li>创建日期：2015-6-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nvr 视频监控网络硬盘录像机实例对象，含下属视频通道实例集合
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @SuppressWarnings("unused")
    private void saveByStencil(SiteVideoNvr nvr) throws BusinessException, NoSuchFieldException {
        SiteVideoNvr entity = this.siteVideoNvrManager.getModel(nvr.getNvrIP(), nvr.getNvrPort(), nvr.getSiteID());
        if (null == entity) {
            String[] validateMsg = this.siteVideoNvrManager.validateUpdate(nvr);
            if (null != validateMsg) {
                throw new BusinessException(validateMsg[0]);
            }
            this.siteVideoNvrManager.saveOrUpdate(nvr);
            entity = nvr;
        } else if (!nvr.equals(entity)) {
            entity.setNvrName(nvr.getNvrName());
            entity.setUsername(nvr.getUsername());
            entity.setPassword(nvr.getPassword());
            String[] validateMsg = this.siteVideoNvrManager.validateUpdate(entity);
            if (null != validateMsg) {
                throw new BusinessException(validateMsg[0]);
            }
            this.siteVideoNvrManager.saveOrUpdate(entity);
        }
        // 保存NVR下属的通道号
        List<SiteVideoNvrChanel> chanelList = nvr.getChanelList();
        if (null == chanelList || 0 >= chanelList.size()) {
            return;
        }
        List<SiteVideoNvrChanel> entityList = new ArrayList<SiteVideoNvrChanel>();
        for (SiteVideoNvrChanel nvrChanel : chanelList) {
            String hql = "From SiteVideoNvrChanel Where videoNvrIDX = ? And chanelID = ?";
            SiteVideoNvrChanel nvrChanelEntity = (SiteVideoNvrChanel) this.daoUtils.findSingle(hql, new Object[]{entity.getIdx(), nvrChanel.getChanelID()});
            if (null == nvrChanelEntity) {
                nvrChanel.setVideoNvrIDX(entity.getIdx());
                entityList.add(nvrChanel);
            } else if (nvrChanelEntity.getChanelID().intValue() != nvrChanel.getChanelID().intValue() 
                || !nvrChanelEntity.getChanelName().equals(nvrChanel.getChanelName())) {
                nvrChanelEntity.setChanelID(nvrChanel.getChanelID());
                nvrChanelEntity.setChanelName(nvrChanel.getChanelName());
                entityList.add(nvrChanelEntity);
            }
        }
        this.saveOrUpdate(entityList);
    }
    
}
