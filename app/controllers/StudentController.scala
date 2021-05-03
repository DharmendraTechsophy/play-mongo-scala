package controllers

import models.{Student, StudentInfo}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import repositories.StudentRepository
import utils.SecureAction

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class StudentController @Inject()(
                                 val studentRepository: StudentRepository,
                                 val controllerComponents: ControllerComponents)(implicit executionContext: ExecutionContext)
  extends BaseController {


  def findAll():Action[AnyContent] = {
    Action.async {
      studentRepository.findAll().map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }

  def findByName(name: String):Action[AnyContent] = {
    Action.async {
      studentRepository.findByName(name).map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }

  def findOne(studentId: Int): Action[AnyContent] = {
    Action.async { _ =>
      studentRepository.findOne(studentId).map { studentOpt =>
        studentOpt.fold(Ok(Json.toJson("{}")))(student => Ok(
          Json.toJson(student))).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }

//  def createByIdByUser: Action[JsValue] = {
//    println("Requesting for Adding the Record......")
//    Action.async(parse.json) {
//      request =>
//        println("request is : "+request.body)
//        request.body.validate[StudentInfo].fold(
//          error => Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
//          { s =>
//            val student = Student(s._id,s.name,s.email,s.universityId,request.user.id.get,s.id)
//            studentRepository.create(student).map { createdStudentId =>
//              Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*")
//            }
//          })
//
//    }
//  }


  def create: Action[JsValue] = {
    println("Requesting for Adding the Record......")
    Action.async(parse.json) {
      request =>
        println("request is : "+request.body)
        request.body.validate[Student].fold(
          error => Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
          {
            student => studentRepository.create(student).map {
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
      request.body.validate[Student].fold(
        error =>
          Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
        { student =>
          studentRepository.update(student).map { _ => Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*") }
        }
      )
    }
  }


  def delete(studentId: Int): Action[AnyContent] = {
    Action.async { _ =>
      studentRepository.delete(studentId).map { _ =>
        Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }




}