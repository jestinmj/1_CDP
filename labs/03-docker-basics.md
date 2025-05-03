# Docker Basics

## Indice

- Docker Command Basics (Mandatory)
- Docker image (Mandatory)
- Manage Data in Docker (Mandatory)
- Docker Registry (Mandatory)
- Docker Networking (Mandatory)
- Dockerfile (Mandatory)
- Docker Compose (Optional)

## Docker Command Basics

  1  docker pull ubuntu
  2  docker pull -q alpine
  3  docker run -d --name myubuntu ubuntu
  4  docker ps
  5  docker run -d --name myubuntu -i ubuntu
  6  docker rm myubuntu
  7  docker run -d --name myubuntu -i ubuntu
  8  docker ps
  9  docker exec myubuntu whoami
  10  docker exec myubuntu cat /etc/lsb-release
  11  docker exec -it myubuntu bash
  12  docker run -d --name yourubuntu ubuntu
  13  docker ps -a
  14  docker top myubuntu

### "Challenge: Docker Command Basics"

1. How can you run the nginx container in the background?

```bash
docker run -d --name nginx -i nginx
```

2. Give the container a name such as webserver and add any environment variables into it

```bash
docker run -d -e NGINX_ENTRYPOINT_QUIET_LOGS=1 --name webserver -i nginx 
```

3. Don’t forget to bind container port to the host as port 80 so you can access it through this url

```bash
docker run -d -e NGINX_ENTRYPOINT_QUIET_LOGS=1 -p 80:80 --name webserver -i nginx 
```

## Docker Image

  1  git clone https://gitlab.practical-devsecops.training/pdso/django.nv
  2  cd django.nv
  3  cat Dockerfile
  4  docker build -t django.nv:1.0 .
  5  docker images

### "Challenge: Docker Image"

1. Rename the docker image django.nv:1.0 to django.nv:1.1

```bash
docker tag django.nv:1.0 django.nv:1.1
```

2. Override Entrypoint to run a bash shell (/bin/bash)

```bash
docker run -it --entrypoint /bin/bash django.nv:1.1
```

3. Delete the docker image django.nv:1.0

```bash
docker rmi django.nv:1.0
```

## Manage Data in Docker

```bash
docker volume --help
docker volume ls
docker volume create demo
ls /var/lib/docker/volumes/
ls /var/lib/docker/volumes/demo/
ls /var/lib/docker/volumes/demo/_data/
docker run --name ubuntu -d -v demo:/opt -it ubuntu:18.04
ls /var/lib/docker/volumes/demo/_data/
ls /var/lib/docker/volumes/demo/
docker ps
docker run --name ubuntu2 -d -v /opt:/opt -it ubuntu:18.04
docker exec -it ubuntu2 bash
docker run --name ubuntu2 -d -v /opt:/opt -it ubuntu:18.04
docker ps
docker rm 6f31b5198bd2 f69a6bdff654
docker ps
docker kill 6f31b5198bd2 f69a6bdff654
docker run --name ubuntu2 -d -v /opt:/opt -it ubuntu:18.04
docker ps -a
docker kill 6f31b5198bd2 f69a6bdff654
docker ps
docker ps -a
docker rm 6f31b5198bd2 f69a6bdff654
docker run --name ubuntu2 -d -v /opt:/opt -it ubuntu:18.04
ls -l /opt
ls /opt
rm /opt/hello.txt
```

### "Challenge: Manage Data in Docker"

1. Create a hello.txt file in the /opt directory. The file can have some random text of your choice

```bash
echo "some random text of your choice" > /opt/hello.txt
```

2. Create a container with name bindmount using ubuntu:20.04 image. Mount the above file (not the directory) as a bind mount inside this container at /src location.

```bash
docker run --name bindmount -v /opt/hello.txt:/src/hello.txt ubuntu:20.04
```

3. Create a new volume called data with docker volume command

```bash
docker volume create data
```

4. Run an another ubuntu:20.04 container with name volumemount. Mount the data volume we just created into the container at /src location. Create a file /src/hello.txt in this container with some random text

