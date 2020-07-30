package net.rolodophone.paraula

import android.content.Context
import android.util.TypedValue
import androidx.fragment.app.*
import androidx.lifecycle.*
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