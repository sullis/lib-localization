package io.flow.localization.rates

import com.fasterxml.jackson.databind.ObjectMapper
import io.flow.localization.countries.AvailableCountriesProvider
import io.flow.localization.pricing.FlowSkuPrice._
import io.flow.localization.pricing.FlowSkuPrice
import io.flow.reference.data.{Countries, Currencies}
import io.flow.localization.utils.DataClient
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.msgpack.jackson.dataformat.MessagePackFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LocalizerSpec extends WordSpec with MockitoSugar with Matchers with ScalaFutures {

  private val pricing50Cad = Map(
    CurrencyKey -> Currencies.Cad.iso42173,
    SalePriceKey -> 50.0,
    MsrpPriceKey -> 100.0
  ).asJava

  private val pricing25Eur = Map(
    CurrencyKey -> Currencies.Eur.iso42173,
    SalePriceKey -> 25.0,
    MsrpPriceKey -> 50.0
  ).asJava

  private val pricing5Eur = Map(
    CurrencyKey -> Currencies.Eur.iso42173,
    SalePriceKey -> 5.0,
    MsrpPriceKey -> 10.0
  ).asJava

  private val pricing50Usa = Map(
    CurrencyKey -> Currencies.Usd.iso42173,
    SalePriceKey -> 50.0,
    MsrpPriceKey -> 100.0
  ).asJava

  private val pricing10Usa = Map(
    CurrencyKey -> Currencies.Usd.iso42173,
    SalePriceKey -> 10.0,
    MsrpPriceKey -> 20.0
  ).asJava

  // { "i": "Includes VAT and duty", "c": "PHP", "b": 9100, "m": 22000, "t": 1507581191, "a": 16400 }
  private val serializedPricing = Array(134, 161, 105, 181, 73, 110, 99, 108, 117, 100, 101, 115, 32, 86, 65, 84, 32,
    97, 110, 100, 32, 100, 117, 116, 121, 161, 99, 163, 80, 72, 80, 161, 98, 205, 35, 140, 161, 109, 205, 85, 240, 161,
    116, 206, 89, 219, 221, 7, 161, 97, 205, 64, 16).map(_.toByte)

  private val deserializedPricing = Map[String, Any](
    "i" -> "Includes VAT and duty",
    "c" -> Currencies.Php.iso42173,
    "b" -> 9100,
    "m" -> 22000,
    "t" -> 1507581191,
    "a" -> 16400
  ).asJava

  "Localizer" should {

    "retrieve a localized pricing by country" in {
      val dataClient = mock[DataClient]

      val country = Countries.Can.iso31663
      val itemNumber = "item123"

      val key = s"c-$country:$itemNumber"
      val value: Array[Byte] = new ObjectMapper(new MessagePackFactory()).writeValueAsBytes(pricing50Cad)

      when(dataClient.get[Array[Byte]](key)).thenReturn(Future.successful(Some(value)))

      val localizer = new LocalizerImpl(dataClient, mock[RateProvider], mock[AvailableCountriesProvider])

      val expected = FlowSkuPrice(pricing50Cad).get

      // Verify we can retrieve by iso31663 code
      whenReady(localizer.getPricing(Countries.Can.iso31663, itemNumber = itemNumber)) { res =>
        res shouldBe Some(expected)
        res.get.msrpPrice.get shouldBe expected.msrpPrice.get
      }

      // Verify we can retrieve by iso31662 code lowercase
      whenReady(localizer.getPricing(Countries.Can.iso31662.toLowerCase, itemNumber = itemNumber)) { res =>
        res shouldBe Some(expected)
        res.get.msrpPrice.get shouldBe expected.msrpPrice.get
      }
    }

    "retrieve a localized pricing by country - using serialized data" in {
      val dataClient = mock[DataClient]

      val country = Countries.Can.iso31663
      val itemNumber = "item123"

      val key = s"c-$country:$itemNumber"
      when(dataClient.get[Array[Byte]](key)).thenReturn(Future.successful(Some(serializedPricing)))

      val localizer = new LocalizerImpl(dataClient, mock[RateProvider], mock[AvailableCountriesProvider])

      val expected = FlowSkuPrice(deserializedPricing).get

      whenReady(localizer.getPricing(Countries.Can.iso31663, itemNumber = itemNumber)) { res =>
        res shouldBe Some(expected)
        res.get.msrpPrice.get shouldBe expected.msrpPrice.get
      }
    }

    "retrieve and convert a localized pricing by country" in {
      val dataClient = mock[DataClient]
      val rateProvider = mock[RateProvider]

      val country = Countries.Can.iso31663
      val itemNumber = "item123"

      val key = s"c-$country:$itemNumber"
      val value: Array[Byte] = new ObjectMapper(new MessagePackFactory()).writeValueAsBytes(pricing50Cad)

      when(dataClient.get[Array[Byte]](key)).thenReturn(Future.successful(Some(value)))
      when(rateProvider.get(any(), any())).thenReturn(Some(BigDecimal(0.5)))

      val localizer = new LocalizerImpl(dataClient, rateProvider, mock[AvailableCountriesProvider])

      // Verify we can retrieve by iso31663 code
      whenReady(localizer.getSkuPriceByCountryWithCurrency(
        Countries.Can.iso31663, itemNumber = itemNumber, targetCurrency = Currencies.Eur.iso42173)
      ) {
        _ shouldBe FlowSkuPrice(pricing25Eur)
      }

      // Verify we can retrieve by iso31662 code lowercase
      whenReady(localizer.getSkuPriceByCountryWithCurrency(
        Countries.Can.iso31662.toLowerCase, itemNumber = itemNumber, targetCurrency = Currencies.Eur.iso42173)
      ) {
        _ shouldBe FlowSkuPrice(pricing25Eur)
      }
    }

    "retrieve and convert localized pricings by country" in {
      val dataClient = mock[DataClient]
      val rateProvider = mock[RateProvider]

      val country = Countries.Can.iso31663
      val itemNumber1 = "item1"
      val itemNumber2 = "item2"

      val key1 = s"c-$country:$itemNumber1"
      val key2 = s"c-$country:$itemNumber2"
      val value: Array[Byte] = new ObjectMapper(new MessagePackFactory()).writeValueAsBytes(pricing50Cad)

      when(dataClient.mGet[Array[Byte]](Seq(key1, key2))).thenReturn(Future.successful(Seq(Some(value), Some(value))))
      when(rateProvider.get(any(), any())).thenReturn(Some(BigDecimal(0.5)))

      val localizer = new LocalizerImpl(dataClient, rateProvider, mock[AvailableCountriesProvider])

      whenReady(localizer.getSkuPricesByCountryWithCurrency(country, itemNumbers = Seq(itemNumber1, itemNumber2),
        targetCurrency = Currencies.Eur.iso42173)) { res =>
        res should have size 2
        res(0) shouldBe FlowSkuPrice(pricing25Eur)
        res(1) shouldBe FlowSkuPrice(pricing25Eur)
      }
    }

    "update rates" in {
      val dataClient = mock[DataClient]
      val rateProvider = mock[RateProvider]

      val country = Countries.Can.iso31663
      val itemNumber = "item123"

      val key = s"c-$country:$itemNumber"
      val value: Array[Byte] = new ObjectMapper(new MessagePackFactory()).writeValueAsBytes(pricing50Cad)

      when(dataClient.get[Array[Byte]](key)).thenReturn(Future.successful(Some(value)))
      when(rateProvider.get(any(), any()))
        .thenReturn(Some(BigDecimal(0.5)))
        .thenReturn(Some(BigDecimal(0.1)))

      val localizer = new LocalizerImpl(dataClient, rateProvider, mock[AvailableCountriesProvider])

      whenReady(localizer.getSkuPriceByCountryWithCurrency(country, itemNumber = itemNumber, targetCurrency = Currencies.Eur.iso42173)) {
        _ shouldBe FlowSkuPrice(pricing25Eur)
      }

      whenReady(localizer.getSkuPriceByCountryWithCurrency(country, itemNumber = itemNumber, targetCurrency = Currencies.Eur.iso42173)) {
        _ shouldBe FlowSkuPrice(pricing5Eur)
      }

    }

    "return if a country is enabled" in {
      val dataClient = mock[DataClient]

      val availableCountriesProvider = mock[AvailableCountriesProvider]
      when(availableCountriesProvider.isEnabled(Countries.Fra.iso31663)).thenReturn(true)
      when(availableCountriesProvider.isEnabled(Countries.Can.iso31663)).thenReturn(false)

      val localizer = new LocalizerImpl(dataClient, mock[RateProvider], availableCountriesProvider)

      localizer.isEnabled(Countries.Fra.iso31663) shouldBe true
      localizer.isEnabled(Countries.Can.iso31663) shouldBe false
    }

    "retrieve a localized pricing with default country" in {
      val dataClient = mock[DataClient]

      val country = Countries.Can.iso31663
      val defaultCountry = Countries.Usa.iso31663
      val itemNumber = "item123"

      val key = s"c-$country:$itemNumber"
      val defaultKey = s"c-$defaultCountry:$itemNumber"
      val value: Array[Byte] = new ObjectMapper(new MessagePackFactory()).writeValueAsBytes(pricing50Cad)

      when(dataClient.get[Array[Byte]](key)).thenReturn(Future.successful(None))
      when(dataClient.get[Array[Byte]](defaultKey)).thenReturn(Future.successful(Some(value)))

      val localizer = new LocalizerImpl(dataClient, mock[RateProvider], mock[AvailableCountriesProvider])

      val expected = FlowSkuPrice(pricing50Cad).get

      // Verify we can retrieve by iso31663 code
      whenReady(localizer.getPricing(Countries.Can.iso31663, itemNumber = itemNumber)) { res =>
        res shouldBe Some(expected)
        res.get.msrpPrice.get shouldBe expected.msrpPrice.get
      }

      // Verify we can retrieve by iso31662 code lowercase
      whenReady(localizer.getPricing(Countries.Can.iso31662.toLowerCase, itemNumber = itemNumber)) { res =>
        res shouldBe Some(expected)
        res.get.msrpPrice.get shouldBe expected.msrpPrice.get
      }
    }

    "retrieve and convert a localized price by default country" in {
      val dataClient = mock[DataClient]
      val rateProvider = mock[RateProvider]

      val country = Countries.Fra.iso31663
      val defaultCountry = Countries.Usa.iso31663
      val itemNumber = "item123"

      val key = s"c-$country:$itemNumber"
      val defaultKey = s"c-$defaultCountry:$itemNumber"
      val value: Array[Byte] = new ObjectMapper(new MessagePackFactory()).writeValueAsBytes(pricing50Usa)

      when(dataClient.get[Array[Byte]](key)).thenReturn(Future.successful(None))
      when(dataClient.get[Array[Byte]](defaultKey)).thenReturn(Future.successful(Some(value)))
      when(rateProvider.get(any(), any())).thenReturn(Some(BigDecimal(0.5)))

      val localizer = new LocalizerImpl(dataClient, rateProvider, mock[AvailableCountriesProvider])

      // Verify we can retrieve by iso31663 code
      whenReady(localizer.getSkuPriceByCountryWithCurrency(
        Countries.Fra.iso31663, itemNumber = itemNumber, targetCurrency = Currencies.Eur.iso42173)
      ) {
        _ shouldBe FlowSkuPrice(pricing25Eur)
      }

      // Verify we can retrieve by iso31662 code lowercase
      whenReady(localizer.getSkuPriceByCountryWithCurrency(
        Countries.Fra.iso31662.toLowerCase, itemNumber = itemNumber, targetCurrency = Currencies.Eur.iso42173)
      ) {
        _ shouldBe FlowSkuPrice(pricing25Eur)
      }
    }

    "retrieve and convert localized pricings by default country" in {
      val dataClient = mock[DataClient]
      val rateProvider = mock[RateProvider]

      val country = Countries.Fra.iso31663
      val defaultCountry = Countries.Usa.iso31663
      val itemNumber1 = "item1"
      val itemNumber2 = "item2"

      val key1 = s"c-$country:$itemNumber1"
      val key2 = s"c-$country:$itemNumber2"
      val defaultKey1 = s"c-$defaultCountry:$itemNumber1"
      val defaultKey2 = s"c-$defaultCountry:$itemNumber2"
      val value: Array[Byte] = new ObjectMapper(new MessagePackFactory()).writeValueAsBytes(pricing50Usa)

      when(dataClient.mGet[Array[Byte]](Seq(key1, key2))).thenReturn(Future.successful(Seq(None, None)))
      when(dataClient.mGet[Array[Byte]](Seq(defaultKey1, defaultKey2))).thenReturn(Future.successful(Seq(Some(value), Some(value))))
      when(rateProvider.get(any(), any())).thenReturn(Some(BigDecimal(0.5)))

      val localizer = new LocalizerImpl(dataClient, rateProvider, mock[AvailableCountriesProvider])

      whenReady(localizer.getSkuPricesByCountryWithCurrency(country, itemNumbers = Seq(itemNumber1, itemNumber2),
        targetCurrency = Currencies.Eur.iso42173)) { res =>
        res should have size 2
        res(0) shouldBe FlowSkuPrice(pricing25Eur)
        res(1) shouldBe FlowSkuPrice(pricing25Eur)
      }
    }

    "retrieve and convert localized pricings by country and default country" in {
      val dataClient = mock[DataClient]
      val rateProvider = mock[RateProvider]

      val country = Countries.Fra.iso31663
      val defaultCountry = Countries.Usa.iso31663
      val itemNumber1 = "item1"
      val itemNumber2 = "item2"

      val key1 = s"c-$country:$itemNumber1"
      val key2 = s"c-$country:$itemNumber2"
      val defaultKey1 = s"c-$defaultCountry:$itemNumber1"
      val defaultKey2 = s"c-$defaultCountry:$itemNumber2"
      val value: Array[Byte] = new ObjectMapper(new MessagePackFactory()).writeValueAsBytes(pricing25Eur)
      val defaultValue: Array[Byte] = new ObjectMapper(new MessagePackFactory()).writeValueAsBytes(pricing50Usa)

      when(dataClient.mGet[Array[Byte]](Seq(key1, key2))).thenReturn(Future.successful(Seq(Some(value), None)))
      when(dataClient.mGet[Array[Byte]](Seq(defaultKey2))).thenReturn(Future.successful(Seq(Some(defaultValue))))
      when(rateProvider.get(any(), any())).thenReturn(Some(BigDecimal(0.5)))

      val localizer = new LocalizerImpl(dataClient, rateProvider, mock[AvailableCountriesProvider])

      whenReady(localizer.getSkuPricesByCountryWithCurrency(country, itemNumbers = Seq(itemNumber1, itemNumber2),
        targetCurrency = Currencies.Eur.iso42173)) { res =>
        res should have size 2
        res(0) shouldBe FlowSkuPrice(pricing25Eur)
        res(1) shouldBe FlowSkuPrice(pricing25Eur)
      }

      // ensure item order is maintained
      val defaultValue2: Array[Byte] = new ObjectMapper(new MessagePackFactory()).writeValueAsBytes(pricing10Usa)

      when(dataClient.mGet[Array[Byte]](Seq(key1, key2))).thenReturn(Future.successful(Seq(None, Some(value))))
      when(dataClient.mGet[Array[Byte]](Seq(defaultKey1))).thenReturn(Future.successful(Seq(Some(defaultValue2))))
      when(rateProvider.get(any(), any())).thenReturn(Some(BigDecimal(0.5)))

      whenReady(localizer.getSkuPricesByCountryWithCurrency(country, itemNumbers = Seq(itemNumber1, itemNumber2),
        targetCurrency = Currencies.Eur.iso42173)) { res =>
        res should have size 2
        res(0) shouldBe FlowSkuPrice(pricing5Eur)
        res(1) shouldBe FlowSkuPrice(pricing25Eur)
      }
    }

  }

}
