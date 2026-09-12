# DevOps Lab Application

A small Spring Boot application used as the workload for my hands-on DevOps learning environment.

The application itself is intentionally simple. The primary purpose of this repository is to provide a real application artifact that can move through a production-inspired CI/CD workflow involving Maven, Docker, Jenkins, security and quality scanning, a container registry, Kubernetes, and GitOps.

## Project Goals

This repository is used to practice and demonstrate:

* Java application builds with Maven
* Automated testing
* Container image creation
* Immutable artifact promotion
* CI pipelines with Jenkins
* Code quality analysis with SonarQube
* Container scanning with Trivy
* Container registry workflows
* Kubernetes deployments
* GitOps-based continuous delivery with Argo CD

The application will eventually be promoted through:

```text
DEV → UAT → PROD
```

The same built container image should be promoted between environments rather than rebuilt separately for each environment.

## Technology

* Java 21
* Spring Boot
* Maven
* Maven Wrapper
* Spring Web
* Spring Boot Actuator
* Docker

## Application Endpoints

### Version Information

```text
GET /api/version
```

Example response:

```json
{
  "service": "devops-lab-app",
  "version": "0.1.0",
  "environment": "dev"
}
```

The version and environment are supplied through runtime configuration rather than being hardcoded into the application.

### Health

```text
GET /actuator/health
```

Example response:

```json
{
  "status": "UP"
}
```

Spring Boot Actuator also exposes health groups that can later be used for Kubernetes liveness and readiness probes.

## Runtime Configuration

The application supports the following environment variables:

| Variable          | Purpose                      | Default          |
| ----------------- | ---------------------------- | ---------------- |
| `APP_VERSION`     | Application/artifact version | `0.1.0-SNAPSHOT` |
| `APP_ENVIRONMENT` | Runtime environment name     | `local`          |

Example:

```bash
APP_ENVIRONMENT=dev \
APP_VERSION=1.0.0 \
./mvnw spring-boot:run
```

## Build Locally

Run the tests and create the application package:

```bash
./mvnw clean package
```

Run the application:

```bash
./mvnw spring-boot:run
```

Verify it:

```bash
curl http://localhost:8080/api/version
curl http://localhost:8080/actuator/health
```

## Container Image

The project uses a multi-stage Docker build.

The build stage contains the JDK and Maven build environment, while the final runtime image contains only the Java runtime and packaged application.

The application also runs as a non-root Linux user inside the container.

Build the image:

```bash
docker build -t devops-lab-app:0.1.0 .
```

Run it:

```bash
docker run --rm \
  -p 8080:8080 \
  -e APP_ENVIRONMENT=dev \
  -e APP_VERSION=0.1.0 \
  devops-lab-app:0.1.0
```

Verify:

```bash
curl http://localhost:8080/api/version
curl http://localhost:8080/actuator/health
```

## DevOps Architecture

This repository represents the **application source** portion of the larger lab.

The intended delivery flow is:

```text
Developer / GitHub
        |
        v
      Jenkins
        |
        +--> Maven build + tests
        |
        +--> SonarQube
        |
        +--> Docker build
        |
        +--> Trivy scan
        |
        v
Container Registry
        |
        v
GitOps repository updated
        |
        v
      Argo CD
        |
        v
    Kubernetes
```

Jenkins will be responsible for CI.

Argo CD will eventually be responsible for Kubernetes deployment through GitOps. Jenkins should not directly perform `kubectl apply` against the target environments.

## Repository Role

The wider project is intentionally split into multiple repositories:

```text
devops-lab-app
    Application source and CI definition

devops-lab-infra
    Terraform and infrastructure configuration

devops-lab-gitops
    Kubernetes desired state and Argo CD configuration
```

This separation keeps application code, infrastructure, and deployment state independently versioned.

## Current Status

Completed:

* Spring Boot application foundation
* Runtime version/environment configuration
* Actuator health endpoint
* Maven build and test verification
* Multi-stage Docker build
* Non-root runtime container
* Local container verification
* Successful deployment of version `0.1.0` to the local Terraform + Floci + EKS/k3s proof of concept

Planned:

* Jenkins CI pipeline
* SonarQube integration
* Trivy scanning
* DEV/UAT/PROD artifact promotion
* GitOps repository
* Argo CD deployment
* Kubernetes health probes
* Monitoring and alerting
