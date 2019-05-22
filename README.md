# bonfiy6

STEPS TO RUN

Make sure java is installed

DOWNLOAD THE PROJECT GO TO DIRECTORY WHERE PROJECT IS DOWNLOADED 

run following command
./mvnw clean install
cd target/ 
java -jar Question6-0.0.1-SNAPSHOT.jar &
# project information
api.gateway.endpoints  is an array of endpoints


api.gateway.endpoints[0].path=[/]*api/accounts.* <-- url

api.gateway.endpoints[0].method=GET<-- method you need to filter

api.gateway.endpoints[0].location[0]=http://${accounts.queryside.service.host}:8080<-- list of locations for laod balancing

api.gateway.endpoints[0].location[1]=http://${accounts.queryside.service.host}:8090<-- list of locations for laod balancing

api.gateway.endpoints[1].filters[0].filterkey=AuthFilter <--- key of filter it represents filter name
api.gateway.endpoints[1].filters[0].filtervalue=12212112 <--- filter value of the filter it is specific to filter
api.gateway.endpoints[1].filters[0].filterType=REQUEST <--- filter type Request/Response
api.gateway.endpoints[1].filters[1].filterkey=DataFilter <--- FIlter name 
api.gateway.endpoints[1].filters[1].filtervalue=[a-zA-Z0-9]* <---Specific to filter
api.gateway.endpoints[1].filters[1].filterType=REQUEST <---Filter type Request /Response

# project tech stack
It  uses reactive java for scheduling the request call to another server .The reactive java call input output scheduler for calling the external server .Hence Processing threads are never blocked and server is fast

While reciving data it ised compute scheduler  thread for converting the data into string  it in input output intensive task

      Return Type for the Request is Observable<String> which is rxjava object and is asynchornus in nature

# Features checklist
a. Connections, pipelines, pooling & persistency //Implemented using  http clients of apache commons also it supports connection polling
b.Mutiple instance can be run at same time and it provides client side load balancing it provides client side load balancing by getting list of endpoints and randomly selecting one of them in case of failover it retry for 3 time and then randomly selects another once 
c. Circut breaker pattern is not implemented and could be implementing using hystrix.In case of failover from one ip it should call another ip and break circuit from one ip
d.provides request filtering  by providing filtername and filter patttern two type of filter are provided AuthFilter,DataFilter .Auth filter  takes filter value and can provide authentication check by calling IAM or JWT (feature not IMPLEMENTED). Data filter takes regex pattern and allow only that data to be passed  
e .error handling is done using error code and throwing runtime exception from every possible place
