package com.aeris.demo;

import com.aeris.demo.controller.CdfController;
import com.aeris.demo.service.CdfService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private CdfController controller;

	@Autowired
	private CdfService service;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
		assertThat(service).isNotNull();
	}

	@Test
	void invalidFilename() {
		assertThatThrownBy(() -> {
			CdfService test = new CdfService("bad.file");
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Could not locate file");
	}

}
