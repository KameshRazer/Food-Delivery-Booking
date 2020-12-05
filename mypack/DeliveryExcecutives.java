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


    public void availableExcecutives(FileWriter fileWriter){
        
        try{
            fileWriter.write(delExId+"\t\t\t"+deliveryCharge+"\n");
        }catch(IOException e){
            System.out.println("IO Error : "+e.getMessage());
        }
    }

    

    public void assignDeliveryExcecutive(int delExId, String currentLocation, String destination, String currTime, String custId){
        this.delExId += delExId;
        this.currentLocation = currentLocation;
        this.destination = destination;
        this.customerId = custId;
        this.pickUpTime = stringToTime(currTime);
        this.pickUpTime.setTime(this.pickUpTime.getTime() + (15 * 60000)); //Setting pick up time to 15 mins from ordered time
        assignDeliveryExcecutive(50);

    }

    //Method Overloading (assignDeliverExcecutive) for handling combined orders
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
            System.out.println("Time Convention Error");
            return null;
        }        
    }


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


    public String displayExcecutiveActivity(){
        String result="";
        final String TAB = " \t\t ";

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Date deliveryTime = (Date) this.pickUpTime.clone();
        deliveryTime.setTime(deliveryTime.getTime()+(30*60000));

        result = this.delExId + TAB +this.currentLocation + TAB + this.destination+ TAB +this.ordersCount;
        result = result + TAB +dateFormat.format(this.pickUpTime);
        result = result + TAB + dateFormat.format(deliveryTime) + TAB + this.deliveryCharge;

        return result;
    
    }
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
