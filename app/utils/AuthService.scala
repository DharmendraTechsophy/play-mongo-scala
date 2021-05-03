package utils

import java.time.Clock

import models.UserInfo
import org.slf4j.LoggerFactory
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import play.api.libs.json.Json

import scala.util.control.NonFatal
import scala.util.{Failure, Success}


class AuthService {

  val logger = LoggerFactory.getLogger(this.getClass)

  //import JsonFormat._

  private val expiresIn = 1 * 24 * 60 * 60
  implicit val clock: Clock = Clock.systemUTC

  // change me
  private val secretKey = "dharmendra-token-key"

  def encodeToken(user: UserInfo): String = {
    val userJson = Json.toJson(user)
    val claim =
      JwtClaim(Json.prettyPrint(userJson))
        .issuedNow
        .expiresIn(expiresIn)
    Jwt.encode(claim, secretKey, JwtAlgorithm.HS256)
  }

  def validateToken(jwtToken: String): Either[String, UserInfo] = {
    Jwt.decode(jwtToken, secretKey, Seq(JwtAlgorithm.HS256)) match {
      case Success(value) =>
        Right(Json.parse(value.content).as[UserInfo])
      case Failure(ex) =>
        logger.warn("Parsing Error: ", ex)
        Left(ex.getMessage)
    }
  }

}
