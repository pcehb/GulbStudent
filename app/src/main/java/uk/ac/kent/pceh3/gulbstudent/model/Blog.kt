package uk.ac.kent.pceh3.gulbstudent.model

/**
 * Created by pceh3 on 14/07/2019.
 */
class Blog {
    var article: String?
    var date : String?
    var title : String?
    var photoURL : String?


    constructor():this("","", "", "") {

    }

    constructor(article: String?, date: String?, title: String?, photoURL : String?) {
        this.article = article
        this.date = date
        this.title = title
        this.photoURL = photoURL
    }
}