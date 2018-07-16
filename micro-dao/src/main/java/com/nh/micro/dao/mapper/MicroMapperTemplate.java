package com.nh.micro.dao.mapper;



import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.nh.micro.orm.MicroBeanMapUtil;
import com.nh.micro.orm.MicroTableName;

import com.nh.micro.rule.engine.core.GroovyExecUtil;
import com.nh.micro.template.IdHolder;
import com.nh.micro.template.MicroServiceTemplateSupport;

/**
 * 
 * @author ninghao
 * 
 */
public class MicroMapperTemplate<T> extends MicroServiceTemplateSupport {
	private static final String MICRO_DB_NULL="MICRO_DB_NULL";
	public Class defaultClass;
	public Class getDefaultClass() {
		return defaultClass;
	}

	public void setDefaultClass(Class defaultClass) {
		this.defaultClass = defaultClass;
	}

	public MicroMapperTemplate() {

	}

	public MicroMapperTemplate(String dbName) {
		super(dbName);
	}

	//add 201806 ninghao
	public MicroMapperTemplate getMicroMapperTemplate(){
		return this;
	}
	
	public List getInfoListAllMapper(Object example, String orderStr) throws Exception {

		return getInfoListAllMapper4Class(example, orderStr, defaultClass, defaultClass);
	}
	
	public List getInfoListAllMapper4Class(Object example, String orderStr, Class inClass, Class outClass) throws Exception {

		MicroTableName microTableName = (MicroTableName) inClass.getAnnotation(MicroTableName.class);
		if (microTableName == null) {
			throw new RuntimeException("table name is null");
		}
		String tableName = microTableName.name();

		Map paramMap = new HashMap();
		if(example!=null){
			paramMap=MicroBeanMapUtil.beanToMap(example);
		}
		Map sortMap=new HashMap();
		if(orderStr!=null && !"".equals(orderStr)){
			sortMap.put("cusSort", orderStr);
		}
		List<Map> tempList = getInfoListAllService(paramMap, tableName, sortMap);
		List retList=new ArrayList();
		for(Map rowMap:tempList){
			retList.add(MicroBeanMapUtil.mapToBean(rowMap, outClass));
		}
		return retList;
	}
	
	public List getInfoList4PageMapper(Object example, MicroPageInfo pageInfo) throws Exception {
		return getInfoList4PageMapper4Class(example, pageInfo, defaultClass, defaultClass);
	}
	
	public List getInfoList4PageMapper4Class(Object example, MicroPageInfo pageInfo, Class inClass, Class outClass) throws Exception {

		MicroTableName microTableName = (MicroTableName) inClass.getAnnotation(MicroTableName.class);
		if (microTableName == null) {
			throw new RuntimeException("table name is null");
		}
		String tableName = microTableName.name();
		Map paramMap = new HashMap();
		if(example!=null){
			paramMap=MicroBeanMapUtil.beanToMap(example);
		}
		Map sortMap=new HashMap();
		String orderStr=pageInfo.getOrderStr();
		if(orderStr!=null && !"".equals(orderStr)){
			sortMap.put("cusSort", orderStr);
		}
		if(pageInfo!=null){
			sortMap.put("page", pageInfo.getPageNo().toString());
			sortMap.put("rows", pageInfo.getPageRows().toString());
		}
		Map tempMap = getInfoList4PageService(paramMap, tableName, sortMap);
		String total=String.valueOf(tempMap.get("total"));
		if(pageInfo!=null){
			pageInfo.setTotal(Long.valueOf(total));
		}
		List<Map> tempList=(List) tempMap.get("rows");
		List retList=new ArrayList();
		for(Map rowMap:tempList){
			retList.add(MicroBeanMapUtil.mapToBean(rowMap, outClass));
		}
		return retList;
	}	

	public Integer createInfoMapper(Object example) throws Exception{
		return createInfoMapper4Class(example,null,null,defaultClass);
	}
	
	public Integer createInfoMapper4Cus(Object example,String cusCol,String cusValue) throws Exception{
		return createInfoMapper4Class(example,cusCol,cusValue,defaultClass);
	}
	
	public Integer createInfoMapper4Class(Object example,String cusCol,String cusValue, Class inClass) throws Exception{
		MicroTableName microTableName = (MicroTableName) inClass.getAnnotation(MicroTableName.class);
		if (microTableName == null) {
			throw new RuntimeException("table name is null");
		}
		String tableName = microTableName.name();
		Map paramMap = new HashMap();
		if(example!=null){
			paramMap=MicroBeanMapUtil.beanToMap(example);
		}
		String tempKeyId=calcuIdKey();
		boolean setIdFlag=false;
		if(paramMap.get(tempKeyId)==null){
			setIdFlag=true;
		}

		Integer status = createInfoService(paramMap, tableName,cusCol,cusValue);
		if(setIdFlag==true && example!=null){
			Object id=paramMap.get(tempKeyId);
			//add 201806 ning
			Map mappingInfo = MicroBeanMapUtil.getKeyMap(inClass);
			String fieldName=(String) mappingInfo.get(tempKeyId);
			MicroBeanMapUtil.setBeanProperty(example,fieldName,id.toString());
		}
		return status;		
	}	
	
