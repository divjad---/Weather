package trafela.david.weather

import org.simpleframework.xml.Root
import org.simpleframework.xml.Element

/**
 * Created by David Trafela on 19-Jul-18.
 */
@Root(name = "metData", strict = false)
class WeatherInfoBody {

    @set:Element(name = "domain_longTitle", required = false)
    @get:Element(name = "domain_longTitle", required = false)
    internal var longName: String? = null

    @set:Element(name = "tsUpdated", required = false)
    @get:Element(name = "tsUpdated", required = false)
    internal var lastUpdated: String? = null

    @set:Element(name = "t", required = false)
    @get:Element(name = "t", required = false)
    internal var temperature: Int? = null

    @set:Element(name = "dd_longText", required = false)
    @get:Element(name = "dd_longText", required = false)
    internal var windDir: String? = null

    @set:Element(name = "ff_val", required = false)
    @get:Element(name = "ff_val", required = false)
    internal var windSpeed: String? = null

    @set:Element(name = "nn_icon", required = false)
    @get:Element(name = "nn_icon", required = false)
    internal var weatherIcon: String? = null
}