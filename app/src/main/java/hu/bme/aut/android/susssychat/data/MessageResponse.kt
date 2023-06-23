package hu.bme.aut.android.susssychat.data

data class MessageResponse(
    val id: Int,
    val text: String,
    val time: String,
    val user: UserResponse
)