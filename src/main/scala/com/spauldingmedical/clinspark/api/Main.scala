package com.spauldingmedical.clinspark.api

import cats.effect._
import cats.implicits._
import io.circe.syntax._
import org.http4s.syntax._
import java.io.File
import org.http4s.server.blaze._
import org.http4s.server.Router
import org.http4s._, org.http4s.dsl.io._, org.http4s.implicits._
import io.circe.generic.auto._
import org.http4s.circe._
import scala.concurrent.ExecutionContext.Implicits.global
import org.http4s.HttpRoutes
object Main extends IOApp {

val clinspark = HttpRoutes.of[IO] {
  case GET -> Root / "sponsors" =>
    Ok(sponsors.asJson)
}.orNotFound
val sponsors = (new File("/home/ebigram/dev/data/sponsors")).listFiles.filter(_.isDirectory).map(_.toString).map(_.split("/").last)

def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(clinspark)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
