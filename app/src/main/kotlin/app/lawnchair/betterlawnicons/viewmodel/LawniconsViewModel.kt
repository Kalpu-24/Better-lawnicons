package app.lawnchair.betterlawnicons.viewmodel

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.lawnchair.betterlawnicons.model.IconInfoModel
import app.lawnchair.betterlawnicons.model.IconRequestModel
import app.lawnchair.betterlawnicons.model.SearchMode
import app.lawnchair.betterlawnicons.repository.IconRepository
import app.lawnchair.betterlawnicons.repository.IconRequestSettingsRepository
import app.lawnchair.betterlawnicons.repository.NewIconsRepository
import app.lawnchair.betterlawnicons.ui.util.SampleData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface LawniconsViewModel {
    val iconInfoModel: StateFlow<IconInfoModel>
    val searchedIconInfoModel: StateFlow<IconInfoModel>
    val iconRequestModel: StateFlow<IconRequestModel?>
    val newIconsInfoModel: StateFlow<IconInfoModel>

    var iconRequestsEnabled: Boolean

    var expandSearch: Boolean

    val searchMode: SearchMode
    val searchTermTextState: TextFieldState

    fun searchIcons(query: String)
    fun changeMode(mode: SearchMode)
    fun clearSearch()
}

@HiltViewModel
class LawniconsViewModelImpl @Inject constructor(
    private val iconRepository: IconRepository,
    private val newIconsRepository: NewIconsRepository,
    private val iconRequestSettingsRepository: IconRequestSettingsRepository,
) : ViewModel(),
    LawniconsViewModel {
    override val iconInfoModel = iconRepository.iconInfoModel
    override val searchedIconInfoModel = iconRepository.searchedIconInfoModel
    override val iconRequestModel = iconRepository.iconRequestList
    override val newIconsInfoModel = newIconsRepository.newIconsInfoModel

    override var iconRequestsEnabled = false

    override var expandSearch by mutableStateOf(false)

    private var _searchMode by mutableStateOf(SearchMode.LABEL)

    override val searchMode: SearchMode
        get() = _searchMode

    override val searchTermTextState = TextFieldState()

    init {
        viewModelScope.launch {
            val result = runCatching {
                iconRequestSettingsRepository.getEnabledState()
            }

            iconRequestsEnabled = when {
                result.isSuccess -> {
                    result.getOrThrow()
                }

                else -> {
                    // Disable icon requests, as we can't access the internet
                    Log.e(
                        "LawniconsViewModel",
                        "Failed to load icon request settings",
                        result.exceptionOrNull(),
                    )
                    false
                }
            }
        }
    }

    override fun searchIcons(query: String) {
        viewModelScope.launch {
            iconRepository.search(searchMode, searchTermTextState.text.toString())
        }
    }

    override fun changeMode(mode: SearchMode) {
        _searchMode = mode
        viewModelScope.launch {
            iconRepository.search(searchMode, searchTermTextState.text.toString())
        }
    }

    override fun clearSearch() {
        viewModelScope.launch {
            iconRepository.clearSearch()
        }
    }
}

class DummyLawniconsViewModel : LawniconsViewModel {
    private val list = SampleData.iconInfoList

    override val iconInfoModel = MutableStateFlow(IconInfoModel(iconInfo = list, iconCount = list.size)).asStateFlow()
    override val searchedIconInfoModel = MutableStateFlow(IconInfoModel(iconInfo = list, iconCount = list.size)).asStateFlow()
    override val iconRequestModel = MutableStateFlow(IconRequestModel(list = listOf(), iconCount = 0)).asStateFlow()
    override val newIconsInfoModel = MutableStateFlow(IconInfoModel(iconInfo = list, iconCount = list.size)).asStateFlow()

    override var iconRequestsEnabled = true

    override var expandSearch by mutableStateOf(false)

    override val searchTermTextState = TextFieldState()

    override val searchMode = SearchMode.LABEL

    override fun searchIcons(query: String) {}

    override fun changeMode(mode: SearchMode) {}

    override fun clearSearch() {}
}
