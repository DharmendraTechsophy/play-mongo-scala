package repositories

import models.Student
import models.Student.StudentBSONReader
import org.joda.time.DateTime
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class StudentRepository @Inject()(
                                 implicit executionContext: ExecutionContext,
                                 reactiveMongoApi: ReactiveMongoApi
                               ) {
  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("student"))

  def findAll(limit: Int = 100): Future[Seq[Student]] = {
    collection.flatMap(
      _.find(BSONDocument(), Option.empty[Student])
        .cursor[Student](ReadPreference.Primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[Student]]())
    )
  }

  def findOne(id: Int): Future[Option[Student]] = {
    collection.flatMap(_.find(BSONDocument("id" -> id), Option.empty[Student]).one[Student])
  }

  def findByName(name: String,limit:Int = 100): Future[Seq[Student]] = {
    collection.flatMap(
      _.find(BSONDocument("name"->name), Option.empty[Student])
        .cursor[Student](ReadPreference.Primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[Student]]())
    )
  }



  def create(student: Student): Future[WriteResult] = {
    collection.flatMap(_.insert(ordered = false)
      .one(student))
  }


  def update(student: Student):Future[WriteResult] = {
    collection.flatMap(
      _.update(ordered = false).one(BSONDocument("id" -> student.id),
        student)
    )
  }


  def delete(id: Int):Future[WriteResult] = {
    collection.flatMap(
      _.delete().one(BSONDocument("id" -> id), Some(1))
    )
  }


  //db.student.aggregate(
  // [
  //  {$lookup:{from:"university",localField:"universityId",foreignField:"id", as:"universityInfo"}}
  // ]).pretty()

//  def getStudentNameWithUniversityName():Future[List[(String,String)]] = {
//    collection.flatMap(_.
//
//    )
//
//  }

}

