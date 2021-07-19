package hr.dtakac.prognoza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private val repo by inject<ForecastRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ImageView>(R.id.cloud).setOnClickListener {
            lifecycleScope.launch {
                val forecast = repo.getTodayForecastHours()
                Log.d(TAG, "onCreate: $forecast")
            }
        }
    }
}