package com.leetprep.app
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import com.leetprep.app.ui.theme.LeetPrepAndroidTheme
import com.leetprep.app.ui.LeetPrepApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LeetPrepAndroidTheme {
                Surface(tonalElevation = 5.dp) {
                    LeetPrepApp()
                }
            }
        }
    }
}
