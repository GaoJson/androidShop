package com.example.myshop.http

interface HttpCallback {
    public fun success(data: String?)
    public fun fail(error: String?)
}