package com.forgerock.android

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import net.openid.appauth.AuthorizationRequest
import org.forgerock.android.auth.*
import org.forgerock.android.auth.BuildConfig

interface ActivityListener {
    fun logout()
}

class MainActivity: AppCompatActivity(), NodeListener<FRUser>, ActivityListener {

    private val status: TextView by lazy { findViewById(R.id.status) }
    private val loginButton: Button by lazy { findViewById(R.id.login) }
    private val logoutButton: Button by lazy { findViewById(R.id.logout) }
    private val classNameTag = MainActivity::class.java.name
    private var userInfoFragment: UserInfoFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateStatus()
        loginButton.setOnClickListener {
            if(com.forgerock.android.BuildConfig.embeddedLogin) {
                FRUser.login(applicationContext, this)
            }
            else {
                centralizedLogin()
            }
        }
        logoutButton.setOnClickListener {
            FRUser.getCurrentUser().logout()
            updateStatus()
        }
    }

    private fun centralizedLogin() {
        FRUser.browser().appAuthConfigurer()
            .authorizationRequest { r: AuthorizationRequest.Builder ->
                // Add a login hint parameter about the user:
                r.setLoginHint("demo@example.com")
                // Request that the user re-authenticates:
                r.setPrompt("login")
            }
            .customTabsIntent { t: CustomTabsIntent.Builder ->
                // Customize the browser:
                t.setShowTitle(true)
                t.setToolbarColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            }.done()
            .login(this, object : FRListener<FRUser?> {
                override fun onSuccess(result: FRUser?) {
                    Logger.debug(classNameTag, result?.accessToken?.value)
                    getUserInfo(result)
                }
                override fun onException(e: java.lang.Exception) {
                    Logger.error(classNameTag, e.message)
                }
            })
    }


    private fun updateStatus() {
        runOnUiThread {
            loginButton.visibility = View.VISIBLE
            logoutButton.visibility = View.VISIBLE
            status.visibility = View.VISIBLE
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

    private fun getUserInfo(result: FRUser?) {
        result?.getAccessToken(object : FRListener<AccessToken> {
            override fun onSuccess(token: AccessToken) {
                runOnUiThread {
                    updateStatus()
                    loginButton.visibility = View.GONE
                    logoutButton.visibility = View.GONE
                    status.visibility = View.GONE
                    launchUserInfoFragment(token, result)
                }
            }
            override fun onException(e: java.lang.Exception) {}
        })
    }


    private fun launchUserInfoFragment(token: AccessToken, result: FRUser?) {
        val combinedToken = "id-token -- " + token.idToken + "\n" + "refresh-token --" + token.refreshToken + "\n" + "accessToken ==" + result?.accessToken?.value
        userInfoFragment = UserInfoFragment.newInstance(combinedToken, this@MainActivity)
        userInfoFragment?.let {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, it).commit()
        }
    }

    override fun onSuccess(result: FRUser?) {
        getUserInfo(result)
    }

    override fun onException(e: Exception?) {
        Logger.error(classNameTag, e?.message, e)
    }

    override fun onCallbackReceived(node: Node?) {
        val fragment: NodeDialogFragment = NodeDialogFragment.newInstance(node)
        fragment.show(supportFragmentManager, NodeDialogFragment::class.java.name)
    }

    override fun logout() {
        FRUser.getCurrentUser().logout()
        userInfoFragment?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
        updateStatus()
    }
}

