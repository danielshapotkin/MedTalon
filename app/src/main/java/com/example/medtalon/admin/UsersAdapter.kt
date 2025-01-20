import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.User
import com.example.test2.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersAdapter(
    private val context: Context,
    private var users: MutableList<User>,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    private val dataBase: DataBase = DataBase.getInstance()

    // ViewHolder для отображения каждого элемента списка
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val loginTextView: TextView = itemView.findViewById(R.id.loginTextView)
        val passwordTextView: TextView = itemView.findViewById(R.id.passwordTextView)
        val editButton: ImageView = itemView.findViewById(R.id.editButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        // Устанавливаем начальные данные пользователя
        holder.loginTextView.text = user.login
        holder.passwordTextView.text = user.password

        // Обработка редактирования
        holder.editButton.setOnClickListener {
            // Здесь можно добавить логику, чтобы открыть диалоговое окно или экран для редактирования данных
            // Например, можно показать Dialog с полями EditText для ввода нового логина и пароля

            // Пример простого диалога редактирования
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_user, null)
            val editLogin = dialogView.findViewById<EditText>(R.id.editLogin)
            val editPassword = dialogView.findViewById<EditText>(R.id.editPassword)
            editLogin.setText(user.login)
            editPassword.setText(user.password)

            val dialog = AlertDialog.Builder(context)
                .setTitle("Редактировать пользователя")
                .setView(dialogView)
                .setPositiveButton("Сохранить") { _, _ ->
                    val newLogin = editLogin.text.toString()
                    val newPassword = editPassword.text.toString()

                    lifecycleScope.launch(Dispatchers.IO) {
//                        val result = dataBase.updateUser(user.copy(login = newLogin, password = newPassword))
//                        withContext(Dispatchers.Main) {
//                            if (result) {
//                                Toast.makeText(context, "Данные пользователя обновлены", Toast.LENGTH_SHORT).show()
//                                user.login = newLogin
//                                user.password = newPassword
//                                notifyItemChanged(position)
//                            } else {
//                                Toast.makeText(context, "Ошибка обновления данных", Toast.LENGTH_SHORT).show()
//                            }
//                        }
                    }
                }
                .setNegativeButton("Отмена", null)
                .create()

            dialog.show()
        }

        // Обработка удаления пользователя
        holder.deleteButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val result = dataBase.deleteUserByLogin(user.login)
                withContext(Dispatchers.Main) {
                    if (result) {
                        Toast.makeText(context, "Пользователь удален", Toast.LENGTH_SHORT).show()
                        // Удаляем пользователя из списка и обновляем адаптер
                        users.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, users.size)
                    } else {
                        Toast.makeText(context, "Ошибка удаления пользователя", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}

