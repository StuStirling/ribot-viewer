package com.stustirling.ribotviewer.domain.model

import java.util.*

/**
 * Created by Stu Stirling on 21/09/2017.
 */
data class Ribot(
        val id: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val hexColor: String,
        val avatar: String?,
        val dataOfBirth: Date,
        val bio : String?,
        val isActive: Boolean
)