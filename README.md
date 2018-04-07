# Microservices
## Simple services
This script was created by following the tutorial linked below:

https://cloud.google.com/kubernetes-engine/docs/tutorials/hello-app

## Familiarize yourself with the service implementation
* microservices/pom.xml
* microservices/Dockerfile
* microservices/service0/pom.xml
* microservices/service0/src/main/resources/application.yaml
* microservices/service0/src/main/java/service0/Application.java
* microservices/service0/src/main/java/service0/Message.java

## Create a GCP Project
* Projektname: Blissfish
* Projekt-ID: blissfish-191215
* Projektnummer: 768948453873

https://console.cloud.google.com
## Open the GCP web based command shell

## Create Kubernetes (K8) cluster
`gcloud container clusters create blissfish-cluster --num-nodes=3 --zone=europe-west3-a`

`gcloud compute instances list`

## Set some env properties
`gcloud config set project blissfish-191215`

`gcloud config set compute/zone europe-west3-a`

`export PROJECT_ID="$(gcloud config get-value project -q)"`

## Add blissfish-cluster credentials to kubtctl tool 
`gcloud container clusters get-credentials blissfish-cluster`

## Clone the repository from github, build & start run the service
`git clone https://github.com/blissfish/microservices.git`

### Navigate to your home dir in the GCP shell (/home/[user_name])
`pwd` 
`cd microservices/`

`mvn clean package`

`cd service0`

`mvn spring-boot:run`

### Open a 2nd shell and verify the service is running
`curl -i http://localhost:8080/api/`

## Build the service0 Docker image
`docker build -t gcr.io/${PROJECT_ID}/service0:0.0.1 .`
### View generated images
`docker images`
### View running docker processes
`docker ps -s`
### Start the container
`docker run --rm -p 8080:8080 gcr.io/${PROJECT_ID}/service0:0.0.1`
### Call the service and compare output with running container process data
`curl -i http://localhost:8080/api/`

`docker ps -s`
## Push the Docker service image to the registry
### First view already registered images
`gcloud container images list`
### Push the image to the Google Container Registry (gcr.io)
`gcloud docker -- push gcr.io/${PROJECT_ID}/service0:0.0.1`
### View registered image & meta data
`gcloud container images list`

`gcloud container images list-tags gcr.io/blissfish-191215/service0`

`gcloud container images describe gcr.io/blissfish-191215/service0:0.0.1`

### Create K8 POD deployment using the registered docker image
`kubectl get pods`

`kubectl run blissfish-deployment --image=gcr.io/${PROJECT_ID}/service0:0.0.1 --port 8080`
### Verify the deployment status 
`kubectl get pods`

## Where is the POD running?
`kubectl get nodes`

`kubectl get pods`

`kubectl get pod -o json blissfish-deployment-[use-NAME-from-get-pods-output]`

## Vertical scaling
`kubectl scale deployment blissfish-deployment --replicas=3`

## Expose the service to the outside world
### View computing instances 
`gcloud compute instances list`
### Create LB service and assign IP & port support to POD
`kubectl expose deployment blissfish-deployment --type=LoadBalancer --port 80 --target-port 8080`
### Verify status of the service
`kubectl get service`

## Call the service and  rescale
`watch -n 1 curl --no-keepalive -i http://[IP]/api`
### Change nr of instances in 2nd GCP shell
`kubectl scale deployment blissfish-app --replicas=1`
### Observe how LB routing changes in 1st shell 
`kubectl scale deployment blissfish-app --replicas=3`

## Change service version, update, build and re-deploy
#### Change version attribute in file microservices/service0/src/main/resources/application.yml
`version=0.0.2`
### Pull the update from github & rebuild
`cd /home/[user_name]/microservices`

`git pull`

`cd service0/`

`mvn clean package`

`docker build -t gcr.io/${PROJECT_ID}/service0:0.0.2 .`

`gcloud docker -- push gcr.io/${PROJECT_ID}/service0:0.0.2`
### Start a rolling-update with Kubernetes Engine
`kubectl set image deployment/blissfish-deployment blissfish-deployment=gcr.io/${PROJECT_ID}/service0:0.0.2`

## Clean up
### Delete the LB service
`kubectl delete service blissfish-deployment`
### Remove container images from registry
`gcloud container images delete gcr.io/blissfish-191215/service0:0.0.1`

`gcloud container images delete gcr.io/blissfish-191215/service0:0.0.2`

### Remove container images from Docker repository
#### Use either Docker image id or name to delete an image
`docker image rm gcr.io/${PROJECT_ID}/service0:0.0.1`

`docker image rm -f [image id]`
### Delete the deployment
`kubectl delete deployment blissfish-deployment`

### Remove stopped containers and all unused images

`docker system prune -a`
### Finally delete the cluster
`gcloud container clusters delete blissfish-cluster`

## Using yaml scripts for deploy, scale etc.
`cd ~/mcroservices`

`kubectl apply -f deployment-scale.yaml`

`kubectl apply -f deployment-two-containers.yaml`

`kubectl apply -f deployment.yaml`

## Some fun with Spring Boot actuators
`curl -i http://localhost:8080/health`

`curl -i http://localhost:8080/loggers`

`curl -i http://localhost:8080/auditevents`

`curl -i http://localhost:8080/beans`

`curl -i http://localhost:8080/autoconfig`

`curl -i http://localhost:8080/env`

`curl -i http://localhost:8080/metrics`

`curl -i http://localhost:8080/configprops`

`curl -i http://localhost:8080/mappings`

`curl -i http://localhost:8080/dump`

`curl -i http://localhost:8080/info`

`curl -i http://localhost:8080/trace`

## Smme fun with Swagger/OpenAPI
`http://www.javainuse.com/spring/boot_swagger`

`http://localhost:8080/swagger-ui.html`
