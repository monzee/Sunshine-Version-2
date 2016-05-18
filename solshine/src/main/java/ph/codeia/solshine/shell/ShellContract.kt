package ph.codeia.solshine.shell

import android.os.Bundle

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
object ShellContract {
    enum class Feature {
        INDEX, DETAIL
    }

    enum class Duration {
        SHORT, LONG, WHATEVER
    }

    enum class Channel {
        REGULAR, FANCY, MODAL, OUT_OF_BAND
    }

    interface Navigation {
        fun launch(f: Feature, args: Bundle? = null)
        fun back()
        fun home() = launch(Feature.INDEX)
        fun quit()
    }

    interface Messaging {
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
                tell(message.format(xs), Channel.OUT_OF_BAND, Duration.WHATEVER)
    }
}
