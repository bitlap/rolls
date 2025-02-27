ThisBuild / resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots",
  "Sonatype OSS Releases" at "https://s01.oss.sonatype.org/content/repositories/releases"
)

lazy val `rolls-test-deps-version` = "0.3.3"

//ThisBuild / version := `rolls-test-deps-version`

lazy val scala3Version     = "3.4.3"
lazy val jacksonVersion    = "2.16.0"
lazy val scalatestVersion  = "3.2.19"
lazy val scalacheckVersion = "1.18.1"
lazy val munitVersion      = "1.0.4"
lazy val h2Version         = "2.3.232"
lazy val calibanVersion    = "2.3.0"
lazy val zioVersion        = "2.1.16"

inThisBuild(
  List(
    scalaVersion           := scala3Version,
    organization           := "org.bitlap",
    sonatypeCredentialHost := "s01.oss.sonatype.org",
    sonatypeRepository     := "https://s01.oss.sonatype.org/service/local",
    homepage               := Some(url("https://github.com/bitlap/rolls")),
    licenses               := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        id = "jxnu-liguobin",
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
    `rolls-csv`,
    `rolls-zio`,
    `rolls-plugin-tests`,
    `rolls-docs`
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

lazy val `rolls-zio` = (project in file("rolls-zio"))
  .settings(
    commonSettings,
    name := "rolls-zio",
    libraryDependencies ++= Seq(
      "dev.zio"       %% "zio"       % zioVersion       % Provided,
      "org.scalatest" %% "scalatest" % scalatestVersion % Test
    )
  )

lazy val `rolls-core` = (project in file("rolls-core"))
  .settings(
    commonSettings,
    name := "rolls-core",
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module"  %% "jackson-module-scala"       % jacksonVersion   % Provided,
      "com.github.pjfanning"          %% "jackson-module-scala3-enum" % jacksonVersion   % Provided,
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310"    % jacksonVersion   % Provided,
      "org.scalatest"                 %% "scalatest"                  % scalatestVersion % Test,
      "com.h2database"                 % "h2"                         % h2Version        % Test
    )
  )

lazy val `rolls-compiler-plugin` = (project in file("rolls-compiler-plugin"))
  .settings(
    commonSettings,
    name := "rolls-compiler-plugin",
    libraryDependencies ++= List(
      "org.scala-lang" %% "scala3-compiler" % scala3Version % Provided
    )
  )

lazy val reader = scala.io.Source.fromFile("config.properties")

lazy val config = {
  val ret = reader.getLines().toList.map(p => s"-P:RollsCompilerPlugin:$p")
  reader.close()
  ret
}

lazy val `rolls-plugin-tests` = (project in file("rolls-plugin-tests"))
  .settings(
    commonSettings,
    publish / skip := true,
    name           := "rolls-plugin-tests",
    scalacOptions ++= config,
    autoCompilerPlugins := true,
    addCompilerPlugin("org.bitlap" %% "rolls-compiler-plugin" % `rolls-test-deps-version`),
    libraryDependencies ++= Seq(
      "org.scalatest"                 %% "scalatest"                  % scalatestVersion  % Test,
      "org.scalacheck"                %% "scalacheck"                 % scalacheckVersion % Test,
      "com.fasterxml.jackson.module"  %% "jackson-module-scala"       % jacksonVersion    % Test,
      "com.github.pjfanning"          %% "jackson-module-scala3-enum" % jacksonVersion    % Test,
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310"    % jacksonVersion    % Test,
      "com.github.ghostdogpr"         %% "caliban"                    % calibanVersion
    )
  )
  .dependsOn(`rolls-core`)

lazy val `rolls-docs` = project
  .in(file("rolls-docs"))
  .dependsOn(`rolls-core`, `rolls-csv`, `rolls-plugin-tests`)
  .settings(
    scalaVersion   := scala3Version,
    publish / skip := true,
    moduleName     := "rolls-docs"
  )
  .enablePlugins(MdocPlugin, DocusaurusPlugin)
