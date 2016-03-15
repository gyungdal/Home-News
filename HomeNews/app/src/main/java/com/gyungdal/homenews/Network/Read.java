package com.gyungdal.homenews.Network;
import java.net.URL;
import java.util.List;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;
import android.util.Log;

public class Read{
    private static Thread thread = null;
    private static String parsing_url = "192.168.0.156";  // URL
    private String get_data;  // 파싱 결과 저장
    private static final String TAG = "Network Read";
    private String data;
    final byte ch;
    public Read(){
        ch = 5;
    }
    public Read(byte i){
        this.ch = i;
    }
    public void setUrl(String Url){
        this.parsing_url = Url;
    }
    public String getUrl(){
        return parsing_url;
    }
    public String get(){
        Runnable task = new Runnable() {
            public void run() {
               data = getData(parsing_url,ch);
            }
        };
        thread = new Thread(task);
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return data;
    }


    // 파싱 작업을 하는 메서드
    public String getData(String strURL,byte ch){
        Source source;
        get_data = "";
        String[] type = {"Temp", "Humi", "Gas", "Dust"}; //0은 온도, 1은 습도, 2는 먼지, 3은 가스
        try{
            URL url = new URL(strURL);
            source = new Source(url);
            Element element = null;
            List<Element> list = source.getAllElements(HTMLElementName.A); // a 태그의 엘리먼트 가져옴
            for(int i = 0; i < list.size(); i++){
                element = list.get(i);
                String attributevalue = element.getAttributeValue("type");  // a 태그의 type을 찾는다
                if(attributevalue != null){
                    if(attributevalue.equalsIgnoreCase(type[ch])){  // type의 값이 type[ch] 이면
                        TextExtractor textExtractor = element.getTextExtractor();  // 해당 문자값을 가져온다
                        get_data = textExtractor.toString();  // 가져온 값을 스트링으로 변환후 출력
                    }
                }
            }
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
            get_data = "0";
        }
        return get_data;  // 입력된 배열값을 리턴
    }


}

