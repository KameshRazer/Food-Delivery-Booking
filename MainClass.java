import mypack.*;
import java.io.*;
import java.util.*;


class MainClass{
    public static int numberOfExcecutive = 5 ;
    public static DeliveryExcecutives[] delEx = new DeliveryExcecutives[numberOfExcecutive];
    public static int id =0;
    public static String assignedDelExceID ="";

    public static void handleBooking(String custId, String currentLocation, String destination, String orderTime){
        Boolean result = false;

        for(int i=0; i<id; i++ ){
            result = delEx[i].checkPreviousOrder(currentLocation, destination,orderTime);
            if(result){
                result = delEx[i].assignDeliveryExcecutive(5);
                assignedDelExceID ="DE"+ id + " (because same location within 15 minutes)";
                break;
            }
        }

        if(!result){
            if((id+1) < numberOfExcecutive){
                delEx[id++].assignDeliveryExcecutive(currentLocation, destination, orderTime, custId);
                assignedDelExceID ="DE"+ id;
            }else
                System.out.println("Delivery Excecutives UnAvailable");                
        }            

    }


    public static void main(String[] args){
        
        for(int i=0; i<numberOfExcecutive; i++)
            delEx[i] = new DeliveryExcecutives(i+1);

        try{
            File inputFile = new File("TextFiles/input.txt");
            Scanner inputReader = new Scanner(inputFile);
            while(inputReader.hasNextLine()){
                String custId = inputReader.nextLine().trim();
                String restaurant = inputReader.nextLine().trim();
                String destPoint = inputReader.nextLine().trim();
                String orderTime = inputReader.nextLine().trim();

                //Cleaning Raw Input for further process
                custId = custId.substring(13);
                restaurant = restaurant.substring(12);
                destPoint = destPoint.substring(19);
                orderTime = orderTime.substring(6);
                orderTime = orderTime.replace('.', ':');
                inputReader.nextLine();

                try{
                    FileWriter fileWriter = new FileWriter("TextFiles/output.txt",true);
                    fileWriter.write("Booking Id : "+custId+"\n");
                    fileWriter.write("Available Excecutives:\n\n");
                    fileWriter.write("Excecutives \t\t Delivery Charge Earned\n");
                    for(int i=0; i<numberOfExcecutive; i++ )
                        delEx[i].availableExcecutives(fileWriter);
                    
                    handleBooking(custId,restaurant,destPoint,orderTime);
                    
                    fileWriter.write("Allotted Delivery Executive : "+assignedDelExceID+"\n");
                    fileWriter.write("-----------------------------------------\n\n");
                    fileWriter.close();
                }catch(IOException e){
                    System.out.println("IO Error : "+e.getMessage());
                }   
            }
            inputReader.close();
        }catch(FileNotFoundException e){
            System.out.println("File not found");
        }

        try{
            //Writing Delivery History in Output.txt file

            FileWriter fileWriter = new FileWriter("TextFiles/output.txt",true);
            fileWriter.write("Delivery History \n");
            fileWriter.write("TRIP\tEXCECUTIVE\t   RESTAURANT\t  DESTINATION POINT\t   ORDERS\t     PICK-UP TIME\t   DELIVERY TIME\t     DELIVERY CHARGES\n");
            for(int i=0; i<id; i++ ){
                String result = delEx[i].displayExcecutiveActivity();
                fileWriter.write((i+1)+"\t\t"+result+"\n");
            }

            fileWriter.write("\nTotal Earned\n");
            fileWriter.write("Excecutive\t Allowance\t Deliver Charges\t Total\n");
            for(int i=0; i<id; i++){
                String result = delEx[i].totalEarned();
                fileWriter.write(result+"\n");
            }
            fileWriter.close();
        }catch(IOException e){
            System.out.println("IO Error : "+e.getMessage());
        }
    }
    
   
}