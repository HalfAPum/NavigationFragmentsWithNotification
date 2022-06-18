package oleksand.narvatov.navigationfragmentswithnotification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import oleksand.narvatov.navigationfragmentswithnotification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = NotificationCounterFragment()
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, fragment)
            addToBackStack(null)
            commit()
        }
    }
}