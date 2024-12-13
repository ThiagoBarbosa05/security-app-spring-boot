# Use uma imagem base do JDK 17 para compilar e executar o aplicativo
FROM eclipse-temurin:17-jdk AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo pom.xml e outros arquivos necessários para resolver dependências
COPY pom.xml mvnw .
COPY .mvn .mvn




# Baixa as dependências do projeto
RUN ./mvnw dependency:go-offline

# Copia o código-fonte do aplicativo para o container
COPY src src

# Compila o aplicativo usando o Maven
RUN ./mvnw clean package -DskipTests

# Cria uma nova imagem para a execução do aplicativo
FROM eclipse-temurin:17-jre

# Define o diretório de trabalho para o container
WORKDIR /app

# Copia o jar gerado do estágio de build para o container final
COPY --from=build /app/target/*.jar app.jar

# Define a variável de ambiente para os parâmetros do Java (opcional)
ENV JAVA_OPTS="-Xms512m -Xmx512m"

# Expõe a porta padrão do Spring Boot (8080)
EXPOSE 8080

# Define as variáveis de ambiente necessárias para a build
ENV DATABASE_URL=${DATABASE_URL}
ENV GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
ENV GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}
ENV AWS_ACCESS_KEY=${AWS_ACCESS_KEY}
ENV AWS_ACCESS_SECRET_KEY=${AWS_ACCESS_SECRET_KEY}
ENV AWS_S3_BUCKET_NAME=${AWS_S3_BUCKET_NAME}
ENV AWS_REGION=${AWS_REGION}
ENV SMTP_MAIL_USERNAME=${SMTP_MAIL_USERNAME}
ENV SMTP_MAIL_PASSWORD=${SMTP_MAIL_PASSWORD}
ENV BASE_URL=${BASE_URL}

# Define o comando de inicialização do container
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]