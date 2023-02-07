/*
 * Copyright 2022 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.googlewallet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.googlewallet.databinding.ActivityMainBinding

import com.google.android.gms.pay.Pay
import com.google.android.gms.pay.PayApiAvailabilityStatus
import com.google.android.gms.pay.PayClient
import java.util.Date
import java.util.UUID


/**
 * Checkout implementation for the app
 */
class MainActivity : AppCompatActivity() {

    private lateinit var layout: ActivityMainBinding
    private lateinit var addToGoogleWalletButton: View

    // TODO: Add a request code for the save operation
    private val addToGoogleWalletRequestCode = 1000

    // TODO: Create a client to interact with the Google Wallet API
    private lateinit var walletClient: PayClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Instantiate the Pay client
        walletClient = Pay.getClient(this)

        // Use view binding to access the UI elements
        layout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(layout.root)

        // TODO: Check if the Google Wallet API is available
        fetchCanUseGoogleWalletApi()

        // TODO: Set an on-click listener on the "Add to Google Wallet" button
        addToGoogleWalletButton = layout.addToGoogleWalletButton.root
        addToGoogleWalletButton.setOnClickListener {
            walletClient.savePasses(

//                TODO("Token goes here"),
                newObjectJson,
                this,
                addToGoogleWalletRequestCode
            )
        }
    }

    // TODO: Create a method to check for the Google Wallet SDK and API
    private fun fetchCanUseGoogleWalletApi() {
        walletClient
            .getPayApiAvailabilityStatus(PayClient.RequestType.SAVE_PASSES)
            .addOnSuccessListener { status ->
                if (status == PayApiAvailabilityStatus.AVAILABLE)
                    layout.passContainer.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                // Hide the button and optionally show an error message
            }
    }

    // TODO: Handle the result
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == addToGoogleWalletRequestCode) {
            when (resultCode) {
                RESULT_OK -> {
                    Toast.makeText(this, "SUCCESS $resultCode", Toast.LENGTH_LONG).show()
                }

                RESULT_CANCELED -> {
                    Toast.makeText(this, "CANCELED $resultCode", Toast.LENGTH_LONG).show()
                    // Save canceled
                }

                PayClient.SavePassesResult.SAVE_ERROR -> data?.let { intentData ->
                    val errorMessage = intentData.getStringExtra(PayClient.EXTRA_API_ERROR_MESSAGE)
                    Toast.makeText(this, "$errorMessage $resultCode", Toast.LENGTH_LONG).show()
                    Log.v("walletError", errorMessage!!)
                    // Handle error. Consider informing the user.
                }

                else -> {
                    Toast.makeText(this, "non-API exception $resultCode", Toast.LENGTH_LONG).show()
                    // Handle unexpected (non-API) exception
                }
            }
        }
    }

//    private val issuerEmail = ""
//    private val issuerId = "3388000000022196161"
//    private val passClass = "3388000000022196161.6d5aa886-ddfe-4520-9dfa-0dcdb5e56caf"
//    private val passId = UUID.randomUUID().toString()

    private val issuerEmail = ""
    private val issuerId = "3388000000022194436"
    private val passClass = "3388000000022194436.123"
//    private val passClass = "3388000000022194436.0987"
    private val passId = UUID.randomUUID().toString()

    private val newObjectJson = """
        {
  "iss": "$issuerEmail",
  "aud": "google",
  "typ": "savetowallet",
  "iat": ${Date().time/1000L},
  "origins": [
    "www.example.com"
  ],
  "payload": {
    "genericObjects": [
      {
        "id": "$issuerId.$passId",
        "classId": "$passClass",
        "logo": {
          "sourceUri": {
            "uri": "https://storage.googleapis.com/wallet-lab-tools-codelab-artifacts-public/pass_google_logo.jpg"
          },
          "contentDescription": {
            "defaultValue": {
              "language": "en",
              "value": ""
            }
          }
        },
        "cardTitle": {
          "defaultValue": {
            "language": "en",
            "value": "Google I/O [DEMO ONLY]"
          }
        },
        "subheader": {
          "defaultValue": {
            "language": "en",
            "value": "Attendee"
          }
        },
        "header": {
          "defaultValue": {
            "language": "en",
            "value": "Alex McJacobs"
          }
        },
        "textModulesData": [
          {
            "id": "points",
            "header": "POINTS",
            "body": "1112"
          },
          {
            "id": "contacts",
            "header": "CONTACTS",
            "body": "79"
          }
        ],
        "barcode": {
          "type": "QR_CODE",
          "value": "BARCODE_VALUE",
          "alternateText": null
        },
        "hexBackgroundColor": "#4285f4",
        "heroImage": {
          "sourceUri": {
            "uri": "https://storage.googleapis.com/wallet-lab-tools-codelab-artifacts-public/google-io-hero-demo-only.png"
          },
          "contentDescription": {
            "defaultValue": {
              "language": "en",
              "value": "HERO_IMAGE_DESCRIPTION"
            }
          }
        }
      }
    ]
  }
}"""
}