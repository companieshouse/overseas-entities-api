ARG IMAGE_VERSION="latest"
FROM 416670754337.dkr.ecr.eu-west-2.amazonaws.com/ci-corretto-runtime-21:${IMAGE_VERSION} 


WORKDIR /opt
COPY /app .
COPY docker_start.sh .

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
CMD ["./docker_start.sh"]
 