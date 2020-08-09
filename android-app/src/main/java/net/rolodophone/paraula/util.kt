package net.rolodophone.paraula

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.RawRes
import androidx.fragment.app.*
import androidx.lifecycle.*
import net.rolodophone.paraula.learning.*
import java.io.*
import kotlin.math.roundToInt

/**
	Converts an integer dp value to px.
 */
fun Int.toPx(context: Context): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).roundToInt()


class BaseViewModelFactory<VM>(val creator: () -> VM) : ViewModelProvider.Factory {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		@Suppress("UNCHECKED_CAST")
		return creator() as T
	}
}

/**
	Returns a factory which runs `creator` to obtain a ViewModel.
	```
	val vm: SomeViewModel by lazy { getViewModel { SomeViewModel(parameter) } }
	```
 */
inline fun <reified T : ViewModel> Fragment.getViewModel(noinline creator: () -> T): T
		= ViewModelProvider(this, BaseViewModelFactory(creator)).get(T::class.java)

/**
	Returns a factory which runs `creator` to obtain a {ViewModel} for the activity.
	```
	val vm: SomeViewModel by lazy { requireActivity().getViewModel { SomeViewModel(parameter) } }
	```
 */
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(noinline creator: () -> T): T
		= ViewModelProvider(this, BaseViewModelFactory(creator)).get(T::class.java)

/**
 * Reads an InputStream into a string
 */
fun readTextFile(resources: Resources, @RawRes file: Int): String {

	val inputStream = resources.openRawResource(file)

	val outputStream = ByteArrayOutputStream()
	val buf = ByteArray(1024)
	var len: Int

	try {

		while (inputStream.read(buf).also { len = it } != -1) {
			outputStream.write(buf, 0, len)
		}

		outputStream.close()
		inputStream.close()
	}
	catch (e: IOException) {}

	return outputStream.toString()
}


lateinit var levels: List<Level>
lateinit var examples: Examples
fun randomExample(phrase: String) = examples.english.plus(examples.catalan).filter { phrase in it }.random()