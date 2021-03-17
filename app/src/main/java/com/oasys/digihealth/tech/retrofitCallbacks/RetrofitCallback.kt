package com.oasys.digihealth.tech.retrofitCallbacks;

import retrofit2.Response;

public interface RetrofitCallback<T> {
    fun onSuccessfulResponse(responseBody: Response<T?>)

    fun onBadRequest(errorBody: Response<T?>)

    fun onServerError(response: Response<*>?)

    fun onUnAuthorized()

    fun onForbidden()

    fun onFailure(s: String?)

    fun onEverytime()
}
