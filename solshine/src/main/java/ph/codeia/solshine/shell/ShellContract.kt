package ph.codeia.solshine.shell

import android.os.Bundle
import android.support.annotation.IntDef

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
object ShellContract {
    const val INDEX = 0L
    const val DETAIL = 1L
    const val SETTINGS = 2L
    const val MAP = 3L

    @[
    IntDef(INDEX, DETAIL, SETTINGS, MAP)  // why long??
    Retention(AnnotationRetention.SOURCE)
    ] annotation class Screen

    enum class Duration {
        SHORT, LONG, WHATEVER
    }

    enum class Channel {
        REGULAR, FANCY, MODAL, OUT_OF_BAND
    }

    interface Navigation {
        fun launch(@Screen f: Long, args: Bundle? = null): Boolean
        fun back()
        fun home() = launch(INDEX)
    }

    interface Feedback {
        fun tell(
                message: String,
                via: Channel = Channel.REGULAR,
                duration: Duration = Duration.LONG)

        fun toast(message: String, duration: Duration = Duration.LONG) =
                tell(message, Channel.REGULAR, duration)

        fun snackbar(message: String, duration: Duration = Duration.LONG) =
                tell(message, Channel.FANCY, duration)

        fun alert(message: String, duration: Duration = Duration.LONG) =
                tell(message, Channel.MODAL, duration)

        fun log(message: String, vararg xs: Any) =
                tell(message.format(*xs), Channel.OUT_OF_BAND, Duration.WHATEVER)

        fun here(message: String = "I'M HERE!") =
                log("-----=====[ $message ]=====-----")
    }
}
