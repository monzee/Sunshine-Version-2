package ph.codeia.solshine.shell

import android.app.Activity
import android.util.Log
import android.widget.Toast
import ph.codeia.solshine.shell.ShellContract.Duration
import ph.codeia.solshine.shell.ShellContract.Channel
import javax.inject.Inject

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class Messenger @Inject constructor(private val context: Activity) : ShellContract.Feedback {
    override fun tell(message: String, via: Channel, duration: Duration) {
        when (via) {
            Channel.OUT_OF_BAND -> Log.i("mz/Messenger", message)
            else -> Toast.makeText(context, message, when (duration) {
                Duration.LONG -> Toast.LENGTH_LONG
                else -> Toast.LENGTH_SHORT
            }).show()
        }
    }
}
