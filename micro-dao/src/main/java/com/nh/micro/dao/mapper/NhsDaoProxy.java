package com.nh.micro.dao.mapper;



import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import com.nh.micro.dao.mapper.MicroMapperTemplate;
import com.nh.micro.dao.mapper.MicroCommonMapper;
import com.nh.micro.dao.mapper.MicroMapperUtil;
import com.nh.micro.dao.mapper.UseMicroDao;
import com.nh.micro.orm.MicroDbName;


/**
 * 
 * @author ninghao
 *
 */
public class NhsDaoProxy implements InvocationHandler {
	public String groovyName=null;
	
	public String getGroovyName() {
		return groovyName;
	}
	public void setGroovyName(String groovyName) {
		this.groovyName = groovyName;
	}	
	
	private MicroMapperTemplate microMapperTemplate=null;
	private Class mapperInterface=null;
	public MicroMapperTemplate getMicroMapperTemplate() {
		return microMapperTemplate;
	}
	public void setMicroMapperTemplate(MicroMapperTemplate microMapperTemplate) {
		this.microMapperTemplate = microMapperTemplate;
	}
	public Class getMapperInterface() {
		return mapperInterface;
	}
	public void setMapperInterface(Class mapperInterface) {
		this.mapperInterface = mapperInterface;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		  if(this.microMapperTemplate==null){
			  MicroDbName dbNameAnno=(MicroDbName) mapperInterface.getAnnotation(MicroDbName.class);
			  String dbName="default";
			  if(dbNameAnno!=null){
				  dbName=dbNameAnno.name();
			  }			  
			  MicroMapperTemplate microMapperTemplate=new MicroMapperTemplate(dbName);
			  Class entityClass=MicroMapperUtil.getEntityClass(MicroCommonMapper.class, mapperInterface);
			  microMapperTemplate.setDefaultClass(entityClass);
			  this.microMapperTemplate=microMapperTemplate;
		  }		
		  
		  UseMicroDao useMicro=method.getAnnotation(UseMicroDao.class);
		  if(useMicro!=null){

			  return MicroMapperUtil.executeMethod(MicroCommonMapper.class, mapperInterface, method, this.microMapperTemplate, args);
		  }

		  String targetGroovyName=mapperInterface.getSimpleName();
		  if(groovyName!=null && !"".equals(groovyName)){
			  targetGroovyName=groovyName;
		  }		  
		  String methodName=method.getName();

		  //add 201806 ninghao
		  if(methodName.equals("toString")){
			  String temp="NhsDaoProxy to "+ groovyName+"."+methodName;
			  return temp;
		  }
		  
		 // String sql=(String) GroovyExecUtil.execGroovyRetObj(groovyName, methodName, args, placeList);	
		  Class oc=method.getReturnType();
		  boolean listFlag=false;
		  if(oc.isAssignableFrom(List.class)){
			  listFlag=true;
		  }
		  Class outClass=microMapperTemplate.getDefaultClass();
		  
		  if(listFlag==false){
			  //add 201806 ninghao
			  outClass=oc;
			  
		  }else{
			  ListInnerClass innerClassAnno=method.getAnnotation(ListInnerClass.class);
			  if(innerClassAnno!=null){
				  outClass=innerClassAnno.name();
			  }
		  }		  
		  
		  return microMapperTemplate.execServiceByGroovy(targetGroovyName, methodName, outClass, listFlag, args);

	}

}
