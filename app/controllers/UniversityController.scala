package controllers

import models.University
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import repositories.UniversityRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class UniversityController @Inject()(
                                   implicit executionContext: ExecutionContext,
                                   val universityRepository: UniversityRepository,
                                   val controllerComponents: ControllerComponents)
  extends BaseController {


  def findAll():Action[AnyContent] = {
    Action.async {
      universityRepository.findAll().map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }

  def findOne(studentId: Int): Action[AnyContent] = {
    Action.async { _ =>
      universityRepository.findOne(studentId).map { universityOpt =>
        universityOpt.fold(Ok(Json.toJson("{}")))(user => Ok(
          Json.toJson(user))).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }

  def findByName(name: String):Action[AnyContent] = {
    Action.async {
      universityRepository.findByName(name).map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }


  def create: Action[JsValue] = {
    println("Requesting for Adding the Record......")
    Action.async(parse.json) {
      request =>
        println("request is : "+request.body)
        request.body.validate[University].fold(
          error => Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
          {
            university => universityRepository.create(university).map {
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
      request.body.validate[University].fold(
        error =>
          Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
        { university=>
          universityRepository.update(university).map { _ => Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*") }
        }
      )
    }
  }


  def delete(studentId: Int): Action[AnyContent] = {
    Action.async { _ =>
      universityRepository.delete(studentId).map { _ =>
        Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }




}