package models
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

// case class User(firstName:String,lastName:String,userName:String,email:String,password:String,createdDate:String,id:Option[Int]=None)

case class User(
                    _id:Option[BSONObjectID],
                    firstName:String,
                    lastName:String,
                    userName:String,
                    email:String,
                    password:String,
                    createdDate:String,
                    id: Option[Int]=None
                  )
object User{
  implicit val fmt : Format[User] = Json.format[User]
  implicit object UserBSONReader extends BSONDocumentReader[User] {
    def read(doc: BSONDocument): User = {
      User(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("firstName").get,
        doc.getAs[String]("lastName").get,
        doc.getAs[String]("userName").get,
        doc.getAs[String]("email").get,
        doc.getAs[String]("password").get,
        doc.getAs[String]("createdDate").get,
        doc.getAs[Int]("id")
      )
    }
  }

  implicit object UserBSONWriter extends BSONDocumentWriter[User] {
    def write(user: User): BSONDocument = {
      BSONDocument(
        "_id" -> user._id,
        "firstName"->user.firstName,
        "lastName"->user.lastName,
        "userName"->user.userName,
        "email"->user.email,
        "password"->user.password,
        "createdDate"->user.createdDate,
        "id"->user.id
      )
    }
  }

}