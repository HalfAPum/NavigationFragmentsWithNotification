package oleksand.narvatov.navigationfragmentswithnotification.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import oleksand.narvatov.navigationfragmentswithnotification.entity.FragmentAction
import oleksand.narvatov.navigationfragmentswithnotification.entity.FragmentAction.CreateNextFragment
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _fragmentActionStateFlow = MutableSharedFlow<FragmentAction>(replay = 1)
    val fragmentActionStateFlow = _fragmentActionStateFlow.asSharedFlow()

    fun sendFragmentAction(action: FragmentAction) {
        viewModelScope.launch { _fragmentActionStateFlow.emit(action) }
    }

}