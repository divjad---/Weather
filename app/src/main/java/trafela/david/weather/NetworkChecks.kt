package trafela.david.weather

/**
 * Created by David Trafela on 20-Jul-18.
 */
class NetworkChecks{
    fun isOnline(): Boolean{
        try {
            val p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com")
            val returnVal = p1.waitFor()
            return returnVal == 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}