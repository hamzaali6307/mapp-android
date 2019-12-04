package com.emusksystems.muskeapp

import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var drawerToogle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //config actionbar
        toolbar.bringToFront()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navController = Navigation.findNavController(this, R.id.owner_host_fragment)
        bottom_nav.setupWithNavController(navController)
        increaseScanIconSize()

        //navigation change destination callback
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.signupFragment -> {
                    hideBottomNavigation()
                }
                else -> {
                    showBottomNavigation()
                }
            }
        }

        //navigation drawer toogle
        drawerToogle = ActionBarDrawerToggle(
            this,
            navigation_drawer, toolbar,
            R.string.drawer_open_content_desc,
            R.string.drawer_close_content_desc
        )
        navigation_drawer.addDrawerListener(drawerToogle)
        drawerToogle.syncState()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToogle.onConfigurationChanged(newConfig)
    }


    private fun increaseScanIconSize(){
        val menuView = bottom_nav.getChildAt(0) as BottomNavigationMenuView
        val iconView: View = menuView.getChildAt(1).findViewById(R.id.icon)
        val layoutParams: ViewGroup.LayoutParams = iconView.layoutParams
        val displayMetrics = resources.displayMetrics
        layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, displayMetrics).toInt()
        layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, displayMetrics).toInt()
        iconView.layoutParams = layoutParams
    }

    private fun hideBottomNavigation(){
        // bottom_navigation is BottomNavigationView
        with(bottom_nav) {
            if (visibility == View.VISIBLE && alpha == 1f) {
                animate()
                    .alpha(0f)
                    .withEndAction { visibility = View.GONE }
                    .duration = 1
                //hide scan image button
                image_scan.visibility = View.GONE
            }
        }
    }

    private fun showBottomNavigation(){
        // bottom_navigation is BottomNavigationView
        with(bottom_nav) {
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .duration = 1
            //show scan button
            image_scan.visibility = View.VISIBLE
        }
    }
}
