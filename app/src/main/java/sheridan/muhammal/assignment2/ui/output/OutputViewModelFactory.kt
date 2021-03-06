package sheridan.muhammal.assignment2.ui.output

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OutputViewModelFactory(
    private val envelopeKey: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OutputViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OutputViewModel(envelopeKey, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
