package trafela.david.weather

/**
 * Created by David Trafela on 20-Jul-18.
 */
class NetworkChecks{
    //initialize function for checking internet connection
    fun isOnline(): Boolean{
        try {
            //ping website to check for active connection
            val p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com")
            val returnVal = p1.waitFor()

            //return result
            return returnVal == 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}