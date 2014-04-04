package controllers

import models._
import models.JsonFormats._
import org.slf4j.{LoggerFactory, Logger}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import play.api.libs.json._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor
import scala.concurrent.Future

class reports extends Controller with MongoController{

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Report])

  /*
   * Get a JSONCollection (a Collection implementation that is designed to work
   * with JsObject, Reads and Writes.)
   * Note that the `collection` is not a `val`, but a `def`. We do _not_ store
   * the collection reference to avoid potential problems in development with
   * Play hot-reloading.
   */
  def collection: JSONCollection = db.collection[JSONCollection]("reports")


  def createReport = Action.async(parse.json) {
   request =>
     request.body.validate[Report].map {
       report =>
         collection.insert(report).map {
           lastError =>
             logger.debug(s"Successfully inserted with LastError: $lastError")
             Created(s"Report Created")
         }
     }.getOrElse(Future.successful(BadRequest("invalid json")))
 }

   def findReports = Action.async {
    // let's do our query
    val cursor: Cursor[Report] = collection.
      // find all
      find(Json.obj("active" -> true)).
      // sort them by creation date
      sort(Json.obj("created" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[Report]

    // gather all the JsObjects in a list
    val futureReportList: Future[List[Report]] = cursor.collect[List]()

    // transform the list into a JsArray
    val futureReportJsonArray: Future[JsArray] = futureReportList.map { reports =>
      Json.arr(reports)
    }
    // everything's ok! Let's reply with the array
    futureReportJsonArray.map {
      reports =>
        Ok(reports(0))
    }
  }
}