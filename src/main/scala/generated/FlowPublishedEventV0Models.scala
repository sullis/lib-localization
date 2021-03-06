/**
 * Generated by API Builder - https://www.apibuilder.io
 * Service version: 0.4.90
 * apibuilder 0.14.3 app.apibuilder.io/flow/published-event/0.4.90/play_2_x_standalone_json
 */
package io.flow.published.event.v0.models {

  sealed trait PublishedEvent extends _root_.scala.Product with _root_.scala.Serializable

  /**
   * Defines the valid discriminator values for the type PublishedEvent
   */
  sealed trait PublishedEventDiscriminator extends _root_.scala.Product with _root_.scala.Serializable

  object PublishedEventDiscriminator {

    case object OrganizationRatesPublished extends PublishedEventDiscriminator { override def toString = "organization_rates_published" }
    case object OrganizationCountriesPublished extends PublishedEventDiscriminator { override def toString = "organization_countries_published" }
    case object OrganizationRatecardTransitWindowsPublished extends PublishedEventDiscriminator { override def toString = "organization_ratecard_transit_windows_published" }

    final case class UNDEFINED(override val toString: String) extends PublishedEventDiscriminator

    val all: scala.List[PublishedEventDiscriminator] = scala.List(OrganizationRatesPublished, OrganizationCountriesPublished, OrganizationRatecardTransitWindowsPublished)

    private[this] val byName: Map[String, PublishedEventDiscriminator] = all.map(x => x.toString.toLowerCase -> x).toMap

    def apply(value: String): PublishedEventDiscriminator = fromString(value).getOrElse(UNDEFINED(value))

    def fromString(value: String): _root_.scala.Option[PublishedEventDiscriminator] = byName.get(value.toLowerCase)

  }

  /**
   * @param available List of ISO 3166-3 country codes available as defined in
   *        https://api.flow.io/reference/countries
   */
  final case class OrganizationCountriesData(
    available: Seq[String]
  )

  /**
   * Represents the list of countries that are currently marked available for an
   * organization. This is a bulk event - meaning any time an organization enables or
   * disables a specific region, we publish the new list of all of the countries that
   * the organization is making available to its consumers.
   */
  final case class OrganizationCountriesPublished(
    eventId: String,
    timestamp: _root_.org.joda.time.DateTime,
    organization: String,
    data: io.flow.published.event.v0.models.OrganizationCountriesData
  ) extends PublishedEvent

  final case class OrganizationRatecardTransitWindowsData(
    transitWindows: Seq[io.flow.published.event.v0.models.TransitWindow]
  )

  /**
   * Represents the list of transit windows available for an organization. This is a
   * bulk event - meaning any time a transit window changes, we publish a new
   * complete list of all transit windows.
   */
  final case class OrganizationRatecardTransitWindowsPublished(
    eventId: String,
    timestamp: _root_.org.joda.time.DateTime,
    organization: String,
    data: io.flow.published.event.v0.models.OrganizationRatecardTransitWindowsData
  ) extends PublishedEvent

  final case class OrganizationRatesData(
    rates: Seq[io.flow.currency.v0.models.Rate]
  )

  /**
   * Represents the full list of exchange rates in effect for an organization. This
   * is a bulk event - meaning any time an individual rate changes, we publish the
   * new complete list of exchange rates. Note this event is published a few minutes
   * after a rate is published, allowing us to aggregate multiple changes in a short
   * period of time to reduce the number of organization_rates_published we publish.
   */
  final case class OrganizationRatesPublished(
    eventId: String,
    timestamp: _root_.org.joda.time.DateTime,
    organization: String,
    data: io.flow.published.event.v0.models.OrganizationRatesData
  ) extends PublishedEvent

  /**
   * @param from min transit
   * @param to max transit
   */
  final case class TransitWindow(
    originCountry: String,
    destinationCountry: String,
    from: Long,
    to: Long
  )

  /**
   * Provides future compatibility in clients - in the future, when a type is added
   * to the union PublishedEvent, it will need to be handled in the client code. This
   * implementation will deserialize these future types as an instance of this class.
   * 
   * @param description Information about the type that we received that is undefined in this version of
   *        the client.
   */
  final case class PublishedEventUndefinedType(
    description: String
  ) extends PublishedEvent

}

