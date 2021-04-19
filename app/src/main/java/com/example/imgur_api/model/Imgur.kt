package com.example.imgur_api.model

data class Imgur(
    var id: String,
    var title: String,
    var description: String,
    var datetime: Long,
    var account_url: String,
    var account_id: Long,
    var views: Long,
    var link: String,
    var ups: Long,
    var downs: Long,
    var comment_count: Long,
    var images: List<Images>?
)


