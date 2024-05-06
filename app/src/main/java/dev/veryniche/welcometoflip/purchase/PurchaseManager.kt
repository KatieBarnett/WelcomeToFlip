package dev.veryniche.welcometoflip.purchase

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase.PurchaseState
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import com.google.common.collect.ImmutableList
import dev.veryniche.welcometoflip.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

data class InAppProduct(
    val productId: String,
    val productName: String,
    val productDescription: String,
    val purchasePrice: String?,
    val purchaseCurrency: String?,
    val purchased: Boolean?,
) {
    val displayedPrice = "$purchasePrice $purchaseCurrency"
}

internal fun getProductQuery(id: String) =
    QueryProductDetailsParams.Product.newBuilder()
        .setProductId(id)
        .setProductType(BillingClient.ProductType.INAPP)
        .build()

class PurchaseManager(
    private val activity: Activity,
    private val coroutineScope: CoroutineScope
) {
    private val _purchases = MutableStateFlow<List<String>>(emptyList())
    val purchases = _purchases.asStateFlow()

    private val _availableProducts = MutableStateFlow<List<InAppProduct>>(emptyList())
    val availableProducts = _availableProducts.asStateFlow()

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { _, _ ->
            coroutineScope.launch {
                processPurchases()
            }
        }

    private var billingClient = BillingClient.newBuilder(activity)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    fun connectToBilling() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    coroutineScope.launch {
                        processAvailableProducts()
                        processPurchases()
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                billingClient.startConnection(this)
            }
        })
    }

    suspend fun processAvailableProducts() {
        val productList = ArrayList<QueryProductDetailsParams.Product>()
        productList.addAll(productList)

        val params = QueryProductDetailsParams.newBuilder()
        params.setProductList(appProductList)

        // leverage queryProductDetails Kotlin extension function
        val productDetailsResult = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params.build())
        }

        // Process the result.
        // Update products list
        _availableProducts.update {
            val newList = it.toMutableList()
            newList.addAll(
                productDetailsResult.productDetailsList?.map {
                    InAppProduct(
                        productId = it.productId,
                        productName = it.name,
                        productDescription = it.description,
                        purchasePrice = it.oneTimePurchaseOfferDetails?.formattedPrice,
                        purchaseCurrency = it.oneTimePurchaseOfferDetails?.priceCurrencyCode,
                        purchased = null
                    )
                } ?: listOf()
            )
            newList
        }
    }

    suspend fun processPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)

        // uses queryPurchasesAsync Kotlin extension function
        val purchasesResult = withContext(Dispatchers.IO) {
            billingClient.queryPurchasesAsync(params.build())
        }

        _purchases.update {
            val newList = it.toMutableList()
            newList.addAll(
                purchasesResult.purchasesList.filter {
                    it.purchaseState == PurchaseState.PURCHASED
                }.map {
                    it.products.firstOrNull().toString()
                }
            )
            newList
        }
    }

    fun purchase(
        productId: String,
        onError: (message: Int) -> Unit,
        retryCount: Int = 0
    ) {
        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    ImmutableList.of(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(productId)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
                    )
                )
                .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val productDetails = productDetailsList.firstOrNull { productDetails ->
                    productDetails.productId == productId
                }
                if (productDetails != null) {
                    initiateBilling(productDetails)
                    Timber.i("Initiating purchase of $productId")
                } else {
                    onError.invoke(R.string.purchase_error_generic)
                    Timber.e("Product details for $productId are null")
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) {
                billingClient.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            if (retryCount < 3) {
                                purchase(productId, onError, retryCount + 1)
                            } else {
                                onError.invoke(R.string.purchase_error_disconnected)
                                Timber.e("Can't connect to billing client - too many retries")
                            }
                        } else {
                            onError.invoke(R.string.purchase_error_disconnected)
                            Timber.e("Can't connect to billing client")
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        onError.invoke(R.string.purchase_error_disconnected)
                        Timber.e("Can't connect to billing client")
                    }
                })
            }
        }
    }

    private fun initiateBilling(productDetails: ProductDetails) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }
}
