[JAVA_BADGE]: https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white
[SPRING_BADGE]: https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=Spring&logoColor=white
[AWS_BADGE]: https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white
[POSTGRES]: https://img.shields.io/badge/postgresql-4169e1?style=for-the-badge&logo=postgresql&logoColor=white

<h1 align="center" style="font-weight: bold;">Security App Spring Boot 💻</h1>

![AWS][AWS_BADGE]
![spring][SPRING_BADGE]
![java][JAVA_BADGE]
![Postgresql][POSTGRES]

<p align="center">
 <a href="#started">Getting Started</a> • 
  <a href="#routes">API Endpoints</a> •
 <a href="#colab">Collaborators</a> •
 <a href="#contribute">Contribute</a>
</p>

<p align="center">
  <b>Authentication API built with spring boot, where the user can authenticate with email/password or Oauth2 google which returns a jwt access token for authorization on private routes.</b>
</p>

## ⚙️ Features

- password confirmation
- upload profile image
- openid google authentication
- sending email to recover password
- password recovery

<h2 id="started">🚀 Getting started</h2>

Running locally

Clone the project

```bash
  git clone https://github.com/ThiagoBarbosa05/security-app-spring-boot.git
```

Run postgresql database

```bash
  docker compose up -d
```

Enter the project directory

```bash
  cd security-app-spring-boot
```

Start the server

For this step you will need to have [Maven](https://maven.apache.org/download.cgi) and [Java (version 17)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) installed on your machine, or you can open it in your preferred IDE.

```bash
  mvn spring-boot:run
```

<h3>Prerequisites</h3>

Here you list all prerequisites necessary for running your project. For example:

- [Java](https://maven.apache.org/download.cgi)
- [Maven](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)

<h3> Environment Variables</h2>

Use the `application.properties.example` as reference to create your configuration file `application.properties` with your AWS Credentials

```yaml
spring.datasource.url=${DATABASE_URL}
aws.accessKeyId={YOUR_AWS_KEY_ID}
aws.secretKey={YOUR_AWS_SECRET}
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
aws.access.key.id=${AWS_ACCESS_KEY}
aws.access.secret.key=${AWS_ACCESS_SECRET_KEY}
aws.s3.bucket.name=${AWS_S3_BUCKET_NAME}
aws.region=${AWS_REGION}
spring.mail.username=${SMTP_MAIL_USERNAME}
spring.mail.password=${SMTP_MAIL_PASSWORD}
spring.mail.properties.mail.smtp.from=${SMTP_MAIL_USERNAME}
base.url=${BASE_URL}
security.token.secret=${TOKEN_SECRET}
```

## Documentation

After running the api locally, access this url to see the documentation

```bash
http://localhost:8080/swagger-ui/index.html#/
```

[Documentation](http://localhost:8080/swagger-ui/index.html#/)

<table>
  <tr>
    <td align="center">
      <a href="#">
        <img src="https://avatars.githubusercontent.com/u/61393836?v=4" width="100px;" alt="Foto doe Thiago Barbosa"/><br>
        <sub>
          <b>Thiago Barbosa</b>
        </sub>
      </a>
    </td>
  </tr>
</table>

<h2 id="contribute">📫 Contribute</h2>

1. `git clone https://github.com/ThiagoBarbosa05/security-app-spring-boot.git`
2. `git checkout -b feature/NAME`
3. Follow commit patterns
4. Open a Pull Request explaining the problem solved or feature made, if exists, append screenshot of visual modifications and wait for the review!

<h3>Documentations that might help</h3>

[📝 How to create a Pull Request](https://www.atlassian.com/br/git/tutorials/making-a-pull-request)

[💾 Commit pattern](https://gist.github.com/joshbuchea/6f47e86d2510bce28f8e7f42ae84c716)
