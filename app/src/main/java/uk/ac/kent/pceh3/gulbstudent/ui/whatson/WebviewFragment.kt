package uk.ac.kent.pceh3.gulbstudent.ui.whatson


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uk.ac.kent.pceh3.gulbstudent.R
import kotlinx.android.synthetic.main.fragment_webview.*

class WebviewFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_webview, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = this.arguments?.getString("url")
        webView.loadUrl(url)
    }

}
