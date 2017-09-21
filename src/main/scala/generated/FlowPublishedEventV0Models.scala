/**
 * Generated by API Builder - https://www.apibuilder.io
 * Service version: 0.3.88
 * apibuilder:0.12.61 https://app.apibuilder.io/flow/published-event/0.3.88/play_2_x_standalone_json
 */
package io.flow.published.event.v0.models {

  sealed trait PublishedEvent extends _root_.scala.Product with _root_.scala.Serializable

  /**
   * Defines the valid discriminator values for the type PublishedEvent
   */
  sealed trait PublishedEventDiscriminator extends _root_.scala.Product with _root_.scala.Serializable

  object PublishedEventDiscriminator {

    case object CatalogItemCountryStatesPublished extends PublishedEventDiscriminator { override def toString = "catalog_item_country_states_published" }
    case object OrganizationRatesPublished extends PublishedEventDiscriminator { override def toString = "organization_rates_published" }
    case object OrganizationCountriesPublished extends PublishedEventDiscriminator { override def toString = "organization_countries_published" }

    case class UNDEFINED(override val toString: String) extends PublishedEventDiscriminator

    val all: scala.List[PublishedEventDiscriminator] = scala.List(CatalogItemCountryStatesPublished, OrganizationRatesPublished, OrganizationCountriesPublished)

    private[this] val byName: Map[String, PublishedEventDiscriminator] = all.map(x => x.toString.toLowerCase -> x).toMap

    def apply(value: String): PublishedEventDiscriminator = fromString(value).getOrElse(UNDEFINED(value))

    def fromString(value: String): _root_.scala.Option[PublishedEventDiscriminator] = byName.get(value.toLowerCase)

  }

  case class CatalogItemCountryStates(
    country: String,
    statuses: Seq[io.flow.catalog.v0.models.SubcatalogItemStatus]
  )

  case class CatalogItemCountryStatesData(
    itemNumber: String,
    countries: Seq[io.flow.published.event.v0.models.CatalogItemCountryStates]
  )

  /**
   * Represents a catalog item and a list of countries with the item's availability
   * in each. This is a bulk event - meaning any time an individual item status
   * changes, we publish the new complete list of the item's country availability
   * statuses.
   */
  case class CatalogItemCountryStatesPublished(
    eventId: String,
    timestamp: _root_.org.joda.time.DateTime,
    organization: String,
    data: io.flow.published.event.v0.models.CatalogItemCountryStatesData
  ) extends PublishedEvent

  /**
   * @param available List of ISO 3166-3 country codes available as defined in
   *        https://api.flow.io/reference/countries
   */
  case class OrganizationCountriesData(
    available: Seq[String]
  )

  /**
   * Represents the list of countries that are currently marked available for an
   * organization. This is a bulk event - meaning any time an organization enables or
   * disables a specific region, we publish the new list of all of the countries that
   * the organization is making available to its consumers.
   */
  case class OrganizationCountriesPublished(
    eventId: String,
    timestamp: _root_.org.joda.time.DateTime,
    organization: String,
    data: io.flow.published.event.v0.models.OrganizationCountriesData
  ) extends PublishedEvent

  case class OrganizationRatesData(
    rates: Seq[io.flow.currency.v0.models.Rate]
  )

  /**
   * Represents the full list of exchange rates in effect for an organization. This
   * is a bulk event - meaning any time an individual rate changes, we publish the
   * new complete list of exchange rates. Note this event is published a few minutes
   * after a rate is published, allowing us to aggregate multiple changes in a short
   * period of time to reduce the number of organization_rates_published we publish.
   */
  case class OrganizationRatesPublished(
    eventId: String,
    timestamp: _root_.org.joda.time.DateTime,
    organization: String,
    data: io.flow.published.event.v0.models.OrganizationRatesData
  ) extends PublishedEvent

  /**
   * Provides future compatibility in clients - in the future, when a type is added
   * to the union PublishedEvent, it will need to be handled in the client code. This
   * implementation will deserialize these future types as an instance of this class.
   * 
   * @param description Information about the type that we received that is undefined in this version of
   *        the client.
   */
  case class PublishedEventUndefinedType(
    description: String
  ) extends PublishedEvent

}

