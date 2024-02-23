/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.janmeckelholt.mars_realestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janmeckelholt.mars_realestate.R
import de.janmeckelholt.mars_realestate.network.MarsApi
import de.janmeckelholt.mars_realestate.network.MarsProperty
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

enum class MarsApiStatus { LOADING, ERROR, DONE }
class OverviewViewModel : ViewModel() {

    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty?>()
    val navigateToSelectedProperty: LiveData<MarsProperty?>
        get() = _navigateToSelectedProperty

    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus>
        get() = _status

    private val _headerText = MutableLiveData<String>()
    val headerText: LiveData<String>
        get() = _headerText

    private val _properties = MutableLiveData<List<MarsProperty>?>()

    private val _shownProperties = MutableLiveData<List<MarsProperty>?>()
    val shownProperties: LiveData<List<MarsProperty>?>
        get() = _shownProperties


    fun showProperties(type: Int) {
        when (type) {
            R.id.show_rent_menu -> {
                _shownProperties.value  = _properties.value?.filter { it.isRental }
                _headerText.value = "Rent"
            }

            R.id.show_buy_menu -> {
                _shownProperties.value  = _properties.value?.filter { !it.isRental }
                _headerText.value = "Buy"
            }

            else -> {
                _shownProperties.value = _properties.value
                _headerText.value = "All"
            }

        }

    }

    init {
        getMarsRealEstateProperties()
    }

    fun displayPropertyDetails(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.value = marsProperty
    }

    fun navigationDone() {
        _navigateToSelectedProperty.value = null
    }


    private fun getMarsRealEstateProperties() {
        viewModelScope.launch {
            MarsApiStatus.LOADING
            try {
                val listResult = MarsApi.retrofitService.getProperties()
                _status.value = MarsApiStatus.DONE
                if (listResult.isNotEmpty()) {
                    _properties.value = listResult
                    _shownProperties.value = listResult
                    _headerText.value = "All"
                    Timber.i("got ${listResult.size} items")
                }
            } catch (e: Exception) {
                Timber.e("Failure getting Mars Properties: $e")
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
                _shownProperties.value = ArrayList()
            }
        }

    }
}
