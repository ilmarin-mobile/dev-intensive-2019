package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardClosed
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.extensions.showKeyboard
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var rootView: View
    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView

    lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("_MainActivity","onCreate ")

        rootView = v_rootView
        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        messageEt.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleAnswer()
                true
            } else false
        })
        sendBtn = iv_send
        sendBtn.setOnClickListener(this)

        benderObj = Bender()

        savedInstanceState?.getString("MESSAGE")?.let {
            messageEt.setText(it)
        }

        savedInstanceState?.getString("STATUS")?.let {
            benderObj.status = Bender.Status.valueOf(it)
        }

        savedInstanceState?.getString("QUESTION")?.let {
            benderObj.question = Bender.Question.valueOf(it)
        }

        updateViews()

    }

    private fun updateViews() {
        val (r,g,b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("_MainActivity","onRestart ") 
    }

    override fun onStart() {
        super.onStart()
        Log.d("_MainActivity","onStart ") 
    }

    override fun onResume() {
        super.onResume()
        Log.d("_MainActivity","onResume ")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity","onPause ")
    }

    override fun onStop() {
        super.onStop()
        Log.d("_MainActivity","onStop ") 
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("_MainActivity","onDestroy ")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString("STATUS", benderObj.status.name)
        outState?.putString("QUESTION", benderObj.question.name)
        outState?.putString("MESSAGE", messageEt.text.toString())

        Log.d("_MainActivity","onSaveInstanceState ${benderObj.status.name} ${benderObj.question.name} ${messageEt.text}")
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send) {
            handleAnswer()
        }
    }

    private fun handleAnswer() {
        Log.d("height","handleAnswer rootView.isVisible = ${rootView.isVisible}")

        Log.d("height","handleAnswer isKeyboardOpen = ${isKeyboardOpen()}")
        Log.d("height","handleAnswer rootView.height = ${rootView.height}")

        hideKeyboard()
        val (phrase, color) =  benderObj.listenAnswer(messageEt.text.toString().toLowerCase())
        messageEt.setText("")

        benderImage.setColorFilter(
            Color.rgb(color.first, color.second, color.third),
            PorterDuff.Mode.MULTIPLY
        )

        textTxt.text = phrase

    }

}
