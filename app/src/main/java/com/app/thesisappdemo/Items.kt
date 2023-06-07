package com.app.thesisappdemo

import com.google.firebase.storage.StorageReference

data class Items (
    var image_url: String,
    var item_code: String,
    var item_name: String,
    var price: Float,
    var sport_category: String,
    var item_bio: String
)