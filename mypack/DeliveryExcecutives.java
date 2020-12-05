package mypack;

import java.util.Date;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class DeliveryExcecutives {
    String delExId ="DE";
    String currentLocation = "\0";
    String destination = "\0";
    String customerId ="\0";
    Date pickUpTime ;
    int ordersCount = 0;
    int deliveryCharge = 0;


    public DeliveryExcecutives(int i){
        this.delExId = this.delExId + i;
    }

    //Return Available Excecutives list and their current Earning
    public void availableExcecutives(FileWriter fileWriter){
        try{
            fileWriter.write(delExId+"\t\t\t\t\t\t"+deliveryCharge+"\n");
        }catch(IOException e){
            System.out.println("IO Error : "+e.getMessage());
        }
    }

    
    //Assigning Excecutives
    public void assignDeliveryExcecutive(String currentLocation, String destination, String currTime, String custId){
        this.currentLocation = currentLocation;
        this.destination = destination;
        this.customerId = custId;
        this.pickUpTime = stringToTime(currTime);
        this.pickUpTime.setTime(this.pickUpTime.getTime() + (15 * 60000)); //Setting pick up time to 15 mins from ordered time
        assignDeliveryExcecutive(50);

    }

    // Handling combined orders and orders count
    public boolean assignDeliveryExcecutive( int amount){   
        if(this.ordersCount< 5){
            this.deliveryCharge += amount;
            this.ordersCount += 1;
            return true;
        }else{
            return false;
        }
    }

    //Convert String to Time
    public Date stringToTime(String time){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            return dateFormat.parse(time);
        }catch(Exception e){
            System.out.println("Time Convention Error "+e.getMessage());
            return null;
        }        
    }


    //Checking Previous Order Location and Time gap
    public boolean checkPreviousOrder(String location, String destPoint ,String time){
        if(currentLocation.equals(location) && destination.equals(destPoint)){
            Date currTime = stringToTime(time);
            long difference = currTime.getTime() - pickUpTime.getTime();
            difference = (difference/1000)/60;

            if(difference>15)
                return false;
            else
                return true;
        }else{
            return false;
        }
    }

    //Return Excecutive Activity
    public String displayExcecutiveActivity(){
        String result="";
        final String TAB = " \t\t\t\t ";

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Date deliveryTime = (Date) this.pickUpTime.clone();
        deliveryTime.setTime(deliveryTime.getTime()+(30*60000));

        result = this.delExId + TAB +this.currentLocation + TAB + this.destination+ TAB +this.ordersCount;
        result = result + TAB +dateFormat.format(this.pickUpTime);
        result = result + TAB + dateFormat.format(deliveryTime) + TAB + this.deliveryCharge;

        return result;
    
    }

    //Return Excecution allowance, total earnings
    public String totalEarned(){
        final String TAB="\t\t\t\t";
        String result ="";
        result = delExId+ TAB +"10"+ TAB +deliveryCharge+ TAB + (deliveryCharge+10);
        return result;
    }
    
    //Return Excecutives Earning
    public int getDeliveryCharge(){
        return deliveryCharge;
    }
    //Display Results
    public void printValues(){
        System.out.print(delExId+" || ");
        System.out.print(customerId+" || ");
        System.out.print(currentLocation+" || ");
        System.out.print(destination+" || ");
        System.out.print(deliveryCharge+" || ");
        System.out.print(ordersCount+" || ");
        System.out.println(pickUpTime+" || ");

    }
    
}
