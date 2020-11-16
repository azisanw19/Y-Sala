package id.canwar.ysala.models

import java.util.*

data class Payment(val bookingId: String = "",
                   val timePayment: Date = Date(),
                   val totalPayment: Int = 0,
                   val beenPay: Int = 0)