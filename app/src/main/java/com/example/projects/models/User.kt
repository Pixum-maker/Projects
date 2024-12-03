package com.example.projects.models

data class SignupModel(
    var username : String = "",
    var email : String = "",
    var password : String=  "",
    var confirmPassword :String="",
    var userId :String ="",

)