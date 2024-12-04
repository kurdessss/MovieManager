package com.example.ndroidapp.Domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FilmItem(
    @SerializedName("id") val id: Int,  // Добавим id
    @SerializedName("poster") val poster: String,
    @SerializedName("title") val title: String,
    @SerializedName("imdb_rating") val imdbRating: String,
    @SerializedName("runtime") val runtime: String,
    @SerializedName("plot") val plot: String,
    @SerializedName("actors") val actors: String,
    var videoUrl: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),  // Чтение id из Parcel
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)  // Запись id в Parcel
        parcel.writeString(poster)
        parcel.writeString(title)
        parcel.writeString(imdbRating)
        parcel.writeString(runtime)
        parcel.writeString(plot)
        parcel.writeString(actors)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<FilmItem> {
        override fun createFromParcel(parcel: Parcel): FilmItem {
            return FilmItem(parcel)
        }

        override fun newArray(size: Int): Array<FilmItem?> {
            return arrayOfNulls(size)
        }
    }
}
