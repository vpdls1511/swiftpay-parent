plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  id("org.springframework.boot") version "3.5.6"
  id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ngyu"
version = "0.0.1-SNAPSHOT"
description = "swiftpay-core"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(17)
  }
}

tasks.bootJar { // 실행 파일 안만듦
  enabled = false
}

tasks.jar { // 라이브러리용 파일은 만듦
  enabled = true
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  implementation("org.springframework.boot:spring-boot-starter") {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
  }

  api("org.springframework.boot:spring-boot-starter-log4j2")
  implementation("org.springframework.boot:spring-boot-starter-web")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
