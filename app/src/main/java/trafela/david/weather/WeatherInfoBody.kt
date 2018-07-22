package trafela.david.weather

import org.simpleframework.xml.Root
import org.simpleframework.xml.Element

/**
 * Created by David Trafela on 19-Jul-18.
 */
@Root(name = "metData", strict = false)
class WeatherInfoBody {

    //set and initialize all data that we want from xml

    @set:Element(name = "domain_longTitle", required = false)
    @get:Element(name = "domain_longTitle", required = false)
    internal var longName: String? = ""

    @set:Element(name = "tsUpdated", required = false)
    @get:Element(name = "tsUpdated", required = false)
    internal var lastUpdated: String? = ""

    @set:Element(name = "t", required = false)
    @get:Element(name = "t", required = false)
    internal var temperature: Int? = 255

    @set:Element(name = "dd_shortText", required = false)
    @get:Element(name = "dd_shortText", required = false)
    internal var windDir: String? = ""

    @set:Element(name = "ff_val", required = false)
    @get:Element(name = "ff_val", required = false)
    internal var windSpeed: String? = "0"

    @set:Element(name = "nn_icon", required = false)
    @get:Element(name = "nn_icon", required = false)
    internal var weatherIcon: String? = ""

    @set:Element(name = "ddff_icon", required = false)
    @get:Element(name = "ddff_icon", required = false)
    internal var windIcon: String? = ""

    @set:Element(name = "rh", required = false)
    @get:Element(name = "rh", required = false)
    internal var humidity: String? = ""

    @set:Element(name = "vis_value", required = false)
    @get:Element(name = "vis_value", required = false)
    internal var visibility: String? = ""

    @set:Element(name = "sunrise", required = false)
    @get:Element(name = "sunrise", required = false)
    internal var sunrise: String? = ""

    @set:Element(name = "sunset", required = false)
    @get:Element(name = "sunset", required = false)
    internal var sunset: String? = ""

}