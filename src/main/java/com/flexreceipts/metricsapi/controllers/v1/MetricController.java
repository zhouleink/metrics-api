package com.flexreceipts.metricsapi.controllers.v1;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.flexreceipts.metricsapi.api.v1.model.MetricDTO;
import com.flexreceipts.metricsapi.api.v1.model.MetricsDTO;
import com.flexreceipts.metricsapi.services.MetricService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@RestController
@RequestMapping(MetricController.BASE_URL)
public class MetricController {
    public static final String BASE_URL = "/api/v1/metrics";
    private final String JSON_INPUT_ERROR_MSG = "Invalid input values!";

    private final MetricService metricService;

    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    /**
     * GET: Retrieve all metrics
     * @return A list of metric objects
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public MetricsDTO getAllMetrics() {
        return new MetricsDTO(metricService.getAllMetrics());
    }

    /**
     * GET: Retrieve a metric by its name
     * @param name
     * @return
     */
    @GetMapping(params = {"name"})
    @ResponseStatus(HttpStatus.OK)
    public MetricDTO getMetricByName(@RequestParam String name) {
        return metricService.getMetricByName(name);
    }

    /**
     * GET: Retrieve a metric by its id
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public MetricDTO getMetricById(@PathVariable String id) {
        return metricService.getMetricById(new Long(id));
    }

    /**
     * POST: Create a new metric, if a resource with the same name has already existed, throw a customized exception
     * @param metricDTO
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MetricDTO createNewMetric(@RequestBody MetricDTO metricDTO) {
        return metricService.createNewMetric(metricDTO);
    }

    /**
     * Handle the error when posting non-numeric values to a metric
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<Object> handleJsonMappingException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(JSON_INPUT_ERROR_MSG,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * GET: Retrieve the arithmetic mean of the values of a metric according to its id
     * Param method: getMean
     * @param id
     * @return mean value in JSON format
     */
    @GetMapping("{id}/getMean")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Double> getMeanById(@PathVariable String id) {
        return new HashMap<String, Double>() {
            {
                put("response", metricService.getMeanByMetricId(new Long(id)));
            }
        };
    }

    /**
     * GET: Retrieve the arithmetic mean of the values of a metric according to its name
     * Param method: getMean
     * @param name
     * @return mean value in JSON format
     */
    @GetMapping(params = {"name", "method=getMean"})
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Double> getMeanByName(@RequestParam String name) {
        return new HashMap<String, Double>() {
            {
                put("response", metricService.getMeanByMetricName(name));
            }
        };
    }

    /**
     * GET: Retrieve the median of the values of a metric according to its id
     * Param method: getMedian
     * @param id
     * @return median value in JSON format
     */
    @GetMapping("{id}/getMedian")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Double> getMedianById(@PathVariable String id) {
        return new HashMap<String, Double>() {
            {
                put("response", metricService.getMedianByMetricId(new Long(id)));
            }
        };
    }

    /**
     * GET: Retrieve the arithmetic mean of the values of a metric according to its name
     * Param method: getMedian
     * @param name
     * @return median value in JSON format
     */
    @GetMapping(params = {"name", "method=getMedian"})
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Double> getMedianByName(@RequestParam String name) {
        return new HashMap<String, Double>() {
            {
                put("response", metricService.getMedianByMetricName(name));
            }
        };
    }

    /**
     * GET: Retrieve the min of the values of a metric according to its id
     * Param method: getMin
     * @param id
     * @return min value in JSON format
     */
    @GetMapping("{id}/getMin")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Double> getMinById(@PathVariable String id) {
        return new HashMap<String, Double>() {
            {
                put("response", metricService.getMinByMetricId(new Long(id)));
            }
        };
    }

    /**
     * GET: Retrieve the min of the values of a metric according to its name
     * Param method: getMin
     * @param name
     * @return min value in JSON format
     */
    @GetMapping(params = {"name", "method=getMin"})
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Double> getMinByName(@RequestParam String name) {
        return new HashMap<String, Double>() {
            {
                put("response", metricService.getMinByMetricName(name));
            }
        };
    }

    /**
     * GET: Retrieve the max of the values of a metric according to its id
     * Param method: getMax
     * @param id
     * @return max value in JSON format
     */
    @GetMapping("{id}/getMax")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Double> getMaxById(@PathVariable String id) {
        return new HashMap<String, Double>() {
            {
                put("response", metricService.getMaxByMetricId(new Long(id)));
            }
        };
    }

    /**
     * GET: Retrieve the max of the values of a metric according to its name
     * Param method: getMax
     * @param name
     * @return max value in JSON format
     */
    @GetMapping(params = {"name", "method=getMax"})
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Double> getMaxByName(@RequestParam String name) {
        return new HashMap<String, Double>() {
            {
                put("response", metricService.getMaxByMetricName(name));
            }
        };
    }

    /**
     * PUT: Update or Save the metric according to its id
     * @param id
     * @param metricDTO
     * @return the updated metric object
     */
    @PutMapping({"{id}"})
    @ResponseStatus(HttpStatus.OK)
    public MetricDTO updateMetricById(@PathVariable String id, @RequestBody MetricDTO metricDTO) {
        return metricService.updateOrSaveMetricById(new Long(id), metricDTO);
    }

    /**
     * PUT: Update or Save the metric according to its name
     * @param name
     * @param metricDTO
     * @return the updated metric object
     */
    @PutMapping(params = {"name"})
    @ResponseStatus(HttpStatus.OK)
    public MetricDTO updateMetricByName(@RequestParam String name, @RequestBody MetricDTO metricDTO) {
        return metricService.updateOrSaveMetricByName(name, metricDTO);
    }

    /**
     * PATCH: Partially update a metric according to its id
     * @param id
     * @param metricDTO
     * @return the updated metric object
     */
    @PatchMapping({"{id}"})
    @ResponseStatus(HttpStatus.OK)
    public MetricDTO patchMetricById(@PathVariable String id, @RequestBody MetricDTO metricDTO) {
        return metricService.patchMetricById(new Long(id), metricDTO);
    }

    /**
     * PATCH: Partially update a metric according to its name
     * @param name
     * @param metricDTO
     * @return the updated metric object
     */
    @PatchMapping(params = {"name"})
    @ResponseStatus(HttpStatus.OK)
    public MetricDTO patchMetricByName(@RequestParam String name, @RequestBody MetricDTO metricDTO) {
        return metricService.patchMetricByName(name, metricDTO);
    }

    /**
     * DELETE: delete a metric according to its id
     * @param id
     * @return the deleted metric object
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public MetricDTO deleteMetricById(@PathVariable String id) {
        return metricService.deleteMetricById(new Long(id));
    }

    /**
     * DELETE: delete a metric according to its name
     * @param name
     * @return the deleted metric object
     */
    @DeleteMapping(params = {"name"})
    @ResponseStatus(HttpStatus.OK)
    public MetricDTO deleteMetricByName(@RequestParam String name) {
        return metricService.deleteMetricByName(name);
    }

}
