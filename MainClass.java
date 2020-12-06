import mypack.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;


class MainClass{
    public static int numberOfExcecutive = 5 ;
    public static DeliveryExcecutives[] delEx = new DeliveryExcecutives[numberOfExcecutive];
    public static int id =0;
    public static String assignedDelExceID ="";
    public static int maxAllotedId=-1;

    public static void handleBooking(String custId, String currentLocation, String destination, Date orderTime){
        Boolean result = false;
        //Loop is checking with Previous order ,order count. If valid combine order
        for(int i=0; i<=maxAllotedId; i++ ){
            result = delEx[i].checkPreviousOrder(currentLocation, destination,orderTime);
            if(result){
                result = delEx[i].assignDeliveryExcecutive(5);
                assignedDelExceID ="DE"+ (i+1) + " (because same location within 15 minutes)";
                break;
            }
        }

        //Not matched with Previous order Assign new Excecutive if Possible
        if(!result){
            if(id < numberOfExcecutive){
                delEx[id].assignDeliveryExcecutive(currentLocation, destination, orderTime, custId);
                maxAllotedId = id;
                id++;
                assignedDelExceID ="DE"+ (id);
            }else{
                //Checking least Delivery Excecutive
                List<Integer> availableExcecutiveId = new ArrayList<>();
                for(int i=0; i<maxAllotedId; i++){
                    int difference = delEx[i].checkTimeDifference(orderTime);
                    if(difference>=0){
                        availableExcecutiveId.add(i);
                    }
                }
                //Display All Excecutives are busy
                if(availableExcecutiveId.isEmpty()){
                    System.out.println("Delivery Excecutives are Busy");
                    assignedDelExceID = "Delivery Excecutives are Busy";
                }else{
                    //Finding Excecutive who has least delivery charge
                    int leastId = availableExcecutiveId.get(0);
                    for (Integer integer : availableExcecutiveId) {
                        if(delEx[leastId].getDeliveryCharge() >delEx[integer].getDeliveryCharge()){
                            leastId = integer;
                        }
                    }
                    //Assign order to least Delivery Excecutive
                    delEx[leastId].assignDeliveryExcecutive(currentLocation, destination, orderTime, custId);
                    assignedDelExceID ="DE"+ (leastId+1);
                }
                
            }                
        }            

    }
    
    


    //Program Excecution Starts here
    public static void main(String[] args){
        
        //Creating number of Delivery Excecutives
        for(int i=0; i<numberOfExcecutive; i++)
            delEx[i] = new DeliveryExcecutives(i+1);

        try{
            File inputFile = new File("TextFiles/input.txt");
            Scanner inputReader = new Scanner(inputFile);
            while(inputReader.hasNextLine()){
                String custId = inputReader.nextLine().trim();
                String restaurant = inputReader.nextLine().trim();
                String destPoint = inputReader.nextLine().trim();
                String time = inputReader.nextLine().trim();

                //Cleaning Raw Input for further process
                custId = custId.substring(13);
                restaurant = restaurant.substring(12);
                destPoint = destPoint.substring(19);
                time = time.substring(6);
                time = time.replace('.', ':');
                inputReader.nextLine();
                

                try{
                    FileWriter fileWriter = new FileWriter("TextFiles/output.txt",true);
                    fileWriter.write("Booking Id : "+custId+"\n");
                    fileWriter.write("Available Excecutives:\n\n");
                    fileWriter.write("Excecutives \t\t Delivery Charge Earned\n");
                    for(int i=0; i<numberOfExcecutive; i++ )
                        delEx[i].availableExcecutives(fileWriter);
                    
                    Date orderTime = stringToTime(time);
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
            fileWriter.write("TRIP\tEXCECUTIVE\t RESTAURANT\t  DESTINATION POINT\t ORDERS\t PICK-UP TIME\t DELIVERY TIME\t     DELIVERY CHARGES\n");
            for(int i=0; i<=maxAllotedId; i++ ){
                String result = delEx[i].displayExcecutiveActivity();
                fileWriter.write((i+1)+"\t\t"+result+"\n");
            }

            fileWriter.write("\nTotal Earned\n");
            fileWriter.write("Excecutive   Allowance \t Deliver Charges \t Total\n");
            for(int i=0; i<=maxAllotedId; i++){
                String result = delEx[i].totalEarned();
                fileWriter.write(result+"\n");
            }
            fileWriter.write("\nEnd of the Output\n\n");
            fileWriter.close();
        }catch(IOException e){
            System.out.println("IO Error : "+e.getMessage());
        }
    }

    //Convert String to Time
    public static Date stringToTime(String time){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            return dateFormat.parse(time);
        }catch(Exception e){
            System.out.println("Time Convention Error "+e.getMessage());
            return null;
        }        
    }
    
   
}