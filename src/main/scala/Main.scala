package sample

import io.circe._
import io.circe.syntax._

import models.Tables._

object Main extends App {

  println {
    UsersRow(1, Some("Person")).asJson
  }
}