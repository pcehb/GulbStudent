package uk.ac.kent.pceh3.gulbstudent.model

class Comp {

    var closeDate: String?
    var description : String?
    var photoURL : String?
    var title: String?


    constructor():this("","", "",  "") {

    }

    constructor(closeDate: String?, description: String?, photoURL: String?, title: String?) {
        this.closeDate = closeDate
        this.description = description
        this.photoURL = photoURL
        this.title = title
    }

}