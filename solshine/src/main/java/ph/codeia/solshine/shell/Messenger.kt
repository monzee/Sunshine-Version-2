package ph.codeia.solshine.shell

import android.app.Activity
import android.widget.Toast
import ph.codeia.solshine.shell.ShellContract.Duration
import ph.codeia.solshine.shell.ShellContract.Pigeon
import javax.inject.Inject

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class Messenger @Inject constructor(val context: Activity) : ShellContract.Messaging {
    override fun tell(message: String, via: Pigeon, duration: Duration) {
        // only toasts for now
        val length = when (duration) {
            Duration.LONG -> Toast.LENGTH_LONG
            else -> Toast.LENGTH_SHORT
        }
        Toast.makeText(context, message, length).show()
    }
}
