package oleksand.narvatov.navigationfragmentswithnotification.data.storage

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.reflect.Type
import javax.inject.Inject

class SharedPrefFragmentsStorage @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
    }

    fun storeFragments(fragmentsCount: Int) {
        preferences.edit().apply {
            putInt(
                FRAGMENTS_COUNT,
                if (fragmentsCount <= 0) 1 else fragmentsCount
            )
            apply()
        }
    }

    fun loadFragments() = preferences.getInt(FRAGMENTS_COUNT, DEFAULT_FRAGMENTS_COUNT)

    companion object {
        private const val STORAGE_NAME = "SOME_NAME"
        private const val FRAGMENTS_COUNT = "SOME_COUNT"
        private const val DEFAULT_FRAGMENTS_COUNT = 1
    }
}