FROM node:18 AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration=production

FROM node:18-alpine
WORKDIR /app
COPY --from=build-stage /app/dist/frontend/browser /app
RUN npm install -g http-server
CMD ["http-server", "-p", "4444", "-a", "0.0.0.0"]
