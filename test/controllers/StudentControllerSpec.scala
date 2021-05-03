package controllers

import models.{Student, University}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.i18n.{DefaultLangs, MessagesApi}
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication, _}
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import repositories.StudentRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success


class StudentControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest {

  implicit val mockedRepo: StudentRepository = mock[StudentRepository]

  "StudentController " should {

    "create a student" in new WithStudentApplication() {
      val id: BSONObjectID = BSONObjectID.generate
      val student = Student(Some(id),"abhi", "abhi@gmail.com", 2,1,Some(1))
      when(mockedRepo.create(student)) thenReturn Future.successful(1)
      val result = studentController.create().apply(FakeRequest().withBody(Json.toJson(student)))
      val resultAsString = contentAsString(result)
      resultAsString mustBe """{}"""
    }

    "update a student" in new WithStudentApplication() {
      val updatedStudent = Student(Some(BSONObjectID.generate),"abhi","abhik@gmail.com",2,1,Some(1))
      when(mockedRepo.update(updatedStudent)) thenReturn Future.successful(1)
      val result = studentController.update().apply(FakeRequest().withBody(Json.toJson(updatedStudent)))
      val resultAsString = contentAsString(result)
      resultAsString mustBe """{}"""
    }

    "get a student" in new WithStudentApplication() {
      val student = Student(Some(BSONObjectID.generate),"abhi","abhik@gmail.com",2,1,Some(1))
      when(mockedRepo.findOne(1)) thenReturn Future.successful(Some(student))
      val result = studentController.findOne(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """{"name":"abhi","email":"abhi@outlook.com","UID":3,"id":1}"""
    }

    "delete a student" in new WithStudentApplication() {
      when(mockedRepo.delete(1)) thenReturn Future.successful(1)
      val result = studentController.delete(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """"{}""""
    }

    "get all list" in new WithStudentApplication() {
      val student = Student(Some(BSONObjectID.generate),"abhi","abhik@gmail.com",2,1,Some(1))
      when(mockedRepo.findAll()) thenReturn Future.successful(List(student))
      val result = studentController.findAll().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """[{"name":"abhi","email":"abhi@outlook.com","UID":3,"id":1}]"""
    }

  }

}

class WithStudentApplication(implicit mockedRepo: StudentRepository) extends WithApplication with Injecting {

  implicit val ec = inject[ExecutionContext]

  val messagesApi = inject[MessagesApi]

  val studentController: StudentController = new StudentController(mockedRepo, stubControllerComponents())

}
