package trafela.david.weather

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


/**
 * Created by David Trafela on 19-Jul-18.
 */
@Root(name = "data", strict = false)
class WeatherInfo {
    @set:ElementList(entry = "metData", inline = true)
    @get:ElementList(entry = "metData", inline = true)
    internal var weatherDetail: List<WeatherInfoBody>? = null
}
