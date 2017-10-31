/*
 * Copyright 2003 InterMost Corporation, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must be authorized by InterMost
 *   , this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. INTERMOST AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL
 * INTERMOST OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
 * OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL
 * OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
 * LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE,
 * EVEN IF InterMost HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * lfor use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package com.yunda;

import java.util.TreeMap;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

import com.yunda.common.SysParam;

/**
 * <li>类型名称：Application
 * <li>说明：获取当前程序集相关对象
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-1-18
 * <li>修改人： 
 * <li>修改日期：
 */
public class Application {

	private static Application app = new Application();

	private SessionFactory sf = null;

	private Map<String, SysParam> sp = new TreeMap<String, SysParam>();

	private ApplicationContext ac = null;

	private Application() {
	}

	static Application getInstance() {
		return app;
	}

	public static SessionFactory getSessionFactory() {
		return app.sf;
	}

	void setSessionFactory(SessionFactory sf) {
		this.sf = sf;
	}

	void setSysOptions(Map<String, SysParam> options) {
		this.sp = options;
	}

	void setSpringApplicationContext(ApplicationContext appContext) {
		this.ac = appContext;
	}

	public static ApplicationContext getSpringApplicationContext() {
		return app.ac;
	}

	public static Map<String, SysParam> getSysOptions() {
		return app.sp;
	}
}
