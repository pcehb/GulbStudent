package uk.ac.kent.pceh3.gulbstudent.model

/**
 * Created by pceh3 on 07/06/2019.
 */
class Deal{
    var code: String?
    var description : String?


    constructor():this("","") {

    }

    constructor(code: String?, description: String?) {
        this.code = code
        this.description = description
    }
}