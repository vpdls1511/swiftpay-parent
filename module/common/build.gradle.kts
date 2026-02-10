plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"

  kotlin("plugin.jpa") version "1.9.25"
  kotlin("kapt")

  id("org.springframework.boot") version "3.5.6"
  id("io.spring.dependency-management") version "1.1.7"
}

group = "me.ngyu.swiftpay"
version = "0.0.1-SNAPSHOT"
description = "swiftpay-common"

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

allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
  annotation("jakarta.persistence.Embeddable")
}

dependencies {
  implementation(project(":module:core"))

  // Security + Web
  api("org.springframework.boot:spring-boot-starter-security")
  api("org.springframework.boot:spring-boot-starter-web")

  // JPA + QueryDSL
  api("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")  // 추가
  kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")  // 버전 통일
  kapt("jakarta.persistence:jakarta.persistence-api")

  // Redis
  api("org.springframework.boot:spring-boot-starter-data-redis")
  api("io.lettuce:lettuce-core")

  implementation("io.jsonwebtoken:jjwt-api:0.12.6")
  runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
  runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
