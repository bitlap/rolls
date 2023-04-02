val pluginVersion = "0.1.3-SNAPSHOT"

ThisBuild / resolvers ++= Seq(
  Resolver.mavenLocal,
  "Sonatype OSS Snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots",
  "Sonatype OSS Releases" at "https://s01.oss.sonatype.org/content/repositories/releases"
)
ThisBuild / version := pluginVersion
inThisBuild(
  List(
    organization           := "org.bitlap",
    sonatypeCredentialHost := "s01.oss.sonatype.org",
    sonatypeRepository     := "https://s01.oss.sonatype.org/service/local",
    homepage               := Some(url("https://github.com/bitlap/bitlap")),
    licenses               := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        id = "dreamylost",
        name = "梦境迷离",
        email = "dreamylost@outlook.com",
        url = url("https://blog.dreamylost.cn")
      )
    )
    // publish
    // credentials += Credentials(Path.userHome / ".ivy2" / ".bitlap_sonatype_credentials")
  )
)

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheckAll")

lazy val scala3Version  = "3.2.2"
lazy val jacksonVersion = "2.14.1"

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
//      "-Xshow-phases"
    )
  )

lazy val `rolls` = (project in file("."))
  .aggregate(
    `rolls-compiler-plugin`,
    `rolls-core`,
    `rolls-server`,
    `rolls-tests`
  )
  .settings(
    publish / skip := true,
    commonSettings
  )

lazy val `rolls-core` = (project in file("rolls-core"))
  .settings(
    commonSettings,
    name := "rolls-core",
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module"  %% "jackson-module-scala"       % jacksonVersion,
      "com.github.pjfanning"          %% "jackson-module-scala3-enum" % jacksonVersion,
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310"    % jacksonVersion
    )
  )

lazy val `rolls-server` = (project in file("rolls-server"))
  .settings(
    commonSettings,
    publish / skip := true,
    name           := "rolls-server",
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.6.0",
      "com.typesafe"   % "config"     % "1.4.2"
    )
  )
  .dependsOn(`rolls-compiler-plugin`, `rolls-core`)

lazy val `rolls-compiler-plugin` = (project in file("rolls-compiler-plugin"))
  .settings(
    commonSettings,
    name := "rolls-compiler-plugin",
    libraryDependencies ++= List(
      "org.scala-lang" %% "scala3-compiler" % scala3Version
    )
  )

lazy val `rolls-tests` = (project in file("rolls-tests"))
  .settings(
    commonSettings,
    publish / skip := true,
    name           := "rolls-tests",
    scalacOptions ++= Seq(
//      "-P:RollsCompilerPlugin:/Users/liguobin/Projects/rolls/config.properties",
      "-Xprint:parser,typer,posttyper,erasure"
    ),
    autoCompilerPlugins := true,
    addCompilerPlugin("org.bitlap" %% "rolls-compiler-plugin" % pluginVersion),
    libraryDependencies ++= Seq(
      "org.scalatest"  %% "scalatest"  % "3.2.15" % Test,
      "org.scalacheck" %% "scalacheck" % "1.17.0" % Test
    )
  )
  .dependsOn(`rolls-core`)
