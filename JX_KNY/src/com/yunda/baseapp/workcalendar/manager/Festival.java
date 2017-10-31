package com.yunda.baseapp.workcalendar.manager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 节日类,包括公历和农历的节日.
 * <li>创建人：谭诚
 * <li>创建日期：2013-6-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
/*******************************************************************************
 * Festival.java,此类为一个节日类,包括公历和农历的节日.
 ******************************************************************************/
public final class Festival {
    
    /** 国际节日二维数组 */
    private static final String[][] INTER_FESTIVAL = new String[12][31];
    
    /** 初始化国际节日二维数组 */
    static {
        /** 以下是1月份的节日 */
        INTER_FESTIVAL[0][0]  = "元旦节"; //0 1月1日
        INTER_FESTIVAL[0][5]  = "中国第十三亿人口日"; //1 1月6日
        INTER_FESTIVAL[0][25] = "国际海关日"; //2 1月26日
        
        /** 以下是2月份的节日 */
        INTER_FESTIVAL[1][1]  = "世界湿地日"; //3 2月2日
        INTER_FESTIVAL[1][6]  = "国际声援南非日";//4 2月7日
        INTER_FESTIVAL[1][9]  = "国际气象节";//5 2月10日
        INTER_FESTIVAL[1][13] = "西方情人节";//6 2月14日
        INTER_FESTIVAL[1][14] = "中国12亿人口日";//7 2月15日
        INTER_FESTIVAL[1][20] = "反对殖民制度斗争日 国际母语日";//8 2月21日
        INTER_FESTIVAL[1][23] = "第三世界青年日";//9 2月24日
        INTER_FESTIVAL[1][27] = "世界居住青年调查日";//10 2月28日
        
        /** 以下是3月份的节日 */
        INTER_FESTIVAL[2][0]  = "国际海豹日";//11 3月1日
        INTER_FESTIVAL[2][2]  = "全国爱耳日";//12 3月3日
        INTER_FESTIVAL[2][4]  = "中国青年志愿者服务日 向雷锋同志学习纪念日";//13 3月5日
        INTER_FESTIVAL[2][5]  = "世界青光眼日";//14 3月6日
        INTER_FESTIVAL[2][7]  = "国际劳动妇女节";//15 3月8日 
        INTER_FESTIVAL[2][11] = "植树节 孙中山逝世纪念日"; //16 3月12日
        INTER_FESTIVAL[2][13] = "国际警察日"; //17 3月14日
        INTER_FESTIVAL[2][14] = "国际消费者权益日"; //18 3月15日
        INTER_FESTIVAL[2][15] = "手拉手情系贫困小伙伴全国统一行动日"; //19 3月16日
        INTER_FESTIVAL[2][16] = "中国国医节"; //20 3月17日
        INTER_FESTIVAL[2][20] = "世界林业节"; //21 3月21日
        
        /** 以下是4月份的节日 */
        INTER_FESTIVAL[3][0]  = "国际愚人节"; //22 4月1日
        INTER_FESTIVAL[3][1]  = "国际儿童图书日"; //23 4月2日
        INTER_FESTIVAL[3][6]  = "世界卫生日"; //24 4月7日
        INTER_FESTIVAL[3][20] = "全国企业家活动日"; //25 4月21日
        INTER_FESTIVAL[3][21] = "世界地球日";//26   4月22日
        INTER_FESTIVAL[3][22] = "世界法律日 世界图书和版权日"; //27 4月23日
        INTER_FESTIVAL[3][23] = "世界青年反对殖民主义日 亚非新闻工作者日"; //28 4月24日
        INTER_FESTIVAL[3][24] = "全国儿童预防接种宣传日"; //29 4月25日
        INTER_FESTIVAL[3][25] = "世界知识产权日";//30 4月26日
        INTER_FESTIVAL[3][26] = "联谊城日";//31 4月27日
        INTER_FESTIVAL[3][29] = "全国交通安全反思日";//32 4月30日
        
        /** 以下是5月份的节日 */
        INTER_FESTIVAL[4][0]  = "国际劳动节";//33 5月1日
        INTER_FESTIVAL[4][2]  = "世界新闻自由日";//34 5月3日
        INTER_FESTIVAL[4][3]  = "中国五四青年节 五四运动纪念日 科技传播日";//35   5月4日
        INTER_FESTIVAL[4][7]  = "世界红十字日 世界微笑日";//36    5月8日
        INTER_FESTIVAL[4][11] = "国际护士节";//37   5月12日
        INTER_FESTIVAL[4][14] = "全国碘缺乏病防治日";//38 5月15日
        INTER_FESTIVAL[4][16] = "世界电信日";//39 5月17日
        INTER_FESTIVAL[4][17] = "国际博物馆日";//40 5月18日
        INTER_FESTIVAL[4][19] = "全国母乳喂养宣传日 中国学生营养日";//41 5月20日
        INTER_FESTIVAL[4][21] = "生物多样性国际日";//42 5月22日
        INTER_FESTIVAL[4][25] = "世界向人体条件挑战日";//43 5月26日
        INTER_FESTIVAL[4][29] = "‘五卅’反对帝国主义运动纪念日";//44 5月30日
        INTER_FESTIVAL[4][30] = "世界无烟日";//45 5月31日
        
        /** 以下是6月份的节日 */
        INTER_FESTIVAL[5][0]  = "国际儿童节";//46 6月1日
        INTER_FESTIVAL[5][4]  = "世界环境日";//47 6月5日
        INTER_FESTIVAL[5][5]  = "全国爱眼日";//48 6月6日
        INTER_FESTIVAL[5][10] = "全国人口日";//49 6月11日
        INTER_FESTIVAL[5][16] = "世界防止沙漠化和干旱日";//50 6月17日
        INTER_FESTIVAL[5][19] = "世界难民日";//51 6月20日
        INTER_FESTIVAL[5][21] = "中国儿童慈善活动日";//52 6月22日
        INTER_FESTIVAL[5][22] = "国际奥林匹克日 世界手球日";//53 6月23日
        INTER_FESTIVAL[5][24] = "全国土地日";//54 6月25日
        INTER_FESTIVAL[5][25] = "国际禁毒日 国际宪章日";//55 6月26日
        INTER_FESTIVAL[5][29] = "世界青年联欢日";//56 6月30日
        
        /** 以下是7月份的节日 */
        INTER_FESTIVAL[6][0]  = "建党节 香港回归纪念日 国际建筑日 亚洲三十亿人口日";//57 7月1日
        INTER_FESTIVAL[6][1]  = "国际体育记者日";//58 7月2日
        INTER_FESTIVAL[6][6]  = "中国人民抗日战争纪念日";//59 7月7日
        INTER_FESTIVAL[6][10] = "世界人口日";//60 7月11日
        INTER_FESTIVAL[6][25] = "世界语（言）创立日";//61 7月26日
        INTER_FESTIVAL[6][30] = "非洲妇女日";//62 7月31日
        
        /** 以下是8月份的节日 */
        INTER_FESTIVAL[7][0]  = "中国人民解放军建军节";//63 8月1日
        INTER_FESTIVAL[7][5]  = "国际电影节";//64 8月6日
        INTER_FESTIVAL[7][7]  = "中国男子节（爸爸节）";//65 8月8日
        INTER_FESTIVAL[7][12] = "国际左撇子节";//66 8月13日
        INTER_FESTIVAL[7][14] = "日本正式宣布无条件投降日";//67 8月15日
        INTER_FESTIVAL[7][25] = "全国律师咨询日";//68 8月26日
        
        /** 以下是9月份的节日 */
        INTER_FESTIVAL[8][2]  = "中国抗日战争胜利纪念日";//69 9月3日
        INTER_FESTIVAL[8][7]  = "国际新闻工作者日 世界扫盲日";//70 9月8日
        INTER_FESTIVAL[8][9]  = "中国教师节";//71 9月10日
        INTER_FESTIVAL[8][13] = "世界清洁地球日";//72 9月14日
        INTER_FESTIVAL[8][15] = "国际臭氧层保护日";//73 9月16日
        INTER_FESTIVAL[8][17] = "‘九一八’事件纪念日（中国国耻日）";//74 9月18日
        INTER_FESTIVAL[8][19] = "全国爱牙日";//75 9月20日
        INTER_FESTIVAL[8][20] = "国际和平日";//76 9月21日
        INTER_FESTIVAL[8][26] = "世界旅游日";//77 9月27日
        
        /** 以下是10月份的节日 */
        INTER_FESTIVAL[9][0]  = "国庆节";//78 10月1日
        INTER_FESTIVAL[9][3]  = "世界动物日";//79 10月4日
        INTER_FESTIVAL[9][8]  = "世界邮政日";//80 10月9日
        INTER_FESTIVAL[9][9]  = "辛亥革命纪念日 世界精神卫生日";//81 10月10日
        INTER_FESTIVAL[9][10] = "声援南非政治犯日";//82 10月11日
        INTER_FESTIVAL[9][12] = "中国少年先锋队诞辰日";//83 10月13日
        INTER_FESTIVAL[9][13] = "世界标准日";//84 10月14日
        INTER_FESTIVAL[9][14] = "国际盲人节";//85 10月15日
        INTER_FESTIVAL[9][15] = "世界粮食日";//86 10月16日
        INTER_FESTIVAL[9][16] = "世界消除贫困日";//87 10月17日
        INTER_FESTIVAL[9][21] = "世界传统医药日";//88 10月22日
        INTER_FESTIVAL[9][23] = "联合国日 世界发展信息日";//89 10月24日
        INTER_FESTIVAL[9][27] = "世界男性健康日";//90 10月28日
        INTER_FESTIVAL[9][30] = "世界勤俭日";//91 10月31日
        
        /** 以下是11月份的节日 */
        INTER_FESTIVAL[10][7]  = "中国记者节";//92 11月8日
        INTER_FESTIVAL[10][8]  = "中国消防宣传日";//93 11月9日
        INTER_FESTIVAL[10][9]  = "世界青年节";//94 11月10日
        INTER_FESTIVAL[10][10] = "光棍节";//116 11月11日
        INTER_FESTIVAL[10][13] = "世界糖尿病日";//95 11月14日
        INTER_FESTIVAL[10][16] = "国际大学生节";//96 11月17日
        INTER_FESTIVAL[10][20] = "世界电视日 世界问候日";//97 11月21日
        
        /** 以下是12月份的节日 */
        INTER_FESTIVAL[11][0]  = "世界艾滋病日";//98 12月1日
        INTER_FESTIVAL[11][1]  = "废除一切形式奴役世界日";//99 12月2日
        INTER_FESTIVAL[11][3]  = "世界残疾人日";//100 12月3日
        INTER_FESTIVAL[11][3]  = "中国法制宣传日";//101    12月4日
        INTER_FESTIVAL[11][4]  = "促进经济和社会发展资源人员国际日 世界弱能人士日";//102 12月5日
        INTER_FESTIVAL[11][6]  = "国际民航日";//103 12月7日
        INTER_FESTIVAL[11][8]  = "一二九运动纪念日 国际反腐败日 世界足球日";//104 12月9日
        INTER_FESTIVAL[11][9]  = "世界人权日";//105 12月10日
        INTER_FESTIVAL[11][10] = "世界防治哮喘日";//106 12月11日
        INTER_FESTIVAL[11][11] = "西安事变纪念日";//107 12月12日
        INTER_FESTIVAL[11][12] = "南京大屠杀纪念日";//108 12月13日
        INTER_FESTIVAL[11][14] = "世界强化免疫日";//109 12月15日
        INTER_FESTIVAL[11][19] = "澳门回归纪念日";//110 12月20日
        INTER_FESTIVAL[11][20] = "国际篮球日";//111 12月21日
        INTER_FESTIVAL[11][23] = "平安夜";//112 12月24日
        INTER_FESTIVAL[11][24] = "圣诞节";//113 12月25日
        INTER_FESTIVAL[11][25] = "节礼节";//114 12月26日
        INTER_FESTIVAL[11][28] = "生物多样性国际日";//115 12月29日
    }

