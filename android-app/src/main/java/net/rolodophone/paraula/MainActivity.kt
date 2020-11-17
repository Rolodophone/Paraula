package net.rolodophone.paraula

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import net.rolodophone.paraula.multiplatform.log

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		log = { Log.i("multiplatform", it) }
	}
}