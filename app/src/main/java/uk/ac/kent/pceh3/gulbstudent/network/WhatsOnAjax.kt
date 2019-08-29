package uk.ac.kent.pceh3.gulbstudent.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn

// class to get what's on show data from Gulb website
class WhatsOnAjax {

    val whatsOnList = MutableLiveData<List<WhatsOn>>()

    fun getWhatsOn(search: String, eventType : String, startDate : String, endDate : String): LiveData<List<WhatsOn>> {

        val eventList = ArrayList<WhatsOn>()
        doAsyncResult {
            //fetching the HTML from gulb site URL

            Jsoup.connect("https://thegulbenkian.co.uk/wp-admin/admin-ajax.php?numPosts=200&pageNumber="+eventType+"&event_category=&searchEvents="+search+"&start_date="+startDate+"&end_date="+endDate+"&action=whatson_loop_handler").get().run {
                //parses and scrapes the HTML response
                select("div.item-wrapper").forEachIndexed { index, element ->
                    val labelAnchor = element.select("div.image-container a div.labels")
                    val label = labelAnchor.text()
                    val bookLinkAnchor = element.select("div.text-container div.event-bottom a")
                    val bookLink = bookLinkAnchor.attr("href")
                    val urlAnchor = element.select("div.image-container a")
                    val url = urlAnchor.attr("href")
                    val imageAnchor = element.select("div.image-container a img")
                    val image = imageAnchor.attr("src")
                    val titleAnchor = element.select("div.text-container h4 a")
                    val title = titleAnchor.text()
                    val excerptAnchor = element.select("div.text-container div.excerpt p")
                    val excerpt = excerptAnchor.text()
                    val dateAnchor = element.select("div.text-container div.event-bottom div.date")
                    val date = dateAnchor.text()

                    val event = WhatsOn(url, image, label, title, excerpt, date, bookLink, index)
                    eventList.add(event)
                }
            }

            uiThread {
                whatsOnList.value = eventList
            }
        }


        return whatsOnList

    }

}