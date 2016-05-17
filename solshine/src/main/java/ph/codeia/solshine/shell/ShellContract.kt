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

    enum class Pigeon {
        TOAST, SNACKBAR, MODAL
    }

    interface Navigation {
        fun launch(f: Feature, args: Bundle? = null)
        fun back()
        fun home() = launch(Feature.INDEX)
        fun quit()
    }

    interface Messaging {
        fun tell(message: String, via: Pigeon = Pigeon.TOAST, duration: Duration = Duration.LONG)
        fun toast(message: String, duration: Duration = Duration.LONG) =
                tell(message, Pigeon.TOAST, duration)
        fun snackbar(message: String, duration: Duration = Duration.LONG) =
                tell(message, Pigeon.SNACKBAR, duration)
        fun alert(message: String, duration: Duration = Duration.LONG) =
                tell(message, Pigeon.MODAL, duration)
    }
}
