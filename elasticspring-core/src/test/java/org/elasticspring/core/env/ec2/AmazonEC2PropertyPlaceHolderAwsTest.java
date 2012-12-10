/*
 *
 *  * Copyright 2010-2012 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.elasticspring.core.env.ec2;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Properties;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class AmazonEC2PropertyPlaceHolderAwsTest {

	@Test
	@IfProfileValue(name = "test-groups", value = "aws-test")
	public void testGetUserProperties() throws Exception {


		PropertiesFactoryBean factoryBean = new PropertiesFactoryBean();
		factoryBean.setLocation(new ClassPathResource("access.properties"));
		factoryBean.afterPropertiesSet();
		Properties properties = factoryBean.getObject();

		String accessKey = properties.getProperty("accessKey");
		String secretKey = properties.getProperty("secretKey");


		InstanceIdProvider instanceIdProvider = Mockito.mock(InstanceIdProvider.class);
		Mockito.when(instanceIdProvider.getCurrentInstanceId()).thenReturn("i-4a1bfc25");

		AmazonEC2UserDataPropertySource amazonEC2PropertySource = new AmazonEC2UserDataPropertySource("test", new AmazonEC2Client(new BasicAWSCredentials(accessKey, secretKey)));
		amazonEC2PropertySource.setInstanceIdProvider(instanceIdProvider);
		amazonEC2PropertySource.getProperty("test");
	}
}