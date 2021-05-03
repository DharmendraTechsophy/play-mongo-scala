package models
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._


case class University(
                  _id:Option[BSONObjectID],
                  name:String,
                  location:String,
                  userId:Int,
                  id:Option[Int] = None
                )


object University{


  implicit val fmt : Format[University] = Json.format[University]
  implicit object UniversityBSONReader extends BSONDocumentReader[University] {
    def read(doc: BSONDocument): University = {
      University(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("name").get,
        doc.getAs[String]("location").get,
        doc.getAs[Int]("userId").get,
        doc.getAs[Int]("id")
      )
    }
  }

  implicit object UniversityBSONWriter extends BSONDocumentWriter[University] {
    def write(university: University): BSONDocument = {
      BSONDocument(
        "_id" -> university._id,
        "name" -> university.name,
        "location" -> university.location,
        "userId" -> university.userId,
        "id" -> university.id
      )
    }
  }


}