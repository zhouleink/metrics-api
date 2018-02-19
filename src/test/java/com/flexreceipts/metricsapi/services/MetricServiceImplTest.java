package com.flexreceipts.metricsapi.services;

import com.flexreceipts.metricsapi.api.v1.mapper.MetricMapper;
import com.flexreceipts.metricsapi.api.v1.model.MetricDTO;
import com.flexreceipts.metricsapi.domain.Metric;
import com.flexreceipts.metricsapi.exceptions.ResourceAlreadyExistsException;
import com.flexreceipts.metricsapi.exceptions.ResourceNotFoundException;
import com.flexreceipts.metricsapi.repositories.MetricRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MetricServiceImplTest {

    private static final List<Double> VALUES = Arrays.asList(11.0, 12.0, 13.0, 14.0, 15.0);
    private static final String NAME = "Metric_Test";


    @Mock
    MetricRepository metricRepository;

    MetricMapper metricMapper = MetricMapper.INSTANCE;
    MetricService metricService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        metricService = new MetricServiceImpl(metricMapper, metricRepository);
    }

    @Test
    public void getAllMetrics() {
        //given
        Metric metric1 = new Metric();
        metric1.setId(1l);
        metric1.setName("Metric1");
        metric1.setValues(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));

        Metric metric2 = new Metric();
        metric2.setId(2l);
        metric2.setName("Metric2");
        metric2.setValues(Arrays.asList(11.0, 12.0, 13.0, 14.0, 15.0));

        when(metricRepository.findAll()).thenReturn(Arrays.asList(metric1, metric2));

        //when
        List<MetricDTO> metricDTOS = metricService.getAllMetrics();

        //then
        assertEquals(2, metricDTOS.size());
    }

    @Test
    public void getMetricById() {
        //given
        Metric metric1 = new Metric();
        metric1.setId(1l);
        metric1.setName(NAME);
        metric1.setValues(VALUES);

        //when
        when(metricRepository.findById(anyLong())).thenReturn(Optional.ofNullable(metric1));
        MetricDTO metricDTO = metricService.getMetricById(1L);

        assertEquals(NAME, metricDTO.getName());
        assertEquals(VALUES, metricDTO.getValues());
        assertNotSame(VALUES, metricDTO.getValues());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getMetricByIdNotFound() throws Exception {
        //given
        given(metricRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        MetricDTO metricDTO = metricService.getMetricById(1L);
        //then
        then(metricRepository).should(times(1)).findById(anyLong());
    }

    @Test
    public void getMetricByName() {
        //given
        Metric metric1 = new Metric();
        metric1.setId(1l);
        metric1.setName(NAME);
        metric1.setValues(VALUES);

        //when
        when(metricRepository.findByName(anyString())).thenReturn(Optional.ofNullable(metric1));
        MetricDTO metricDTO = metricService.getMetricByName(NAME);

        assertEquals(NAME, metricDTO.getName());
        assertEquals(VALUES, metricDTO.getValues());
        assertNotSame(VALUES, metricDTO.getValues());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getMetricByNameNotFound() {
        //given
        given(metricRepository.findByName(anyString())).willReturn(Optional.empty());
        //when
        MetricDTO metricDTO = metricService.getMetricByName(NAME);
        //then
        then(metricRepository).should(times(1)).findByName(anyString());
    }

    @Test
    public void createNewMetric() {
        //given
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setName(NAME);
        metricDTO.setValues(VALUES);

        Metric savedMetric = new Metric();
        savedMetric.setName(NAME);
        savedMetric.setValues(VALUES);
        savedMetric.setId(1l);

        //when
        when(metricRepository.save(any(Metric.class))).thenReturn(savedMetric);
        MetricDTO savedMetricDto = metricService.createNewMetric(metricDTO);

        //then
        assertEquals(savedMetric.getName(), savedMetricDto.getName());
        assertEquals("/api/v1/metrics/1", savedMetricDto.getMetricUrl());
        assertEquals(savedMetric.getValues(), savedMetricDto.getValues());
        assertNotSame(savedMetric.getValues(), savedMetricDto.getValues());
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void createNewMetricConflict() {
        //given
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setName(NAME);
        metricDTO.setValues(VALUES);
        metricDTO.setId(1L);
        metricDTO.setMetricUrl("/api/v1/metrics/1");
        ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException(new Object[]{metricDTO});
        //when
        when(metricRepository.save(any(Metric.class))).thenThrow(ex);
        MetricDTO savedMetricDto = metricService.createNewMetric(metricDTO);
        //then
        verify(metricRepository, times(1)).save(any(Metric.class));
    }

    @Test
    public void updateOrSaveMetricById() {
        //given
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setName(NAME);
        metricDTO.setValues(VALUES);

        Metric savedMetric = new Metric();
        savedMetric.setName(metricDTO.getName());
        savedMetric.setValues(metricDTO.getValues());
        savedMetric.setId(1l);

        //when
        when(metricRepository.save(any(Metric.class))).thenReturn(savedMetric);
        MetricDTO savedDto = metricService.updateOrSaveMetricById(1L, metricDTO);

        //then
        assertEquals(metricDTO.getName(), savedDto.getName());
        assertEquals("/api/v1/metrics/1", savedDto.getMetricUrl());
        assertEquals(metricDTO.getValues(), savedDto.getValues());
        assertNotSame(metricDTO.getValues(), savedDto.getValues());
    }

    @Test
    public void updateOrSaveMetricByName() {
        //given
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setName(NAME);
        metricDTO.setValues(VALUES);

        Metric savedMetric = new Metric();
        savedMetric.setName(metricDTO.getName());
        savedMetric.setValues(metricDTO.getValues());
        savedMetric.setId(1l);

        //when
        when(metricRepository.save(any(Metric.class))).thenReturn(savedMetric);
        MetricDTO savedDto = metricService.updateOrSaveMetricByName(NAME, metricDTO);

        //then
        assertEquals(metricDTO.getName(), savedDto.getName());
        assertEquals("/api/v1/metrics/1", savedDto.getMetricUrl());
        assertEquals(metricDTO.getValues(), savedDto.getValues());
        assertNotSame(metricDTO.getValues(), savedDto.getValues());
    }

    @Test
    public void patchMetricById() {
        //given
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setName(NAME);

        Metric savedMetric = new Metric();
        savedMetric.setName(metricDTO.getName());
        savedMetric.setId(1l);

        //when
        when(metricRepository.save(any(Metric.class))).thenReturn(savedMetric);
        MetricDTO savedDto = metricService.updateOrSaveMetricById(1L, metricDTO);

        //then
        assertEquals(metricDTO.getName(), savedDto.getName());
        assertEquals("/api/v1/metrics/1", savedDto.getMetricUrl());
    }

    @Test
    public void deleteMetricById() {
        metricRepository.deleteById(1L);
        verify(metricRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void deleteMetricByName() {
        metricRepository.delete(new Metric());
        verify(metricRepository, times(1)).delete(any(Metric.class));
    }
}