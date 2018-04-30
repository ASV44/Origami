package com.example.data.portability

interface Consumer<T> {

    fun accept(t: T)
}