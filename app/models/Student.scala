package models
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

case class Student(
                    _id:Option[BSONObjectID],
                    name:String,
                    email:String,
                    universityId:Int,
                    userId:Int,
                    id: Option[Int]=None
                  )
object Student{
  implicit val fmt : Format[Student] = Json.format[Student]
  implicit object StudentBSONReader extends BSONDocumentReader[Student] {
    def read(doc: BSONDocument): Student = {
      Student(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("name").get,
        doc.getAs[String]("email").get,
        doc.getAs[Int]("universityId").get,
        doc.getAs[Int]("userId").get,
        doc.getAs[Int]("id")
      )
    }
  }

  implicit object StudentBSONWriter extends BSONDocumentWriter[Student] {
    def write(student: Student): BSONDocument = {
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