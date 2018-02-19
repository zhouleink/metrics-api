package com.flexreceipts.metricsapi.services;

import com.flexreceipts.metricsapi.api.v1.model.MetricDTO;

import java.util.List;

public interface MetricService {

    List<MetricDTO> getAllMetrics();

    MetricDTO getMetricById(Long id);

    MetricDTO getMetricByName(String name);

    MetricDTO createNewMetric(MetricDTO metricDTO);

    MetricDTO updateOrSaveMetricById(Long id, MetricDTO metricDTO);

    MetricDTO updateOrSaveMetricByName(String name, MetricDTO metricDTO);

    MetricDTO patchMetricById(Long id, MetricDTO metricDTO);

    MetricDTO patchMetricByName(String name, MetricDTO metricDTO);

    MetricDTO deleteMetricById(Long id);

    MetricDTO deleteMetricByName(String name);

    Double getMeanByMetricId(Long id);

    Double getMeanByMetricName(String name);

    Double getMedianByMetricId(Long id);

    Double getMedianByMetricName(String name);

    Double getMinByMetricId(Long id);

    Double getMinByMetricName(String name);

    Double getMaxByMetricId(Long id);

    Double getMaxByMetricName(String name);
}
