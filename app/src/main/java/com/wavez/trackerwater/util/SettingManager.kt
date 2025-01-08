package com.example.basekotlin.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.example.basekotlin.R
import com.example.basekotlin.dialog.rate.RatingDialog
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory

object SettingManager {

    const val linkPolicy = ""
    const val email = "abcd@gmail.com"

    fun onShare(activity: Activity) {

        val intentShare = Intent(Intent.ACTION_SEND)
        intentShare.type = "text/plain"
        intentShare.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name))
        intentShare.putExtra(
            Intent.EXTRA_TEXT,
            "Download application :https://play.google.com/store/apps/details?id=${activity.packageName}"
        )
        activity.startActivity(Intent.createChooser(intentShare, "Share with"))
    }

    fun onFeedback(activity: Activity) {
        val uriText = """
                 mailto:$email?subject=Feedback for ${activity.getString(R.string.app_name)}
                 &body=${activity.getString(R.string.app_name)}
                 Feedback: 
                 """.trimIndent()
        val uri = Uri.parse(uriText)
        val sendIntent = Intent(Intent.ACTION_SENDTO)
        sendIntent.data = uri
        try {
            activity.startActivity(
                Intent.createChooser(
                    sendIntent, activity.getString(R.string.Send_Email)
                )
            )
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                activity, activity.getString(R.string.There_is_no), Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun onRateUs(activity: Activity, view: View?) {
        RatingDialog(activity, true, onSend = { rating ->
            val uriText = """
                 mailto:${email}?subject=Review for ${activity.getString(R.string.app_name)}&body=${
                activity.getString(
                    R.string.app_name
                )
            }
                 Rate : $rating
                 Content: 
                 """.trimIndent()
            val uri = Uri.parse(uriText)
            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = uri
            try {
                view!!.visibility = View.GONE
                activity.startActivity(
                    Intent.createChooser(
                        sendIntent, activity.getString(R.string.Send_Email)
                    )
                )
                SharedPreUtils.getInstance().forceRated(activity)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    activity, activity.getString(R.string.There_is_no), Toast.LENGTH_SHORT
                ).show()
            }
        }, onRate = {
            val manager = ReviewManagerFactory.create(activity)
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task: Task<ReviewInfo?> ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    val flow = manager.launchReviewFlow(
                        activity, reviewInfo!!
                    )
                    flow.addOnSuccessListener {
                        SharedPreUtils.getInstance().forceRated(activity)
                        view!!.visibility = View.GONE
                    }
                }
            }
        }).show()

    }

}