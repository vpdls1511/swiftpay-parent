plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  id("org.springframework.boot") version "3.5.6"
  id("io.spring.dependency-management") version "1.1.7"
}


group = "com.ngyu"
version = "0.0.1-SNAPSHOT"
description = "swiftpay-parent"

allprojects {
  group = "com.ngyu.swiftpay"
  version = "1.0.0"
}

tasks.bootJar {
  enabled = false
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(17)
  }
}

repositories {
  mavenCentral()
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

subprojects {
  configurations {
    all {
      exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
