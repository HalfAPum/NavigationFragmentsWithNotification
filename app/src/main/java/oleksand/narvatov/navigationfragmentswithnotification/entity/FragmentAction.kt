package oleksand.narvatov.navigationfragmentswithnotification.entity

sealed class FragmentAction {

    data class CreateNextFragment(val page: Int = 1): FragmentAction()

    object DeleteFragment : FragmentAction()

}
