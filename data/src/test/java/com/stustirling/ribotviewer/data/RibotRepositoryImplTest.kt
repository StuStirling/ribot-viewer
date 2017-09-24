package com.stustirling.ribotviewer.data

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.stustirling.ribotviewer.data.api.RibotService
import com.stustirling.ribotviewer.data.api.model.ApiRibot
import com.stustirling.ribotviewer.data.local.dao.RibotDao
import com.stustirling.ribotviewer.data.local.model.LocalRibot
import com.stustirling.ribotviewer.domain.RefreshTrigger
import com.stustirling.ribotviewer.domain.Resource
import com.stustirling.ribotviewer.domain.RibotRepository
import com.stustirling.ribotviewer.domain.model.Ribot
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.net.SocketTimeoutException
import java.util.*

/**
 * Created by Stu Stirling on 24/09/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class RibotRepositoryImplTest {
    companion object {
        private const val ID = "testid"
        private const val EMAIL = "testemail"
        private const val FIRST_NAME = "testfirst"
        private const val LAST_NAME = "testlast"
        private const val AVATAR = "testlast"
        private val DOB = Date()
        private const val BIO = "testbio"
        private const val ACTIVE = true
        private const val COLOR = "testcolor"

        private fun buildApiRibot() : ApiRibot =
                ApiRibot(ApiRibot.ApiRibotProfile(
                        ID,
                        ApiRibot.ApiRibotProfile.ApiRibotName(FIRST_NAME, LAST_NAME),
                        EMAIL,
                        COLOR,
                        AVATAR,
                        DOB,
                        BIO,
                        ACTIVE))
        private fun buildLocalRibot() : LocalRibot =
                LocalRibot(ID,
                        FIRST_NAME,
                        LAST_NAME,
                        EMAIL,
                        DOB,
                        COLOR,
                        BIO,
                        AVATAR,
                        ACTIVE)
        private fun buildDomainRibot() : Ribot =
                Ribot(ID,
                        FIRST_NAME,
                        LAST_NAME,
                        EMAIL,

                        COLOR,
                        AVATAR,
                        DOB,
                        BIO,
                        ACTIVE)

    }

    @Mock lateinit var mockService: RibotService
    @Mock lateinit var mockRibotDao: RibotDao
    private lateinit var ribotRepo: RibotRepository

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        ribotRepo = RibotRepositoryImpl(mockService,mockRibotDao)
    }

    @Test
    fun shouldAttemptToRetrieveLocalData() {
        val testSubscription = ribotRepo.getRibots()
                .test()

        testSubscription.assertValueAt(0,{it.status == Resource.Status.LOADING})

        verify(mockRibotDao,times(1))
                .getRibots()
    }

    @Test
    fun shouldAttemptToRetrieveApiData() {
        whenever(mockRibotDao.getRibots())
                .thenReturn(Flowable.empty())
        whenever(mockService.getRibots())
                .thenReturn(Single.just(listOf(buildApiRibot())))

        ribotRepo.getRibots()
                .test()

        verify(mockService, times(1))
                .getRibots()
    }

    @Test
    fun shouldMapAndStoreApiToLocal() {
        whenever(mockRibotDao.getRibots())
                .thenReturn(Flowable.empty())

        whenever(mockService.getRibots())
                .thenReturn(Single.just(listOf(buildApiRibot())))

        ribotRepo.getRibots()
                .test()

        val captor = argumentCaptor<List<LocalRibot>>()
        verify(mockRibotDao).insertAll(captor.capture())

        Assert.assertEquals(listOf(buildLocalRibot()),captor.lastValue)
    }

    @Test
    fun shouldReturnLocalAndApi() {
        whenever(mockRibotDao.getRibots())
                .thenReturn(Flowable.just(listOf(buildLocalRibot())))

        whenever(mockService.getRibots())
                .thenReturn(Single.just(listOf(buildApiRibot())))

        val allValues = ribotRepo.getRibots()
                .test()
                .values()

        Assert.assertEquals(2,
                allValues.filter { it.status == Resource.Status.SUCCESS }.size)
    }

    @Test
    fun shouldReturnErrorFromService() {
        whenever(mockRibotDao.getRibots())
                .thenReturn(Flowable.empty())

        val exception = SocketTimeoutException()
        whenever(mockService.getRibots())
                .thenReturn(Single.error(exception))

        val lastValue = ribotRepo.getRibots()
                .test()
                .values().last()

        Assert.assertEquals(exception,lastValue.throwable)
    }

    @Test
    fun shouldTriggerRefresh() {
        whenever(mockRibotDao.getRibots())
                .thenReturn(Flowable.empty())

        whenever(mockService.getRibots())
                .thenReturn(Single.just(listOf(buildApiRibot())))

        val refreshTrigger = RefreshTrigger()

        ribotRepo.getRibots(refreshTrigger)
                .test()

        refreshTrigger.refresh()

        verify(mockService, times(2))
                .getRibots()
    }



}