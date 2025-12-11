package com.joyersapp.auth.data.remote.dto

data class MultiStepRegisterRequestDto(
    var token: String = "",
    var id: String = "",
    var name: String = "",
    var joyer_location: String = "",
    var joyer_status: String = "",
    var title: String = "",
    var sub_title: String = "",
)
