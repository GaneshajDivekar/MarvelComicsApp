package com.ganesh.divekar.feature_home.ui.views

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.ganesh.divekar.feature_home.R
import com.ganesh.divekar.feature_home.databinding.FragmentDetailBinding
import com.ganesh.divekar.feature_home.ui.bindings.ComicsDataBindingModel
import com.ganesh.divekar.feature_home.ui.mapper.ComicsUIEntityMapper
import com.ganesh.divekar.home.domain.entities.ComicsEntity
import com.ganesh.divekar.home.presentation.factory.ComicsViewModelFactory
import com.ganesh.divekar.home.presentation.viewmodels.HomeVM
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class ComicsDetailFragment : Fragment() {
    lateinit var viewModel: HomeVM
    @Inject
    lateinit var viewModelFactory: ComicsViewModelFactory
    private val comicsUIEntityMapper = ComicsUIEntityMapper()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentDetailBinding>(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        )

        activity?.let {
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(HomeVM::class.java)
        }


        viewModel.currentComics.observe(this, Observer<ComicsEntity> {
            if (it.id != "default") {

                val comicsModel = ComicsDataBindingModel(
                    comicsUIEntityMapper.to(it),
                    comicsUIEntityMapper
                )
                if (binding.comicsUiModelObserver?.comics?.id != it.id)
                    binding.comicsUiModelObserver = comicsModel


            }

        })
        binding.header.desc.movementMethod = ScrollingMovementMethod()

        binding.header.backButtoon.setOnClickListener {
              it.findNavController()
                    .navigateUp()
        }
        return binding.root
    }



    override fun onDetach() {
        super.onDetach()
        viewModel.currentComics.removeObservers(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.currentComics.removeObservers(this)
    }


}

