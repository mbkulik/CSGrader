group = "edu.unh.cs"
version = "0.2"

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
  api("org.junit.jupiter:junit-jupiter:5.13.4")
  implementation("com.google.code.gson:gson:2.13.2")
  implementation("org.junit.platform:junit-platform-launcher")
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
