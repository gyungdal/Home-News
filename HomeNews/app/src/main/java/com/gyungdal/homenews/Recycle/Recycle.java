package com.gyungdal.homenews.Recycle;


import com.gyungdal.homenews.Card.Description;
import com.gyungdal.homenews.Network.Check;
import com.gyungdal.homenews.Network.Read;

/**
 * Created by GyungDal on 2015-11-12.
 */
public class Recycle{
    private int image;
    private String title;
    private byte Num;
    private String Des;
    public int getImage(){return this.image;}
    public String getTitle(){ return this.title; }
    public String getDes(){return this.Des;}
    public byte getNum(){return this.Num;}

    public Recycle(int image, String title, byte Num){
        this.image= image;
        this.title= title;
        this.Num = Num;
    }
    public void initDes(){ this.Des = "0"; }
    public void setDes() {
        Des = new Read(Num).get();
        image = new Description().Image_Select(Num, Double.valueOf(Des), new Check().isNetwork());
    }
}
