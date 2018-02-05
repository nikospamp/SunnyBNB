package com.example.ptuxiaki.sunnybnb.ui.WelcomeScreen

import android.content.Intent
import android.os.Bundle
import com.chyrta.onboarder.OnboarderActivity
import com.chyrta.onboarder.OnboarderPage
import com.example.ptuxiaki.sunnybnb.R
import com.example.ptuxiaki.sunnybnb.ui.MainActivity
import java.util.*

class WelcomeScreenActivity : OnboarderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val welcomePage1 = OnboarderPage("Explore new places", "Discover new local and abroad experiences", R.drawable.ic_explore)
        val welcomePage2 = OnboarderPage("Friendships", "Build new bonds with interesting people", R.drawable.ic_friends)
        val welcomePage3 = OnboarderPage("Reserve", "Easy booking with no hustle", R.drawable.bookingdate)

        welcomePage1.setBackgroundColor(R.color.material_blue_500)
        welcomePage2.setBackgroundColor(R.color.material_brown_400)
        welcomePage3.setBackgroundColor(R.color.material_cyan_200)

        val pages = ArrayList<OnboarderPage>()

        pages.add(welcomePage1)
        pages.add(welcomePage2)
        pages.add(welcomePage3)

        for (page in pages) {
            page.setTitleColor(R.color.material_blue_grey_800)
            page.setDescriptionColor(R.color.white)
        }


        setSkipButtonTitle("Skip")
        setFinishButtonTitle("Finish")
        setOnboardPagesReady(pages)
    }

    override fun onFinishButtonPressed() {
        val intent = Intent(this@WelcomeScreenActivity, MainActivity::class.java)
        intent.putExtra("welcome_first_time", false)
        startActivity(intent)
    }

}
