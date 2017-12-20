package com.github.jeffreyning.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.jeffreyning.test.dao.TestRep;
import com.github.jeffreyning.test.dto.MicroTest;
import com.nh.micro.dao.mapper.DefaultPageInfo;
import com.nh.micro.dao.mapper.MicroPageInfo;
import com.nh.micro.template.IdHolder;

public class TestMicroDao {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		TestRep testRep=(TestRep) ac.getBean("testRep");
		
		/*根据实体bean 创建*/
		MicroTest microTest=new MicroTest();
		microTest.setMetaKey("t1");
		microTest.setMetaName("test1");
		testRep.createInfoMapper(microTest);
		String id=microTest.getId();
		
		/*根据实体bean的example 分页查询*/
		DefaultPageInfo pageInfo=new DefaultPageInfo();
		List list=testRep.getInfoList4PageMapper(null, pageInfo);
		Long total=pageInfo.getTotal();
		System.out.println(list.toString());
		System.out.println("total="+total);
		
		/*根据实体bean的example 查询*/
		List alllist=testRep.getInfoListAllMapper(null, "");
		System.out.println(alllist.toString());		
		
		/*根据实体bean的Id 查询*/
		MicroTest tempBean=testRep.getInfoByIdMapper(id);
		System.out.println(tempBean.toString());
		
		/*根据实体bean 更新记录*/
		tempBean.setMetaName("test12");
		testRep.updateInfoMapper(tempBean);
		
		/*根据实体bean的id 删除记录*/
		testRep.delInfoByIdMapper(id);
		
		/*根据sql 插入*/
		Map paramMap=new HashMap();
		paramMap.put("meta_key", "test123");
		paramMap.put("meta_name", "123");
		IdHolder idHolder=new IdHolder();
		testRep.insertInfoByNhs(paramMap,idHolder);
		Object idObj=idHolder.getIdVal();
		System.out.println("id="+idObj.toString());
		
		/*根据sql 更新*/
		MicroTest bean1=new MicroTest();
		bean1.setId(idObj.toString());
		bean1.setMetaName("new name");
		testRep.updateInfoByNhs(bean1);
		
		/*根据sql 联合查询*/
		String name="tom";
		List list2=testRep.queryInfoByUserName(name);
		System.out.println(list2.toString());
		
		/*根据sql 联合查询*/
		Map pMap=new HashMap();
		pMap.put("user_age", 20);
		List list3=testRep.queryInfoByUserAge(pMap);
		System.out.println(list3.toString());	

	}

}
