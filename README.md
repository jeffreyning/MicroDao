# MicroDao
dao java orm

MicroDaoΪ�˽��mybatis����ȱ�ݣ�����ȫ�·�װ��dao��ܣ����ܸ���mybatis���ұ�mybatis����
MicroDaoͬʱ֧��mysql��oracle

MicroDao���mybatis���ŵ㣺
1��sql�ű�֧���޸ĺ��Ȳ���ʵʱ��Ч��
2��bean�����ݿ��ֶ�ӳ���ϵ��ͨ��ע�����õ�bean�У�������sql�ű������֡�
3��sql�ű�֧������jsp��д�����Ҳ�������select��updateʹ�ò�ͬ��ǩ��������
4������Ҫʹ�ò��������֧�������ҳ��
5������Ҫʹ�ò��������֧�����bean�ı�׼��ɾ�Ĳ鹦�ܡ�
6������Ҫʹ�ò��������֧�ֶ�д���룬�ֿ�ֱ�
7�����mysql5.7֧�ֶ�̬�ֶΡ�

֧��mapper��template����orm����ģʽ֧��ҵ��ϵͳ
1��mapperָ��ͨ��ɨ��ӿڣ�����ʱ�Զ�����daoʵ����
2��templateָ��ͨ���̳�template��׼���࣬����dao���ࣻ
3����ormָ��ֱ��ʹ��microDaoʵ��������ִ�з�orm���������ݿ������

*mapperģʽ����˵��*

1,��дʵ��bean
ʹ��@MicroTableNameע��˵����Ӧ�ı�����
ʹ��@MicroMappingAnnoע��˵����Ӧ���ֶ�����

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

2,��дdao��ӿ�

ͨ���̳�MicroCommonMapper<T>ʵ�ֶ�ʵ��bean(�ڷ���������)�ı�׼��ɾ�Ĳ鹦��
�ӿڵķ�������updateInfoByNhs��ӳ��Ϊsql�ű��ж�������
�ӿڲ������������;�������


���ڽ��ӳ�䣺
��ѯ������¼�ǣ�����ӿ����õķ���ֵΪʵ��bean�����Զ�����ӳ�䡣
�ӿڷ���ֵ�����list��������@ListInnerClassע������list��ʵ���࣬����������list<Map>����

���ڷ�ҳ��
��ҳ��ҳ��ÿҳ�ļ�¼������ѯ���ܼ�¼������ͨ��DefaultPageInfo������������
ʹ��mapperģʽʱ�ӿڷ����д���DefaultPageInfo����ʱ�Զ����з�ҳ����

���ڲ���ʱ����id���أ�
ʹ��MicroCommonMapperͨ�ò��뷽��ʱ��ͨ��ʵ��bean��������id��
ʹ��sqlʱ��ͨ��IdHolder�����������id��ʹ��mapperģʽʱ�ӿڷ����д���IdHolder��������id����

���ڶ�д���룺
ͨ���ڽӿ�������@MicroDbNameע�⣬�����ײ��л��ĸ�����Դ���в����������ô˹���ʵ�ֶ�д���롣

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

3,��дsql�ű�
�ű��ļ�Ϊtxt��ʽ��������.nhs��Ϊ�ļ�����׺���磺TestRep.nhs��
���ö���<%! <sql id="xxxx"> %> xxxxxxxx <%! </sql> %>
����id����ӿڷ�������ͬ��
���䲻����select����update��ͳһ��sql��ʶ��
�߼��жϲ�������jsp��<% some script %>��ʾ
��mybatisһ������ֵʹ��#{} ${}���ַ�ʽ����
Ӧ��ʱ������paramArray��ʼ��paramArray��һ��������ӿڲ������ݶ�Ӧ��
set�ַ�ƴ��ʱ���׸����Ż��ڿ���ڲ��Զ�ɾ��������Ҫ�����⴦��

