package com.ziemniak.bibliomobile.repositories

interface LoginResponseListener {
	fun onAction(accepted:Boolean,jwt:String?)
}