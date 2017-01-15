#!/bin/bash

if [ $# -eq 0 ]
  then
    docker build -t oydeu/oyd-pia_base .
  else
    docker build --no-cache oydeu/oyd-pia_base .
fi
docker push oydeu/oyd-pia_base
