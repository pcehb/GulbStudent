package uk.ac.kent.pceh3.gulbstudent.model

import android.os.Parcel
import android.os.Parcelable

class WhatsOn : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        throw NotImplementedError()
    }

    override fun describeContents(): Int {
        throw NotImplementedError()
    }

    var url: String?
    var imageUrl : String?
    var label : String?
    var title : String?
    var excerpt : String?
    var date : String?
    var bookLink : String?
    var index : Int?
    var bookmarked : Boolean?

    constructor(url: String?, imageUrl: String?, label: String?, title: String?, excerpt: String?, date: String?, bookLink: String?, index: Int?, bookmarked: Boolean?) {
        this.url = url
        this.imageUrl = imageUrl
        this.label = label
        this.title = title
        this.excerpt = excerpt
        this.date = date
        this.bookLink = bookLink
        this.index = index
        this.bookmarked = bookmarked
    }

}