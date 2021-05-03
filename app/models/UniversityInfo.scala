package models
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._


case class UniversityInfo(
                       _id:Option[BSONObjectID],
                       name:String,
                       location:String,
                       userId:Int,
                       id:Option[Int] = None
                     )


object UniversityInfo{


  implicit val fmt : Format[UniversityInfo] = Json.format[UniversityInfo]
  implicit object UniversityInfoBSONReader extends BSONDocumentReader[UniversityInfo] {
    def read(doc: BSONDocument): UniversityInfo = {
      UniversityInfo(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("name").get,
        doc.getAs[String]("location").get,
        doc.getAs[Int]("userId").get,
        doc.getAs[Int]("id")
      )
    }
  }

  implicit object UniversityInfoBSONWriter extends BSONDocumentWriter[UniversityInfo] {
    def write(university: UniversityInfo): BSONDocument = {
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