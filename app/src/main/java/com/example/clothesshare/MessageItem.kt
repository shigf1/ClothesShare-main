package com.example.clothesshare

import android.os.Parcel
import android.os.Parcelable

/* data class for the information messages hold in the messaging tab of app*/
data class MessageItem(
    val profile_pic: String ?= null,
    val username: String ?= null,
    val most_recent_message: String ?= null,
    val most_recent_message_date: String ?= null,
    val conversationId: String? = null  // Add this field
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(profile_pic)
        parcel.writeString(username)
        parcel.writeString(most_recent_message)
        parcel.writeString(most_recent_message_date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageItem> {
        override fun createFromParcel(parcel: Parcel): MessageItem {
            return MessageItem(parcel)
        }

        override fun newArray(size: Int): Array<MessageItem?> {
            return arrayOfNulls(size)
        }
    }
}