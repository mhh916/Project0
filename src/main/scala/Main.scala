import scala.io.StdIn._
import org.mongodb.scala._
import help.Helpers._
import help.Hotel
import help.DBConnect

object Main {
  def main(args: Array[String]): Unit = {
    var loop = true
    val hotel = new Hotel()
    val db = new DBConnect()
   
    do{
      
      println("Please select an option")
      println("1. Check-in\n2. Check-out\n3. Charge Client\n4. Check Available Rooms\n5. Group Register\n6. Export Guest List\n7. View Guest List\n8. Quit Application")
      
      try {
      val option = readInt()
      println()
      option match{
        case 1 => {
          // Generates values based on user input
          val id = readLine("Insert Customer ID: ")
          val date = readLine("Insert Date: ")
          val nights = readLine("Insert Amount of Nights: ")
          val room = readLine("Insert Room Number: ")
          println()
          // Passes values as parameters to checkIn function
          hotel.checkIn(room.toInt, id.toInt, date, nights.toInt, db.getBookings(),db.getRooms())        
        }

        case 2 => {
          // Generates values based on user input
          val id = readLine("Insert Customer ID: ")
          // Passes values as parameters to checkOut function
          hotel.checkOut(id.toInt, db.getBookings(), db.getRooms())
        }

        case 3 => {
          // Generates values based on user input
          val charge = readLine("Insert Charge Amount: ")
          val id = readLine("Insert Customer ID: ")
          // Passes values as parameters to chargeGuest function
          hotel.chargeGuest(charge.toInt, id.toInt, db.getBookings())
        }

        case 4 => {
          // Generates values based on user input
          val beds = readLine("Would you like 1 or 2 beds?: ")
          // Passes values as parameters to checkRooms function
          hotel.checkRooms(beds.toInt, db.getRooms())
        }

         case 5 => {
          // Uses getters from DBConnect to pass collections to importBookings
          hotel.importBookings(db.getBookings(), db.getRooms())
        }

        case 6 => {
          // Uses getters from DBConnect to pass collections to exportGuestList
          hotel.exportGuestList(db.getBookings(), db.getCustomers())
        }

        case 7 => {
          // Uses getters from DBConnect to pass collections to viewGuestList
          hotel.viewGuestList(db.getBookings(), db.getCustomers())
        }  


        case 8 => {
          // Calls function from DBConnect Object to close client connection and ends loop
          db.disconnect()
          loop = false
        }
      }
      }catch {
        case e: MatchError => println("Please pick a number between 1~8\n")
        case e: NumberFormatException => println("\nPlease enter a number\n") 
      }
      
    } while(loop) 
    println("Thank you for using Venom Corporation's Hotel Management System")
  }
}