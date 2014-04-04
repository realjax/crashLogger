package models



case class Report(
    ANDROID_VERSION: Int,
    APP_VERSION_CODE: Int,
    APP_VERSION_NAME: Int,
    AVAILABLE_MEM_SIZE: Long,
    BRAND: String)

object JsonFormats {
  import play.api.libs.json.Json
  implicit val reportFormat = Json.format[Report]
}