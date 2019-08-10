package uk.ac.kent.pceh3.gulbstudent.model

import android.os.Parcel
import android.os.Parcelable

class WhatsOn(url: String?, imageUrl: String?, label: String?, title: String?, excerpt: String?, date: String?, bookLink: String?, index: Int?) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(url)
        dest.writeString(imageUrl)
        dest.writeString(label)
        dest.writeString(title)
        dest.writeString(excerpt)
        dest.writeString(date)
        dest.writeString(bookLink)
        dest.writeInt(index!!)
    }

    override fun describeContents(): Int {
        throw NotImplementedError()
    }

    var url: String? = url
    var imageUrl : String? = imageUrl
    var label : String? = label
    var title : String? = title
    var excerpt : String? = excerpt
    var date : String? = date
    var bookLink : String? = bookLink
    var index : Int? = index

    constructor(parcel: Parcel) : this(null, null, null, null, null, null, null, null) {
        url = parcel.readString()
        imageUrl = parcel.readString()
        label = parcel.readString()
        title = parcel.readString()
        excerpt = parcel.readString()
        date = parcel.readString()
        bookLink = parcel.readString()
        index = parcel.readValue(Int::class.java.classLoader) as? Int
    }

//    constructor(url: String?, imageUrl: String?, label: String?, title: String?, excerpt: String?, date: String?, bookLink: String?, index: Int?) {
//        this.url = url
//        this.imageUrl = imageUrl
//        this.label = label
//        this.title = title
//        this.excerpt = excerpt
//        this.date = date
//        this.bookLink = bookLink
//        this.index = index
//    }

    companion object CREATOR : Parcelable.Creator<WhatsOn> {
        override fun createFromParcel(parcel: Parcel): WhatsOn {
            return WhatsOn(parcel)
        }

        override fun newArray(size: Int): Array<WhatsOn?> {
            return arrayOfNulls(size)
        }
    }

}