import android.annotation.SuppressLint
import java.time.LocalDateTime

data class Polyclinic(
    val address: String,
    val email: String,
    val name: String,
    val url: String,
    val worktime: String
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        ""

    )
}
