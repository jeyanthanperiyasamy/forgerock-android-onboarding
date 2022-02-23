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
4. create a MainActivity class and have a login, status and logout button 
5. Add Forgerock Login , Logout Listener for the buttons