# Usa imagem com Java 21 e Maven
FROM eclipse-temurin:21-jdk

# Define diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Dá permissão ao mvnw
RUN chmod +x mvnw

# Instala dependências e compila sem testes
RUN ./mvnw clean package -DskipTests

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
