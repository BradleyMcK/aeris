# Aeris Demonstration Project

### Running Locally

First verify that you have Java-17 and Docker installed.  Then navigate to an empty directory and execute the following commands:
```
% git clone https://github.com/BradleyMcK/aeris.git
% cd ./aeris
% mvn clean install spring-boot:build-image
```
The build-image goal will take a number of minutes to complete the first time it is run.  If successful it should finish with something like this:
```
Successfully built image 'docker.io/library/aeris:0.0.1-SNAPSHOT'
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  11.766 s
[INFO] Finished at: 2023-08-22T15:00:23-06:00
```
You can run the application inside a docker container with this command:
```
% docker run -it -p8080:8080 aeris:0.0.1-SNAPSHOT
```

The container should now be running and listening to port 8080.  You can test endpoints by opening a browser and entering these URLS:

http://localhost:8080/demo/get-info

http://localhost:8080/demo/get-data?tIndex=2&zIndex=0

http://localhost:8080/demo/get-image?tIndex=2&zIndex=0
