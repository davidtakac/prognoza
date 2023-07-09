package hr.dtakac.prognoza.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openLink(link: String, context: Context) {
  try {
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
  } catch (_: Exception) {
  }
}