package com.hitec.presentation.main.search

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.hitec.presentation.util.EventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
) : ViewModel(), ContainerHost<SearchState, SearchSideEffect> {

    override val container: Container<SearchState, SearchSideEffect> = container(
        initialState = SearchState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(SearchSideEffect.Toast(throwable.message.toString()))
                    Log.e(TAG, "error handler: ${throwable.message}")
                }
            }
        }
    )

    init {
        getSubAreaListFromMainViewModel()
    }


    private fun getSubAreaListFromMainViewModel() = intent {
        EventBus.subAreaListState.collectLatest { subAreaList ->
            reduce {
                state.copy(subAreaList = subAreaList)
            }
            Log.e(TAG, "getSubAreaListFromMainViewModel: $subAreaList")
        }
    }

    fun onChipSelected(selectedChip: String) = intent {
        reduce {
            if (state.selectedChip == selectedChip) {
                // 이미 선택된 칩을 다시 클릭하면 선택 해제
                state.copy(selectedChip = "")
            } else {
                // 새로운 칩 선택
                state.copy(selectedChip = selectedChip)
            }
        }
        Log.d(TAG, "Chip selection changed: ${state.selectedChip}")
    }

    fun search(query: String) = intent {
        // 검색 로직 구현
        Log.d(TAG, "Searching for: $query")
        // TODO: 실제 검색 기능 구현
        postSideEffect(SearchSideEffect.Toast("Searching for: $query"))
    }

    companion object {
        const val TAG = "SearchViewModel"
    }
}

@Immutable
data class SearchState(
    val subAreaList: List<String> = emptyList(),
    val selectedChip: String = "",
    val searchQuery: String = ""
)

sealed interface SearchSideEffect {
    class Toast(val message: String) : SearchSideEffect
}