	// 某月的第几个星期几。 5,6,7,8 表示到数第 1,2,3,4 个星期几
	String wFtv[] = {
			"0110 黑人日",
			"0150 世界麻风日", // 一月的最后一个星期日（月倒数第一个星期日）
			"0520 国际母亲节", "0530 全国助残日", "0630 父亲节", "0911 劳动节", "0932 国际和平日",
			"0940 国际聋人节 世界儿童日", "0950 世界海事日", "1011 国际住房日",
			"1013 国际减轻自然灾害日(减灾日)", "1144 感恩节" };

	// 农历节日
	public String lFtv[] = { 
			"春节", // 0 	正月初一
			"元宵节", // 1	正月十五
			"端午节", // 2 五月初五
			"中国七夕情人节",// 3 七月初七
			"中秋节",//4 八月十五
			"重阳节",//5 九月初九
			"除夕",//6 腊月最后一天
			"腊八节",//7 腊月初八
			"小年",//8 腊月二十三、二十四
			"苗族妹妹节",//9 农历三月15日
			"苗族龙船节",//10 农历五月二十四
			"彝族火把节",//11 农历六月二十四
			"寒食",//12 农历三月2日
			"中元节-鬼节",//13 七月十五
			"苗年",//14 农历十月四日
			"下元节",//15 农历十月十五
			
			"测试数据"//11月28
			};

