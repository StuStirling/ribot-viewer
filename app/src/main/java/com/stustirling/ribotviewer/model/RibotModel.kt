package com.stustirling.ribotviewer.model

import android.os.Parcel
import android.os.Parcelable
import com.stustirling.ribotviewer.domain.model.Ribot
import java.util.*

/**
 * Created by Stu Stirling on 24/09/2017.
 */
data class RibotModel(val id: String,
                      val firstName: String,
                      val lastName: String,
                      val email: String,
                      val hexColor: String,
                      val avatar: String?,
                      val dataOfBirth: Date,
                      val bio: String?,
                      val isActive: Boolean) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readSerializable() as Date,
            source.readString(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(firstName)
        writeString(lastName)
        writeString(email)
        writeString(hexColor)
        writeString(avatar)
        writeSerializable(dataOfBirth)
        writeString(bio)
        writeInt((if (isActive) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RibotModel> = object : Parcelable.Creator<RibotModel> {
            override fun createFromParcel(source: Parcel): RibotModel = RibotModel(source)
            override fun newArray(size: Int): Array<RibotModel?> = arrayOfNulls(size)
        }
    }
}

fun Ribot.toRibotModel() : RibotModel =
    RibotModel(id,firstName,lastName, email, hexColor, avatar, dataOfBirth, bio, isActive)
