ThisBuild / resolvers ++= Seq(
  Resolver.mavenLocal,
  "Sonatype OSS Snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots",
  "Sonatype OSS Releases" at "https://s01.oss.sonatype.org/content/repositories/releases"
)

lazy val `rolls-test-deps-version` = "0.2.3+17-6cd034ed+20230417-1937-SNAPSHOT"

//ThisBuild / version := `rolls-test-deps-version`

lazy val scala3Version     = "3.2.0"
lazy val jacksonVersion    = "2.14.1"
lazy val scalatestVersion  = "3.2.15"
lazy val scalacheckVersion = "1.17.0"
lazy val munitVersion      = "0.7.29"
lazy val configVersion     = "1.4.2"
lazy val postgresqlVersion = "42.6.0"
lazy val h2Version         = "2.1.214"
lazy val calibanVersion    = "2.1.0"

inThisBuild(
  List(
    scalaVersion           := scala3Version,
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
      "-feature"
//      "-Ydebug"
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
  .dependsOn(`rolls-core`)

lazy val `rolls-core` = (project in file("rolls-core"))
  .settings(
    commonSettings,
    name := "rolls-core",
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module"  %% "jackson-module-scala"       % jacksonVersion,
      "com.github.pjfanning"          %% "jackson-module-scala3-enum" % jacksonVersion,
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310"    % jacksonVersion,
      "org.scalatest"                 %% "scalatest"                  % scalatestVersion % Test,
      "com.h2database"                 % "h2"                         % h2Version        % Test
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

lazy val reader = scala.io.Source.fromFile("config.properties")
lazy val config = {
  val ret = reader.getLines().toList.map(p => s"-P:RollsCompilerPlugin:$p")
  reader.close()
  ret
}

lazy val `rolls-tests` = (project in file("rolls-tests"))
  .settings(
    commonSettings,
    publish / skip := true,
    name           := "rolls-tests",
    scalacOptions ++= config,
    autoCompilerPlugins := true,
    addCompilerPlugin("org.bitlap" %% "rolls-compiler-plugin" % `rolls-test-deps-version`),
    libraryDependencies ++= Seq(
      "org.scalatest"         %% "scalatest"  % scalatestVersion  % Test,
      "org.scalacheck"        %% "scalacheck" % scalacheckVersion % Test,
      "com.github.ghostdogpr" %% "caliban"    % calibanVersion
    )
  )
  .dependsOn(`rolls-core`)

lazy val `rolls-docs` = project
  .in(file("rolls-docs"))
  .dependsOn(`rolls-core`, `rolls-compiler-plugin`, `rolls-csv`, `rolls-tests`)
  .settings(
    scalaVersion   := scala3Version,
    publish / skip := true,
    moduleName     := "rolls-docs"
  )
  .enablePlugins(MdocPlugin, DocusaurusPlugin)
