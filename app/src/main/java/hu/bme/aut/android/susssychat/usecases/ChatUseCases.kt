package hu.bme.aut.android.susssychat.usecases

import hu.bme.aut.android.susssychat.clients.ThreadsClient

class ChatUseCases(client: ThreadsClient) {
    val createThreadUseCase = CreateThreadUseCase()
    val deleteMessageUseCase = DeleteMessageUseCase()
    val deleteThreadUseCase = DeleteMessageUseCase()
    val editMessageUseCase = EditMessageUseCase()
    val loadThreadListUseCase = LoadThreadListUseCase(client)
    val loadThreadUseCase = LoadThreadUseCase(client)
    val reactMessageUseCase = ReactMessageUseCase()
    val createMessageUseCase = CreateMessageUseCase()
}