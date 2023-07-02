package com.app.thesisappdemo

import java.sql.Timestamp

data class TransactionItems(
    var UserId : String,
    var CartId : String,
    var ItemName : String,
    var CustomerName : String,
    var TotalPrice : String,
    var PickupPoint : String,
    var Payment : String,
    var PhoneNumber : String,
    var RentDuration : Float,
    var RentDate : String,
    var ImageUrl : String
)