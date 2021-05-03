package models
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

case class StudentInfo(
                    _id:Option[BSONObjectID],
                    name:String,
                    email:String,
                    universityId:Int,
                    userId:Int,
                    id: Option[Int]=None
                  )
object StudentInfo{
  implicit val fmt : Format[StudentInfo] = Json.format[StudentInfo]
  implicit object StudentInfoBSONReader extends BSONDocumentReader[StudentInfo] {
    def read(doc: BSONDocument): StudentInfo = {
      StudentInfo(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("name").get,
        doc.getAs[String]("email").get,
        doc.getAs[Int]("universityId").get,
        doc.getAs[Int]("userId").get,
        doc.getAs[Int]("id")
      )
    }
  }

  implicit object StudentInfoBSONWriter extends BSONDocumentWriter[StudentInfo] {
    def write(student: StudentInfo): BSONDocument = {
      BSONDocument(
        "_id" -> student._id,
        "name" -> student.name,
        "email" -> student.email,
        "universityId" -> student.universityId,
        "userId" -> student.userId,
        "id" -> student.id
      )
    }
  }

}