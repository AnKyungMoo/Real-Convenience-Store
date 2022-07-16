package com.km.real_convenience_store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.km.real_convenience_store.database.AppDatabase
import com.km.real_convenience_store.dto.local.FavoriteProductEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.Default).launch {
            AppDatabase.getInstance(this@MainActivity).userInfoDAO().addFavoriteProduct(
                FavoriteProductEntity(
                    title = "콜라",
                    store = "cu"
                )
            )
        }
    }
}
