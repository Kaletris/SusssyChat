package hu.bme.aut.android.susssychat.usecases

import hu.bme.aut.android.susssychat.clients.ThreadsClient

class ThreadUseCases(client: ThreadsClient) {
    val createThreadUseCase = CreateThreadUseCase()
    val deleteMessageUseCase = DeleteMessageUseCase()
    val deleteThreadUseCase = DeleteMessageUseCase()
    val editMessageUseCase = EditMessageUseCase()
    val loadThreadListUseCase = LoadThreadListUseCase(client)
    val loadThreadUseCase = LoadThreadUseCase()
    val reactMessageUseCase = ReactMessageUseCase()
    val sendMessageUseCase = SendMessageUseCase()
}