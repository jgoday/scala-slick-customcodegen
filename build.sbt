val circeVersion = "0.11.1"
val slickVersion = "3.3.0"
val postgresqlVersion = "42.0.0"

lazy val root = (project in file("."))
  .settings(sharedSettings)
  .settings(slick := slickCodeGenTask.value)
  .settings(sourceGenerators in Compile += slickCodeGenTask.taskValue)
  .dependsOn(codegen)

lazy val codegen = project
  .settings(sharedSettings)
  .settings(libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.3.0")

lazy val sharedSettings = Seq(
  scalaVersion := "2.12.8",
  scalacOptions := Seq("-feature", "-unchecked", "-deprecation"),
  libraryDependencies ++= List(
    "com.typesafe.slick" %% "slick" % slickVersion,
    "org.slf4j" % "slf4j-nop" % "1.7.10",
    "org.postgresql" % "postgresql" % postgresqlVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion
  )
)

lazy val slick = taskKey[Seq[File]]("gen-tables")
lazy val slickCodeGenTask = Def.task {
  val dir = sourceManaged.value
  val cp = (dependencyClasspath in Compile).value
  val r = (runner in Compile).value
  val s = streams.value
  val outputDir = (dir / "slick").getPath
  r.run("codegen.CustomizedCodeGenerator", cp.files, Array(outputDir), s.log)
  val fname = outputDir + "/models/Tables.scala"
  Seq(file(fname))
}

