import cats.effect.{ExitCode, IO, IOApp}
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._

import scala.concurrent.duration._

object Main  extends IOApp {
  private val sleepyService = HttpRoutes.of[IO] {
    case GET -> Root / "oversleep" =>
      IO.sleep(16.hours) *> Ok("200 OK")
    case GET -> Root / "sleep" =>
      IO.sleep(8.hours) *> Ok("200 OK")
    case GET -> Root / "doze" =>
      IO.sleep(3.hours) *> Ok("200 OK")
    case GET -> Root / "nap" =>
      IO.sleep(40.minutes) *> Ok("200 OK")
    case GET -> Root / "repose" =>
      IO.sleep(15.minutes) *> Ok("200 OK")
    case GET -> Root / "blink" =>
      IO.sleep(3.seconds) *> Ok("200 OK")
  }

  private val httpApp = sleepyService.orNotFound

  override def run(args: List[String]): IO[ExitCode] = {
    org.http4s.blaze.server.BlazeServerBuilder [IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp)
      .withIdleTimeout(16.hours)
      .withResponseHeaderTimeout(16.hours)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
