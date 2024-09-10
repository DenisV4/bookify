# Build
FROM node:20.17-alpine AS build
WORKDIR /app
COPY . /app/
ARG API_URL
RUN sed -i "s|^VITE_API_URL=.*|VITE_API_URL=${API_URL}|g" .env.production
RUN npm install && npm run build

# Serve
FROM nginx:1.27.0-alpine
ARG PROXY_URL
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]
