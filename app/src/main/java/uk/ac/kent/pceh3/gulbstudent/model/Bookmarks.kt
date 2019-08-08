package uk.ac.kent.pceh3.gulbstudent.model

class Bookmarks {
    var title : String?
    var date : Int?
    var month : Int?
    var year : Int?

    constructor():this("", 1, 1, 2019) {
    }

    constructor(title: String?, date: Int?, month: Int?, year: Int?) {
        this.title = title
        this.date = date
        this.month = month
        this.year = year
    }
}