package io.flow.published.event.v0.models {

  package object json {
    import play.api.libs.json.__
    import play.api.libs.json.JsString
    import play.api.libs.json.Writes
    import play.api.libs.functional.syntax._
    import io.flow.catalog.v0.models.json._
    import io.flow.currency.v0.models.json._
    import io.flow.published.event.v0.models.json._

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

    implicit def jsonReadsPublishedEventCatalogItemCountryStates: play.api.libs.json.Reads[CatalogItemCountryStates] = {
      (
        (__ \ "country").read[String] and
        (__ \ "statuses").read[Seq[io.flow.catalog.v0.models.SubcatalogItemStatus]]
      )(CatalogItemCountryStates.apply _)
    }

    def jsObjectCatalogItemCountryStates(obj: io.flow.published.event.v0.models.CatalogItemCountryStates): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "country" -> play.api.libs.json.JsString(obj.country),
        "statuses" -> play.api.libs.json.Json.toJson(obj.statuses)
      )
    }

    implicit def jsonWritesPublishedEventCatalogItemCountryStates: play.api.libs.json.Writes[CatalogItemCountryStates] = {
      new play.api.libs.json.Writes[io.flow.published.event.v0.models.CatalogItemCountryStates] {
        def writes(obj: io.flow.published.event.v0.models.CatalogItemCountryStates) = {
          jsObjectCatalogItemCountryStates(obj)
        }
      }
    }

    implicit def jsonReadsPublishedEventCatalogItemCountryStatesData: play.api.libs.json.Reads[CatalogItemCountryStatesData] = {
      (
        (__ \ "item_number").read[String] and
        (__ \ "countries").read[Seq[io.flow.published.event.v0.models.CatalogItemCountryStates]]
      )(CatalogItemCountryStatesData.apply _)
    }

    def jsObjectCatalogItemCountryStatesData(obj: io.flow.published.event.v0.models.CatalogItemCountryStatesData): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "item_number" -> play.api.libs.json.JsString(obj.itemNumber),
        "countries" -> play.api.libs.json.Json.toJson(obj.countries)
      )
    }

    implicit def jsonWritesPublishedEventCatalogItemCountryStatesData: play.api.libs.json.Writes[CatalogItemCountryStatesData] = {
      new play.api.libs.json.Writes[io.flow.published.event.v0.models.CatalogItemCountryStatesData] {
        def writes(obj: io.flow.published.event.v0.models.CatalogItemCountryStatesData) = {
          jsObjectCatalogItemCountryStatesData(obj)
        }
      }
    }

    implicit def jsonReadsPublishedEventCatalogItemCountryStatesPublished: play.api.libs.json.Reads[CatalogItemCountryStatesPublished] = {
      (
        (__ \ "event_id").read[String] and
        (__ \ "timestamp").read[_root_.org.joda.time.DateTime] and
        (__ \ "organization").read[String] and
        (__ \ "data").read[io.flow.published.event.v0.models.CatalogItemCountryStatesData]
      )(CatalogItemCountryStatesPublished.apply _)
    }

    def jsObjectCatalogItemCountryStatesPublished(obj: io.flow.published.event.v0.models.CatalogItemCountryStatesPublished): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "event_id" -> play.api.libs.json.JsString(obj.eventId),
        "timestamp" -> play.api.libs.json.JsString(_root_.org.joda.time.format.ISODateTimeFormat.dateTime.print(obj.timestamp)),
        "organization" -> play.api.libs.json.JsString(obj.organization),
        "data" -> jsObjectCatalogItemCountryStatesData(obj.data)
      )
    }

    implicit def jsonReadsPublishedEventOrganizationCountriesData: play.api.libs.json.Reads[OrganizationCountriesData] = {
      (__ \ "available").read[Seq[String]].map { x => new OrganizationCountriesData(available = x) }
    }

    def jsObjectOrganizationCountriesData(obj: io.flow.published.event.v0.models.OrganizationCountriesData): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "available" -> play.api.libs.json.Json.toJson(obj.available)
      )
    }

    implicit def jsonWritesPublishedEventOrganizationCountriesData: play.api.libs.json.Writes[OrganizationCountriesData] = {
      new play.api.libs.json.Writes[io.flow.published.event.v0.models.OrganizationCountriesData] {
        def writes(obj: io.flow.published.event.v0.models.OrganizationCountriesData) = {
          jsObjectOrganizationCountriesData(obj)
        }
      }
    }

    implicit def jsonReadsPublishedEventOrganizationCountriesPublished: play.api.libs.json.Reads[OrganizationCountriesPublished] = {
      (
        (__ \ "event_id").read[String] and
        (__ \ "timestamp").read[_root_.org.joda.time.DateTime] and
        (__ \ "organization").read[String] and
        (__ \ "data").read[io.flow.published.event.v0.models.OrganizationCountriesData]
      )(OrganizationCountriesPublished.apply _)
    }

    def jsObjectOrganizationCountriesPublished(obj: io.flow.published.event.v0.models.OrganizationCountriesPublished): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "event_id" -> play.api.libs.json.JsString(obj.eventId),
        "timestamp" -> play.api.libs.json.JsString(_root_.org.joda.time.format.ISODateTimeFormat.dateTime.print(obj.timestamp)),
        "organization" -> play.api.libs.json.JsString(obj.organization),
        "data" -> jsObjectOrganizationCountriesData(obj.data)
      )
    }

    implicit def jsonReadsPublishedEventOrganizationRatesData: play.api.libs.json.Reads[OrganizationRatesData] = {
      (__ \ "rates").read[Seq[io.flow.currency.v0.models.Rate]].map { x => new OrganizationRatesData(rates = x) }
    }

    def jsObjectOrganizationRatesData(obj: io.flow.published.event.v0.models.OrganizationRatesData): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "rates" -> play.api.libs.json.Json.toJson(obj.rates)
      )
    }

    implicit def jsonWritesPublishedEventOrganizationRatesData: play.api.libs.json.Writes[OrganizationRatesData] = {
      new play.api.libs.json.Writes[io.flow.published.event.v0.models.OrganizationRatesData] {
        def writes(obj: io.flow.published.event.v0.models.OrganizationRatesData) = {
          jsObjectOrganizationRatesData(obj)
        }
      }
    }

    implicit def jsonReadsPublishedEventOrganizationRatesPublished: play.api.libs.json.Reads[OrganizationRatesPublished] = {
      (
        (__ \ "event_id").read[String] and
        (__ \ "timestamp").read[_root_.org.joda.time.DateTime] and
        (__ \ "organization").read[String] and
        (__ \ "data").read[io.flow.published.event.v0.models.OrganizationRatesData]
      )(OrganizationRatesPublished.apply _)
    }

    def jsObjectOrganizationRatesPublished(obj: io.flow.published.event.v0.models.OrganizationRatesPublished): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "event_id" -> play.api.libs.json.JsString(obj.eventId),
        "timestamp" -> play.api.libs.json.JsString(_root_.org.joda.time.format.ISODateTimeFormat.dateTime.print(obj.timestamp)),
        "organization" -> play.api.libs.json.JsString(obj.organization),
        "data" -> jsObjectOrganizationRatesData(obj.data)
      )
    }

    implicit def jsonReadsPublishedEventPublishedEvent: play.api.libs.json.Reads[PublishedEvent] = new play.api.libs.json.Reads[PublishedEvent] {
      def reads(js: play.api.libs.json.JsValue): play.api.libs.json.JsResult[PublishedEvent] = {
        (js \ "discriminator").asOpt[String].getOrElse { sys.error("Union[PublishedEvent] requires a discriminator named 'discriminator' - this field was not found in the Json Value") } match {
          case "catalog_item_country_states_published" => js.validate[io.flow.published.event.v0.models.CatalogItemCountryStatesPublished]
          case "organization_rates_published" => js.validate[io.flow.published.event.v0.models.OrganizationRatesPublished]
          case "organization_countries_published" => js.validate[io.flow.published.event.v0.models.OrganizationCountriesPublished]
          case other => play.api.libs.json.JsSuccess(io.flow.published.event.v0.models.PublishedEventUndefinedType(other))
        }
      }
    }

    def jsObjectPublishedEvent(obj: io.flow.published.event.v0.models.PublishedEvent): play.api.libs.json.JsObject = {
      obj match {
        case x: io.flow.published.event.v0.models.CatalogItemCountryStatesPublished => jsObjectCatalogItemCountryStatesPublished(x) ++ play.api.libs.json.Json.obj("discriminator" -> "catalog_item_country_states_published")
        case x: io.flow.published.event.v0.models.OrganizationRatesPublished => jsObjectOrganizationRatesPublished(x) ++ play.api.libs.json.Json.obj("discriminator" -> "organization_rates_published")
        case x: io.flow.published.event.v0.models.OrganizationCountriesPublished => jsObjectOrganizationCountriesPublished(x) ++ play.api.libs.json.Json.obj("discriminator" -> "organization_countries_published")
        case other => {
          sys.error(s"The type[${other.getClass.getName}] has no JSON writer")
        }
      }
    }

    implicit def jsonWritesPublishedEventPublishedEvent: play.api.libs.json.Writes[PublishedEvent] = {
      new play.api.libs.json.Writes[io.flow.published.event.v0.models.PublishedEvent] {
        def writes(obj: io.flow.published.event.v0.models.PublishedEvent) = {
          jsObjectPublishedEvent(obj)
        }
      }
    }
  }
}

