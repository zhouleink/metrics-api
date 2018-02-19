package com.flexreceipts.metricsapi.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetricDTO {
    private Long id;
    private String name;
    private List<Double> values;
    @JsonProperty("metric_url")
    private String metricUrl;
}
