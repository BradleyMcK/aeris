package com.aeris.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpRequestTest {

	@Test
	void contextLoads() {
	}

	@Value(value="${local.server.port}")
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testGetInfo() throws Exception {

		String url = "http://localhost:" + port + "/demo/get-info";
		assertThat(this.restTemplate.getForObject(url, String.class)).contains("NetcdfFile");
	}
}
