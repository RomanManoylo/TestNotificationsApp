package com.manoilo.testnotificationsapp.view

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.manoilo.testnotificationsapp.CHANNEL_ID
import com.manoilo.testnotificationsapp.INTENT_PAGE_NUMBER
import com.manoilo.testnotificationsapp.R
import kotlinx.android.synthetic.main.notification_fragment.*

class NotificationFragment : Fragment(R.layout.notification_fragment) {

    private var pageNumber: Int = 1

    companion object {
        fun newInstance(pageNumber: Int): NotificationFragment {
            val args = Bundle()
            args.putInt("PageNumber", pageNumber)

            val fragment = NotificationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageNumber = it.getInt("PageNumber", 1)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createNotification.setOnClickListener {
            val notifyIntent = Intent(context, MainActivity::class.java).apply {
                putExtra(INTENT_PAGE_NUMBER, pageNumber)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val notifyPendingIntent = PendingIntent.getActivity(
                context,
                pageNumber,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder = NotificationCompat.Builder(context!!, CHANNEL_ID).apply {
                setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_blue))
                setSmallIcon(R.drawable.ic_blue)
                setContentTitle(getString(R.string.notification_title))
                setContentText(
                    getString(
                        R.string.notification_text,
                        pageNumber
                    )
                )
                priority = NotificationCompat.PRIORITY_HIGH
                setContentIntent(notifyPendingIntent)
            }
            with(NotificationManagerCompat.from(context!!)) {
                notify(pageNumber, builder.build())
            }
        }
    }
}