```
/* ��ѯ */
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

/* ���� */
<%! <sql id="insertInfoByNhs"> %>
insert into micro_test(id,meta_key) values( 
#{paramArray[0].id},#{paramArray[0].meta_key}
	)
<%! </sql> %>
 
 
/* ���� */ 
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

4����spring������ɨ��sql�ű��ͽӿ�
����NhsInitUtil��������sql�ű�,����rulePath�ǽű���Ŀ¼·����
����BeanScannerConfigurer��������mapper�ӿڣ�����scanPath�ǽӿڸ�Ŀ¼·��.·��Ϊ���ʱ�ö��ŷָ���
MicroDao�ڲ���Ҫʹ��jdbctemplate��
����microDbHolder��MicroDao�ڲ��������ò��ڶ�д����ʱ�л��������Դ��jdbctemplate���Ļ��ƣ���������һ��default����Դ��
����dbTypeMap����������������Դ���Ϳ���дmysql��oracle

��������
��ʹ��spring�������TransactionManager���й���

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

    <!-- ����platform transaction manager-->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- ����ʽ������������������advice-->
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

    <!-- �����������advice���÷�Χ����������-->
    <aop:config>
        <aop:pointcut id="serviceLayerTransaction" expression="execution( * com.github.jeffreyning.test.dao.*..*(..))"/>
        <aop:advisor pointcut-ref="serviceLayerTransaction" advice-ref="txAdvice"/>
    </aop:config>	

```

5��ʹ��mapper����daoʵ���������ݿ�

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

		/*����ʵ��bean ����*/
		MicroTest microTest=new MicroTest();
		microTest.setMetaKey("t1");
		microTest.setMetaName("test1");
		testRep.createInfoMapper(microTest);
		String id=microTest.getId();
		
		/*����ʵ��bean��example ��ҳ��ѯ*/
		DefaultPageInfo pageInfo=new DefaultPageInfo();
		List list=testRep.getInfoList4PageMapper(null, pageInfo);
		Long total=pageInfo.getTotal();
		System.out.println(list.toString());
		System.out.println("total="+total);
		
		/*����ʵ��bean��example ��ѯ*/
		List alllist=testRep.getInfoListAllMapper(null, "");
		System.out.println(alllist.toString());		
		
		/*����ʵ��bean��Id ��ѯ*/
		MicroTest tempBean=testRep.getInfoByIdMapper(id);
		System.out.println(tempBean.toString());
		
		/*����ʵ��bean ���¼�¼*/
		tempBean.setMetaName("test12");
		testRep.updateInfoMapper(tempBean);
		
		/*����ʵ��bean��id ɾ����¼*/
		testRep.delInfoByIdMapper(id);
		
		/*����sql ����*/
		Map paramMap=new HashMap();
		paramMap.put("meta_key", "test123");
		paramMap.put("meta_name", "123");
		IdHolder idHolder=new IdHolder();
		testRep.insertInfoByNhs(paramMap,idHolder);
		Object idObj=idHolder.getIdVal();
		System.out.println("id="+idObj.toString());
		
		/*����sql ����*/
		MicroTest bean1=new MicroTest();
		bean1.setId(idObj.toString());
		bean1.setMetaName("new name");
		testRep.updateInfoByNhs(bean1);
		
		/*����sql ���ϲ�ѯ*/
		String name="tom";
		List list2=testRep.queryInfoByUserName(name);
		System.out.println(list2.toString());
		
		/*����sql ���ϲ�ѯ*/
		Map pMap=new HashMap();
		pMap.put("user_age", 20);
		List list3=testRep.queryInfoByUserAge(pMap);
		System.out.println(list3.toString());	

	}

}
```



**templateģʽ����˵��**
ͨ���̳�MicroMapperTemplate<T>ʵ�����ʵ��bean����ɾ�Ĳ������sql����


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

**��ormģʽ����˵��**

ͨ���̳�MicroServiceTemplateSupportʵ����Ա����ɾ�Ĳ������sql������

���������Ϊmap����map��value��Ϊstring���ͣ�key��Ӧ�����ơ�
��������string���ո�ʽyyyy-MM-dd HH:mm:ss��д��

```
//����
public Integer createInfoService(Map requestParamMap,String tableName)
//��ҳ��ѯ
public Map getInfoList4PageService(Map requestParamMap,String tableName,Map pageMap)
//����ҳ��ѯ
public List getInfoListAllService(Map requestParamMap,String tableName,Map sortMap)
//����
public Integer updateInfoService(Map requestParamMap,String tableName)
//ɾ��
public Integer delInfoService(Map requestParamMap,String tableName)
```

