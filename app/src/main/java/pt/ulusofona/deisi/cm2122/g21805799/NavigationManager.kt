package pt.ulusofona.deisi.cm2122.g21805799

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object NavigationManager {

    private fun placeFragment(fm: FragmentManager, fragment: Fragment) {
        val transition = fm.beginTransaction()
        transition.replace(R.id.frame, fragment)
        transition.addToBackStack(null)
        transition.commit()
    }

    fun goToFiresListFragment(fm: FragmentManager) {
        placeFragment(fm, FiresListFragment())
    }

    fun goToDashboardFragment(fm: FragmentManager) {
        placeFragment(fm, DashBoardFragment())
    }

    fun goToFireFormFragment(fm: FragmentManager) {
        placeFragment(fm, FireFormFragment())
    }

    fun goToAboutFragment(fm: FragmentManager) {
        placeFragment(fm, AboutFragment())
    }

    fun goToFiresMapFragment(fm: FragmentManager) {
        placeFragment(fm, FiresMapFragment())
    }

    fun goToFireDetail(fm: FragmentManager, fire: FireUi) {
        placeFragment(fm, FireDetailFragment.newInstance(fire))
    }


}