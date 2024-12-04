package com.example.ndroidapp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VideoPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        val videoView: VideoView = findViewById(R.id.videoView)
        val videoUrl = intent.getStringExtra("VIDEO_URL") // Получение URL из Intent

        if (videoUrl != null) {
            val uri = Uri.parse(videoUrl)
            videoView.setVideoURI(uri)
            videoView.setOnPreparedListener { videoView.start() }
            videoView.setOnCompletionListener {
                // Действия по завершении видео
                finish()
            }
        } else {
            Toast.makeText(this, "Видео не найдено", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
