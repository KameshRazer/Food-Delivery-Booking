package mypack;

import java.util.Date;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class DeliveryExecutives {
    String delExId ="DE";
    String currentLocation = "\0";
    String destination = "\0";
    String customerId ="\0";
    Date pickUpTime ;
    Date deliveryTime;
    int ordersCount = 0;
    int deliveryCharge = 0;

    public DeliveryExecutives(){}
    public DeliveryExecutives(int i){
        this.delExId = this.delExId + i;
    }

    //Return Available Executives list and their current Earning
    public void availableExecutives(FileWriter fileWriter){
        try{
            fileWriter.write(delExId+"\t\t\t\t"+deliveryCharge+"\n");
        }catch(IOException e){
            System.out.println("IO Error : "+e.getMessage());
        }
    }

    
    //Assigning Executives
    public void assignDeliveryExecutive(String currentLocation, String destination, Date orderTime, String custId){
        this.currentLocation = currentLocation;
        this.destination = destination;
        this.customerId = custId;
        this.pickUpTime = orderTime;
        this.pickUpTime.setTime(orderTime.getTime() + (15 * 60000)); //Setting pick up time to 15 mins from ordered time
        this.deliveryTime = (Date) this.pickUpTime.clone();
        this.deliveryTime.setTime(deliveryTime.getTime() + (30 * 6000));
        assignDeliveryExecutive(50);

    }

    // Handling combined orders and orders count
    public boolean assignDeliveryExecutive( int amount){   
        if(this.ordersCount< 5){
            this.deliveryCharge += amount;
            this.ordersCount += 1;
            return true;
        }else{
            return false;
        }
    }

    //Checking Previous Order Location and Time gap
    public boolean checkPreviousOrder(String location, String destPoint ,Date orderTime){
        if(currentLocation.equals(location) && destination.equals(destPoint)){
            long difference = orderTime.getTime() - pickUpTime.getTime();
            difference = (difference/1000)/60;

            if(difference>15)
                return false;
            else
                return true;
        }else{
            return false;
        }
    }

    //Return Executive Activity
    public String displayExecutiveActivity(){
        String result="";
        final String TAB = " \t\t ";

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        result = delExId + TAB +currentLocation + TAB + destination+ TAB + ordersCount;
        result = result + TAB +dateFormat.format(pickUpTime);
        result = result + TAB + dateFormat.format(deliveryTime) + TAB + deliveryCharge;
        return result;
    
    }

    //Return Excecution allowance, total earnings
    public String totalEarned(){
        final String TAB="\t\t";
        String result ="";
        result = delExId+ TAB +"10"+ TAB +deliveryCharge+ TAB + (deliveryCharge+10);
        return result;
    }

    public int checkTimeDifference(Date currOrderTime){
        long difference = currOrderTime.getTime() - deliveryTime.getTime();
        return (int)(difference/1000)/60 ;
    }
    
    //Return Executives Earning
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
