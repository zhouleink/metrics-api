package com.flexreceipts.metricsapi.bootstrap;

import com.flexreceipts.metricsapi.domain.Metric;
import com.flexreceipts.metricsapi.repositories.MetricRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Bootstrap implements CommandLineRunner {

    private MetricRepository metricRepository;

    public Bootstrap(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        loadMetrics();
    }

    private void loadMetrics() {
        Metric stockPrices = new Metric();
        stockPrices.setName("Stock_Prices");
        stockPrices.setValues(Arrays.asList(106.53, 172.43, 177.36, 1095.50, 1448.69));

        Metric foodPrices = new Metric();
        foodPrices.setName("Food_Prices");
        foodPrices.setValues(Arrays.asList(2.99, 3.99, 4.99, 5.99, 6.99, 7.99));

        Metric vehiclePrices = new Metric();
        vehiclePrices.setName("Vehicle_Prices");
        vehiclePrices.setValues(Arrays.asList(23495.0, 26255.0, 30750.0, 48400.0, 52155.0));


        metricRepository.save(stockPrices);
        metricRepository.save(foodPrices);
        metricRepository.save(vehiclePrices);
    }

}
