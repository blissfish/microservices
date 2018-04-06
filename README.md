# microservices
## simple echo service
This script was created by following the tutorial linked below:

https://cloud.google.com/kubernetes-engine/docs/tutorials/hello-app

## Content of service implementation
POM
Dockerfile
application.properties
Application.java
Message.java

## GCP Project
Projektname: Blissfish
Projekt-ID: blissfish-191215
Projektnummer: 768948453873

https://console.cloud.google.com

## Set some env properties

gcloud config set project blissfish-191215
gcloud config set compute/zone europe-west3-a
export PROJECT_ID="$(gcloud config get-value project -q)"

## Code

git clone https://github.com/blissfish/blissfish.git
cd blissfish/blissfish-samples/
mvn clean package 
cd simple-service
mvn spring-boot:run

curl -i http://localhost:8080/api/message


## Build Docker image

docker build -t gcr.io/${PROJECT_ID}/simple-service:0.0.1 .

docker images
docker ps -s

docker run --rm -p 8080:8080 gcr.io/${PROJECT_ID}/simple-service:0.0.1

docker image rm gcr.io/${PROJECT_ID}/simple-service:0.0.1
## you can use the image id (docker images) to delete an image as well



## Create cluster

gcloud container clusters create blissfish-cluster --num-nodes=3 --zone=europe-west3-a

gcloud compute instances list

## Add blissfish-cluster credentials to kubtctl tool 

gcloud container clusters get-credentials blissfish-cluster


## Push docker image to registry

kubectl get pods

gcloud container images list
gcloud docker -- push gcr.io/${PROJECT_ID}/simple-service:0.0.1
gcloud container images list-tags gcr.io/blissfish-191215/simple-service


## Create k8 POD deployment using the docker image

kubectl run blissfish-web --image=gcr.io/${PROJECT_ID}/simple-service:0.0.1 --port 8080


## Where is the POD running

kubectl get nodes
kubectl get pods 
kubectl get pod -o json blissfish-web-5fd67588bc-f424v


## Scale

kubectl scale deployment blissfish-web --replicas=3


## Create LB service and assign IP support to POD

gcloud compute instances list
kubectl expose deployment blissfish-web --type=LoadBalancer --port 80 --target-port 8080

kubectl get service


## Test and rescale


watch -n .5 curl --no-keepalive -i http://35.198.191.48

kubectl scale deployment blissfish-web --replicas=1
kubectl scale deployment blissfish-web --replicas=3


while sleep 5; do curl -i http://35.198.79.128; done
while sleep 2; do curl -i http://35.198.79.128; done


## Change version in application.properties, update and re-build

version=0.0.2
cd /home/mark_evertz/blissfish
git pull
cd simple-service/
mvn clean package
docker build -t gcr.io/${PROJECT_ID}/simple-service:0.0.2 .
gcloud docker -- push gcr.io/${PROJECT_ID}/simple-service:0.0.2


## Rolling-Update with Kubernetes Engine

kubectl set image deployment/blissfish-web blissfish-web=gcr.io/${PROJECT_ID}/simple-service:0.0.2


## Clean up

gcloud container images delete [HOSTNAME]/[PROJECT-ID]/[IMAGE]
gcloud container images delete blissfish-191215
docker image rm -f 4f9d0e004754

kubectl delete service blissfish-web
gcloud container clusters delete blissfish-cluster


## Spring Boot actuators


curl -i http://localhost:8080/health
curl -i http://localhost:8080/loggers
curl -i http://localhost:8080/auditevents
curl -i http://localhost:8080/beans
curl -i http://localhost:8080/autoconfig
curl -i http://localhost:8080/env
curl -i http://localhost:8080/metrics
curl -i http://localhost:8080/configprops
curl -i http://localhost:8080/mappings
curl -i http://localhost:8080/dump
curl -i http://localhost:8080/info
curl -i http://localhost:8080/trace

## Swagger OpenAPI


http://www.javainuse.com/spring/boot_swagger
http://localhost:8080/swagger-ui.html
