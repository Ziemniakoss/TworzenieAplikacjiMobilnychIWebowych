package com.ziemniak.bibliomobile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class RegisterActivity : AppCompatActivity(){
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_register)
		btnRegister.setOnClickListener {
			//todo rejestrowania
			val intent = Intent(this, MainMenuActivity::class.java)
			startActivity(intent)
		}
	}
}