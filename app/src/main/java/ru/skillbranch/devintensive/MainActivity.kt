package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val KEY_STATUS = "STATUS"
        const val KEY_QUESTION = "QUESTION"
    }

    private lateinit var benderImage: ImageView
    private lateinit var textTxt: TextView
    private lateinit var messageEd: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Log.d("M_MainActivity", "onCreate")

        //benderImage = findViewById(R.id.iv_bender)
        benderImage = iv_bender
        textTxt = tv_text
        messageEd = et_message
        sendBtn = iv_send

        val status = savedInstanceState?.getString(KEY_STATUS) ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString(KEY_QUESTION) ?: Bender.Question.NAME.name
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)

        messageEd.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendAnswer()
                true
            } else {
                false
            }
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send) {
            sendAnswer()
        }
    }

    private fun sendAnswer() {
        if (this.isKeyboardOpen()) this.hideKeyboard()
        val answer = messageEd.text.toString()
        if (answer.isEmpty()) return
        val (phrase, color) = benderObj.listenAnswer(answer)
        messageEd.setText("")
        val (r, g, b) = color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        textTxt.text = phrase
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(KEY_STATUS, benderObj.status.name)
        outState.putString(KEY_QUESTION, benderObj.question.name)
    }
}
