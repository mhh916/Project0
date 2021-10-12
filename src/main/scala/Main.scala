
import scala.io.StdIn._

object Main {
  def main(args: Array[String]): Unit = {
    var loop = true

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
          println("Not yet implemented")
          println()
          Thread.sleep(500)
        }

        case 2 => {
          println("Not yet implemented")
          println()
          Thread.sleep(500)
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
          println("Partially implemented")
          importList()
          Thread.sleep(500)
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

  //def splash(): Unit = {
    // println("Welcome to Venom Corporation's Hotel Management System")
    // manager = readLine("Insert Username: ")
    // println()
    // println(s"Welcome $manager, please select an option")
    // db = new DBConnect(manager)
    // db.connect
  //}
  
  def importList(): Unit = {
    //println("Room, FirstName, LastName, Price, Nights, Credit Card, ccMonth, cvc, total")
    val bs = io.Source.fromFile("C:/Users/M1/Desktop/Work/Rev/Scala Big Data/Scala Code/Project/Project0/src/main/scala/sample.csv")
    for (line <- bs.getLines()) {
        val cols = line.split(",").map(_.trim)
        
        //println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}|${cols(4)}|${cols(5)}|${cols(6)}|${cols(7)}|${cols(8)}")
        //add code to send to database
    }
    bs.close()

    println("Group registered successfully")
    println()
  }

}

 
class Hotel(val room: Int, var fname: String, var lname: String, var price: Double, var nights: Int, var creditCard: Int, val ccMonth: String, val cvc: Int) {
  var total = price * nights

  def checkIn(): Unit = {
    
  }

  def checkOut(): Unit = {
    
  }

  def chargeGuest(charge: Double): Unit = {
    total += charge
  }
}

class DBConnect(user: String) {
  def connect(): Unit = {

  }

  def disconnect(): Unit = {
    println("Logout Successfull")
    println()
    Thread.sleep(1001)
  }

}