	//add 201807 ning
	public List getNullFlagList(Object example){
		Class cls=example.getClass();
		Field[] fields=cls.getDeclaredFields();
		if(fields==null){
			return null;
		}
		

		for (int i = 0; i < fields.length; i++) {
			if(fields[i].getName().equals("microNullFlagList"))
			{
				try {
					List retList=(List) fields[i].get(example);
					return retList;
				} catch (Exception e) {
					return null;
				}

			}
		}
		return null;
	}
	
	//add 201807 ning
	public static void flagNull2Map(Map paramMap, List<String> flagList){
		if(flagList==null){
			return ;
		}
		for(String key:flagList){
			if(paramMap.containsKey(key)){
				paramMap.put(key, MICRO_DB_NULL);
			}
		}
	}	
	
	public Integer updateInfoMapper(Object example) throws Exception{
		return updateInfoMapper4Class(example, null, null, defaultClass);
	}	
	public Integer updateInfoMapper4Cus(Object example,String cusCondition,String cusSetStr) throws Exception{
		return updateInfoMapper4Class(example, cusCondition, cusSetStr, defaultClass);
	}
	public Integer updateInfoMapper4Class(Object example,String cusCondition,String cusSetStr,Class inClass) throws Exception{
		MicroTableName microTableName = (MicroTableName) inClass.getAnnotation(MicroTableName.class);
		if (microTableName == null) {
			throw new RuntimeException("table name is null");
		}
		String tableName = microTableName.name();
		Map paramMap = new HashMap();
		if(example!=null){
			paramMap=MicroBeanMapUtil.beanToMap(example);
		}
		
		//add 201807 ning
		List flagList=getNullFlagList(example);
		flagNull2Map(paramMap,flagList);

		Integer status = updateInfoService(paramMap, tableName,cusCondition,cusSetStr);

		return status;			
	}
	
	public Integer delInfoMapper(Object example) throws Exception{
		return delInfoMapper4Class(example,defaultClass);
	}
	
	public Integer delInfoMapper4Class(Object example, Class inClass) throws Exception{
		MicroTableName microTableName = (MicroTableName) inClass.getAnnotation(MicroTableName.class);
		if (microTableName == null) {
			throw new RuntimeException("table name is null");
		}
		String tableName = microTableName.name();
		Map paramMap = new HashMap();
		if(example!=null){
			paramMap=MicroBeanMapUtil.beanToMap(example);
		}


		Integer status = delInfoService(paramMap, tableName);

		return status;			
	}
	public Integer delInfoByIdMapper(String id){
		return delInfoByIdMapper4Class(id,defaultClass);
	}
	public Integer delInfoByIdMapper4Class(String id, Class inClass){
		MicroTableName microTableName = (MicroTableName) inClass.getAnnotation(MicroTableName.class);
		if (microTableName == null) {
			throw new RuntimeException("table name is null");
		}
		String tableName = microTableName.name();
		Map paramMap = new HashMap();

		Integer status = delInfoByIdService(id, tableName);

		return status;			
	}
	
	public Integer delInfoByBizIdMapper(String bizId,String bizCol){
		return delInfoByBizIdMapper4Class(bizId, bizCol, defaultClass);
	}
	
	public Integer delInfoByBizIdMapper4Class(String bizId,String bizCol, Class inClass){
		MicroTableName microTableName = (MicroTableName) inClass.getAnnotation(MicroTableName.class);
		if (microTableName == null) {
			throw new RuntimeException("table name is null");
		}
		String tableName = microTableName.name();
		Map paramMap = new HashMap();

		Integer status = delInfoByBizIdService(bizId, tableName, bizCol);

		return status;			
	}
	
	public T getInfoByIdMapper(String id) throws Exception{
		return getInfoByIdMapper4Class(id,defaultClass,defaultClass);
	}
	public T getInfoByIdMapper4Class(String id,Class inClass, Class outClass) throws Exception{
		MicroTableName microTableName = (MicroTableName) inClass.getAnnotation(MicroTableName.class);
		if (microTableName == null) {
			throw new RuntimeException("table name is null");
		}
		String tableName = microTableName.name();


		Map tempMap = getInfoByIdService(id, tableName);
		T retObj=(T) MicroBeanMapUtil.mapToBean(tempMap, outClass);

		return retObj;		
	}
	
	public T getInfoByBizIdMapper(String bizId,String bizCol) throws Exception{
		return getInfoByBizIdMapper4Class(bizId,bizCol,defaultClass,defaultClass);
	}
	public T getInfoByBizIdMapper4Class(String bizId,String bizCol,Class inClass, Class outClass) throws Exception{
		MicroTableName microTableName = (MicroTableName) inClass.getAnnotation(MicroTableName.class);
		if (microTableName == null) {
			throw new RuntimeException("table name is null");
		}
		String tableName = microTableName.name();


		Map tempMap = getInfoByBizIdService(bizId, tableName, bizCol);
		T retObj=(T) MicroBeanMapUtil.mapToBean(tempMap, outClass);

		return retObj;			
	}
	
