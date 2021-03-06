ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "scala-tips",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0",
    libraryDependencies += "org.typelevel" %% "spire" % "0.18.0-M3",
    libraryDependencies += "org.typelevel" %% "spire-extras" % "0.18.0-M3"
  )
