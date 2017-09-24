package com.stustirling.ribotviewer.data

import com.stustirling.ribotviewer.data.api.RibotService
import com.stustirling.ribotviewer.data.api.model.ApiRibot
import com.stustirling.ribotviewer.data.local.dao.RibotDao
import com.stustirling.ribotviewer.data.local.model.LocalRibot
import com.stustirling.ribotviewer.domain.RefreshTrigger
import com.stustirling.ribotviewer.domain.Resource
import com.stustirling.ribotviewer.domain.RibotRepository
import com.stustirling.ribotviewer.domain.model.Ribot
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Stu Stirling on 23/09/2017.
 */
class RibotRepositoryImpl @Inject constructor(
        val ribotService: RibotService,
        val ribotDao: RibotDao ) : RibotRepository {
    override fun getRibots(refreshTrigger: RefreshTrigger?): Flowable<Resource<List<Ribot>>> {
        return Flowable.create({emitter -> object:  NetworkBoundResource<List<LocalRibot>,List<ApiRibot>,List<Ribot>>(emitter,refreshTrigger) {
            override fun saveCallResult(item: List<LocalRibot>) = ribotDao.insertAll(item)

            override fun loadFromDb(): Flowable<List<LocalRibot>> =
                    ribotDao.getRibots().subscribeOn(Schedulers.io())

            override fun createCall(): Single<List<ApiRibot>> {
                return ribotService.getRibots()
            }

            override fun mapToLocal(): Function<List<ApiRibot>, List<LocalRibot>> {
                return Function { it.map {
                    LocalRibot(it.profile.id,
                            it.profile.name.first,
                            it.profile.name.last,
                            it.profile.email,
                            it.profile.dob,
                            it.profile.color,
                            it.profile.bio,
                            it.profile.avatar,
                            it.profile.isActive) } }
            }

            override fun mapToDomain(): Function<List<LocalRibot>, List<Ribot>> {
                return Function { it.map {
                    Ribot(it.id,
                            it.firstName,
                            it.lastName,
                            it.email,
                            it.color,
                            it.avatar,
                            it.dateOfBirth,
                            it.bio,
                            it.isActive)
                } }
            }
        }},BackpressureStrategy.LATEST)
    }

    override fun insertRibots(ribots: List<Ribot>) =
            ribotDao.insertAll(ribots.map {
                LocalRibot(it.id,
                        it.firstName,
                        it.lastName,
                        it.email,
                        it.dataOfBirth,
                        it.hexColor,
                        it.bio,
                        it.avatar,
                        it.isActive)
            })
}