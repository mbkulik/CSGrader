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

group = "edu.unh.cs"
version = "0.1"

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
  implementation("com.google.code.gson:gson:2.13.1")
  implementation("org.junit.jupiter:junit-jupiter-api:5.13.4")
  implementation("org.junit.platform:junit-platform-launcher:1.13.4")
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
