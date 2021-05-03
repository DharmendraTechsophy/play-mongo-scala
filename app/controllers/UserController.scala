package controllers

import models.{User, UserInfo}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import repositories.UserRepository
import utils.AuthService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class UserController @Inject()(
                                   implicit executionContext: ExecutionContext,
                                   val userRepository: UserRepository,
                                   auth:AuthService,
                                   val controllerComponents: ControllerComponents)
  extends BaseController {


  def search(): Action[JsValue] = {
    Action.async(parse.json) { request =>
      request.body.validate[User].fold(error => {Future.successful(BadRequest(JsError.toJson(error)))},
        {
          user =>
            userRepository.search(user.email,user.password).map {
              case Some(user) =>
                Ok(Json.toJson(Map("token"-> auth.encodeToken(UserInfo(user._id,user.firstName,user.lastName,user.email,user.id))))).withHeaders("Access-Control-Allow-Origin" -> "*")
              case None =>
                BadRequest("Email doesn't Exist").withHeaders("Access-Control-Allow-Origin" -> "*")
            }
        })
    }
  }

  def findAll():Action[AnyContent] = {
    Action.async {
      userRepository.findAll().map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }

  def findOne(userId: Int): Action[AnyContent] = {
    Action.async { _ =>
      userRepository.findOne(userId).map { userOpt =>
        userOpt.fold(Ok(Json.toJson("{}")))(user => Ok(
          Json.toJson(user))).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }


  def create: Action[JsValue] = {
    println("Requesting for Adding the Record......")
    Action.async(parse.json) {
      request =>
        println("request is : "+request.body)
        request.body.validate[User].fold(
          error => Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
          {
            user => userRepository.create(user).map {
              _ =>Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*")
            }
          }
        )
    }

  }

  def update: Action[JsValue] = {
    println("Requesting for Updating the Record......")
    Action.async(parse.json) { request =>
      println("request is : "+request.body)
      request.body.validate[User].fold(
        error =>
          Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
        { user=>
          userRepository.update(user).map { _ => Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*") }
        }
      )
    }
  }


  def delete(userId: Int): Action[AnyContent] = {
    Action.async { _ =>
      userRepository.delete(userId).map { _ =>
        Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }




}