package uk.ac.kent.pceh3.gulbstudent.model

/**
 * Created by pceh3 on 07/06/2019.
 */
class Deal{
    var code: String?
    var description : String?
    var photoURL : String?


    constructor():this("","", "") {

    }

    constructor(code: String?, description: String?, photoURL: String?) {
        this.code = code
        this.description = description
        this.photoURL = photoURL
    }
}