	public Integer updateInfoServiceByOrm(String groovyName, String methodName, Object... paramArray) throws Exception{
		List placeList=new ArrayList();

		String sql=(String) GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray, placeList);
		return updateInfoServiceBySql(sql,placeList);
	}
	
	public List getInfoList4PageServiceByOrm(String countSql, List countPlaceList, String sql, List placeList, MicroPageInfo pageInfo, Class outClass) throws Exception{

		Map sortMap=new HashMap();
		String orderStr=pageInfo.getOrderStr();
		if(orderStr!=null && !"".equals(orderStr)){
			sortMap.put("cusSort", orderStr);
		}
		if(pageInfo!=null){
			sortMap.put("page", pageInfo.getPageNo().toString());
			sortMap.put("rows", pageInfo.getPageRows().toString());
		}


		
		Map tempMap=getInfoList4PageServiceBySql(countSql, countPlaceList, sql, placeList, sortMap);		
		String total=String.valueOf(tempMap.get("total"));
		if(pageInfo!=null){
			pageInfo.setTotal(Long.valueOf(total));
		}
		List<Map> tempList=(List) tempMap.get("rows");
		List retList=new ArrayList();
		if(outClass!=null){
			for(Map rowMap:tempList){
				retList.add(MicroBeanMapUtil.mapToBean(rowMap, outClass));
			}	
		}else{
			retList=tempList;
		}
		return retList;
	}	
	
	public List getInfoListAllServiceByOrm(String sql, List placeList, Class outClass) throws Exception{
		//List placeList=new ArrayList();

		//String sql=(String) GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray,placeList);
		List<Map> tempList=getInfoListAllServiceBySql(sql, placeList);
		List retList=new ArrayList();
		if(outClass!=null){
			for(Map rowMap:tempList){
				retList.add(MicroBeanMapUtil.mapToBean(rowMap, outClass));
			}	
		}else{
			retList=tempList;
		}	
		return retList;
	}	
	
	public Object getSingleInfoServiceByOrm(String sql, List placeList, Class outClass) throws Exception{
		Map retMap=getSingleInfoService(sql, placeList);
		return MicroBeanMapUtil.mapToBean(retMap, outClass);
	}
	
	public Integer createInfoServiceByOrm(String sql, List placeList, IdHolder idHolder) throws Exception{
		return createInfoServiceBySql(sql, placeList, idHolder);
	}
	
	public Object execServiceByGroovy(String groovyName, String methodName, Class outClass, boolean listFlag, Object... paramArray) throws Exception{
		List placeList=new ArrayList();

		String sql=(String) GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray,placeList);
		String type=checkSqlType(sql);
		if(type.equals("select")){
			MicroPageInfo pageInfo=checkPage(paramArray);
			if(pageInfo==null){
				if(listFlag==true){
					return getInfoListAllServiceByOrm(sql, placeList, outClass);
				}else{
					return getSingleInfoServiceByOrm(sql, placeList, outClass);
				}
			}else{
				String countSql="select count(1) from ( "+sql+" )";
				String dbType=calcuDbType();
				if(dbType!=null && !dbType.equals("oracle")){
					countSql=countSql+"  as micro_total_alias";
				}

				return getInfoList4PageServiceByOrm(countSql, placeList, sql, placeList, pageInfo, outClass);
			}
		}else if(type.equals("insert")){
			IdHolder idHolder=checkIdHolder(paramArray);
			return createInfoServiceBySql(sql, placeList, idHolder);
		}else if(type.equals("update") || type.equals("delete")){
			return updateInfoServiceBySql(sql, placeList);
		}
		return null;
	}		
	
	private String checkSqlType(String sql){
		int indexSel=sql.toLowerCase().indexOf("select");
		int indexUpdate=sql.toLowerCase().indexOf("update");
		int indexDel=sql.toLowerCase().indexOf("delete");
		int indexInsert=sql.toLowerCase().indexOf("insert");
		int[] indexArray={indexSel,indexUpdate,indexDel,indexInsert};
		Arrays.sort(indexArray);
		int indexMix=-1;
		for(int index:indexArray){
			if(index>=0){
				indexMix=index;
				break;
			}
		}
		String type="ddl";
		if(indexSel>=0 && indexSel==indexMix){
			type="select";
		}else if(indexUpdate>=0 && indexUpdate==indexMix){
			type="update";
		}else if(indexDel>=0 && indexDel==indexMix){
			type="delete";
		}else if(indexInsert>=0 && indexInsert==indexMix){
			type="insert";
		}
		return type;
	}
	
	private MicroPageInfo checkPage(Object[] args){
		if(args==null){
			return null;
		}
		for(Object obj:args){
			if(obj instanceof MicroPageInfo){
				return (MicroPageInfo) obj;
			}
		}
		return null;
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
