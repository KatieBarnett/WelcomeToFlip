package dev.veryniche.welcometoflip.ads

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dev.veryniche.welcometoflip.MainActivity
import dev.veryniche.welcometoflip.util.Analytics
import dev.veryniche.welcometoflip.util.trackAction
import timber.log.Timber

// https://medium.com/@amodpkanthe/androids-jetpack-compose-and-admob-ads-33948c55214a

enum class InterstitialAdLocation(val adId: String) {
    Test(adId = "ca-app-pub-3940256099942544/1033173712"),
    StartGame(adId = "ca-app-pub-4584531662076255/5101194881"),
    ReshuffleGame(adId = "ca-app-pub-4584531662076255/5101194881"),
    EndGame(adId = "ca-app-pub-4584531662076255/5101194881")
}

fun showInterstitialAd(activity: MainActivity, location: InterstitialAdLocation) {
    val adRequest = AdRequest.Builder().build()
    val adUnitId = if (adRequest.isTestDevice(activity)) {
        InterstitialAdLocation.Test.adId
    } else {
        location.adId
    }

    InterstitialAd.load(
        activity,
        adUnitId,
        adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Timber.d("Interstitial ad failed to load: ${adError.message}")
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Timber.d("Interstitial ad loaded")
                interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        trackAction(Analytics.Action.InterstitialAdClick + interstitialAd.adUnitId)
                        Timber.d("Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        Timber.d("Ad dismissed fullscreen content.")
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when ad fails to show.
                        Timber.e("Ad failed to show fullscreen content: ${adError.message}")
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Timber.d("Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Timber.d("Ad showed fullscreen content.")
                    }
                }
                interstitialAd.show(activity)
            }
        }
    )
}
