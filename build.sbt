ThisBuild / resolvers ++= Seq(
  Resolver.mavenLocal,
  "Sonatype OSS Snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Releases" at "https://s01.oss.sonatype.org/content/repositories/releases"
)

inThisBuild(
  List(
    organization           := "org.bitlap",
    sonatypeCredentialHost := "s01.oss.sonatype.org",
    sonatypeRepository :=
      "https://s01.oss.sonatype.org/service/local",
    homepage := Some(url("https://github.com/bitlap/bitlap")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        id = "dreamylost",
        name = "梦境迷离",
        email = "dreamylost@outlook.com",
        url = url("https://blog.dreamylost.cn")
      )
    )
  )
)

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheckAll")

lazy val scala3Version = "3.2.0"

Global / onChangedBuildSource := ReloadOnSourceChanges
lazy val commonSettings =
  Seq(
    scalaVersion                  := scala3Version,
    Global / onChangedBuildSource := ReloadOnSourceChanges,
//    publishConfiguration      := publishConfiguration.value.withOverwrite(true),
//    publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),
    scalacOptions ++= Seq(
      /** "-Ycheck:all",** */
      "-language:dynamics",
      "-explain",
      "unchecked",
      "-deprecation",
      "-feature",
      "-Ydebug"
    )
  )

lazy val `rhs` = (project in file("."))
  .aggregate(
    `rhs-compiler-plugin`,
    `rhs-annotations`,
    `rhs-server`
  )
  .settings(
    publish / skip := true,
    commonSettings
  )

lazy val `rhs-annotations` = (project in file("rhs-annotations"))
  .settings(
    commonSettings,
    name := "rhs-annotations"
  )

lazy val `rhs-server` = (project in file("rhs-server"))
  .settings(
    commonSettings,
    name := "rhs-server",
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.6.0",
      "com.typesafe"   % "config"     % "1.4.2"
    )
  )

lazy val `rhs-compiler-plugin` = (project in file("rhs-compiler-plugin"))
  .settings(
    commonSettings,
    name := "rhs-compiler-plugin",
    libraryDependencies ++= List(
      "org.scala-lang" %% "scala3-compiler" % scala3Version
    )
  )

lazy val `rhs-example` = (project in file("rhs-example"))
  .settings(
    commonSettings,
    publish / skip      := true,
    name                := "rhs-example",
    autoCompilerPlugins := true,
    addCompilerPlugin("org.bitlap" %% "rhs-compiler-plugin" % "0.0.0+6-5032529d+20230320-2154-SNAPSHOT")
  ).dependsOn(`rhs-annotations`)
