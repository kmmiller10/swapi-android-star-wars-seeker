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

fun Context.showSimpleAlert(@StringRes title: Int, @StringRes message: Int, onAcknowledge: DialogInterface.OnClickListener?) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, onAcknowledge)
        .show()
}

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

fun Fragment.formatString(@StringRes stringRes: Int, stringValue: String): String {
    return String.format(getString(stringRes), stringValue)
}