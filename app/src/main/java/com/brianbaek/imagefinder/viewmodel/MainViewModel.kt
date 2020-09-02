package com.brianbaek.imagefinder.viewmodel

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brianbaek.imagefinder.BuildConfig
import com.brianbaek.imagefinder.network.KakaoService
import com.brianbaek.imagefinder.network.dto.Document
import com.brianbaek.imagefinder.network.dto.Image
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

const val SORT_ACCURACY = "accuracy"
const val SORT_RECENCY = "recency"
const val PAGE_MAX = 50
const val SIZE = 80

class MainViewModel @Inject constructor(
    val kakaoService: KakaoService
) : ViewModel() {
    var filterLive: MutableLiveData<MutableList<String>> = MutableLiveData()
    var selectCollection: String? = "all"

    var documentObs: ObservableField<MutableList<Document>> = ObservableField()
    var documentShowObs: ObservableField<MutableList<Document>> = ObservableField()

    var sortObs: ObservableField<String> = ObservableField(SORT_ACCURACY)

    var page: Int = 1

    var queryText: ObservableField<String> = ObservableField("카카오")



    fun init() {
        documentObs.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                Log.d("MainViewModel", "documentObs.addOnPropertyChangedCallback")
                processShowList()
            }
        })
    }

    fun processShowList() {
        var list: MutableList<Document>? = documentObs.get()

        var resultList: MutableList<Document> = mutableListOf()

        if (list != null) {
            Single.fromCallable{
                Log.d("MainViewModel", "processShowList list != null selectCollection == $selectCollection")
                if (selectCollection.equals("all")) {
                    resultList = list
                } else{
                    for (doc in list) {
                        if (doc.collection.equals(selectCollection)) {
                            resultList.add(doc)
                        }
                    }
                }

            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({t: Unit? ->
                    if (resultList == null) {
                        Log.d("MainViewModel", "processShowList resultList == null")
                    }
                    documentShowObs.set(resultList)
                })
        }
    }

    fun getImage(isReset: Boolean) {
        if (isReset) {
            page = 1
            documentObs.set(null)
            documentShowObs.set(null)
            filterLive.postValue(null)
        }

        val query: String? = queryText.get()

        if (query == null || query.trim().length == 0) {
            Log.d("mainVM", "getImage query == null")
            return
        }

        Log.d("mainVM", "getImage query == $query")

        if (page >= PAGE_MAX) {
            return
        }

        kakaoService.getSearchImage(
            BuildConfig.REST_API_KEY,
            query.trim(),
            sortObs.get(),
            page,
            SIZE
            ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({res: Response<Image>? ->
                if (res == null) {
                    if (documentObs.get() == null || documentObs.get()?.size == 0) {
                        documentObs.set(null)
                    }
                } else{
                    page++
                    Log.d("mainVM", "getImage result notnull")
                    val image: Image? = res.body()

                    if (image != null) {
                        var filterlist: MutableList<String>? = filterLive.value

                        if (filterlist == null) {
                            filterlist = mutableListOf()
                        }

                        if (!filterlist.contains("all"))
                            filterlist.add("all")

                        for (item in image.documents) {

                            if (!filterlist.contains(item.collection)) {
                                filterlist.add(item.collection)
                            }
                        }

                        filterLive.postValue(filterlist)

                        if (documentObs.get() == null) {
                            documentObs.set(image.documents)
                        } else{
                            documentObs.get()?.addAll(image.documents)
                            documentObs.notifyChange()
                        }
                    }
                }
            },
                {t: Throwable? -> {

                } })
    }

    fun setDocumentObs(list: MutableList<Document>) {
        documentObs?.set(list)
    }
}