package org.gsm.olio.model.data.response

import org.gsm.olio.model.data.response.Data

data class ResponseUrl(
    var code: String,
    var data: Data,
    var message: String,
    var success: Boolean
)