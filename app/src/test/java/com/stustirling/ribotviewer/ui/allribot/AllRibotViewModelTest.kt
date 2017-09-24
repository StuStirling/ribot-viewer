package com.stustirling.ribotviewer.ui.allribot

import com.nhaarman.mockito_kotlin.whenever
import com.stustirling.ribotviewer.domain.Resource
import com.stustirling.ribotviewer.domain.RibotRepository
import com.stustirling.ribotviewer.domain.model.Ribot
import io.reactivex.Flowable
import org.junit.Before
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

    @Mock lateinit var mockRepo : RibotRepository

    private lateinit var viewModel: AllRibotViewModel

    @Before
    fun setup() {

    }

    @Test
    fun shouldReturnRibots() {
        whenever(mockRepo.getRibots())
                .thenReturn(Flowable.just(Resource.success(listOf(buildRibot()))))

        viewModel = AllRibotViewModel(mockRepo)

        viewModel.getRibotRetrievedSuccessfully()
                .test()
                .assertValueCount(1)
    }

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
    }



}