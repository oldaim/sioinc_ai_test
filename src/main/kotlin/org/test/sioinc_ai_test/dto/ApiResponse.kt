package org.test.sioinc_ai_test.dto

data class ApiResponse<T>(
    var code: Int?,
    var message: String?,
    var data: T?
)
