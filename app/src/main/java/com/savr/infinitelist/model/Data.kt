package com.savr.infinitelist.model

class Data (var category: String) {
    var title: String? = null
    var subtitle: String? = null
    init {
        this.category = category
    }
}