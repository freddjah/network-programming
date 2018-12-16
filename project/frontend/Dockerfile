FROM nginx:1.13-alpine

# Create config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy code
RUN mkdir -p /usr/share/nginx/html
COPY ./build /usr/share/nginx/html
