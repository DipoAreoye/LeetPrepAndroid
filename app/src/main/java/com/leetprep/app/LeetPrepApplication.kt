package com.leetprep.app

import android.app.Application
import com.leetprep.app.data.network.APIService
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class LeetPrepApplication: Application() {

}