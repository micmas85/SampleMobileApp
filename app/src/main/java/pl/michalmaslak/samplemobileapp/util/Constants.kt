package pl.michalmaslak.samplemobileapp.util


class Constants {

    companion object {
        const val BASE_URL: String = "https://michalmaslak-sampleapp.herokuapp.com/api/"
        const val NETWORK_TIMEOUT = 6000L
        const val CACHE_TIMEOUT = 2000L
        const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing
        const val PAGINATION_PAGE_SIZE = 10
    }
}
