package ph.codeia.solshine.shell

import android.os.Bundle

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
object ShellContract {
    enum class Feature {
        INDEX, DETAIL
    }

    interface Navigation {
        fun launch(f: Feature, args: Bundle? = null)
        fun back()
        fun home() = launch(Feature.INDEX)
        fun quit()
    }
}
