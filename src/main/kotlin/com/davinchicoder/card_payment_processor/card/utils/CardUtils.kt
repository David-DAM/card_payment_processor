package com.davinchicoder.card_payment_processor.card.utils


fun String.toMaskedPan(): String = this.replace(Regex("\\d(?=\\d{4})"), "*")

