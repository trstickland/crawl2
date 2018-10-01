# 
#    creates a docker image to enable travis-ci build under xenial; Crawl2 has no docker dependency
# 

FROM ubuntu:xenial

MAINTAINER ts24@sanger.ac.uk

RUN   apt-get update -qq && \
      apt-get install --yes software-properties-common

# Crawl2 requires downgrade to Java 7
RUN   add-apt-repository ppa:openjdk-r/ppa && \
      apt-get update && \
      apt-get install --yes openjdk-7-jdk

RUN   apt-get install --yes maven
