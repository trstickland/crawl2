# Crawl 2
A data mining tool for the purpose of querying a sequence and annotation data source.

[![Build Status](https://travis-ci.org/sanger-pathogens/crawl2.svg?branch=master)](https://travis-ci.org/sanger-pathogens/crawl2)  
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-brightgreen.svg)](https://github.com/sanger-pathogens/crawl2/blob/master/LICENSE)

## Content
  * [Introduction](#introduction)
  * [Installation](#installation)
    * [The Essential build](#the-essential-build)
    * [Running the tests](#running-the-tests)
  * [Usage](#usage)
    * [CHADO](#chado)
    * [Indexed GFF3](#indexed-gff3)
  * [License](#license)
  * [Feedback/Issues](#feedbackissues)

## Introduction
CRAWL2 is a Java based version of CRAWL, a data mining tool for the purpose of querying a sequence and annotation data source. Crawl provides a standardised API, in the form of web services, for displaying genomic data. It can work directly off a Chado database, or instead off an ElasticSearch cluster, which uses Lucene as a back end, and can be indexed off a combination of data sources. More detailed documentation can be found in [doc/index.html](https://github.com/sanger-pathogens/crawl2/blob/master/doc/index.html).

## Installation
Please see [doc/setup.html](https://github.com/sanger-pathogens/crawl2/blob/master/doc/setup.html) for more detailed information.

Please note that all the example data used here are subject to the WSTI's data policies, see http://www.sanger.ac.uk/legal/

The setup is done using Gradle. You don't need to install it because there is an executable provided in the root folder of the checkout ('gradlew').

If you need to be able to run or have users run the crawl.jar somewhere outside the project checkout, then you can run:

```
./gradlew install -Pdir=/usr/local/crawl
```

The specified folder will be populated with a bin and lib folder.

Please note that property files will not be copied over. If you're only ever going to run things as yourself, this step might is entirely superfluous.

### The Essential build

The initial build setup might take a while because it will download dependencies, but you should only ever have to run it once. After that any updates to dependencies should be incremental. To setup the build, run: 

```
./gradlew build
```

This can take 5-10 minutes depending on your internet connection.

The build step will download and install dependencies. If you are behind a proxy, you may have to initially supply proxyHost and proxyPort Java settings, e.g.:

```
./gradlew build -Dhttp.proxyHost=wwwcache.sanger.ac.uk -Dhttp.proxyPort=3128 
```

This will perform all the build steps, including dependency download and running the test harness.

### Running the tests

Crawl2 is unit tested everytime it's built.

To only test a singe case:

```
./gradlew -Dtest.single=ClientTest clean test
```

To not run the tests:

```
./gradlew build -x test
```

## Usage
For more detailed usage, please see [doc/index.html](https://github.com/sanger-pathogens/crawl2/blob/master/doc/index.html).

### CHADO

To run crawl2 off the GeneDB public Chado database:

```
./gradlew -Pconfig=resource-chado-public.properties jettyRunWar
```

Then go to http://localhost:8080/services/index.html.

### Indexed GFF3

To index a GFF file and run crawl2 off this, try (assumes you're in bash):

```
./crawl gff2es -pe resource-elasticsearch-local.properties \
    -g  src/test/resources/data/Pf3D7_01.gff.gz \
    -o '{
    "ID":27,
    "common_name":"Pfalciparum",
    "genus":"Plasmodium",
    "species":"falciparum",
    "translation_table":11,
    "taxonID":5833
}'
```
Then:
```
./gradlew -Pconfig=resource-elasticsearch-local.properties jettyRunWar
```
and goto http://localhost:8080/services/index.html.

## License
Crawl2 is free software, licensed under [GPLv3](https://github.com/sanger-pathogens/crawl2/blob/master/LICENSE).

## Feedback/Issues
Please report any issues to the [issues page](https://github.com/sanger-pathogens/crawl2/issues) or email path-help@sanger.ac.uk