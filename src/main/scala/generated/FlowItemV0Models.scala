/**
 * Generated by API Builder - https://www.apibuilder.io
 * Service version: 0.3.60
 * apibuilder:0.12.52 https://app.apibuilder.io/flow/item/0.3.60/play_2_x_standalone_json
 */
package io.flow.item.v0.models {

  /**
   * The Local Item represents all of the data for a given item that is localized to
   * a given experinece.
   */
  case class LocalItem(
    id: String,
    experience: io.flow.common.v0.models.ExperienceSummary,
    item: io.flow.common.v0.models.CatalogItemReference,
    pricing: io.flow.item.v0.models.LocalItemPricing,
    status: io.flow.catalog.v0.models.SubcatalogItemStatus
  )

  /**
   * Localized item pricing information
   * 
   * @param price The localized item.price for this experience. This represents the price a
   *        consumer will pay to purchase this item in this experience.
   * @param attributes All attributes with intent price as keys of this map - with each of those
   *        attributes mapped to its value in the local currency. For example, given an
   *        attribute named 'msrp' with intent 'price', this map will contain a key named
   *        'msrp'
   */
  case class LocalItemPricing(
    price: io.flow.catalog.v0.models.LocalizedItemPrice,
    attributes: Map[String, io.flow.common.v0.models.PriceWithBase]
  )

}

package io.flow.item.v0.models {

  package object json {
    import play.api.libs.json.__
    import play.api.libs.json.JsString
    import play.api.libs.json.Writes
    import play.api.libs.functional.syntax._
    import io.flow.catalog.v0.models.json._
    import io.flow.common.v0.models.json._
    import io.flow.currency.v0.models.json._
    import io.flow.item.v0.models.json._

    private[v0] implicit val jsonReadsUUID = __.read[String].map(java.util.UUID.fromString)

    private[v0] implicit val jsonWritesUUID = new Writes[java.util.UUID] {
      def writes(x: java.util.UUID) = JsString(x.toString)
    }

    private[v0] implicit val jsonReadsJodaDateTime = __.read[String].map { str =>
      import org.joda.time.format.ISODateTimeFormat.dateTimeParser
      dateTimeParser.parseDateTime(str)
    }

    private[v0] implicit val jsonWritesJodaDateTime = new Writes[org.joda.time.DateTime] {
      def writes(x: org.joda.time.DateTime) = {
        import org.joda.time.format.ISODateTimeFormat.dateTime
        val str = dateTime.print(x)
        JsString(str)
      }
    }

    implicit def jsonReadsItemLocalItem: play.api.libs.json.Reads[LocalItem] = {
      (
        (__ \ "id").read[String] and
        (__ \ "experience").read[io.flow.common.v0.models.ExperienceSummary] and
        (__ \ "item").read[io.flow.common.v0.models.CatalogItemReference] and
        (__ \ "pricing").read[io.flow.item.v0.models.LocalItemPricing] and
        (__ \ "status").read[io.flow.catalog.v0.models.SubcatalogItemStatus]
      )(LocalItem.apply _)
    }

    def jsObjectLocalItem(obj: io.flow.item.v0.models.LocalItem): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "id" -> play.api.libs.json.JsString(obj.id),
        "experience" -> io.flow.common.v0.models.json.jsObjectExperienceSummary(obj.experience),
        "item" -> io.flow.common.v0.models.json.jsObjectCatalogItemReference(obj.item),
        "pricing" -> jsObjectLocalItemPricing(obj.pricing),
        "status" -> play.api.libs.json.JsString(obj.status.toString)
      )
    }

    implicit def jsonWritesItemLocalItem: play.api.libs.json.Writes[LocalItem] = {
      new play.api.libs.json.Writes[io.flow.item.v0.models.LocalItem] {
        def writes(obj: io.flow.item.v0.models.LocalItem) = {
          jsObjectLocalItem(obj)
        }
      }
    }

    implicit def jsonReadsItemLocalItemPricing: play.api.libs.json.Reads[LocalItemPricing] = {
      (
        (__ \ "price").read[io.flow.catalog.v0.models.LocalizedItemPrice] and
        (__ \ "attributes").read[Map[String, io.flow.common.v0.models.PriceWithBase]]
      )(LocalItemPricing.apply _)
    }

    def jsObjectLocalItemPricing(obj: io.flow.item.v0.models.LocalItemPricing): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "price" -> io.flow.catalog.v0.models.json.jsObjectLocalizedItemPrice(obj.price),
        "attributes" -> play.api.libs.json.Json.toJson(obj.attributes)
      )
    }

    implicit def jsonWritesItemLocalItemPricing: play.api.libs.json.Writes[LocalItemPricing] = {
      new play.api.libs.json.Writes[io.flow.item.v0.models.LocalItemPricing] {
        def writes(obj: io.flow.item.v0.models.LocalItemPricing) = {
          jsObjectLocalItemPricing(obj)
        }
      }
    }
  }
}

