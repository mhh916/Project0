
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
    
    Thread.sleep(100)

      
    do{
      println()
      println("Welcome to Venom Corporation's Hotel Management System")
      println()
      println("Welcome, please select an option")
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
          
          // Passes values as parameters to checkIn function
          hotel.checkIn(room.toInt, id.toInt, date, nights.toInt, db.getBookings(),db.getRooms())
          Thread.sleep(100)
        
        }

        case 2 => {
          val id = readLine("Insert Customer ID: ")
          hotel.checkOut(id.toInt, db.getBookings(), db.getRooms())
        }

        case 3 => {
          val charge = readLine("Insert Charge Amount: ")
          val id = readLine("Insert Customer ID: ")
          hotel.chargeGuest(charge.toInt, id.toInt, db.getBookings())
        }

        case 4 => {
          println("Not yet implemented")
          println()
          Thread.sleep(500)
        }

         case 5 => {
          hotel.importBookings(db.getBookings(), db.getRooms())
        }

        case 6 => {
          println("Not yet implemented")
          println()
          Thread.sleep(500)
        }

        case 7 => {
          
        }  
        
        case 8 => loop = false
        
      }
      }catch {
        case e: MatchError => println("Please pick a number between 1~8\n")
        case e: NumberFormatException => println("\nPlease enter a number\n")
        
      }
      
    } while(loop) 

    println("Thank you for using Venom Corporation's Hotel Management System")
    Thread.sleep(500)
  }
}