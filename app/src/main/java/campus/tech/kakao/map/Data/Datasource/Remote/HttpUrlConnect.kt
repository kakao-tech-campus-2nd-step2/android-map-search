package campus.tech.kakao.map.Data.Datasource.Remote

import android.util.Log
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.Data.Datasource.Remote.Response.Document
import campus.tech.kakao.map.Data.Datasource.Remote.Response.Meta
import campus.tech.kakao.map.Data.Datasource.Remote.Response.SameName
import campus.tech.kakao.map.Data.Datasource.Remote.Response.SearchResponse
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class HttpUrlConnect : RemoteService{

    override fun getPlaceResponse(query: String): List<Document> {
        val result = mutableListOf<Document>()
        var response: SearchResponse? = null

        val thread = Thread {
            response = request(query, START_PAGE)
        }
        thread.start()
        thread.join()

        response?.apply {
            result.addAll(this.documents)
            val pageCount : Int = this.meta.pageable_count ?: 0

            if (pageCount > MAX_PAGE) {
                val threads = mutableListOf<Thread>()

                for (i in START_PAGE + 1..MAX_PAGE) {
                    val t = Thread {
                        request(query, i)?.apply {
                            result.addAll(this.documents)
                        }
                    }
                    threads.add(t)
                    t.start()
                }

                threads.forEach { it.join() }
            }
        }

        return result
    }


    private fun request(query: String, page: Int): SearchResponse? {
        val url = URL(BASE + URL)
        val conn = url.openConnection() as HttpsURLConnection

        conn.setReadTimeout(READ_TIMEOUT)
        conn.setConnectTimeout(CONNECT_TIMEOUT)
        conn.setRequestMethod("GET")
        conn.setDoOutput(true)
        conn.setRequestProperty("authorization", KEY)

        val body = "query=${query}&page=${page}"

        val os = conn.outputStream
        val writer = BufferedWriter(
            OutputStreamWriter(os, "UTF-8")
        )
        writer.write(body)
        writer.flush()
        writer.close()
        os.close()

        val responseCode = conn.getResponseCode()

        when (responseCode) {
            HttpsURLConnection.HTTP_OK -> {
                val br = BufferedReader(InputStreamReader(conn.inputStream))
                while (true) {
                    val line = br.readLine() ?: break

                    val jsonObject = JSONObject(line)
                    val meta = Meta.fromJSON(jsonObject.getJSONObject("meta"))
                    val document = Document.fromJSON(jsonObject.getJSONArray("documents"))

                    return SearchResponse(document,meta)
                }
            }

            else -> {
                return null
            }
        }
        return null
    }

    companion object {
        private const val BASE = BuildConfig.BASE_URL
        private const val KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
        private const val URL = "v2/local/search/keyword.json"
        private const val READ_TIMEOUT = 1000
        private const val CONNECT_TIMEOUT = 1500
        private const val MAX_PAGE = 2
        private const val START_PAGE = 1
    }
}