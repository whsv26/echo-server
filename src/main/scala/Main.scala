import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.middleware.Logger
import org.http4s.{HttpApp, HttpRoutes}

object Main extends IOApp {
  val dsl = new Http4sDsl[IO] { }
  import dsl._

  val route: HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root / "ping" => Ok("pong! version 1.0.0")
    }

  override def run(args: List[String]): IO[ExitCode] = {
    val httpApp: HttpApp[IO] = Logger.httpApp(
      logHeaders = false,
      logBody = false
    )(route.orNotFound)

    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}