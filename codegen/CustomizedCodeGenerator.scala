package codegen

import slick.jdbc.PostgresProfile
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import Config._

object CustomizedCodeGenerator {
  def main(args: Array[String]): Unit = {
    Await.ready(
      codegen.map(_.writeToFile(
        "slick.jdbc.PostgresProfile",
        args(0),
        "models",
        "Tables",
        "Tables.scala"
      )),
      20.seconds
    )
  }

  val db = PostgresProfile
    .api
    .Database
    .forURL(url, driver=jdbcDriver, user=user, password=password)

  val codegen = db.run {
    PostgresProfile.defaultTables
        .flatMap(PostgresProfile
          .createModelBuilder(_,false).buildModel)
  } map { model =>
    new slick.codegen.SourceCodeGenerator(model){
      val importCirce =
        "import io.circe.Encoder\nimport io.circe.generic.semiauto._"

      val implicits = model.tables.map(t => {
        val name = entityName(t.name.table)
        s"implicit val ${name}Encoder: Encoder[${name}] = deriveEncoder[${name}]\n"
      }).mkString("\n")

      override def code: String =
          super.code + "\n" + importCirce + "\n\n" + implicits
    }
  }
}