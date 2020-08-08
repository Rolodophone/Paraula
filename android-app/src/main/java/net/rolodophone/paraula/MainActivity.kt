package net.rolodophone.paraula

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Moshi
import net.rolodophone.paraula.learning.ParaulaApiJsonAdapter
import net.rolodophone.paraula.multiplatform.log

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		log = { Log.i("multiplatform", it) }

		// get levels from json
		val moshi = Moshi.Builder().build()
		val paraulaApi = ParaulaApiJsonAdapter(moshi).fromJson(readTextFile(resources.openRawResource(R.raw.levels)))!!
		levels = paraulaApi.levels
		englishExamples = paraulaApi.englishExamples
		catalanExamples = paraulaApi.catalanExamples
	}
}