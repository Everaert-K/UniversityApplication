In a team of 5 students we created this application for a fictious university. The goal was to practice our skills in the creation of distributed systems using the micro-service architecture, docker, kubernetes and Java spring. 

## Installation & running
### With docker-compose
Use the **docker-compose.yaml** file, docker-compose has to be in the same folder as the services.
In every service-folder there is a Dockerfile present, this can be used to build the service when `docker-compose build` is called.
The services were als added to **docker.io** by the user *vmnaesse*.
By removing the build-lines in the docker-compose and replacing the images by **vmnaesse/[servicenaam]**, the services don't have to be build anymore and `docker-compose up` can be use immidiately.
### On kubernetes
The K8 files kanbe activated on K8 by using `kubectl apply -f file1,file2,..`.
To the K8 files creat-scripts were added, because of this the service can be activated on K8 by using  `./create_[servicenaam].sh`. 
To start all the services at the same time use `./create_all.sh` 
To clear after you are done use `kubectl delete service service1 service2 ...` and `kubectl delete deployment service1 service2 ...`.
This was also automated `./cleanup_all.sh`.
## Testing
Use the api-gateway.
Navigate to *http://[ip van gateway]:8080* 
To test **chaosmonkey**: `./chaos.sh` 
