package com.aeris.demo;

import com.aeris.demo.controller.CdfController;
import com.aeris.demo.model.ConcentrationGrid;
import com.aeris.demo.model.ConcentrationGrid.ConcentrationRow;
import com.aeris.demo.service.CdfService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AerisApplicationTests {

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
			assertThat(test).isNotNull();
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Could not locate file");
	}

	@Test
	void testConcentrationGrid() {
		var rows = new ConcentrationRow[1];
		rows[0] = new ConcentrationRow(2.0, 2);
		var grid = new ConcentrationGrid(2.0, 1.2, rows);
		assertThat(grid).isNotNull();
		assertThat(grid.rows()[0].getX()).isNotNull();
		assertThat(grid.rows()[0].getY()).isEqualTo(2.0);
		assertThat(grid.rows()[0].getConcentrations()).isNotNull();
	}
}
