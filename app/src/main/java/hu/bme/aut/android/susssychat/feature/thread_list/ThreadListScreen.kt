package hu.bme.aut.android.susssychat.feature.thread_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.susssychat.ChatApplication
import hu.bme.aut.android.susssychat.R
import hu.bme.aut.android.susssychat.ui.common.ThreadListAppBar
import hu.bme.aut.android.susssychat.ui.model.toUiText

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun ThreadListScreen(
    onListItemClick: (String, Int) -> Unit,
    onFabClick: (String) -> Unit,
    viewModel: ThreadListViewModel = viewModel(factory = ThreadListViewModel.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { onFabClick(viewModel.getToken()) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        topBar = {
            ThreadListAppBar(
                title = stringResource(id = R.string.app_bar_title_thread_list),
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(ThreadListEvent.CreateThread)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Create, contentDescription = null)
                    }
                }
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(
                    color = if (!state.isLoading && !state.isError) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.background
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            } else if (state.isError) {
                Text(
                    text = state.error?.toUiText()?.asString(context)
                        ?: stringResource(id = R.string.some_error_message)
                )
            } else {
                if (state.threadList.isEmpty()) {
                    Text(text = stringResource(id = R.string.text_empty_thread_list))
                } else {
                    Text(text = stringResource(id = R.string.text_thread_list))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(0.98f)
                            .padding(it)
                            .clip(RoundedCornerShape(5.dp))
                    ) {
                        items(state.threadList.size) { i ->
                            ListItem(
                                headlineText = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = state.threadList[i].name)
                                        Icon(
                                            imageVector = Icons.Default.Circle,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(22.dp)
                                                .padding(start = 10.dp),
                                        )
                                    }
                                },
/*                                modifier = Modifier.combinedClickable(
                                    onClick = { onListItemClick(state.ThreadList[i].id) },
                                    onLongClick = {
                                        viewModel.onEvent(
                                            TodosEvent.ShareTodo(
                                                context,
                                                state.todos[i].id
                                            )
                                        )
                                    }
                                ),*/
                            )
                            if (i != state.threadList.lastIndex) {
                                Divider(
                                    thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}