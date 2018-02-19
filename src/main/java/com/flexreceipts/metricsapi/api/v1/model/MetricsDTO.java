package com.flexreceipts.metricsapi.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MetricsDTO {
    List<MetricDTO> metrics;
}
