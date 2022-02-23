package com.forgerock.android

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.forgerock.android.auth.FRUser
import org.forgerock.android.auth.Logger
import org.forgerock.android.auth.Node
import org.forgerock.android.auth.NodeListener

class MainActivity: AppCompatActivity(), NodeListener<FRUser> {

    private val status: TextView by lazy { findViewById(R.id.status) }
    private val loginButton: Button by lazy { findViewById(R.id.login) }
    private val logoutButton: Button by lazy { findViewById(R.id.logout) }
    private val classNameTag = MainActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateStatus()
        loginButton.setOnClickListener {
            FRUser.login(applicationContext, this)
        }
        logoutButton.setOnClickListener {
            FRUser.getCurrentUser().logout()
            updateStatus()
        }
    }


    private fun updateStatus() {
        runOnUiThread {
            if(FRUser.getCurrentUser() == null) {
                status.text = "User is not authenticated"
                loginButton.apply { this.isEnabled = true }
                logoutButton.apply { this.isEnabled = false }
            } else {
                status.text = "User is authenticated"
                loginButton.apply { this.isEnabled = false }
                logoutButton.apply { this.isEnabled = true }
            }
        }
    }

    override fun onSuccess(result: FRUser?) {
    }

    override fun onException(e: Exception?) {
        Logger.error(classNameTag, e?.message, e)
    }

    override fun onCallbackReceived(node: Node?) {
        Logger.debug(classNameTag,"i am here")
    }
}

