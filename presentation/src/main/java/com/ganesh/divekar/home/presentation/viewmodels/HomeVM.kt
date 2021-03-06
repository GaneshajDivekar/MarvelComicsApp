package com.ganesh.divekar.home.presentation.viewmodels


import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ganesh.divekar.home.domain.entities.ComicsEntity
import com.ganesh.divekar.home.domain.usecases.CleanAction
import com.ganesh.divekar.home.domain.usecases.GetComicsListAction
import com.ganesh.divekar.home.domain.usecases.GetComicsListAction.Params
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class HomeVM @Inject internal constructor(
    private val getComicsListAction: GetComicsListAction,
    private val cleanAction: CleanAction
) : ViewModel(), CoroutineScope {
    companion object {
        private const val LIMIT = 20
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + viewModelJob
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val bgScope = Dispatchers.IO

    private val filterRequestLiveData = MutableLiveData<Params>()

    private val defaultComics = ComicsEntity(
        id = "default",
        title = "",
        description = "",
        thumbNail = "",
        imageUrls = emptyList(),
        flagged = false
    )

    var currentComics = MutableLiveData<ComicsEntity>()

    private var filterRequest = Params(limit = LIMIT)
    val isLoading = ObservableField<Boolean>()

    fun lastSearchQuery() = filterRequestLiveData.value?.searchKey

    private val pagingConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(LIMIT)
        .setPageSize(LIMIT)
        .build()


    val comicsListSource: LiveData<PagedList<ComicsEntity>>
        get() = Transformations.switchMap(filterRequestMediator) { it }

    private val filterRequestMediator = MediatorLiveData<LiveData<PagedList<ComicsEntity>>>()


    init {
        currentComics.value = defaultComics
        filterRequestMediator.addSource(filterRequestLiveData) { param ->
            uiScope.launch {
                withContext(bgScope) {
                    with(getComicsListAction.getComicsListActionResult(param)) {
                        filterRequestMediator.postValue(
                            LivePagedListBuilder(dataSource, pagingConfig)
                                .setBoundaryCallback(boundaryCallback)
                                .build()
                        )
                    }
                }
            }
        }
    }

    fun search() {
        filterRequest = filterRequest.copy(
            flagged = false
        )
        filterRequestLiveData.postValue(filterRequest)
    }

    override fun onCleared() {
        cleanAction.execute()
    }
}