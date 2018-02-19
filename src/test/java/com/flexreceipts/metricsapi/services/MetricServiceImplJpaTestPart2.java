package com.flexreceipts.metricsapi.services;

import com.flexreceipts.metricsapi.api.v1.mapper.MetricMapper;
import com.flexreceipts.metricsapi.api.v1.model.MetricDTO;
import com.flexreceipts.metricsapi.bootstrap.Bootstrap;
import com.flexreceipts.metricsapi.domain.Metric;
import com.flexreceipts.metricsapi.exceptions.ResourceAlreadyExistsException;
import com.flexreceipts.metricsapi.exceptions.ResourceNotFoundException;
import com.flexreceipts.metricsapi.repositories.MetricRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertArrayEquals;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MetricServiceImplJpaTestPart2 {
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
    public void getMetricByName() {
        String name = "Stock_Prices";
        MetricDTO metricDTO = metricService.getMetricByName(name);
        assertNotNull(metricDTO);
        Metric metric = metricRepository.findByName(name).get();
        assertNotNull(metric);
        assertEquals(metric.getName(), metricDTO.getName());
        assertEquals(metric.getId(), metricDTO.getId());
        assertEquals(BASE_URL + "/" + metric.getId(), metricDTO.getMetricUrl());
        assertArrayEquals(metric.getValues().toArray(), metricDTO.getValues().toArray());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getMetricByNameNotFound() {
        metricService.getMetricByName("Test_Metrics");
    }

    @Test
    public void createNewMetric() {
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setName("Created_Metric");
        metricDTO.setValues(Arrays.asList(6.0, 2.0, 5.0, 3.0, 4.0));

        MetricDTO returnedMetricDTO = metricService.createNewMetric(metricDTO);
        Optional<Metric> createdMetricOptional = metricRepository.findByName(metricDTO.getName());
        MetricDTO createdMetricDTO = createdMetricOptional
                .map(MetricMapper.INSTANCE::metricToMetricDTO).get();
        assertNotNull(returnedMetricDTO);
        assertNotNull(createdMetricDTO);
        assertEquals(createdMetricDTO.getName(), returnedMetricDTO.getName());
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void createNewMetricConflict() {
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setName("Stock_Prices");
        metricDTO.setValues(Arrays.asList(6.0, 2.0, 5.0, 3.0, 4.0));
        metricService.createNewMetric(metricDTO);
    }

    @Test
    public void deleteMetricByName() {
        String name = "Stock_Prices";
        MetricDTO metricDTO = metricService.getMetricByName(name);
        MetricDTO deletedMetricDTO = metricService.deleteMetricByName(name);
        assertEquals(metricDTO.getName(), deletedMetricDTO.getName());
        assertEquals(metricDTO.getMetricUrl(), deletedMetricDTO.getMetricUrl());
        assertArrayEquals(metricDTO.getValues().toArray(), deletedMetricDTO.getValues().toArray());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteMetricByNameNotFound() {
        metricService.deleteMetricByName("Test_Metric");
    }

    @Test
    public void deleteMetricById() {
        MetricDTO metricDTO = metricService.getMetricById(3l);
        MetricDTO deletedMetricDTO = metricService.deleteMetricById(3l);
        assertEquals(metricDTO.getName(), deletedMetricDTO.getName());
        assertEquals(metricDTO.getMetricUrl(), deletedMetricDTO.getMetricUrl());
        assertArrayEquals(metricDTO.getValues().toArray(), deletedMetricDTO.getValues().toArray());
    }

    @Test
    public void patchMetricByName() {
        String name = "Stock_Prices";
        MetricDTO newMetricDTO = new MetricDTO();
        newMetricDTO.setName("Updated_Stock_Prices");

        MetricDTO updatedMetricDTO = metricService.patchMetricByName(name, newMetricDTO);
        assertEquals(newMetricDTO.getName(), updatedMetricDTO.getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void patchMetricByNameNotFound() {
        String name = "Test_Metric";
        MetricDTO newMetricDTO = new MetricDTO();
        newMetricDTO.setName("Updated_Stock_Prices");
        metricService.patchMetricByName(name, newMetricDTO);
    }


}
