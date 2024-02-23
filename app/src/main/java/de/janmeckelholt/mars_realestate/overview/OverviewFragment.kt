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

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import de.janmeckelholt.mars_realestate.R
import de.janmeckelholt.mars_realestate.databinding.FragmentOverviewBinding

class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding : FragmentOverviewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_overview, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.propertiesList.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })
        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            it?.let {
                val bundle = Bundle()
                bundle.putParcelable("marsProperty", it)
                this.findNavController().navigate(R.id.action_showDetail, bundle)
                viewModel.navigationDone()
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(OverflowMenu(viewModel), viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    class OverflowMenu(private val viewModel: OverviewViewModel) : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.overflow_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            viewModel.showProperties(menuItem.itemId)
            return true
        }
    }

}
