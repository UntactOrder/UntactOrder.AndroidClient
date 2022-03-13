package io.github.untactorder.manual

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.untactorder.databinding.ActivityManualDisplayBinding
import io.github.untactorder.manual.ManualResources


class ManualDisplayActivity : AppCompatActivity() {
    private lateinit var layout: ActivityManualDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layout binding.
        layout = ActivityManualDisplayBinding.inflate(layoutInflater)
        setContentView(layout.root)


    }
}