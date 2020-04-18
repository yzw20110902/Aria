package com.example.arial.downloaddemo

import androidx.test.runner.AndroidJUnit4
import com.arialyy.aria.util.CommonUtil
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.pow

@RunWith(AndroidJUnit4::class)
class ApiTest {

  @Test
  fun testSpeed() {
    val speed = 1024.0.pow(4.0) * 2
    println("speed = ${CommonUtil.formatFileSize(speed)}")

  }
}