package com.example.planter.UserAuth

data class JoinVO(val Email : String, val Nick : String, val waterAlram : String, val messageAlram : String) {

    constructor() : this("","","","")
}

