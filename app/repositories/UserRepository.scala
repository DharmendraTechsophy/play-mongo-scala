package repositories

import models.User
import org.joda.time.DateTime
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserRepository @Inject()(
                                      implicit executionContext: ExecutionContext,
                                      reactiveMongoApi: ReactiveMongoApi
                                    ) {
  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("user"))

  def findAll(limit: Int = 100): Future[Seq[User]] = {
    collection.flatMap(
      _.find(BSONDocument(), Option.empty[User])
        .cursor[User](ReadPreference.Primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[User]]())
    )
  }

  def search(email :String,pass:String):Future[Option[User]]={
    collection.flatMap(_.find(BSONDocument("email" -> email,"password"->pass), Option.empty[User]).one[User])
  }

  def findOne(id: Int): Future[Option[User]] = {
    collection.flatMap(_.find(BSONDocument("id" -> id), Option.empty[User]).one[User])
  }

  def create(user:User): Future[WriteResult] = {
    collection.flatMap(_.insert(ordered = false)
      .one(user))
  }

  def update(user: User):Future[WriteResult] = {
    collection.flatMap(
      _.update(ordered = false).one(BSONDocument("id" -> user.id),
        user)
    )
  }

  def delete(id: Int):Future[WriteResult] = {
    collection.flatMap(
      _.delete().one(BSONDocument("id" -> id), Some(1))
    )
  }

}

