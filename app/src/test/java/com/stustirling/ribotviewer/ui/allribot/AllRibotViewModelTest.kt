package com.stustirling.ribotviewer.ui.allribot

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.whenever
import com.stustirling.ribotviewer.domain.Resource
import com.stustirling.ribotviewer.domain.RibotRepository
import com.stustirling.ribotviewer.domain.model.Ribot
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


/**
 * Created by Stu Stirling on 23/09/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class AllRibotViewModelTest {
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

        private fun buildRibot() : Ribot =
                Ribot(
                        ID,
                        FIRST_NAME,
                        LAST_NAME,
                        EMAIL,
                        COLOR,
                        AVATAR,
                        DOB,
                        BIO,
                        ACTIVE)

        @BeforeClass @JvmStatic
        fun setupSchedulers() {
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        }
    }

    @Mock lateinit var mockRepo : RibotRepository

    private lateinit var viewModel: AllRibotViewModel

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()


    @Before
    fun setup() {

    }

    @Test
    fun shouldReturnRibots() {
        val ribotList = listOf(buildRibot())
        whenever(mockRepo.getRibots(anyOrNull()))
                .thenReturn(Flowable.just(Resource.success(ribotList)))

        viewModel = AllRibotViewModel(mockRepo)

        Assert.assertNotNull(viewModel.ribots.value)
        Assert.assertNull(viewModel.retrievalError.value)
    }

    @Test
    fun shouldReturnError() {
        val exception = IllegalArgumentException()
        whenever(mockRepo.getRibots(anyOrNull()))
                .thenReturn(Flowable.just(Resource.error(exception)))

        viewModel = AllRibotViewModel(mockRepo)

        Assert.assertEquals(exception,viewModel.retrievalError.value)
        Assert.assertNull(viewModel.ribots.value)
    }

    /**
     * Simulate successful retrieval from database,
     * and an error on network */
    @Test
    fun shouldReturnRibotsAndError() {
        val ribotList = listOf(buildRibot())
        val throwable = IllegalStateException()

        var emitter : FlowableEmitter<Resource<List<Ribot>>>? = null
        val flowable = Flowable.create<Resource<List<Ribot>>>({e: FlowableEmitter<Resource<List<Ribot>>> ->
            emitter = e
        },BackpressureStrategy.LATEST)

        whenever(mockRepo.getRibots(anyOrNull()))
                .thenReturn(flowable)

        viewModel = AllRibotViewModel(mockRepo)
        emitter?.onNext(Resource.success(ribotList))
        emitter?.onNext(Resource.error(throwable))

        Assert.assertNotNull(viewModel.ribots.value)
        Assert.assertNotNull(viewModel.retrievalError.value)

        emitter?.onNext(Resource.success(ribotList))
        Assert.assertNull(viewModel.retrievalError.value)
    }

    @Test
    fun shouldEmitLoading() {
        val ribotList = listOf(buildRibot())
        val throwable = IllegalStateException()

        var emitter : FlowableEmitter<Resource<List<Ribot>>>? = null
        val flowable = Flowable.create<Resource<List<Ribot>>>({e: FlowableEmitter<Resource<List<Ribot>>> ->
            emitter = e
        },BackpressureStrategy.LATEST)

        whenever(mockRepo.getRibots(anyOrNull()))
                .thenReturn(flowable)

        viewModel = AllRibotViewModel(mockRepo)
        emitter?.onNext(Resource.loading())

        Assert.assertTrue(viewModel.loading.value!!)

        emitter?.onNext(Resource.success(ribotList))

        Assert.assertNotNull(viewModel.ribots.value)
        Assert.assertFalse(viewModel.loading.value!!)

        emitter?.onNext(Resource.error(throwable))
        Assert.assertNotNull(viewModel.retrievalError.value)
        Assert.assertFalse(viewModel.loading.value!!)

    }

}