package uk.ac.kent.pceh3.gulbstudent.model

class WhatsOn {

    var url: String?
    var imageUrl : String?
    var label : String?
    var title : String?
    var excerpt : String?
    var date : String?
    var bookLink : String?

    constructor(url: String?, imageUrl: String?, label: String?, title: String?, excerpt: String?, date: String?, bookLink: String?) {
        this.url = url
        this.imageUrl = imageUrl
        this.label = label
        this.title = title
        this.excerpt = excerpt
        this.date = date
        this.bookLink = bookLink
    }

}