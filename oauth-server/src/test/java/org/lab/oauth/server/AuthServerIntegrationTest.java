package org.lab.oauth.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.oauth.server.AuthorizationServerApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthorizationServerApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthServerIntegrationTest {

	@Test
	public void whenLoadApplication_thenSuccess() {

	}
}