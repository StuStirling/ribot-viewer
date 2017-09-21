package com.stustirling.ribotviewer.data

import com.stustirling.ribotviewer.data.api.GsonConverterFactoryProvider
import com.stustirling.ribotviewer.data.api.RetrofitProvider
import com.stustirling.ribotviewer.data.api.RibotService
import com.stustirling.ribotviewer.data.api.model.ApiRibot
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import java.util.*

/**
 * Created by Stu Stirling on 21/09/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class RibotServiceTest {

    private lateinit var ribotService : RibotService
    private val mockWebserver = MockWebServer()

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        val converterFactory = GsonConverterFactoryProvider().build()

        val url = mockWebserver.url("/test/")
        val retrofit = RetrofitProvider(url.toString(),converterFactory).build()

        ribotService = retrofit.create(RibotService::class.java)
    }

    @Test
    fun shouldReceiveAndParseRibots() {

        val jsonStream =
                RibotServiceTest::class.java.classLoader.getResourceAsStream("ribots.json")

        val ribotsResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(String(jsonStream.readBytes()))
        }

        mockWebserver.enqueue(ribotsResponse)

        val testSubscriber = ribotService.getRibots()
                .test()

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)

        val ribotList = testSubscriber.values()[0]
        Assert.assertEquals(2,ribotList.size)

        assertFirstRibot(ribotList[0])
    }

    @Test
    fun shouldFailWith404() {
        mockWebserver.enqueue(MockResponse().apply { setResponseCode(404) })

        ribotService.getRibots()
                .test()
                .assertError(HttpException::class.java)
                .assertError{ (it as HttpException).code() == 404 }
    }

    private fun assertFirstRibot(firstRibot: ApiRibot) {
        Assert.assertEquals("https://s3-eu-west-1.amazonaws.com/api.ribot.io/trevor_big.png",firstRibot.profile.avatar)
        Assert.assertEquals("trevor@ribot.co.uk",firstRibot.profile.email)
        Assert.assertEquals("#B60042",firstRibot.profile.color)
        Assert.assertEquals(true,firstRibot.profile.isActive)
        Assert.assertEquals("Trevor",firstRibot.profile.name.first)
        Assert.assertEquals("May",firstRibot.profile.name.last)

        val calendar = Calendar.getInstance().apply {
            set(1990,0,1,0,0,0)
            set(Calendar.MILLISECOND,0)
            timeZone = TimeZone.getTimeZone("UTC")}
        Assert.assertEquals(calendar.time,firstRibot.profile.dob)
    }



    @After
    fun tearDown() {
        mockWebserver.shutdown()
    }

}