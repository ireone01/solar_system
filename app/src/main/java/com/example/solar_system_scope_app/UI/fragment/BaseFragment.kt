package com.example.solar_system_scope_app.UI.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.solar_system_scope_app.model.AudioManager
import com.example.solar_system_scope_app.model.PlanetDataProvider
import com.example.solar_system_scope_app.utils.updateLocale
import java.util.Locale

abstract class BaseFragment : Fragment(), TextToSpeech.OnInitListener{

    protected lateinit var tts: TextToSpeech
    protected lateinit var btnRead: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(requireContext(),this)
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            if(PlanetDataProvider.getLanguage(requireContext())=="vn"){
            val langResult = tts.setLanguage(Locale("vi", "VN" ))
//            if(langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED){
//                showLanguageDialog()
//            }else{
//                Log.d("TextToSpeech", "Tiếng Việt được hỗ trợ")
//            }
            }else{
                val langResult = tts.setLanguage(Locale.ENGLISH)
//                if(langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED){
//                    showLanguageDialogEnglish()
//                }else{
//                    Log.d("TextToSpeech", "English is supported")
//                }
            }
        }else{
            Log.e("TextToSpeech", "TextToSpeech không khởi tạo thành công")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context.updateLocale(PlanetDataProvider.getLanguage(context)))
    }

    protected fun readText(textToRead: String){
        if(::tts.isInitialized){
            val utteranceId = "READ_TEXT"
            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener(){
                override fun onStart(p0: String?) {
                    Log.d("TextToSpeech","Bắt đầu đọc văn bản.")
                }

                override fun onDone(p0: String?) {
                    activity?.runOnUiThread {
                        AudioManager.start(requireContext())
                    }
                }

                override fun onError(p0: String?) {
                    activity?.runOnUiThread {
                        AudioManager.start(requireContext())
                    }
                }
            })
            val params= Bundle().apply {
                putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,utteranceId)
            }
            tts.speak(textToRead,TextToSpeech.QUEUE_FLUSH,params,utteranceId)
        }else{
            AudioManager.start(requireContext())
        }
    }

    private fun showLanguageDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Cần cài đặt ngôn ngữ tiếng việt")
            .setMessage("Thiết bị của bạn chưa cài đặt ngôn ngữ tiếng việt")
            .setPositiveButton("Cài đặt") { _,_ ->
                val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Hủy") {dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun showLanguageDialogEnglish(){
        AlertDialog.Builder(requireContext())
            .setTitle("Need to install English language")
            .setMessage("Your device does not have English language installed")
            .setPositiveButton("install") { _,_ ->
                val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("cancel") {dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        if(::tts.isInitialized){
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}