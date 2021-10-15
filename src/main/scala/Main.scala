
import scala.io.StdIn._
import org.mongodb.scala._
import help.Helpers._
import help.Hotel
import help.DBConnect

object Main {
  def main(args: Array[String]): Unit = {
    var loop = true
    val hotel = new Hotel()
    
    println()
    println("Welcome to Venom Corporation's Hotel Management System")
    var manager = readLine("Insert Username: ")
    println()
    
    var db = new DBConnect(manager)
    db.connect()
    println(s"Welcome $manager, please select an option")

      
    do{
      
      println("1. Check-in\n2. Check-out\n3. Charge Client\n4. View Guest List\n5. Group Register\n6. Export Guest List\n7. Log-Out\n8. Quit Application")
      
      try {
      val option = readInt()
      println()
      option match{
        case 1 => {
          // Generates values based on user input
          val id = readLine("Insert Customer ID: ")
          val date = readLine("Insert Date: ")
          val room = readLine("Insert Room Number: ")
          
          // Passes values as parameters to checkIn function
          hotel.checkIn(room.toInt, id.toInt, date, db.getBookings())
        
        }

        case 2 => {
          val id = readLine("Insert Customer ID: ")
          val date = readLine("Insert Date: ")

          hotel.checkOut()
        }

        case 3 => {
          println("Not yet implemented")
          println()
          Thread.sleep(500)
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
          db.disconnect()
          Thread.sleep(500)
          println("Welcome to Venom Corporation's Hotel Management System")
          manager = readLine("Insert Username: ")
          println()
          println(s"Welcome $manager, please select an option")
          db = new DBConnect(manager)
          db.connect()
          
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