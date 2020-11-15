package id.canwar.ysala.models

import java.util.*

data class Payment(val bookingId: String,
                   val timePayment: Date,
                   val totalPayment: Int,
                   val beenPay: Int)