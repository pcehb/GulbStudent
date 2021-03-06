package uk.ac.kent.pceh3.gulbstudent.model

// Class for user bookmarks of shows

class Bookmarks {
    var title : String?
    var index : String?
    var date : Int?
    var month : Int?
    var year : Int?
    var id : Int?
    var photoURL: String?
    var description: String?

    constructor() : this("", "", 1, 1, 2019, 0, "", "")

    constructor(title: String?, index: String?, date: Int?, month: Int?, year: Int?, id: Int?, photoURL: String?, description: String?) {
        this.title = title
        this.index = index
        this.date = date
        this.month = month
        this.year = year
        this.id = id
        this.photoURL = photoURL
        this.description = description
    }
}