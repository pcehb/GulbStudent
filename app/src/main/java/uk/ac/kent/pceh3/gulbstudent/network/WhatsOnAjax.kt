package uk.ac.kent.pceh3.gulbstudent.network

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks.await
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn

/**
 * Created by pceh3 on 14/07/2019.
 */
class WhatsOnAjax {
    val whatsOnList = MutableLiveData<List<WhatsOn>>()

    fun getWhatsOn(): LiveData<List<WhatsOn>> {

        val eventList = ArrayList<WhatsOn>()
        doAsyncResult {
            //1. Fetching the HTML from a given URL
            Jsoup.connect("https://thegulbenkian.co.uk/wp-admin/admin-ajax.php?numPosts=200&pageNumber=&event_type=&event_category=&searchEvents=&start_date=&end_date=&action=whatson_loop_handler").get().run {
                //2. Parses and scrapes the HTML response
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