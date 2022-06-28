FROM maven:3.8.5-openjdk-11-slim
WORKDIR /application

COPY . .

RUN mvn clean package

CMD ["./target/bin/taskmanagement_webapp"]