import android.annotation.SuppressLint
import java.time.LocalDateTime

data class Polyclinic(
    val name: String,
    val workTime: String,
    val email: String,
    val adress: String,
    val url: String,
    val img: String
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        ""
    )
}
