package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.Timer
import kotlin.concurrent.timer

//클릭 이벤트를 처리하는 인터페이스
class MainActivity : AppCompatActivity(), View.OnClickListener {
    
    var isRunning = false //실행 여부 확인용 변수

    var timer : Timer? =  null
    var time = 0

    private lateinit var btn_start: Button
    private lateinit var btn_refresh: Button
    private lateinit var tv_millisecond: TextView
    private lateinit var tv_second: TextView
    private lateinit var tv_minute: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //뷰 가져오기, findViewById 함수로 xml 레이아웃 파일에서 정의한 뷰들을 액티비티에서
        //사용할 수 있도록 가져옴
        btn_start = findViewById(R.id.btn_start)
        btn_refresh = findViewById(R.id.btn_refresh)
        tv_millisecond = findViewById(R.id.tv_millisecond)
        tv_second = findViewById(R.id.tv_second)
        tv_minute = findViewById(R.id.tv_minute)

        // 버튼별 OnClickListener 등록, 등록해야 버튼 클릭 가능
        btn_start.setOnClickListener(this)
        btn_refresh.setOnClickListener(this)
    }

    //클릭 이벤트 처리
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_start -> {
                if(isRunning) {
                    pause()
                }else{
                    start()
                }
            }

            R.id.btn_refresh -> {
                refresh()
            }
        }
    }

    private fun start(){
        btn_start.text = "일시정지"
        btn_start.setBackgroundColor(getColor(R.color.red))
        isRunning = true

        //코틸린에서 제공하는 timer는 timer(period=[주기]) {} 함수는 일정한 주기로 반복하는 동작을
        //수행할 때 유용함, {}부분은 모두 백그라운드 스레드에서 실행
        timer = timer(period = 10){ //변수10은 10밀리초마다 실행된다는 의미

            time++ //10밀리초 단위 타이머, 0.01초마다 time에 1을 더함(1000밀리초는 1초)

            //시간계산
            val milli_second = time % 100
            val second = (time % 6000) / 100
            val minute = time / 6000

            runOnUiThread { //UI 스레드 생성
                if (isRunning) {
                    //밀리초, if문을 추가하여 전체 텍스트 길이를 두 자리로 유지
                    tv_millisecond.text = if (milli_second < 10) ".0${milli_second}" else ".${milli_second}"

                    //초, if문을 추가하여 전체 텍스트 길이를 두 자리로 유지
                    tv_second.text = if(second < 10) ":0${second}" else ":${second}"

                    //분
                    tv_minute.text = "${minute}"
                }
            }

        }
    }

    private fun pause(){
        btn_start.text = "시작"
        btn_start.setBackgroundColor(getColor(R.color.blue))

        isRunning = false
        timer?.cancel()
    }

    private fun refresh(){
        timer?.cancel()

        btn_start.text = "시작"
        btn_start.setBackgroundColor(getColor(R.color.blue))
        isRunning = false

        time = 0

        tv_millisecond.text = ".00"
        tv_second.text = ":00"
        tv_minute.text = "00"
    }
}