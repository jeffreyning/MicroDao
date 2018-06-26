package com.nh.micro.dao.mapper;



import java.util.List;

import com.nh.micro.dao.mapper.MicroPageInfo;

public interface MicroCommonMapper<T> {
	

@UseMicroDao
public  List getInfoListAllMapper(T example,String orderStr) throws Exception;

@UseMicroDao
public  List getInfoList4PageMapper(T example,MicroPageInfo pageInfo) throws Exception;

@UseMicroDao
public T getInfoByIdMapper(String id) throws Exception;

@UseMicroDao
public T getInfoByBizIdMapper(String id, String bizCol) throws Exception;

@UseMicroDao
public Integer createInfoMapper(T example) throws Exception;

@UseMicroDao
public Integer createInfoMapper4Cus(T example,String cusCol,String cusValue) throws Exception;

@UseMicroDao
public Integer updateInfoMapper(Object example) throws Exception;

@UseMicroDao
public Integer updateInfoMapper4Cus(Object example,String cusCondition,String cusSetStr) throws Exception;

@UseMicroDao
public Integer delInfoMapper(Object example) throws Exception;

@UseMicroDao
public Integer delInfoByIdMapper(String id)  throws Exception;

@UseMicroDao
public Integer delInfoByBizIdMapper(String bizId,String bizCol)  throws Exception;

@UseMicroDao
public MicroMapperTemplate getMicroMapperTemplate();

}
