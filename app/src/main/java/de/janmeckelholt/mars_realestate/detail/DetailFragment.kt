/*
 *  Copyright 2018, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.janmeckelholt.mars_realestate.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import de.janmeckelholt.mars_realestate.R
import de.janmeckelholt.mars_realestate.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private val args : DetailFragmentArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val binding : FragmentDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = this
        val viewModelFactory = DetailViewModelFactory(args.marsProperty, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }
}
