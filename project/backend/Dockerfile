FROM node:10.14.2

RUN wget -O /usr/local/bin/dumb-init https://github.com/Yelp/dumb-init/releases/download/v1.2.0/dumb-init_1.2.0_amd64 && \
  chmod +x /usr/local/bin/dumb-init

ENTRYPOINT ["/usr/local/bin/dumb-init", "--"]
WORKDIR /usr/local/node

# Install
COPY yarn.lock package.json ./
COPY . .

EXPOSE 3001
CMD node server.js
