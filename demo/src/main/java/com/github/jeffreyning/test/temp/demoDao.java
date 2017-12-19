package com.github.jeffreyning.test.temp;

import com.github.jeffreyning.test.dto.MicroTest;
import com.nh.micro.dao.mapper.MicroMapperTemplate;

public class demoDao extends MicroMapperTemplate<MicroTest> {
	public MicroTest getInfo(String id) throws Exception {
		return getInfoByIdMapper(id);
	}
}
