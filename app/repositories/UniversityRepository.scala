package repositories

import models.University
import org.joda.time.DateTime
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UniversityRepository @Inject()(
                                 implicit executionContext: ExecutionContext,
                                 reactiveMongoApi: ReactiveMongoApi
                               ) {
  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("university"))

  def findAll(limit: Int = 100): Future[Seq[University]] = {
    collection.flatMap(
      _.find(BSONDocument(), Option.empty[University])
        .cursor[University](ReadPreference.Primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[University]]())
    )
  }

  def findOne(id: Int): Future[Option[University]] = {
    collection.flatMap(_.find(BSONDocument("id" -> id), Option.empty[University]).one[University])
  }


  def findByName(name: String,limit:Int = 100): Future[Seq[University]] = {
    collection.flatMap(
      _.find(BSONDocument("name"->name), Option.empty[University])
        .cursor[University](ReadPreference.Primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[University]]())
    )
  }

  def create(university:University): Future[WriteResult] = {
    collection.flatMap(_.insert(ordered = false)
      .one(university))
  }

  def update(university: University):Future[WriteResult] = {
    collection.flatMap(
      _.update(ordered = false).one(BSONDocument("id" -> university.id),
        university)
    )
  }

  def delete(id: Int):Future[WriteResult] = {
    collection.flatMap(
      _.delete().one(BSONDocument("id" -> id), Some(1))
    )
  }

}

