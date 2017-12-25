# MicroDao
dao java orm

MicroDao为了解决mybatis固有缺陷，进行全新封装的dao框架，功能覆盖mybatis，且比mybatis更灵活。
MicroDao同时支持mysql和oracle

MicroDao相对mybatis的优点：
1，sql脚本支持修改后热部署实时生效。
2，bean与数据库字段映射关系，通过注解设置到bean中，不必在sql脚本中体现。
3，sql脚本支持类似jsp的写法，且不必区分select、update使用不同标签，更加灵活。
4，不需要使用插件，内置支持物理分页。
5，不需要使用插件，内置支持针对bean的标准增删改查功能。
6，不需要使用插件，内置支持读写分离，分库分表。
7，针对mysql5.7支持动态字段。

支持mapper、template、非orm三种模式支撑业务系统
1，mapper指，通过扫描接口，运行时自动生成dao实例；
2，template指，通过继承template标准父类，生成dao子类；
3，非orm指，直接使用microDao实例，可以执行非orm更灵活的数据库操作。

*mapper模式技术说明*

1,编写实体bean
使用@MicroTableName注解说明对应的表名称
使用@MicroMappingAnno注解说明对应的字段名称

```
package com.github.jeffreyning.test.dto;
import com.nh.micro.orm.MicroMappingAnno;
import com.nh.micro.orm.MicroTableName;

@MicroTableName(name="micro_test")
public class MicroTest {
	@MicroMappingAnno(name="id")
	private String id;
	
	@MicroMappingAnno(name="meta_key")
	private String metaKey;
	
	@MicroMappingAnno(name="meta_name")
	private String metaName;
	
	@MicroMappingAnno(name="meta_type")
	private String metaType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMetaKey() {
		return metaKey;
	}

	public void setMetaKey(String metaKey) {
		this.metaKey = metaKey;
	}

	public String getMetaName() {
		return metaName;
	}

	public void setMetaName(String metaName) {
		this.metaName = metaName;
	}

	public String getMetaType() {
		return metaType;
	}

	public void setMetaType(String metaType) {
		this.metaType = metaType;
	}
}
```

2,编写dao层接口

通过继承MicroCommonMapper<T>实现对实体bean(在泛型中设置)的标准增删改查功能
接口的方法名如updateInfoByNhs会映射为sql脚本中段落名称
接口参数个数和类型均无限制


关于结果映射：
查询单条记录是，如果接口设置的返回值为实体bean则，则自动进行映射。
接口返回值如果是list，可以用@ListInnerClass注解设置list中实体类，不设置则按照list<Map>返回

关于分页：
分页的页数每页的记录数，查询出总记录数，均通过DefaultPageInfo对象带入带出。
使用mapper模式时接口方法中带有DefaultPageInfo对象时自动进行分页处理。

关于插入时自增id返回：
使用MicroCommonMapper通用插入方法时，通过实体bean带回自增id。
使用sql时，通过IdHolder对象带出自增id。使用mapper模式时接口方法中传入IdHolder按照自增id处理。

关于读写分离：
通过在接口中设置@MicroDbName注解，决定底层切换哪个数据源进行操作。可以用此功能实现读写分离。

```
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
```

3,编写sql脚本
脚本文件为txt格式但必须以.nhs作为文件名后缀（如：TestRep.nhs）
设置段落<%! <sql id="xxxx"> %> xxxxxxxx <%! </sql> %>
段落id需与接口方法名相同。
段落不区分select还是update，统一用sql标识。
逻辑判断部分类似jsp用<% some script %>表示
与mybatis一样参数值使用#{} ${}两种方式引用
应用时必须以paramArray开始，paramArray是一个数组与接口参数数据对应。
set字符拼接时，首个逗号会在框架内部自动删除，不需要做额外处理。

```
/* 查询 */
<%! <sql id="queryInfoByUserName"> %>
select * from micro_test t left join micro_user u on  t.user_id=u.user_id where 1=1 
<% if(paramArray[0]!=null){ %>		
	and u.user_name like '%${paramArray[0]}%' 			
<% 	} %>
<%! </sql> %>

<%! <sql id="queryInfoByUserAge"> %>
select * from micro_test t left join micro_user u on  t.user_id=u.user_id where 1=1 
<% if(paramArray[0].get("user_age")!=null){ %>		
	and u.user_age >= #{paramArray[0].user_age} 			
<% 	} %>
<%! </sql> %>

/* 插入 */
<%! <sql id="insertInfoByNhs"> %>
insert into micro_test(id,meta_key) values( 
#{paramArray[0].id},#{paramArray[0].meta_key}
	)
<%! </sql> %>
 
 
/* 更新 */ 
<%! <sql id="updateInfoByNhs"> %>
update micro_test set  
			
<% if(paramArray[0].metaKey!=null){%>
,meta_key=#{paramArray[0].metaKey}	
<% } %>	
<% if(paramArray[0].metaName!=null){%>
,meta_name=#{paramArray[0].metaName}	
<% } %>	

where id=#{paramArray[0].getId()}
<%! </sql> %>
```

