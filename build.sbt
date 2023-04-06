ThisBuild / resolvers ++= Seq(
  Resolver.mavenLocal,
  "Sonatype OSS Snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots",
  "Sonatype OSS Releases" at "https://s01.oss.sonatype.org/content/repositories/releases"
)

lazy val exampleVersion = "0.1.0"

lazy val scala3Version     = "3.2.2"
lazy val jacksonVersion    = "2.14.1"
lazy val scalatestVersion  = "3.2.15"
lazy val scalacheckVersion = "1.17.0"
lazy val munitVersion      = "0.7.29"
lazy val configVersion     = "1.4.2"
lazy val postgresqlVersion = "42.6.0"

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
    `rolls-plugin-server`,
    `rolls-csv`,
    `rolls-tests`
  )
  .settings(
    publish / skip := true,
    commonSettings,
    commands ++= Commands.value
  )

lazy val `rolls-csv` = (project in file("rolls-csv"))
  .settings(
    commonSettings,
    name := "rolls-csv",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % munitVersion % Test
    )
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

lazy val `rolls-plugin-server` = (project in file("rolls-plugin-server"))
  .settings(
    commonSettings,
    publish / skip := true,
    name           := "rolls-plugin-server",
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % postgresqlVersion,
      "com.typesafe"   % "config"     % configVersion
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
    addCompilerPlugin("org.bitlap" %% "rolls-compiler-plugin" % exampleVersion),
    libraryDependencies ++= Seq(
      "org.scalatest"  %% "scalatest"  % scalatestVersion  % Test,
      "org.scalacheck" %% "scalacheck" % scalacheckVersion % Test
    )
  )
  .dependsOn(`rolls-core`)
