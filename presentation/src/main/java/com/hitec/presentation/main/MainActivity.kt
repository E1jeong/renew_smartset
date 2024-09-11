package com.hitec.presentation.main

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.hitec.presentation.login.LoginActivity
import com.hitec.presentation.nfc_lib.NfcManager
import com.hitec.presentation.theme.RenewSmartSetTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var nfcManager: NfcManager
    private val mainViewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        nfcManager.startNfcManager(this, this, 1)

        setContent {
            RenewSmartSetTheme {
                MainNavHost(sharedViewModel = mainViewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.i("NFC_TEST", "onNewIntent - 01");

        val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        if (tag != null) {
            Log.i("NFC_TEST", "Tag detected: $tag")
            nfcManager.handleIntent(intent)
        } else {
            Log.i("NFC_TEST", "No NFC tag detected.")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("NFC_TEST", "onResume => 01")
        if (nfcManager.nfcAdapter != null) {
            nfcManager.NfcAdapterEnableForeground()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.i("NFC_TEST", "onPause => 01")

        if (nfcManager.nfcAdapter != null) {
            Log.i("NFC_TEST", "onPause => 02")
            nfcManager.nfcAdapter.disableForegroundDispatch(this)
        }
    }
}