package io.flow.published.event.v0.models {

  package object json {
    import play.api.libs.json.__
    import play.api.libs.json.JsString
    import play.api.libs.json.Writes
    import play.api.libs.functional.syntax._
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

    private[v0] implicit val jsonReadsJodaLocalDate = __.read[String].map { str =>
      import org.joda.time.format.ISODateTimeFormat.dateParser
      dateParser.parseLocalDate(str)
    }

    private[v0] implicit val jsonWritesJodaLocalDate = new Writes[org.joda.time.LocalDate] {
      def writes(x: org.joda.time.LocalDate) = {
        import org.joda.time.format.ISODateTimeFormat.date
        val str = date.print(x)
        JsString(str)
      }
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
      for {
        eventId <- (__ \ "event_id").read[String]
        timestamp <- (__ \ "timestamp").read[_root_.org.joda.time.DateTime]
        organization <- (__ \ "organization").read[String]
        data <- (__ \ "data").read[io.flow.published.event.v0.models.OrganizationCountriesData]
      } yield OrganizationCountriesPublished(eventId, timestamp, organization, data)
    }

    def jsObjectOrganizationCountriesPublished(obj: io.flow.published.event.v0.models.OrganizationCountriesPublished): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "event_id" -> play.api.libs.json.JsString(obj.eventId),
        "timestamp" -> play.api.libs.json.JsString(_root_.org.joda.time.format.ISODateTimeFormat.dateTime.print(obj.timestamp)),
        "organization" -> play.api.libs.json.JsString(obj.organization),
        "data" -> jsObjectOrganizationCountriesData(obj.data)
      )
    }

    implicit def jsonReadsPublishedEventOrganizationRatecardTransitWindowsData: play.api.libs.json.Reads[OrganizationRatecardTransitWindowsData] = {
      (__ \ "transit_windows").read[Seq[io.flow.published.event.v0.models.TransitWindow]].map { x => new OrganizationRatecardTransitWindowsData(transitWindows = x) }
    }

    def jsObjectOrganizationRatecardTransitWindowsData(obj: io.flow.published.event.v0.models.OrganizationRatecardTransitWindowsData): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "transit_windows" -> play.api.libs.json.Json.toJson(obj.transitWindows)
      )
    }

    implicit def jsonWritesPublishedEventOrganizationRatecardTransitWindowsData: play.api.libs.json.Writes[OrganizationRatecardTransitWindowsData] = {
      new play.api.libs.json.Writes[io.flow.published.event.v0.models.OrganizationRatecardTransitWindowsData] {
        def writes(obj: io.flow.published.event.v0.models.OrganizationRatecardTransitWindowsData) = {
          jsObjectOrganizationRatecardTransitWindowsData(obj)
        }
      }
    }

    implicit def jsonReadsPublishedEventOrganizationRatecardTransitWindowsPublished: play.api.libs.json.Reads[OrganizationRatecardTransitWindowsPublished] = {
      for {
        eventId <- (__ \ "event_id").read[String]
        timestamp <- (__ \ "timestamp").read[_root_.org.joda.time.DateTime]
        organization <- (__ \ "organization").read[String]
        data <- (__ \ "data").read[io.flow.published.event.v0.models.OrganizationRatecardTransitWindowsData]
      } yield OrganizationRatecardTransitWindowsPublished(eventId, timestamp, organization, data)
    }

    def jsObjectOrganizationRatecardTransitWindowsPublished(obj: io.flow.published.event.v0.models.OrganizationRatecardTransitWindowsPublished): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "event_id" -> play.api.libs.json.JsString(obj.eventId),
        "timestamp" -> play.api.libs.json.JsString(_root_.org.joda.time.format.ISODateTimeFormat.dateTime.print(obj.timestamp)),
        "organization" -> play.api.libs.json.JsString(obj.organization),
        "data" -> jsObjectOrganizationRatecardTransitWindowsData(obj.data)
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
      for {
        eventId <- (__ \ "event_id").read[String]
        timestamp <- (__ \ "timestamp").read[_root_.org.joda.time.DateTime]
        organization <- (__ \ "organization").read[String]
        data <- (__ \ "data").read[io.flow.published.event.v0.models.OrganizationRatesData]
      } yield OrganizationRatesPublished(eventId, timestamp, organization, data)
    }

    def jsObjectOrganizationRatesPublished(obj: io.flow.published.event.v0.models.OrganizationRatesPublished): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "event_id" -> play.api.libs.json.JsString(obj.eventId),
        "timestamp" -> play.api.libs.json.JsString(_root_.org.joda.time.format.ISODateTimeFormat.dateTime.print(obj.timestamp)),
        "organization" -> play.api.libs.json.JsString(obj.organization),
        "data" -> jsObjectOrganizationRatesData(obj.data)
      )
    }

    implicit def jsonReadsPublishedEventTransitWindow: play.api.libs.json.Reads[TransitWindow] = {
      for {
        originCountry <- (__ \ "origin_country").read[String]
        destinationCountry <- (__ \ "destination_country").read[String]
        from <- (__ \ "from").read[Long]
        to <- (__ \ "to").read[Long]
      } yield TransitWindow(originCountry, destinationCountry, from, to)
    }

    def jsObjectTransitWindow(obj: io.flow.published.event.v0.models.TransitWindow): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "origin_country" -> play.api.libs.json.JsString(obj.originCountry),
        "destination_country" -> play.api.libs.json.JsString(obj.destinationCountry),
        "from" -> play.api.libs.json.JsNumber(obj.from),
        "to" -> play.api.libs.json.JsNumber(obj.to)
      )
    }

    implicit def jsonWritesPublishedEventTransitWindow: play.api.libs.json.Writes[TransitWindow] = {
      new play.api.libs.json.Writes[io.flow.published.event.v0.models.TransitWindow] {
        def writes(obj: io.flow.published.event.v0.models.TransitWindow) = {
          jsObjectTransitWindow(obj)
        }
      }
    }

    implicit def jsonReadsPublishedEventPublishedEvent: play.api.libs.json.Reads[PublishedEvent] = new play.api.libs.json.Reads[PublishedEvent] {
      def reads(js: play.api.libs.json.JsValue): play.api.libs.json.JsResult[PublishedEvent] = {
        (js \ "discriminator").asOpt[String].getOrElse { sys.error("Union[PublishedEvent] requires a discriminator named 'discriminator' - this field was not found in the Json Value") } match {
          case "organization_rates_published" => js.validate[io.flow.published.event.v0.models.OrganizationRatesPublished]
          case "organization_countries_published" => js.validate[io.flow.published.event.v0.models.OrganizationCountriesPublished]
          case "organization_ratecard_transit_windows_published" => js.validate[io.flow.published.event.v0.models.OrganizationRatecardTransitWindowsPublished]
          case other => play.api.libs.json.JsSuccess(io.flow.published.event.v0.models.PublishedEventUndefinedType(other))
        }
      }
    }

    def jsObjectPublishedEvent(obj: io.flow.published.event.v0.models.PublishedEvent): play.api.libs.json.JsObject = {
      obj match {
        case x: io.flow.published.event.v0.models.OrganizationRatesPublished => jsObjectOrganizationRatesPublished(x) ++ play.api.libs.json.Json.obj("discriminator" -> "organization_rates_published")
        case x: io.flow.published.event.v0.models.OrganizationCountriesPublished => jsObjectOrganizationCountriesPublished(x) ++ play.api.libs.json.Json.obj("discriminator" -> "organization_countries_published")
        case x: io.flow.published.event.v0.models.OrganizationRatecardTransitWindowsPublished => jsObjectOrganizationRatecardTransitWindowsPublished(x) ++ play.api.libs.json.Json.obj("discriminator" -> "organization_ratecard_transit_windows_published")
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

