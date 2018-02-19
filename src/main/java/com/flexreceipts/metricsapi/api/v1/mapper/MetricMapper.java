package com.flexreceipts.metricsapi.api.v1.mapper;

import com.flexreceipts.metricsapi.api.v1.model.MetricDTO;
import com.flexreceipts.metricsapi.domain.Metric;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MetricMapper {
    MetricMapper INSTANCE = Mappers.getMapper(MetricMapper.class);

    MetricDTO metricToMetricDTO(Metric metric);

    Metric metricDtoToMetric(MetricDTO metricDTO);
}
