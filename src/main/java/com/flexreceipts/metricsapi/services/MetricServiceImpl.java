package com.flexreceipts.metricsapi.services;

import com.flexreceipts.metricsapi.api.v1.mapper.MetricMapper;
import com.flexreceipts.metricsapi.api.v1.model.MetricDTO;
import com.flexreceipts.metricsapi.controllers.v1.MetricController;
import com.flexreceipts.metricsapi.domain.Metric;
import com.flexreceipts.metricsapi.exceptions.ResourceAlreadyExistsException;
import com.flexreceipts.metricsapi.exceptions.ResourceNotFoundException;
import com.flexreceipts.metricsapi.repositories.MetricRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class MetricServiceImpl implements MetricService {
    private final MetricMapper metricMapper;
    private final MetricRepository metricRepository;

    public MetricServiceImpl(MetricMapper metricMapper,
                             MetricRepository metricRepository) {
        this.metricMapper = metricMapper;
        this.metricRepository = metricRepository;
    }

    @Override
    public List<MetricDTO> getAllMetrics() {
        return metricRepository.findAll()
                .stream()
                .map(metric -> {
                    MetricDTO metricDTO = metricMapper.metricToMetricDTO(metric);
                    metricDTO.setMetricUrl(getMetricUrl(metric.getId()));
                    return metricDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public MetricDTO getMetricById(Long id) {
        return metricRepository.findById(id)
                .map(metricMapper::metricToMetricDTO)
                .map(metricDTO -> {
                    metricDTO.setMetricUrl(getMetricUrl(id));
                    return metricDTO;
                })
                .orElseThrow(ResourceNotFoundException::new);

    }

    @Override
    public MetricDTO getMetricByName(String name) {
        return metricRepository.findByName(name)
                .map(metric -> {
                    MetricDTO metricDTO = metricMapper.metricToMetricDTO(metric);
                    metricDTO.setMetricUrl(getMetricUrl(metric.getId()));
                    return metricDTO;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public MetricDTO createNewMetric(MetricDTO metricDTO) {
        if (metricDTO.getValues() != null) {
            Collections.sort(metricDTO.getValues());
        }
        Optional<Metric> metric = metricRepository.findByName(metricDTO.getName());
        if (metric.isPresent()) {
            MetricDTO existingDTO = metricMapper.metricToMetricDTO(metric.get());
            existingDTO.setMetricUrl(getMetricUrl(metric.get().getId()));
            throw new ResourceAlreadyExistsException(new Object[]{existingDTO});
        }
        return saveAndReturnMetricDTO(metricMapper.metricDtoToMetric(metricDTO));
    }

    @Override
    public MetricDTO updateOrSaveMetricById(Long id, MetricDTO metricDTO) {
        if (metricDTO.getValues() != null) {
            Collections.sort(metricDTO.getValues());
        }
        Metric metric = metricMapper.metricDtoToMetric(metricDTO);
        metric.setId(id);
        return saveAndReturnMetricDTO(metric);
    }

    @Override
    public MetricDTO updateOrSaveMetricByName(String name, MetricDTO metricDTO) {
        if (metricDTO.getValues() != null) {
            Collections.sort(metricDTO.getValues());
        }
        return metricRepository.findByName(name)
                .map(metric -> {
                    metric.setName(metricDTO.getName());
                    metric.setValues(metricDTO.getValues());
                    MetricDTO returnDto = saveAndReturnMetricDTO(metric);
                    returnDto.setMetricUrl(getMetricUrl(metric.getId()));
                    return returnDto;
                }).orElseGet(() -> saveAndReturnMetricDTO(metricMapper.metricDtoToMetric(metricDTO)));
    }

    @Override
    public MetricDTO patchMetricById(Long id, MetricDTO metricDTO) {
        return metricRepository.findById(id)
                .map(metric -> {
                    if (metricDTO.getName() != null) {
                        metric.setName(metricDTO.getName());
                    }
                    if (metricDTO.getValues() != null) {
                        Collections.sort(metricDTO.getValues());
                        metric.setValues(metricDTO.getValues());
                    }
                    MetricDTO returnDto = saveAndReturnMetricDTO(metric);
                    returnDto.setMetricUrl(getMetricUrl(id));
                    return returnDto;
                }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public MetricDTO patchMetricByName(String name, MetricDTO metricDTO) {
        return metricRepository.findByName(name)
                .map(metric -> {
                    if (metricDTO.getName() != null) {
                        metric.setName(metricDTO.getName());
                    }
                    if (metricDTO.getValues() != null) {
                        Collections.sort(metricDTO.getValues());
                        metric.setValues(metricDTO.getValues());
                    }
                    MetricDTO returnDto = saveAndReturnMetricDTO(metric);
                    returnDto.setMetricUrl(getMetricUrl(metric.getId()));
                    return returnDto;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public MetricDTO deleteMetricById(Long id) {
        Metric metric = metricRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        MetricDTO metricDTO = metricMapper.metricToMetricDTO(metric);
        metricDTO.setMetricUrl(getMetricUrl(metric.getId()));
        metricRepository.deleteById(id);
        return metricDTO;
    }

    @Override
    public MetricDTO deleteMetricByName(String name) {
        Metric metric = metricRepository.findByName(name).orElseThrow(ResourceNotFoundException::new);
        MetricDTO metricDTO = metricMapper.metricToMetricDTO(metric);
        metricDTO.setMetricUrl(getMetricUrl(metric.getId()));
        metricRepository.delete(metric);
        return metricDTO;
    }

    private MetricDTO saveAndReturnMetricDTO(Metric metric) {
        Metric savedMetric = metricRepository.save(metric);
        MetricDTO returnedMetricDTO = metricMapper.metricToMetricDTO(savedMetric);
        returnedMetricDTO.setMetricUrl(getMetricUrl(savedMetric.getId()));
        return returnedMetricDTO;
    }

    private String getMetricUrl(Long id) {
        return MetricController.BASE_URL + "/" + id;
    }

    @Override
    public Double getMeanByMetricId(Long id) {
        return getMean(getValuesById(id));
    }

    @Override
    public Double getMeanByMetricName(String name) {
        return getMean(getValuesByName(name));
    }

    @Override
    public Double getMedianByMetricId(Long id) {
        return getMedian(getValuesById(id));
    }

    @Override
    public Double getMedianByMetricName(String name) {
        return getMedian(getValuesByName(name));
    }

    @Override
    public Double getMinByMetricId(Long id) {
        return getMin(getValuesById(id));
    }

    @Override
    public Double getMinByMetricName(String name) {
        return getMin(getValuesByName(name));
    }
    @Override
    public Double getMaxByMetricId(Long id) {
        return getMax(getValuesById(id));
    }

    @Override
    public Double getMaxByMetricName(String name) {
        return getMax(getValuesByName(name));
    }

    private List<Double> getValuesById(Long id) {
        return getMetricById(id).getValues();
    }

    private List<Double> getValuesByName(String name) {
        return getMetricByName(name).getValues();
    }

    private Double getMean(List<Double> values) {
        OptionalDouble mean = values.stream().mapToDouble(Double::doubleValue).average();
        return mean.isPresent() ? mean.getAsDouble() : null;
    }

    private Double getMedian(List<Double> values) {
        if (values.isEmpty() || values.size() == 0) return null;
        int mid = values.size() / 2;
        return values.size() % 2 == 0 ? (values.get(mid) + values.get(mid - 1)) / 2 : values.get(mid);
    }

    private Double getMin(List<Double> values) {
        if (values.isEmpty() || values.size() == 0) return null;
        return values.get(0);
    }

    private Double getMax(List<Double> values) {
        if (values.isEmpty() || values.size() == 0) return null;
        return values.get(values.size() - 1);
    }
}
