package com.github.jeffreyning.test.dao;

import java.util.List;
import java.util.Map;

import com.github.jeffreyning.test.dto.MicroTest;
import com.nh.micro.dao.mapper.ListInnerClass;
import com.nh.micro.dao.mapper.MicroCommonMapper;
import com.nh.micro.orm.MicroDbName;
import com.nh.micro.template.IdHolder;

@MicroDbName(name="default")
public interface TestRep extends MicroCommonMapper<MicroTest>  {
	
	public int updateInfoByNhs(MicroTest microTest);
	
	public int insertInfoByNhs(Map paramMap, IdHolder idHolder);
	
	@ListInnerClass(name=MicroTest.class)
	public List queryInfoByUserName(String name);	

	public List queryInfoByUserAge(Map paramMap);
}
