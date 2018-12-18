#!/bin/bash

git_full_revision=$(git rev-parse HEAD)
git_short_revision=${git_full_revision:0:7}

cd ..

pushd frontend
echo "Building assets..."
yarn build
echo "Building docker image for frontend..."
docker build . --tag chat-frontend:$git_short_revision --tag chat-frontend:latest
popd

pushd backend
echo "Building docker image for backend..."
docker build . --tag chat-backend:$git_short_revision --tag chat-backend:latest
popd

echo "Build was successful"
