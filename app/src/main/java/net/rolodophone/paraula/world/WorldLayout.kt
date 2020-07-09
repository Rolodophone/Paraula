package net.rolodophone.paraula.world

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children

class WorldLayout(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): ViewGroup(context, attrs, defStyleAttr) { // if this doesn't work then ues @jvmOverloads

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (child in children) {

        }
    }
}