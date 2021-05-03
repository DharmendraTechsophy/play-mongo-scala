package models
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

// case class User(firstName:String,lastName:String,userName:String,email:String,password:String,createdDate:String,id:Option[Int]=None)

case class UserInfo(_id:Option[BSONObjectID],
                 firstName:String,
                    lastName:String,
                 email:String,
                 id: Option[Int]
               )

object UserInfo{
  implicit val fmt : Format[UserInfo] = Json.format[UserInfo]
  implicit object UserInfoBSONReader extends BSONDocumentReader[UserInfo] {
    def read(doc: BSONDocument): UserInfo = {
      UserInfo(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("firstName").get,
        doc.getAs[String]("lastName").get,
        doc.getAs[String]("email").get,
        doc.getAs[Int]("id")
      )
    }
  }

  implicit object UserInfoBSONWriter extends BSONDocumentWriter[UserInfo] {
    def write(user: UserInfo): BSONDocument = {
      BSONDocument(
        "_id" -> user._id,
        "firstName"->user.firstName,
        "lastName"->user.lastName,
        "email"->user.email,
        "id"->user.id
      )
    }
  }

}