ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "echo-server",
    version := "1.1.0",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % "0.23.14",
      "org.http4s" %% "http4s-blaze-server" % "0.23.12",
      "ch.qos.logback" % "logback-classic" % "1.2.11",
    ),
    docker / buildOptions := BuildOptions(cache = false),
    docker / imageNames := Seq(ImageName(s"whsv26/${name.value}:${version.value}")),
    docker / dockerfile := {
      val artifact: File = assembly.value
      val artifactTargetPath = s"/app/${artifact.name}"
      new Dockerfile {
        from("openjdk:11-jre")
        add(artifact, artifactTargetPath)
        entryPoint("java", "-jar", artifactTargetPath)
        expose(8080)
      }
    }
  )
  .enablePlugins(DockerPlugin)
