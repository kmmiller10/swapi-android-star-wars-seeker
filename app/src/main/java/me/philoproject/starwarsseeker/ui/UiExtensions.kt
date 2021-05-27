package me.philoproject.starwarsseeker.ui

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import me.philoproject.starwarsseeker.BuildConfig
import me.philoproject.starwarsseeker.R
import me.philoproject.starwarsseeker.remote.base.AppException
import me.philoproject.starwarsseeker.remote.base.AppException.*

/**
 * Shows a simple "OK" alert dialog with a title and message.
 *
 * @param title - String resource for the dialog title
 * @param message - String resource for the dialog message
 * @param onAcknowledge - The action to perform when the OK button is clicked
 */
fun Context.showSimpleAlert(@StringRes title: Int, @StringRes message: Int, onAcknowledge: DialogInterface.OnClickListener?) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, onAcknowledge)
        .show()
}

/**
 * Parses an AppException into a user friendly message detailing what went wrong, then presents
 * the error message in an AlertDialog.
 *
 * @param exception - The AppException to parse
 */
fun Context.showUiErrorForAppException(exception: AppException) {
    @StringRes val errorMsg = when(exception) {
        BAD_REQUEST_EXCEPTION, NOT_FOUND_EXCEPTION -> {
            if(BuildConfig.DEBUG) R.string.request_failed_debug_msg else R.string.unknown_error_msg
        }
        TIMEOUT_EXCEPTION, GATEWAY_TIMEOUT_EXCEPTION -> R.string.connection_timeout_msg
        RATE_LIMIT_EXCEPTION -> R.string.too_many_requests_msg
        SERVER_INTERNAL_EXCEPTION, BAD_GATEWAY_EXCEPTION -> R.string.internal_server_error_msg
        SERVICE_UNAVAILABLE_EXCEPTION -> R.string.server_down_msg
        OFFLINE_EXCEPTION -> R.string.offline_error_msg
        GENERIC_EXCEPTION -> R.string.unknown_error_msg
    }

    showSimpleAlert(R.string.error, errorMsg) { d, _ ->
        d.dismiss()
    }
}

/**
 * Formats a String resource containing a single String argument
 *
 * @param stringRes - The String resource containing a String arg
 * @param stringValue = The argument to provide for the String resource
 * @param unknownRes - If the string value matches "unknown", an alternative resource can be
 *                     provided with measurement units removed
 * @return The formatted string
 */
fun Fragment.formatString(
    @StringRes stringRes: Int,
    stringValue: String,
    @StringRes unknownRes: Int? = null
): String {
    if(stringValue.equals(getString(R.string.unknown), ignoreCase = true) && unknownRes != null) {
        return getString(unknownRes)
    }
    return String.format(getString(stringRes), stringValue)
}