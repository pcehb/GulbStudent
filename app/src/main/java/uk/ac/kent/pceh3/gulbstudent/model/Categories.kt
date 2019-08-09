package uk.ac.kent.pceh3.gulbstudent.model

class Categories {
    var archive: Boolean = false
    var audioDescribed: Boolean = false
    var boing: Boolean = false
    var cafe: Boolean = false
    var captionedSubtitles: Boolean = false
    var comedy: Boolean = false
    var family: Boolean = false
    var festival: Boolean = false
    var foreign: Boolean = false
    var music: Boolean = false
    var live: Boolean = false
    var relaxed: Boolean = false
    var talks: Boolean = false
    var theatreDance: Boolean = false
    var workshops: Boolean = false


    constructor():this(false, false, false, false, false,
            false, false, false, false,false, false, false,
            false, false, false)

    constructor(archive: Boolean, audioDescribed: Boolean, boing: Boolean, cafe: Boolean, captionedSubtitles: Boolean, comedy: Boolean,
                family: Boolean, festival: Boolean, foreign: Boolean, music: Boolean, live: Boolean, relaxed: Boolean,
                talks: Boolean, theatreDance: Boolean, workshops: Boolean) {
        this.archive = archive
        this.audioDescribed = audioDescribed
        this.boing = boing
        this.cafe = cafe
        this.captionedSubtitles = captionedSubtitles
        this.comedy = comedy
        this.family = family
        this.festival = festival
        this.foreign = foreign
        this.music = music
        this.live = live
        this.relaxed = relaxed
        this.talks = talks
        this.theatreDance = theatreDance
        this.workshops = workshops
    }
}