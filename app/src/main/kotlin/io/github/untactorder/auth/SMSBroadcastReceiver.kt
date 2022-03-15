package io.github.untactorder.auth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import io.github.untactorder.printLog

val RESULT_OK= 3092

/*
// Construct a request for phone numbers and show the picker
private fun requestHint() {
    val hintRequest = HintRequest.Builder()
        .setPhoneNumberIdentifierSupported(true)
        .build()
    val intent = Auth.CredentialsApi.getHintPickerIntent(apiClient, hintRequest)
    startIntentSenderForResult(
        intent.intentSender,
        RESOLVE_HINT, RESULT_OK, 0, 0, 0
    )
}

// Obtain the phone number from the result
fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == RESOLVE_HINT) {
        if (resultCode == RESULT_OK) {
            val credential: Credential = data.getParcelableExtra(Credential.EXTRA_KEY)
            // credential.getId();  <-- will need to process phone number string
        }
    }
}
*/

fun Context.startSMSRetriever() {
    // Get an instance of SmsRetrieverClient, used to start listening for a matching
    // SMS message.
    // Get an instance of SmsRetrieverClient, used to start listening for a matching
    // SMS message.
    val client = SmsRetriever.getClient(this /* context */)

    // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
    // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
    // action SmsRetriever#SMS_RETRIEVED_ACTION.

    // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
    // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
    // action SmsRetriever#SMS_RETRIEVED_ACTION.
    val task = client.startSmsRetriever()

    // Listen for success/failure of the start Task. If in a background thread, this
    // can be made blocking using Tasks.await(task, [timeout]);

    // Listen for success/failure of the start Task. If in a background thread, this
    // can be made blocking using Tasks.await(task, [timeout]);
    task.addOnSuccessListener {
        // Successfully started retriever, expect broadcast intent
        // ...
    }

    task.addOnFailureListener {
        // Failed to start retriever, inspect Exception for more details
        // ...
    }
}

/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
class SMSBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            if (status != null) {
                when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {  // Get SMS message contents
                        val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String
                        printLog("SMS message: ", message)
                        // Extract one-time code from the message and complete verification
                        // by sending the code back to your server.
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        // Waiting for SMS timed out (5 minutes)
                        // Handle the error ...
                    }
                }
            }
        }
    }
}
