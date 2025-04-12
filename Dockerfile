FROM bellsoft/liberica-openjfx-alpine:21

WORKDIR /app

RUN apk add --no-cache \
    mesa-gl \
    libxtst \
    libxi \
    ttf-dejavu \
    fontconfig

COPY out/artifacts/sgs_jar/sgs.jar /app/app.jar

# Actualiza las variables de entorno para que coincidan con el nombre de la BD
ENV DATABASE_HOST=db \
    DATABASE_PORT=3306 \
    DATABASE_NAME=sgs_visits_db \
    DATABASE_USER=sgs_user \
    DATABASE_PASSWORD=sgs_password \
    DISPLAY=host.docker.internal:0.0

EXPOSE 8080

CMD ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]