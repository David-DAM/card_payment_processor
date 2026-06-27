package com.davinchicoder.card_payment_processor.card


fun String.toMaskedPan(): String = this.replace(Regex("\\d(?=\\d{4})"), "*")

