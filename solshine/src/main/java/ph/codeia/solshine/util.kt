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
StringDef(Units.METRIC, Units.KELVIN, Units.IMPERIAL)
] annotation class TempUnits

object Units {
    const val METRIC = "metric"
    const val KELVIN = "standard"
    const val IMPERIAL = "imperial"
}

interface JsonDeserializable<T> {
    fun fromJson(json: JSONObject): T
    fun fromJson(json: String): T = fromJson(JSONObject(json))
}

interface Injector<T> {
    fun inject(injectable: T): Boolean
}

inline fun <T> barf(default: T?, block: () -> T): T? = try {
    block()
} catch (_: Throwable) {
    default
}

sealed class Result<T> {
    class Ok<T>(val value: T) : Result<T>()
    class Err<T>(val error: Throwable) : Result<T>()

    companion object Try {
        fun <T> ok(result: T): Ok<T> = Ok(result)
        fun <T> fail(error: Throwable): Err<T> = Err(error)
        fun <T> fail(message: String): Err<T> = Err(RuntimeException(message))
        inline operator fun <T> invoke(block: Try.() -> Result<T>): Result<T> = this.block()
    }
}
