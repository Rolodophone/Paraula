package net.rolodophone.paraula

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.RawRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.io.FileNotFoundException
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


fun readFile(resources: Resources, @RawRes file: Int): String {
	return resources.openRawResource(file).bufferedReader().use {
		it.readText()
	}
}


fun getWordProbabilities(context: Context): List<Double> {
	return try {
		context.openFileInput("wordProbabilities").bufferedReader().use { file ->
			file.readText().split(",").map { it.toDouble() }
		}
	} catch (e: FileNotFoundException) {
		context.openFileOutput("wordProbabilities", Context.MODE_PRIVATE).use {
			it.write("".toByteArray())
		}
		listOf()
	}
}
fun setWordProbabilities(context: Context, value: List<Double>) {
	context.openFileOutput("wordProbabilities", Context.MODE_PRIVATE).use { file ->
		file.write(value.joinToString(",") { it.toString() }.toByteArray())
	}
}