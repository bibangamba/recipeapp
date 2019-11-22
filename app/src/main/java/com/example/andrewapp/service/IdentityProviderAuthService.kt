package com.example.andrewapp.service

import com.example.andrewapp.Constants.FIREBASE_IDP_AUTH_URL
import com.example.andrewapp.db.User
import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * interface representation of the Firebase Identity Provider REST endpoint(s)
 */
interface IdentityProviderAuthService {

    /**
     * Identity provider sign in endpoint
     *
     *  @param postBody a string that contains oauthToken from a federated identity provider and the provider Id
     *                  e.g. "id_token={$oauthToken}&providerId=google.com"
     *  @param requestUri a url string, to which the identity provider should redirect
     *  @param returnIdpCredential boolean to ensure an Idp credential is returned. should always be true, hence the default
     *  @param returnSecureToken boolean to ensure a secure token is returned. should always be true, hence the default
     */
    @FormUrlEncoded
    @POST
    fun idpSignIn(
        @Url authUrl: String = FIREBASE_IDP_AUTH_URL,
        @Field("postBody") postBody: String,
        @Field("requestUri") requestUri: String,
        @Field("returnIdpCredential") returnIdpCredential: Boolean = true,
        @Field("returnSecureToken") returnSecureToken: Boolean = true
    ): Flowable<User>
}
