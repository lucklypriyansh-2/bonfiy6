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

