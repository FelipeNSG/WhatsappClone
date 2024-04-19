package com.example.whatsappclone.data

import android.content.Context
import android.util.Log
import com.example.whatsappclone.utils.getActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit

class AuthenticationFirebaseManager {

    private val authentication = FirebaseAuth.getInstance()
    val getUserId = authentication.currentUser?.uid
    private val authOut = Firebase.auth.signOut()
    var storedVerificationId: String = ""

    fun onLoginClicked(context: Context, phoneNumber: String, onCodeSent: (String) -> Unit){

        authentication.setLanguageCode("en")
        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                println("verification completed")
                signInWithPhoneAuthCredential(context, credential)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                println("verification failed$p0")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                println( "code sent$verificationId")
                storedVerificationId = verificationId
                onCodeSent( storedVerificationId)
            }

        }
        val options = context.getActivity()?.let {
            PhoneAuthOptions.newBuilder(authentication)
                .setPhoneNumber(phoneNumber) //should go with the lada for example: +529212223333
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(it)
                .setCallbacks(callback)
                .build()
        }
        if (options != null) {
            Log.d("phoneBook", options.toString())
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

  private fun signInWithPhoneAuthCredential(context: Context, credential: PhoneAuthCredential) {
        context.getActivity()?.let {
            authentication.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = task.result?.user
                        println( "logged in")
                    } else {
                        // Sign in failed, display a message and update the UI
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            println( "wrong otp")
                        }
                        // Update UI
                    }
                }
        }
    }

 fun verifyPhoneNumberWithCode(context: Context, verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(context, credential)
    }
}