package com.flutter_klarna_payment.flutter_klarna_payment

import com.klarna.mobile.sdk.api.payments.KlarnaPaymentView
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentViewCallback
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentsSDKError

class PaymentViewCallback(
    private val streamHandler: PaymentStreamHandler,
    private val jsonParam: String
) : KlarnaPaymentViewCallback {
    override fun onAuthorized(
        view: KlarnaPaymentView,
        approved: Boolean,
        authToken: String?,
        finalizedRequired: Boolean?
    ) {
        println("PaymentViewCallback: onAuthorized called")
        if(authToken!=null){
            streamHandler.sendMessage(KlarnaPaymentState.AUTHORIZED.toCamelCase(),authToken)
            view.finalize(null)
        }
    }

    override fun onErrorOccurred(view: KlarnaPaymentView, error: KlarnaPaymentsSDKError) {
        streamHandler.sendMessage(KlarnaPaymentState.ERROR_OCCURRED.toCamelCase(),error.message)
    }

    override fun onFinalized(view: KlarnaPaymentView, approved: Boolean, authToken: String?) {
        streamHandler.sendMessage(KlarnaPaymentState.FINALIZED.toCamelCase(), authToken)
    }

    override fun onInitialized(view: KlarnaPaymentView) {
        streamHandler.sendMessage(KlarnaPaymentState.INITIALIZED.toCamelCase(),null)
        streamHandler.setPaymentView(view)
        view.load(jsonParam);
//        view.load("{\"shipping_address\": {\"given_name\": \"US RC MP\", \"family_name\": \"Test\", \"email\": \"icentris.qa6+1261@gmail.com\", \"street_address\": \"707 W 700 S\", \"postal_code\": \"84087-1454\", \"city\": \"WOODS CROSS\", \"country\": \"US\"}, \"customer\": {\"date_of_birth\": \"1984-01-01\"}}");
    }

    override fun onLoadPaymentReview(view: KlarnaPaymentView, showForm: Boolean) {
        streamHandler.sendMessage(KlarnaPaymentState.LOAD_PAYMENT_REVIEW.toCamelCase(),null)
        view.loadPaymentReview();
    }

    override fun onLoaded(view: KlarnaPaymentView) {
        streamHandler.sendMessage(KlarnaPaymentState.LOADED.toCamelCase(),null)

    }

    override fun onReauthorized(view: KlarnaPaymentView, approved: Boolean, authToken: String?) {
        streamHandler.sendMessage(KlarnaPaymentState.REAUTHORIZED.toCamelCase(),null)
    }

}