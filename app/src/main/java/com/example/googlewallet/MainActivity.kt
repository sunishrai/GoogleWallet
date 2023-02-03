

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
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.googlewallet.databinding.ActivityMainBinding

import com.google.android.gms.pay.Pay
import com.google.android.gms.pay.PayApiAvailabilityStatus
import com.google.android.gms.pay.PayClient
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == addToGoogleWalletRequestCode) {
            when (resultCode) {
                RESULT_OK -> {
                    // Pass saved successfully. Consider informing the user.
                }
                RESULT_CANCELED -> {
                    // Save canceled
                }

                PayClient.SavePassesResult.SAVE_ERROR -> data?.let { intentData ->
                    val errorMessage = intentData.getStringExtra(PayClient.EXTRA_API_ERROR_MESSAGE)
                    // Handle error. Consider informing the user.
                }

                else -> {
                    // Handle unexpected (non-API) exception
                }
            }
        }
    }

    private val issuerEmail = ""
    private val issuerId = "3388000000022195306"
    private val passClass = "3388000000022195306.7a7f18ef-c069-4c54-be85-c0dcb01c2b0a"
    private val passId = UUID.randomUUID().toString()

    private val newObjectJson = """
        {
        "id": "3388000000022195306.7a7f18ef-c069-4c54-be85-c0dcb01c2b0a",
        "classTemplateInfo": {
          "cardTemplateOverride": {
            "cardRowTemplateInfos": [
              {
                "twoItems": {
                  "startItem": {
                    "firstValue": {
                      "fields": [
                        {
                          "fieldPath": "object.textModulesData['points']",
                        },
                      ],
                    },
                  },
                  "endItem": {
                    "firstValue": {
                      "fields": [
                        {
                          "fieldPath": "object.textModulesData['contacts']",
                        },
                      ],
                    },
                  },
                },
              },
            ],
          },
          "detailsTemplateOverride": {
            "detailsItemInfos": [
              {
                "item": {
                  "firstValue": {
                    "fields": [
                      {
                        "fieldPath": "class.imageModulesData['event_banner']",
                      },
                    ],
                  },
                },
              },
              {
                "item": {
                  "firstValue": {
                    "fields": [
                      {
                        "fieldPath": "class.textModulesData['game_overview']",
                      },
                    ],
                  },
                },
              },
              {
                "item": {
                  "firstValue": {
                    "fields": [
                      {
                        "fieldPath": "class.linksModuleData.uris['official_site']",
                      },
                    ],
                  },
                },
              },
            ],
          },
        },
        "imageModulesData": [
          {
            "mainImage": {
              "kind": "walletobjects#image",
              "sourceUri": {
                "uri": "https://storage.googleapis.com/wallet-lab-tools-codelab-artifacts-public/google-io-2021-card.png",
              },
              "contentDescription": {
                "kind": "walletobjects#localizedString",
                "defaultValue": {
                  "kind": "walletobjects#translatedString",
                  "language": "en",
                  "value": "Google I/O 2022 Banner",
                },
              },
            },
            "id": "event_banner",
          },
        ],
        "textModulesData": [
          {
            "header": "Gather points meeting new people at Google I/O",
            "body": "Join the game and accumulate points in this badge by meeting other attendees in the event.",
            "id": "game_overview",
          },
        ],
        "linksModuleData": {
          "uris": [
            {
              "uri": "https://io.google/2022/",
              "description": "Official I/O '22 Site",
              "id": "official_site",
            },
          ],
        },
      }
    """.trimIndent()
}