# ===== Stage 1: build =====
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build
COPY . .
RUN mvn -B -DskipTests clean install


# ===== Stage 2: Karaf =====
FROM apache/karaf:4.4.4

# Copy local maven repo vào container
COPY --from=builder /root/.m2 /root/.m2

# Copy feature
COPY --from=builder \
  /build/microservice-a/target/feature/feature.xml \
  /opt/apache-karaf/deploy/microservice-a-feature.xml


