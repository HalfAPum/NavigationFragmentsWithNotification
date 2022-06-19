package oleksand.narvatov.navigationfragmentswithnotification.ui.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import oleksand.narvatov.navigationfragmentswithnotification.ui.fragment.NotificationCounterFragment
import oleksand.narvatov.navigationfragmentswithnotification.utils.previous

class FragmentAdapter(
    private val fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = mutableListOf<Int>()

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) =
        NotificationCounterFragment.create(fragmentList[position])

    fun addFragment(position: Int) {
        if ((fragmentList.getOrNull(position) ?: 0) > 0) return

        fragmentList.add(position.previous, position)
        notifyDataSetChanged()
    }

    fun addFragments(count: Int) {
        val fragments = List(count) { it.plus(1) }

        fragmentList.addAll(fragments)
        notifyDataSetChanged()
    }

    fun removeLastFragment() {
        notifyItemRemoved(fragmentList.lastIndex)
        fragmentList.removeLast()
    }

}