```bash
docker run --name ubuntu -d -v data:/src -it ubuntu:20.04
docker exec -ti ubuntu bash
echo "some random text" > /src/hello.txt
```
OR
`docker run --name volumemount -v data:/src ubuntu:20.04 "/bin/bash" "-c" "echo test>>/src/hello.txt"
`

5. What happens to the hello.txt file when we remove both the containers? Provide an explanation

Answer: Both of theme are stored permanently, one in volume named "data", the other in host file system directory "/opt"

## Docker Registry 

```bash
git clone https://gitlab.practical-devsecops.training/pdso/django.nv
cd django.nv
cat Dockerfile
docker build -t django.nv:1.0 .
docker images
docker run -d -p 5000:5000 --restart=always --name registry registry:2
docker tag django.nv:1.0 localhost:5000/django.nv:1.0
docker push localhost:5000/django.nv:1.0
curl localhost:5000/v2/_catalog
```

### Challenge: Store the docker image in registry

1. Sign up for the Free Docker Hub Account here
2. Use the docker login command on the DevSecOps-Box machine to login into the Docker Hub registry via Command Line Interface (CLI). When prompted for credentials, please use the credentials you just created in the above task 1

```bash
docker logout
docker login -u jestinj88
Login Succeeded
```

3. Push/Upload django.nv:1.0 image you created in the above exercise to Docker Hub

```bash
docker tag django.nv:1.0 frankz/django.nv:1.0
docker push frankz/django.nv:1.0
```

4. Stop the registry container and remove the images to save the disk space

```bash
docker stop registry
docker rmi django.nv:1.0
```

### Docker Networking

> https://blog.oddbit.com/post/2018-03-12-using-docker-macvlan-networks/
> https://docs.docker.com/engine/reference/commandline/network/
> https://docs.docker.com/network/macvlan/
> https://docs.docker.com/engine/reference/commandline/network_connect/

```bash
docker network --help
docker network ls
docker network list
docker network create mynetwork
docker network ls
docker inspect mynetwork
docker run -d --name ubuntu --network mynetwork -it ubuntu:18.04
docker inspect mynetwork
docker exec ubuntu apt update
docker rm -f ubuntu
docker network rm mynetwork
docker network create --driver macvlan mymacvlan
docker network list
ifconfig
docker run -d --name ubuntu --network mymacvlan -it ubuntu:18.04
docker inspect ubuntu -f "{{json .NetworkSettings.Networks }}" | jq
docker rm -f ubuntu
docker network rm mymacvlan
docker run -d --name ubuntu --network=none -it ubuntu:18.04
docker inspect ubuntu -f "{{json .NetworkSettings.Networks }}" | jq
docker exec ubuntu apt update
```

### "Challenge: Docker Networking"

1. Create a network with bridge driver called app and define 172.10.2.0/16 as a subnet

```bash
docker network create --driver=bridge --subnet=172.10.2.0/16 app
docker inspect app -f "{{json .IPAM.Config}}"
```

2. Run the containers with ubuntu:18.04 image in the background

```bash
docker run -d --name app -it ubuntu:18.04
```

3. Attach app network to the running container, detailed how to at this link

```bash
docker network connect app app
```

4. What would happen if we remove the network driver without killing the container?

Docker answer with an error while removing the network

## Dockerfile

> https://docs.docker.com/engine/reference/builder/
> https://goinbigdata.com/docker-run-vs-cmd-vs-entrypoint/
> https://stackoverflow.com/questions/42218957/dockerfile-cmd-instruction-will-exit-the-container-just-after-running-it
> https://docs.docker.com/engine/reference/run/
> https://docs.docker.com/config/containers/multi-service_container/
> https://docs.docker.com/engine/reference/builder
> https://docs.docker.com/develop/develop-images/dockerfile_best-practices