4，在spring中配置扫描sql脚本和接口
配置NhsInitUtil用来加载sql脚本,其中rulePath是脚本根目录路径。
配置BeanScannerConfigurer用来加载mapper接口，其中scanPath是接口根目录路径.路径为多个时用逗号分隔。
MicroDao内部需要使用jdbctemplate。
配置microDbHolder是MicroDao内部用来设置并在读写分离时切换多个数据源（jdbctemplate）的机制，至少配置一个default数据源。
其中dbTypeMap属性用来配置数据源类型可填写mysql或oracle

关于事务：
仍使用spring事务机制TransactionManager进行管理

```
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" destroy-method="close">
        <property name="url" value="jdbc:mysql://localhost:3306/test" />
        <property name="username" value="root" />
        <property name="password" value="root" />
    </bean>

    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name = "dataSource" ref="dataSource"/>
    </bean>
    
    <bean id="microDbHolder" class="com.nh.micro.db.MicroDbHolder" lazy-init="false">
        <property name="dbHolder">
            <map>
                <entry>
                    <key>
                        <value>default</value>
                    </key>
                    <ref bean="jdbcTemplate"></ref></entry>
            </map>
        </property>
        <property name="dbTypeMap">
            <map>
                <entry>
                    <key>
                        <value>default</value>
                    </key>
                    <value>mysql</value>
                </entry>

            </map>
        </property>  
    </bean> 

      
	<bean class="com.nh.micro.nhs.NhsInitUtil" init-method="initGroovyAndThread" lazy-init="false">
		<property name="fileList">
			<list>
				<bean class="com.nh.micro.rule.engine.core.GFileBean">
				<property name="ruleStamp" value="true"></property>
				<property name="jarFileFlag" value="true"></property>
				<property name="dirFlag" value="true"></property>
				<property name="rulePath" value="/groovy/"></property>
				</bean>
			</list>
		</property>
	</bean>	
	<bean class="com.nh.micro.dao.mapper.scan.BeanScannerConfigurer">
		<property name="scanPath" value="com.github.jeffreyning.test.dao"></property>
	</bean>  

    <!-- 配置platform transaction manager-->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- 声明式事物管理，配置事物管理advice-->
    <tx:advice id="txAdvice" transaction-manager="txManager">
        <tx:attributes>
            <!-- method starts with 'get' are read-only-->
            <tx:method name="get*" read-only="true"/>
            <!-- method starts with 'insert/update' use required propagation -->
            <tx:method name="add*" propagation="REQUIRED"/>
            <tx:method name="insert*" propagation="REQUIRED"/>
            <tx:method name="update*" propagation="REQUIRED"/>
            <!-- other method use default transaction setting-->
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice>

    <!-- 配置事物管理advice作用范围与作用条件-->
    <aop:config>
        <aop:pointcut id="serviceLayerTransaction" expression="execution( * com.github.jeffreyning.test.dao.*..*(..))"/>
        <aop:advisor pointcut-ref="serviceLayerTransaction" advice-ref="txAdvice"/>
    </aop:config>	

```

5，使用mapper出的dao实例操作数据库

```
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
```



**template模式技术说明**
通过继承MicroMapperTemplate<T>实现针对实体bean的增删改查操作和sql操作


```
package com.github.jeffreyning.test.temp;

import com.github.jeffreyning.test.dto.MicroTest;
import com.nh.micro.dao.mapper.MicroMapperTemplate;

public class demoDao extends MicroMapperTemplate<MicroTest> {
	public MicroTest getInfo(String id) throws Exception {
		return getInfoByIdMapper(id);
	}
}
```

**非orm模式技术说明**

通过继承MicroServiceTemplateSupport实现针对表的增删改查操作和sql操作。

输入输出均为map，且map的value均为string类型，key对应列名称。
日期类型string按照格式yyyy-MM-dd HH:mm:ss填写。

```
//插入
public Integer createInfoService(Map requestParamMap,String tableName)
//分页查询
public Map getInfoList4PageService(Map requestParamMap,String tableName,Map pageMap)
//不分页查询
public List getInfoListAllService(Map requestParamMap,String tableName,Map sortMap)
//更新
public Integer updateInfoService(Map requestParamMap,String tableName)
//删除
public Integer delInfoService(Map requestParamMap,String tableName)
```

