package id.canwar.ysala.models

import java.util.*

data class Booking(val id: String = "",
                   val homestayId: String = "",
                   val userId: String = "",
                   val timeOrder: Date = Date(),
                   val timeCheckIn: Date = Date(),
                   val timeCheckOut: Date = Date(),
                   val eat: String? = null,
                   val locationPickUp: String? = null)