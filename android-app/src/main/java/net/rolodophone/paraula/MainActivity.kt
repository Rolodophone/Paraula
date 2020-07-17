package net.rolodophone.paraula

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import net.rolodophone.paraula.multiplatform.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        log = { Log.i("multiplatform", it) }
    }
}