package com.example.geeksasaeng

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Base.Example.ExampleFragment
import com.example.geeksasaeng.databinding.ActivitySplashBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashActivity: BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val handler = Handler(Looper.getMainLooper())
        getHashKey();
        handler.postDelayed({
            // Test
            // changeActivity(MainActivity::class.java)

            // Original
            changeActivity(LoginActivity::class.java)
            finish()
        }, 1500)
    }

    private fun getHashKey(){
        var packageInfo: PackageInfo? = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (e: PackageManager.NameNotFoundException ) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");
        for (signature : Signature in packageInfo!!.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
}