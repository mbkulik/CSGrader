group = "edu.unh.cs"
version = "0.3"

repositories {
  mavenCentral()
}

plugins {
  `java-library`
  `maven-publish`
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

tasks.withType<JavaCompile> {
  options.compilerArgs.add("-Xlint:all")
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])
    }
  }

  repositories {
    maven {
      name = "Local"
      url = uri(layout.projectDirectory.dir("docs/maven"))
    }
  }
}

dependencies {
  implementation("org.junit.jupiter:junit-jupiter-api:6.0.3")
  implementation("com.google.code.gson:gson:2.14.0")
  implementation("org.junit.platform:junit-platform-launcher")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:6.0.3")
}

sourceSets {
  main {
    java {
      setSrcDirs(listOf("src/main"))
    }
  }
  test {
    java {
      setSrcDirs(listOf("src/test"))
    }
  }
}
