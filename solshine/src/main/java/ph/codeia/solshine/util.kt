package ph.codeia.solshine

import android.support.annotation.StringDef
import org.json.JSONObject
import javax.inject.Scope

/**
 * This file is a part of the Sunshine-Version-2 project.
 */

@Scope annotation class PerActivity

@Scope annotation class PerFeature

@[
Retention(AnnotationRetention.SOURCE)
StringDef(Temp.METRIC, Temp.KELVIN, Temp.IMPERIAL)
] annotation class TempUnits

object Temp {
    const val METRIC = "metric"
    const val KELVIN = "kelvin"
    const val IMPERIAL = "imperial"
}

interface JsonDeserializable<T> {
    fun fromJson(json: JSONObject): T
    fun fromJson(json: String): T = fromJson(JSONObject(json))
}

interface SuperclassInjector<T> {
    fun inject(obj: T): Boolean
}

inline infix fun <T> T?.rescue(block: () -> T): T? {
    return try {
        block()
    } catch (_: Throwable) {
        this
    }
}
