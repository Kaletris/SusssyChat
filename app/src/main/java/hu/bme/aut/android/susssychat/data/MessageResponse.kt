package hu.bme.aut.android.susssychat.data

import java.time.LocalDate

data class MessageResponse (
        val Id: Int,
        val Text: String,
        val Time: LocalDate,
        val User: UserResponse
        )