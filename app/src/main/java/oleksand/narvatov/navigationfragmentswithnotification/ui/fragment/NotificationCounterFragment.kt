package oleksand.narvatov.navigationfragmentswithnotification.ui.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import oleksand.narvatov.navigationfragmentswithnotification.R
import oleksand.narvatov.navigationfragmentswithnotification.databinding.FragmentNotificationCounterBinding
import oleksand.narvatov.navigationfragmentswithnotification.entity.FragmentAction.DeleteFragment
import oleksand.narvatov.navigationfragmentswithnotification.entity.FragmentAction.CreateNextFragment
import oleksand.narvatov.navigationfragmentswithnotification.ui.activity.MainActivity
import oleksand.narvatov.navigationfragmentswithnotification.ui.viewmodel.SharedViewModel
import oleksand.narvatov.navigationfragmentswithnotification.utils.next
import timber.log.Timber


@AndroidEntryPoint
class NotificationCounterFragment : Fragment(R.layout.fragment_notification_counter) {

    private val binding: FragmentNotificationCounterBinding by viewBinding()

    private val sharedViewModel: SharedViewModel by activityViewModels()

    val notification_channel_id: String
        get() {
            val page = arguments?.getInt(PAGE) ?: DEFAULT_PAGE
            return CHANNEL_BASE_ID + page
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val page = arguments?.getInt(PAGE) ?: DEFAULT_PAGE
        initViews(page)
    }

    private fun initViews(page: Int) {
        with(binding) {
            counterText.text = "$page"

            minusButton.setOnClickListener {
                sharedViewModel.sendFragmentAction(DeleteFragment)
            }

            plusButton.setOnClickListener {
                sharedViewModel.sendFragmentAction(CreateNextFragment(page.next))
            }

            createNotificationButton.setOnClickListener {
                val builder = createNotification(page)

                createNotificationChannel()

                with(NotificationManagerCompat.from(requireContext())) {
                    notify(getNotificationId(page), builder.build())
                }
            }
        }
    }

    private val notificationsList = mutableListOf<Int>()

    private fun getNotificationId(page: Int): Int {
        val notificationId = NOTIFICATION_BASE_ID++ * page
        notificationsList += notificationId
        return notificationId
    }

    private fun createNotification(page: Int)  : NotificationCompat.Builder {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(MainActivity.SELECTED_PAGE, page)
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(requireContext(), page, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(requireContext(), notification_channel_id)
            .setSmallIcon(R.drawable.small_notification_icon)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.notification_icon))
            .setContentTitle(getString(R.string.you_create_a_notification))
            .setContentText(getString(R.string.notification) + " $page")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.messenger)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(notification_channel_id, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = requireActivity().
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.d("tag1 WTF PPP")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("tag1 WTF SSSS")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("tag1 WTF DDD")
        val notificationManager = requireActivity().
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationsList.forEach { notificationManager.cancel(it) }
    }


    companion object {
        fun create(page: Int) = NotificationCounterFragment().apply {
            val bundle = Bundle()
            bundle.putInt(PAGE, page)
            arguments = bundle
        }

        private const val PAGE = "PAGE"
        private const val DEFAULT_PAGE = 1
        private const val CHANNEL_BASE_ID = "857989109"
        private var NOTIFICATION_BASE_ID = 5879
    }
}