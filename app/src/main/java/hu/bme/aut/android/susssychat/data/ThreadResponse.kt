package hu.bme.aut.android.susssychat.data

data class ThreadResponse(
    val id: Int,
    val name: String,
    val owner: UserResponse,
)
