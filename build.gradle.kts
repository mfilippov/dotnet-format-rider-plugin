plugins {
    id("org.jetbrains.intellij") version "1.11.0"
    id("me.filippov.gradle.jvm.wrapper") version "0.14.0"
    kotlin("jvm") version "1.7.10"
}

repositories {
    mavenCentral()
    maven("https://cache-redirector.jetbrains.com/repo1.maven.org/maven2")
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
}

intellij {
    type.set("RD")
    version.set("2022.3.1")
}

tasks {
    wrapper {
        gradleVersion = "7.6"
        distributionType = Wrapper.DistributionType.ALL
        distributionUrl =
            "https://cache-redirector.jetbrains.com/services.gradle.org/distributions/gradle-${gradleVersion}-all.zip"
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
            allWarningsAsErrors = true
        }
    }

    runIde {
        jvmArgs("-Xmx2048m")
    }

    patchPluginXml {
        sinceBuild.set("223")
    }
}
