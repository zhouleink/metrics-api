
# Web API for Metrics
This project is to implement a web application which allows users to create a metric and post values to that metric. (e.g. stock price of a security). Users should be able to also request summary statistics for that metric.


## Description
The metric web api is developed via Spring Boot (v. 2.0.0.RC1), and built by Maven. It runs with a bootstrap class that generates 3 metrics initially.
It exposes the following REST calls:

**GET**:
- ***~{root}/api/v1/metrics***     
  &nbsp;&nbsp; returns all metrics &nbsp;&nbsp;
- ***~{root}/api/v1/metrics/{id}***   
  &nbsp;&nbsp; returns a specific metric by its id  
- ***~{root}/api/v1/metrics?name={name}***
&nbsp;&nbsp;returns a specific metric by its name  
- ***~{root}/api/v1/metrics/{id}/getMean***   
  &nbsp;&nbsp; returns the arithmetic average of a specific metric by its id
- ***~{root}/api/v1/metrics?name={name}&method=getMean*** 
&nbsp;&nbsp;  returns the arithmetic average of a specific metric by its name
- ***~{root}/api/v1/metrics/{id}/getMedian***   
&nbsp;&nbsp;  returns the median value of a specific metric by its id
- ***~{root}/api/v1/metrics?name={name}&method=getMedian*** 
&nbsp;&nbsp;  returns the median value of a specific metric by its name
- ***~{root}/api/v1/metrics/{id}/getMin***  
&nbsp;&nbsp;   returns the min value of a specific metric by its id
- ***~{root}/api/v1/metrics?name={name}&method=getMin*** 
&nbsp;&nbsp;   returns the min value of a specific metric by its name
- ***~{root}/api/v1/metrics/{id}/getMax***   
&nbsp;&nbsp;    returns the max value of a specific metric by its id
- ***~{root}/api/v1/metrics?name={name}&method=getMax*** 
&nbsp;&nbsp;   returns the max value of a specific metric by its name

**POST**:
- ***~{root}/api/v1/metrics***           
&nbsp;&nbsp; creates a new metric and posts values to this metric

**PUT**:
- ***~{root}/api/v1/metrics/{id}***           
&nbsp;&nbsp; updates or saves a metric by its id
- ***~{root}/api/v1/metrics?name={name}***          
 &nbsp;&nbsp; udpates or saves a metric by its name

**PATCH**:
- ***~{root}/api/v1/metrics/{id}***           
&nbsp;&nbsp; partially updates a metric by its id
- ***~{root}/api/v1/metrics?name={name}***           
&nbsp;&nbsp; partially updates a metric by its name
## Efficiency
*POST/PUT/PATCH* calls invoke ***Collections.sort()*** method to sort the values in each metric, so the time complexity is ***O(n * lg(n))*** and space complexity is ***O(n)***
Since the values are sorted, all GET calls except those *getMean* calls are ***O(1)*** and *getMean* calls are ***O(n)***
## Build and deploy
*(Under the root directory of the project)*<br>
**Build**:  *mvn clean package*<br>
**Deploy**: *java -jar target/metrics-api-1.0.0-SNAPSHOT.jar*
