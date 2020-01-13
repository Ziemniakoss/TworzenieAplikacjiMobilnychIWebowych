package com.ziemniak.bibliomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ziemniak.bibliomobile.repositories.LoginResponseListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoginResponseListener {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		UserDetails.addListener(this)
		btnRegister.setOnClickListener {
			val intent = Intent(this, RegisterActivity::class.java)
			startActivity(intent)
		}
		btnLogin.setOnClickListener {
			val login = txtvLoginValue.text.toString()
			val password = txtvPassword.text.toString()
			UserDetails.password = password
			UserDetails.username = login
			UserDetails.updateJwt()
		}
	}

	override fun onAction(accepted: Boolean, jwt:String?) {
		runOnUiThread {
			if (accepted) {
				val intent = Intent(this, MainMenuActivity::class.java)
				startActivity(intent)
				Variables.jwt = jwt

			} else {
				Toast.makeText(this, "Dane są niepoprawne", Toast.LENGTH_SHORT).show()
				txtvPassword.text.clear()
			}
		}

	}
}
