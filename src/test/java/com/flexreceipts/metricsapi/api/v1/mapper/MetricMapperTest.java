package com.flexreceipts.metricsapi.api.v1.mapper;

import com.flexreceipts.metricsapi.api.v1.model.MetricDTO;
import com.flexreceipts.metricsapi.domain.Metric;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MetricMapperTest {

    private static final Long ID = 1L;
    private static final String NAME = "TEST_VALUES";
    private static final List<Double> VALUES = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);

    MetricMapper metricMapper = MetricMapper.INSTANCE;

    @Test
    public void testMetricToMetricDTO() {
        //given
        Metric metric = new Metric();
        metric.setId(ID);
        metric.setName(NAME);
        metric.setValues(VALUES);

        //when
        MetricDTO metricDTO = metricMapper.metricToMetricDTO(metric);

        //then
        assertEquals(Long.valueOf(ID), metricDTO.getId());
        assertEquals(NAME, metricDTO.getName());
        assertEquals(VALUES, metricDTO.getValues());
        assertNotSame(VALUES, metricDTO.getValues());
    }

    @Test
    public void testMetricDtoToMetric() {
        //given
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setId(ID);
        metricDTO.setName(NAME);
        metricDTO.setValues(VALUES);

        //when
        Metric metric = metricMapper.metricDtoToMetric(metricDTO);

        //then
        assertEquals(Long.valueOf(ID), metric.getId());
        assertEquals(NAME, metric.getName());
        assertEquals(VALUES, metric.getValues());
        assertNotSame(VALUES, metric.getValues());
    }

}