	/**
	 * <li>说明：获取公历节日.
     * <li>创建人：谭诚
     * <li>创建日期：2013-6-22
	 * <li>修改人：何涛
	 * <li>修改日期：2015-9-24
	 * <li>修改内容：修改使用二维数组存储国际节日数据，优化查询方法
	 * @param month 月
	 * @param day 日
	 * @return 国际节日名称
	 */
	public String showSFtv(int month, int day) {
        try {
            return Festival.INTER_FESTIVAL[--month][--day];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

	/**
	 * <li>说明：获取农历节日.
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-6-22
	 * <li>修改人：何涛
	 * <li>修改日期：201-09-24
	 * <li>修改内容：添加注释
	 * @param month 月 
	 * @param day 日
	 * @param dayOfmonth 月中天
	 * @return 农历节日名称
	 */
	public String showLFtv(int month, int day, int dayOfmonth) {
		if (month == 1 && day == 1)
			return lFtv[0];//春节
		else if (month == 1 && day == 15)
			return lFtv[1];//正月十五
		else if (month == 5 && day == 5)//端午节
			return lFtv[2];
		else if (month == 7 && day == 7)//七夕节
			return lFtv[3];
		else if (month == 8 && day == 15)//中秋节
			return lFtv[4];
		else if (month == 9 && day == 9)//重阳节
			return lFtv[5];
		else if (month == 12 && day == 29 && dayOfmonth == 29)
			return lFtv[6];// 判断农历最后一个月是否是29天,是则这一天显示除夕.
		else if (month == 12 && day == 30 && dayOfmonth == 30)
			return lFtv[6];// 判断农历最后一个月是否是30天,是则这一天显示除夕.
		else if (month == 12 && day == 8)
			return lFtv[7];//腊八节
		else if (month == 12 && day == 23)
			return lFtv[8];//小年
		else if (month == 12 && day == 24)
			return lFtv[8];//小年，各地传统不一样
		else if (month == 3 && day == 2)
			return lFtv[12];//寒食
		else if (month == 3 && day == 15)
			return lFtv[8];//苗族姊妹节
		else if (month == 5 && day == 24)
			return lFtv[10];//苗族龙船节
		else if (month == 6 && day == 24)
			return lFtv[11];//彝族火把节
		else if (month == 7 && day == 15)
			return lFtv[13];//鬼节
		else if (month == 10 && day == 4)
			return lFtv[14];//苗年
		else if (month == 10 && day == 15)
			return lFtv[15];//下元节
		
		else if (month == 11 && day == 28)
			return lFtv[16];//测试数据
		
		return "";
	}
    
}