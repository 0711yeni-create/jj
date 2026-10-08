package com.example.jaroleplay

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.widget.*

data class Character(var name: String, val color: Int)

class MainActivity : android.app.Activity() {
    private lateinit var chat: LinearLayout
    private lateinit var input: EditText
    private lateinit var turnButton: Button
    private lateinit var title: TextView
    private var active = 0
    private val characters = arrayOf(
        Character("캐릭터 A", Color.rgb(0,122,255)),
        Character("캐릭터 B", Color.rgb(220,220,225))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showChat()
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    private fun textView(text: String, size: Float, color: Int): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(color)
        }
    }

    private fun rounded(color: Int, radius: Float): GradientDrawable =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius
        }

    private fun showChat() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(242,242,247))
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), dp(10), dp(10), dp(10))
            setBackgroundColor(Color.WHITE)
        }

        val back = Button(this).apply {
            text = "‹"
            textSize = 28f
            setBackgroundColor(Color.TRANSPARENT)
            setOnClickListener { showSetup() }
        }
        header.addView(back, LinearLayout.LayoutParams(dp(45), dp(55)))

        title = textView("${characters[0].name}  ·  ${characters[1].name}", 17f, Color.BLACK)
        title.gravity = Gravity.CENTER
        header.addView(title, LinearLayout.LayoutParams(0, dp(55), 1f))

        val reset = Button(this).apply {
            text = "↻"
            textSize = 22f
            setBackgroundColor(Color.TRANSPARENT)
            setOnClickListener { chat.removeAllViews() }
        }
        header.addView(reset, LinearLayout.LayoutParams(dp(45), dp(55)))
        root.addView(header)

        chat = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(14), dp(12), dp(14))
        }
        val scroll = ScrollView(this).apply {
            addView(chat)
        }
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val turnBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(10), dp(7), dp(10), dp(7))
            setBackgroundColor(Color.WHITE)
        }

        turnButton = Button(this).apply {
            text = "현재: ${characters[active].name}"
            setOnClickListener {
                active = 1 - active
                text = "현재: ${characters[active].name}"
            }
        }
        turnBar.addView(turnButton, LinearLayout.LayoutParams(dp(135), dp(52)))

        input = EditText(this).apply {
            hint = "메시지"
            singleLine = false
            maxLines = 3
            setPadding(dp(16), 0, dp(16), 0)
            background = rounded(Color.rgb(242,242,247), dp(22).toFloat())
        }
        turnBar.addView(input, LinearLayout.LayoutParams(0, dp(52), 1f).apply {
            setMargins(dp(7), 0, dp(7), 0)
        })

        val send = Button(this).apply {
            text = "↑"
            textSize = 20f
            setOnClickListener { sendMessage(scroll) }
        }
        turnBar.addView(send, LinearLayout.LayoutParams(dp(52), dp(52)))
        root.addView(turnBar)

        setContentView(root)
    }

    private fun sendMessage(scroll: ScrollView) {
        val message = input.text.toString().trim()
        if (message.isEmpty()) return

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = if (active == 0) Gravity.END else Gravity.START
            setPadding(dp(6), dp(5), dp(6), dp(5))
        }

        val name = textView(characters[active].name, 12f, Color.GRAY)
        name.setPadding(dp(5), 0, dp(5), dp(3))
        row.addView(name)

        val bubble = TextView(this).apply {
            text = message
            textSize = 16f
            setTextColor(if (active == 0) Color.WHITE else Color.BLACK)
            setPadding(dp(14), dp(10), dp(14), dp(10))
            background = rounded(
                if (active == 0) characters[0].color else Color.WHITE,
                dp(20).toFloat()
            )
        }
        row.addView(bubble, LinearLayout.LayoutParams(-2, -2).apply {
            maxWidth = dp(290)
        })

        chat.addView(row)
        input.text.clear()
        input.requestFocus()
        scroll.post { scroll.fullScroll(View.FOCUS_DOWN) }
    }

    private fun showSetup() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(35), dp(20), dp(20))
            setBackgroundColor(Color.rgb(242,242,247))
        }

        val heading = textView("자관 역극 설정", 28f, Color.BLACK)
        heading.setTypeface(null, android.graphics.Typeface.BOLD)
        root.addView(heading)
        root.addView(textView("캐릭터 이름을 정하고 대화를 시작해보세요.", 15f, Color.GRAY))

        val a = EditText(this).apply {
            hint = "캐릭터 A 이름"
            setText(characters[0].name)
        }
        val b = EditText(this).apply {
            hint = "캐릭터 B 이름"
            setText(characters[1].name)
        }
        root.addView(a, LinearLayout.LayoutParams(-1, dp(60)).apply { topMargin = dp(25) })
        root.addView(b, LinearLayout.LayoutParams(-1, dp(60)))

        val start = Button(this).apply {
            text = "대화 시작"
            textSize = 17f
            setOnClickListener {
                characters[0].name = a.text.toString().trim().ifEmpty { "캐릭터 A" }
                characters[1].name = b.text.toString().trim().ifEmpty { "캐릭터 B" }
                active = 0
                showChat()
            }
        }
        root.addView(start, LinearLayout.LayoutParams(-1, dp(55)).apply { topMargin = dp(20) })

        setContentView(root)
    }
}
