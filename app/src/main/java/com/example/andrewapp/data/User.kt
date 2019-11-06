package com.example.andrewapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @ColumnInfo(name = "federated_id") var federatedId: String = "",
    @ColumnInfo(name = "provider_id") var providerId: String = "",
    @ColumnInfo(name = "uid") var localId: String = "",
    @ColumnInfo(name = "email_verified") var emailVerified: Boolean = false,
    @PrimaryKey var email: String = "",
    @ColumnInfo(name = "oauth_id_token") var oauthIdToken: String = "",
    @ColumnInfo(name = "first_name") var firstName: String = "",
    @ColumnInfo(name = "last_name") var lastName: String = "",
    @ColumnInfo(name = "full_name") var fullName: String = "",
    @ColumnInfo(name = "display_name") var displayName: String = "",
    @ColumnInfo(name = "photo_url") var photoUrl: String = "",
    @ColumnInfo(name = "id_token") var idToken: String = "",
    @ColumnInfo(name = "refresh_token") var refreshToken: String = "",
    @ColumnInfo(name = "expires_in") var expiresIn: String = "",
    @ColumnInfo(name = "need_confirmation") var needConfirmation: Boolean = false
)
