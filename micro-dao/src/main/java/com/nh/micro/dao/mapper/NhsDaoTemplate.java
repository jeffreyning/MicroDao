package com.nh.micro.dao.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nh.micro.rule.engine.core.GroovyExecUtil;
import com.nh.micro.template.IdHolder;
import com.nh.micro.template.MicroServiceTemplateSupport;

/**
 * 
 * @author ninghao
 *
 */
public class NhsDaoTemplate {
	private static Map supportHolder=new HashMap();
	private static String dbName="default";
	
	public NhsDaoTemplate(){
		
	};
	public NhsDaoTemplate(String dbName){
		this.dbName=dbName;
	}
	public static NhsDaoTemplate getInstance(){
		NhsDaoTemplate instance=(NhsDaoTemplate) supportHolder.get("default");
		if(instance==null){
			instance=new NhsDaoTemplate();
			supportHolder.put("default", instance);
		}
		return instance;

	}
	
	public static NhsDaoTemplate getInstance(String dbName){
		if(dbName==null || "".equals(dbName)){
			dbName="default";
		}
		NhsDaoTemplate instance=(NhsDaoTemplate) supportHolder.get(dbName);
		if(instance==null){
			instance=new NhsDaoTemplate(dbName);
			supportHolder.put(dbName, instance);
		}
		return instance;
		
	}	
	public Map getInfoList4PageService(String groovyName, String methodName,Map pageMap, Object... paramArray) throws Exception{
		List placeList=new ArrayList();
		String sql=(String) GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray,placeList);
		String countSql="select count(1) from ( "+sql+" )";
		String dbType=MicroServiceTemplateSupport.getInstance(dbName).calcuDbType();
		if(dbType!=null && !dbType.equals("oracle")){
			countSql=countSql+"  as micro_total_alias";
		}
		return MicroServiceTemplateSupport.getInstance(dbName).getInfoList4PageServiceBySql(countSql, sql, pageMap);
	}

	public List getInfoListAllService(String groovyName, String methodName, Object... paramArray) throws Exception{
		List placeList=new ArrayList();
		String sql=(String) GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray,placeList);
		return MicroServiceTemplateSupport.getInstance(dbName).getInfoListAllServiceBySql(sql, placeList);
	}

	public Map getSingleInfoService(String groovyName, String methodName, Object... paramArray) throws Exception{
		List placeList=new ArrayList();
		String sql=(String) GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray,placeList);
		return MicroServiceTemplateSupport.getInstance(dbName).getSingleInfoService(sql, placeList);
	}
	
	public Integer createInfoService(String groovyName,String methodName, Object... paramArray) throws Exception{
		IdHolder idHolder=checkIdHolder(paramArray);
		List placeList=new ArrayList();
		String sql=(String) GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray,placeList);
		return MicroServiceTemplateSupport.getInstance(dbName).createInfoServiceBySql(sql, placeList, idHolder);		
	}
	
	public Integer updateInfoService(String groovyName,String methodName, Object... paramArray) throws Exception{
		List placeList=new ArrayList();
		String sql=(String) GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray,placeList);
		return MicroServiceTemplateSupport.getInstance(dbName).updateInfoServiceBySql(sql, placeList);		
	}	

	public Integer delInfoService(String groovyName,String methodName, Object... paramArray) throws Exception{
		List placeList=new ArrayList();
		String sql=(String) GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray,placeList);
		return MicroServiceTemplateSupport.getInstance(dbName).updateInfoServiceBySql(sql, placeList);		
	}
	
	private IdHolder checkIdHolder(Object[] args){
		if(args==null){
			return null;
		}
		for(Object obj:args){
			if(obj instanceof IdHolder){
				return (IdHolder) obj;
			}
		}
		return null;
	}
	
}
