# We need node 16 and up to run the frontend (for DaisyUI)
FROM node:19-bullseye
WORKDIR /app
COPY . .
RUN npm install --legacy-peer-deps
EXPOSE 5173
CMD [ "npm", "run", "dev"]