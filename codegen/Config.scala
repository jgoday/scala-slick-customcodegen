package codegen

object Config {
  val url = "jdbc:postgresql://127.0.0.1:5432/sample"
  val jdbcDriver =  "org.postgresql.Driver"
  val slickProfile = slick.jdbc.PostgresProfile

  val user = "root"
  val password = "root"
}