package net.rolodophone.paraula

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Moshi
import net.rolodophone.paraula.learning.*
import net.rolodophone.paraula.multiplatform.log

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		log = { Log.i("multiplatform", it) }


		// get levels from json
		moshi = Moshi.Builder().build()
		examples = ExamplesJsonAdapter(moshi).fromJson(readFile(resources, R.raw.examples))!!
		levels = LevelsJsonAdapter(moshi).fromJson(readFile(resources, R.raw.levels))!!.levels
	}
}