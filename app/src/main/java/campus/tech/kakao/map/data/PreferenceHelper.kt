package campus.tech.kakao.map.data

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private const val PREF_NAME = "kakao_map"
    private const val PREF_KEY_SEARCH_QUERY = "search_query"

    fun defaultPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getSearchHistory(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(PREF_KEY_SEARCH_QUERY, emptySet()) ?: emptySet()
    }

    fun removeSearchQuery(context: Context, query: String) {
        val searchHistory = getSearchHistory(context).toMutableSet()
        searchHistory.remove(query)
        defaultPrefs(context).edit().putStringSet(PREF_KEY_SEARCH_QUERY, searchHistory).apply()
    }

    fun saveSearchQuery(context: Context, query: String) {
        val searchHistory = getSearchHistory(context).toMutableSet()
        searchHistory.add(query)
        defaultPrefs(context).edit().putStringSet(PREF_KEY_SEARCH_QUERY, searchHistory).apply()
    }
}