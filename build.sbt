ThisBuild / resolvers ++= Seq(
  Resolver.mavenLocal,
  "Sonatype OSS Snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Releases" at "https://s01.oss.sonatype.org/content/repositories/releases"
)

inThisBuild(
  List(
    version                := "0.0.1-SNAPSHOT",
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
      "-release:11",
      "unchecked",
      "-deprecation",
      "-feature",
      "-Ydebug"
    )
  )

lazy val `id-mapping` = (project in file("."))
  .aggregate(
    `id-mapping-compiler-plugin`,
    `id-mapping-annotations`,
    `id-mapping-server`,
    `id-mapping-compiler-plugin-example`
  )
  .settings(
    publish / skip := true,
    commonSettings
  )

lazy val `id-mapping-annotations` = (project in file("id-mapping-annotations"))
  .settings(
    commonSettings,
    name := "id-mapping-annotations"
  )

lazy val `id-mapping-server` = (project in file("id-mapping-server"))
  .settings(
    commonSettings,
    name := "id-mapping-server",
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.6.0",
      "com.typesafe"   % "config"     % "1.4.2"
    )
  )

lazy val `id-mapping-compiler-plugin` = (project in file("id-mapping-compiler-plugin"))
  .settings(
    commonSettings,
    name := "id-mapping-compiler-plugin",
    libraryDependencies ++= List(
      "org.scala-lang" %% "scala3-compiler" % scala3Version
    )
  )

lazy val `id-mapping-compiler-plugin-example` = (project in file("id-mapping-compiler-plugin-example"))
  .settings(
    scalaVersion        := scala3Version,
    publish / skip      := true,
    name                := "id-mapping-compiler-plugin-example",
    autoCompilerPlugins := true,
    libraryDependencies ++= List(
      "org.bitlap" %% "id-mapping-annotations" % "0.0.1-SNAPSHOT"
    ),
    addCompilerPlugin("org.bitlap" %% "id-mapping-compiler-plugin" % "0.0.1-SNAPSHOT")
  )
