package oleksand.narvatov.navigationfragmentswithnotification.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import oleksand.narvatov.navigationfragmentswithnotification.ui.fragment.NotificationCounterFragment
import oleksand.narvatov.navigationfragmentswithnotification.utils.previous
import timber.log.Timber

class FragmentAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = mutableListOf<NotificationCounterFragment>()

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]

    fun addFragment(position: Int, fragment: NotificationCounterFragment) {
        if (fragmentList.any {
                it.notification_channel_id == fragment.notification_channel_id
        }) return

        fragmentList.add(position.previous, fragment)
        // TODO: consider using notify item added by index
        notifyDataSetChanged()
    }

    fun removeLastFragment() {
        fragmentList.removeLast().cancelNotifications()
        notifyDataSetChanged()
    }

}