FROM gradle:8.4-jdk17-jammy AS builder
WORKDIR /app
COPY . .
RUN gradle dependencies
RUN gradle build -x test

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/wildfly /app/wildfly
COPY --from=builder /app/app/build/libs/app.war /app/wildfly/standalone/deployments/api.war
ENTRYPOINT ["/app/wildfly/bin/standalone.sh"]
