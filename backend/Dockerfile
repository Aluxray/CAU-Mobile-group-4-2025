FROM node:20-alpine

WORKDIR /usr/src/app/backend

COPY backend/package*.json ./
RUN npm install

COPY backend/ .

EXPOSE 3000

CMD ["npm", "run", "start:dev"]
