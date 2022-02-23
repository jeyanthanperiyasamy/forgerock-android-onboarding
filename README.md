# forgerock-android-onboarding
## Installation

1. Added the ForgeRock dependency
```groovy
dependencies {
    implementation 'org.forgerock:forgerock-auth:3.1.2'
}
```

2. Add Android configuration for ForgeRock SDK in strings.xml
```
 <!-- OAuth 2.0 Client Details -->
    <string name="forgerock_oauth_client_id" translatable="false">jeyonboarding</string>
    <string name="forgerock_oauth_redirect_uri" translatable="false">com.forgerock.android:/oauth2redirect</string>
    <string name="forgerock_oauth_scope" translatable="false">openid profile email address phone</string>
    <integer name="forgerock_oauth_threshold" translatable="false">30</integer>
    <!-- AM Instance Details -->
    <string name="forgerock_url" translatable="false">https://openam-forgerrock-sdks.forgeblocks.com/am</string>
    <string name="forgerock_cookie_name" translatable="false">iPlanetDirectoryPro</string>
    <string name="forgerock_realm" translatable="false">alpha</string>
    <integer name="forgerock_timeout" translatable="false">30</integer>
    <!-- Single Sign-On Details -->
    <string name="forgerock">ForgeRock SDK Tutorial</string>
    <string name="forgerock_account_name" translatable="false">Demo User</string>
    <!-- AM Tree Details -->
    <string name="forgerock_auth_service" translatable="false">jeySdkAuthenticationTree</string>
```
3. Created the Application class and start the Forgerock authentication
```
   FRAuth.start(this)
```
## Embedded Login
4. create a MainActivity class and have a login, status and logout button 
5. Add Forgerock Login , Logout Listener for the buttons
6. Create a Dialog Fragment with username, password, cancel, next button   
6. On click of Login button, Invoke the DialogFragment from success of Journey/tree callback to display username/password dialog
8. Add the Listener for the next button on the Dialog fragment which will take the input from username and password field and execute the three step login process
      1. You will receive a SSO token or tokenId from the authentication tree response
      2. Use that SSO token and PKCE you will get the redirect URI and authcode 
      3. Final step is to get the oauth tokens
9. update the Login logout status in Main activity once you received the access token, refresh token , idToken
10. Get the User Info in a different Fragment screen
## Centralized Login
11. create a build variant to run two different product flavours in build.gradle, so that you can use the same code to run centralized and embedded login
12. Add the URI scheme for the app for centralize login
13. Add the openId auth dependency
```
dependencies {
    implementation 'net.openid:appauth:0.7.1'
    }
```
14. make sure your gradle properties has 
```
android.useAndroidX=true
android.enableJetifier=true
```
15. Add queries and RedirectUriReceiverActivity to open the login in browser
16. on click of login buton now invoke the FRUser.browser().appAuthConfigurer()
