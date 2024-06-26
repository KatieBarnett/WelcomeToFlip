package dev.veryniche.welcometoflip.ads

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import dev.veryniche.welcometoflip.util.Analytics
import dev.veryniche.welcometoflip.util.trackAction
import timber.log.Timber

//https://medium.com/@amodpkanthe/androids-jetpack-compose-and-admob-ads-33948c55214a

enum class BannerAdLocation(val adId: String) {
    Test(adId = "ca-app-pub-3940256099942544/6300978111"),
    MainScreen(adId = "ca-app-pub-4584531662076255/5101194881")
}

@Composable
fun BannerAd(location: BannerAdLocation, modifier: Modifier = Modifier) {
    val adRequest = AdRequest.Builder().build()
    val context = LocalContext.current
    Box(modifier = modifier.fillMaxWidth()) {
        if (adRequest.isTestDevice(context)) {
            BannerAd(adId = BannerAdLocation.Test.adId)
        } else {
            BannerAd(adId = location.adId)
        }
    }
}

@Composable
private fun BannerAd(adId: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            // on below line specifying ad view.
            AdView(context).apply {
                // on below line specifying ad size
                // adSize = AdSize.BANNER
                // on below line specifying ad unit id
                // currently added a test ad unit id.
                setAdSize(AdSize.BANNER)
                adUnitId = adId
                // calling load ad to load our ad.
                loadAd(AdRequest.Builder().build())
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Timber.d("Banner ad loaded")
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        Timber.d("Banner ad clicked")
                        trackAction(Analytics.Action.BannerAdClick)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        Timber.d("Banner ad failed to load: ${error.message}")
                    }
                }
            }
        }
    )
}
