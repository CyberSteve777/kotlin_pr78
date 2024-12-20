package me.cybersteve.kotlin_pr78

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import me.cybersteve.kotlin_pr78.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        Handler(Looper.getMainLooper())
        var image: Bitmap?
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.button.setOnClickListener {
            lifecycleScope.launch{
                Log.d("pr", "Some thread ${Thread.currentThread().name}")
                binding.txt.text = "Capaci-cone"

                val imageURL =
                    "https://plantsvszombies.wiki.gg/images/d/d1/Deodarcedar_HD.png"
                image = downloadAndSaveImage(imageURL)
                if (image != null) {
                    binding.imageView.setImageBitmap(image)

                    Log.d("rrr","Downloaded and saved")
                } else {
                    Log.d("rrr","Failed to load")
                }
            }
        }
    }

    private suspend fun downloadAndSaveImage(imageUrl: String): Bitmap? {
        val bitmap = withContext(Dispatchers.IO) {
            downloadImage(imageUrl)
        }
        if (bitmap != null) {
            withContext(Dispatchers.IO) {
                saveImageToDisk(bitmap)
            }
        }
        return bitmap
    }

    fun downloadImage(imageUrl: String): Bitmap? = runCatching {
        val connection = URL(imageUrl).openConnection()
        connection.doInput = true
        val input = connection.getInputStream()
        BitmapFactory.decodeStream(input)
    }.getOrNull()


    private fun saveImageToDisk(bitmap: Bitmap) {
        runCatching {
            val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "downloaded_image.jpg")
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                // принудительная запись данных
                outputStream.flush()
            }
        }.onFailure { Log.d("rrr","Ошибка сохранения изображения") }
    }
}