```bash
mkdir learn-dockerfile
cd learn-dockerfile
touch Dockerfile
cat > Dockerfile <<EOL
FROM ubuntu:18.04

RUN apt install nginx
EOL

cat Dockerfile 
docker build -t nginx-custom .
cat > Dockerfile <<EOL
FROM ubuntu:18.04

RUN apt update && apt install nginx
EOL

cat Dockerfile 
docker build -t nginx-custom .
cat > Dockerfile <<EOL
FROM ubuntu:18.04

RUN apt update && apt install -y nginx
EOL

docker build -t nginx-custom .
docker images
docker run -d --name ubuntu -it ubuntu:18.04
docker exec -it ubuntu bash
cat > Dockerfile <<EOL
FROM ubuntu:18.04

RUN apt update && apt install nginx -y

CMD ["/bin/bash", "-c" , "service nginx start"]
EOL

docker build -t nginx-custom .
docker build -t custom-nginx .
docker run -d -it custom-nginx
docker ps -a
docker 891e9212fef7
docker logs 891e9212fef7
cat > Dockerfile <<EOL
FROM ubuntu:18.04

RUN apt update && apt install nginx -y

CMD ["/bin/bash", "-c" , "service nginx start"]
CMD [";sleep infinity"]
EOL

docker build -t custom-nginx .
cat > Dockerfile <<EOL
FROM ubuntu:18.04

RUN apt update && apt install nginx -y

CMD ["/bin/bash", "-c" , "service nginx start"]
CMD ["/bin/bash", "-c" , "service nginx start; sleep infinity"]
EOL

cat > Dockerfile <<EOL
FROM ubuntu:18.04

RUN apt update && apt install nginx -y

ENTRYPOINT ["/bin/bash", "-c"]

CMD ["service nginx start; sleep infinity"]
EOL

docker build -t custom-nginx .
docker run -d --name nginx -it nginx-custom
docker ps
docker ps -a
docker rm -f $(docker ps -qa)
docker ps -a
docker run -d --name nginx -it nginx-custom
docker run -d --name nginx-one -it nginx-custom bash
docker ps
```

### "Challenge: Dockerfile"

1. The second container is alive. Why?

Puting bash at the end make CMD be ignored and bash interpreter runs instead.
CMD after Entrypoint sets additional default parameters for ENTRYPOINT in exec form.

## Docker Compose

https://docs.docker.com/compose/reference/

```bash
docker run -d --name ubuntu -i ubuntu:18.04
cat >docker-compose.yml<<EOF
version: "3"
 
services:
  ubuntu:
    image: ubuntu:18.04
    stdin_open: true        # the same way like docker run -i
EOF

docker-compose up -d
docker ps
ls
cat >docker-compose.yml<<EOL
version: "3"

services:
  ubuntu:
    image: ubuntu:18.04
    volumes:
     - data:/opt

  alpine:
    image: alpine:3.13
    container_name: alpine
    volumes:
     - data:/tmp

volumes:
  data:
EOL

docker-compose up -d
```

### "Challenge: Docker Compose"

1. Explain why these two containers have stopped (Exit 0)

Both of them execute without problems and have no stdin to work with and any entrypoint for still be working

2. Figure out how can you make the containers run indefinitely. Recall the techniques you have learned in Learn Docker Commands exercise

```bash
cat >docker-compose.yml<<EOL
version: "3"

services:
  ubuntu:
    image: ubuntu:18.04
    stdin_open: true 
    volumes:
     - data:/opt

  alpine:
    image: alpine:3.13
    container_name: alpine
    stdin_open: true 
    volumes:
     - data:/tmp

volumes:
  data:
EOL
```

3. What does container_name line accomplish under the second container’s description (alpine) and why are we not using it in the first one (ubuntu)?

container_name, as the name of the parameter indicates, is the container's name for the service "alpine", service and container have the same name.
Unless you need to use docker to manage a container that Compose started, you usually don’t need to set this either

4. Create a random file inside the ubuntu container using docker-compose exec command and save it in /opt/hello.txt

```bash
docker-compose exec ubuntu touch /opt/hello.txt
# Other option would be:
docker-compose exec ubuntu touch random.file
docker-compose exec ubuntu mv random.file /opt/hello.txt
```

5. Explain behind the scenes behavior of the above step

"docker-compose exec" let us execute commands inside services. If we want to make it in interactive way we can execute "docker-compose exec <service> bash"

> Docker Security: 
> https://www.practical-devsecops.com/lesson-1-understand-docker-from-a-security-perspective/
> https://www.practical-devsecops.com/lesson-2-docker-images-docker-layers-and-registry/
> https://www.practical-devsecops.com/lesson-3-container-reconnaissance-techniques-for-beginners/
