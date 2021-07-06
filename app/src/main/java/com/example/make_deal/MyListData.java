package com.example.make_deal;
public class MyListData{
    private String description;
    private String imgId;
    private String price;
    private String time;
    private String duration_type;
    private String destination;

    public MyListData(String name, String time, String duration_type, String price, String url,String destination) {

        this.description = name;
        this.imgId = url;
        this.price=price;
        this.time=time;
        this.duration_type=duration_type;
        this.destination=destination;
    }

    


    public void arrange_list(){


        //this.description="hi";
      //  this.imgId=android.R.drawable.ic_dialog_map;

    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImgId() {
        return imgId;
    }
    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getDuration_type() {
        return duration_type;
    }
    public void setDuration_type(String duration_type) {
        this.duration_type = duration_type;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }


}

