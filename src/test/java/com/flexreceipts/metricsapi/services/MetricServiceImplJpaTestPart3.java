package com.flexreceipts.metricsapi.services;

import com.flexreceipts.metricsapi.api.v1.mapper.MetricMapper;
import com.flexreceipts.metricsapi.api.v1.model.MetricDTO;
import com.flexreceipts.metricsapi.bootstrap.Bootstrap;
import com.flexreceipts.metricsapi.repositories.MetricRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MetricServiceImplJpaTestPart3 {
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
    public void getMean() {
        String name = "Stock_Prices";
        MetricDTO metricDTO = metricService.getMetricByName(name);
        Double meanById = metricService.getMeanByMetricId(metricDTO.getId());
        Double meanByName = metricService.getMeanByMetricName(name);
        assertEquals(meanById, meanByName);
    }

    @Test
    public void getMedian() {
        String name = "Stock_Prices";
        MetricDTO metricDTO = metricService.getMetricByName(name);
        Double medianById = metricService.getMedianByMetricId(metricDTO.getId());
        Double medianByName = metricService.getMedianByMetricName(name);
        assertEquals(medianById, medianByName);
    }

    @Test
    public void getMin() {
        String name = "Stock_Prices";
        MetricDTO metricDTO = metricService.getMetricByName(name);
        Double minById = metricService.getMinByMetricId(metricDTO.getId());
        Double minByName = metricService.getMinByMetricName(name);
        assertEquals(minById, minByName);
    }

    @Test
    public void getMax() {
        String name = "Stock_Prices";
        MetricDTO metricDTO = metricService.getMetricByName(name);
        Double maxById = metricService.getMaxByMetricId(metricDTO.getId());
        Double maxByName = metricService.getMaxByMetricName(name);
        assertEquals(maxById, maxByName);
    }
}
