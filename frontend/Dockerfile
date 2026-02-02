# Build Stage
FROM node:18 as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Production Stage
FROM nginx:alpine
# 1. Copy the React Build
COPY --from=build /app/build /usr/share/nginx/html

# 2. COPY THE CUSTOM NGINX CONFIG
# This replaces the default configuration with yours
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]