package com.example.movieappmvvm.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
}

class NetworkState(val Status: Status, val mgs: String) {
    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "Running")
        val ERROR: NetworkState = NetworkState(Status.FAILED, "Something went wrong")
        val ENDOFLIST: NetworkState = NetworkState(Status.FAILED, "You have reached the end")
    }
}