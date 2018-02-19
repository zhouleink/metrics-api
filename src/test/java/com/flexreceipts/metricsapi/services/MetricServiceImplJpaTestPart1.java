package com.flexreceipts.metricsapi.services;

import com.flexreceipts.metricsapi.api.v1.mapper.MetricMapper;
import com.flexreceipts.metricsapi.api.v1.model.MetricDTO;
import com.flexreceipts.metricsapi.bootstrap.Bootstrap;
import com.flexreceipts.metricsapi.domain.Metric;
import com.flexreceipts.metricsapi.exceptions.ResourceNotFoundException;
import com.flexreceipts.metricsapi.repositories.MetricRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertArrayEquals;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MetricServiceImplJpaTestPart1 {
    private static final String BASE_URL = "/api/v1/metrics";
    @Autowired
    MetricRepository metricRepository;

    MetricService metricService;

    @Before
    public void setUp() throws Exception {
        //setup data for testing
        Bootstrap bootstrap = new Bootstrap(metricRepository);
        bootstrap.run(); //load data

        metricService = new MetricServiceImpl(MetricMapper.INSTANCE, metricRepository);
    }

    @Test
    public void getAllMetrics() {
        assertEquals(3, metricService.getAllMetrics().size());
    }

    @Test
    public void getMetricById() {
        Long id = new Long(1);
        MetricDTO metricDTO = metricService.getMetricById(id);
        assertNotNull(metricDTO);
        Metric metric = metricRepository.findById(id).get();
        assertNotNull(metric);
        assertEquals(metric.getName(), metricDTO.getName());
        assertEquals(metric.getId(), metricDTO.getId());
        assertEquals(BASE_URL + "/" + metric.getId(), metricDTO.getMetricUrl());
        assertArrayEquals(metric.getValues().toArray(), metricDTO.getValues().toArray());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getMetricByIdNotFound() {
        metricService.getMetricById(10l);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteMetricByIdNotFound() {
        metricService.deleteMetricById(10l);
    }

    @Test
    public void updateOrSaveMetricById() {
        Long id = new Long(1);
        MetricDTO newMetricDTO = new MetricDTO();
        newMetricDTO.setName("Updated_Stock_Prices");
        newMetricDTO.setValues(Arrays.asList(16.0, 22.0, 50.0, 33.0, 24.0));

        MetricDTO updatedMetricDTO = metricService.updateOrSaveMetricById(id, newMetricDTO);
        assertEquals(newMetricDTO.getName(), updatedMetricDTO.getName());
        assertArrayEquals(newMetricDTO.getValues().toArray(), updatedMetricDTO.getValues().toArray());
    }

    @Test
    public void updateOrSaveMetricByName() {
        String name = "Stock_Prices";
        MetricDTO newMetricDTO = new MetricDTO();
        newMetricDTO.setName("Updated_Stock_Prices");
        newMetricDTO.setValues(Arrays.asList(16.0, 22.0, 50.0, 33.0, 24.0));

        MetricDTO updatedMetricDTO = metricService.updateOrSaveMetricByName(name, newMetricDTO);
        assertEquals(newMetricDTO.getName(), updatedMetricDTO.getName());
        assertArrayEquals(newMetricDTO.getValues().toArray(), updatedMetricDTO.getValues().toArray());
    }



}
