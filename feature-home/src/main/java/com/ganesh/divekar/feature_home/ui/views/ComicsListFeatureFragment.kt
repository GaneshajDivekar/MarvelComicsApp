package com.ganesh.divekar.feature_home.ui.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.ganesh.divekar.feature_home.R
import com.ganesh.divekar.feature_home.databinding.FragmentComicsListFeatureBinding
import com.ganesh.divekar.feature_home.ui.mapper.ComicsUIEntityMapper
import com.ganesh.divekar.feature_home.ui.recyclerview.adapters.ComicsPagedListAdapter
import com.ganesh.divekar.home.domain.entities.ComicsEntity
import com.ganesh.divekar.home.domain.entities.RepositoryState
import com.ganesh.divekar.home.domain.entities.RepositoryState.Companion.CONNECTED
import com.ganesh.divekar.home.domain.entities.RepositoryState.Companion.DISCONNECTED
import com.ganesh.divekar.home.domain.entities.RepositoryState.Companion.EMPTY
import com.ganesh.divekar.home.domain.entities.RepositoryState.Companion.ERROR
import com.ganesh.divekar.home.domain.entities.RepositoryState.Companion.LOADED
import com.ganesh.divekar.home.domain.entities.RepositoryState.Companion.LOADING
import com.ganesh.divekar.home.domain.entities.RepositoryStateRelay
import com.ganesh.divekar.home.presentation.factory.ComicsViewModelFactory
import com.ganesh.divekar.home.presentation.viewmodels.HomeVM
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class ComicsListFeatureFragment : Fragment() {
    private var prevState: RepositoryState = EMPTY
    private lateinit var binding: FragmentComicsListFeatureBinding
    private lateinit var viewModel: HomeVM
    @Inject
    lateinit var viewModelFactory: ComicsViewModelFactory

    @Inject
    lateinit var disposable: CompositeDisposable

    @Inject
    lateinit var repositoryStateRelay: RepositoryStateRelay


    private lateinit var comicsListAdapter: ComicsPagedListAdapter


    private var query: String = ""


    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_comics_list_feature,
            container,
            false
        )
        getScreenOrientation(requireActivity())
        binding.rvComicsList.setHasFixedSize(true)
        activity?.let {
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(HomeVM::class.java)
        }
        query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: (viewModel.lastSearchQuery()?:DEFAULT_QUERY)
        binding.viewModel = viewModel
        initAdapter(binding)
        initListeners()

        return binding.root
    }
    fun getScreenOrientation(context: Context): String? {
        val screenOrientation =
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                .orientation
        return when (screenOrientation) {
            Surface.ROTATION_0 -> {
                binding.rvComicsList.layoutManager = GridLayoutManager(getActivity(), 4, GridLayoutManager.HORIZONTAL, false)
                "android portrait screen"}
            Surface.ROTATION_90 -> {
                binding.rvComicsList.layoutManager = GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false)
                "android landscape screen"}
            Surface.ROTATION_180 -> {
                binding.rvComicsList.layoutManager = GridLayoutManager(getActivity(), 4, GridLayoutManager.HORIZONTAL, false)
                "android reverse portrait screen"}
            else -> {
                binding.rvComicsList.layoutManager = GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false)
                "android reverse landscape screen"}
        }
    }
    private val pagedListLiveDataObserver = Observer<PagedList<ComicsEntity>> { pagedList ->
        Log.d(
            "ComicsListFeatureActivityFragment",
            "Current paged list size: ${pagedList?.size} query= $query"
        )
        showEmptyList(pagedList?.size == 0)
        comicsListAdapter.submitList(pagedList)
        comicsListAdapter.notifyDataSetChanged()

    }

    private fun initListeners() {
        viewModel.comicsListSource.observe(this, pagedListLiveDataObserver)
        val subscription =
            repositoryStateRelay.relay.subscribe {
                when (it) {
                    LOADING -> viewModel.isLoading.set(true)
                    LOADED -> {
                        viewModel.isLoading.set(false)
                    }
                    DISCONNECTED -> {
                        viewModel.isLoading.set(false)
                            Toast.makeText(
                            activity,
                            R.string.network_lost,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    CONNECTED -> {
                        if (prevState == DISCONNECTED) {
                            viewModel.search()
                        }
                    }
                    ERROR -> {
                        viewModel.isLoading.set(false)
                        Toast.makeText(
                            activity,
                            R.string.network_error,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    EMPTY -> {
                        viewModel.search()
                    }
                }
                prevState = it
            }
        subscription?.let { disposable.add(it) }
    }

    override fun onDetach() {
        super.onDetach()
        disposable.dispose()

    }


    override fun onDestroyView() {
        binding.rvComicsList.addOnAttachStateChangeListener(object :
            View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) { // no-op
            }

            override fun onViewDetachedFromWindow(v: View) {
                binding.rvComicsList.adapter = null
            }
        })
        super.onDestroyView()
    }

    private fun initAdapter(binding: FragmentComicsListFeatureBinding) {
        comicsListAdapter = ComicsPagedListAdapter(requireActivity(),ComicsUIEntityMapper())
        binding.rvComicsList.adapter = comicsListAdapter
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.rvComicsList.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.rvComicsList.visibility = View.VISIBLE
            repositoryStateRelay.relay.accept(RepositoryState.DB_LOADED)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::viewModel.isInitialized) {
            outState.putString(LAST_SEARCH_QUERY, viewModel.lastSearchQuery())
       }else{

        }
    }


    private fun updateComicsListFromInput() {
                viewModel.search()
                comicsListAdapter.submitList(null)
                comicsListAdapter.notifyDataSetChanged()
                binding.rvComicsList.recycledViewPool.clear()
    }


    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
        //private const val DEFAULT_QUERY = "Avengers"
    }
}
