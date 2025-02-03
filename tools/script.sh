#!/bin/sh
./gradlew bootRun --args="--server.address=0.0.0.0 --server.port=$ENDPOINT_PORT"