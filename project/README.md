# Chat

## Services

* [Frontend](frontend)
* [Backend](backend)

## To build

```bash
cd build
./build-images.sh
```

The images is build with the names `chat-frontend` and `chat-backend`. Both get tagged with current GIT revision name and a latest tag.

## To run built images
```bash
docker-compose up -d
```
