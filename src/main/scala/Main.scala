
import scala.io.StdIn._
object Main {
  def main(args: Array[String]): Unit = {
    println("Welcome to Venom Corporation's Hotel Management System")
    var manager = readLine("Insert Username: ")
    println()

    var loop = true
    println(s"Welcome $manager, please select an option")
    do{
      println("1. Check-in\n2. Check-out\n3. Charge Client\n4. View Guest List\n5. Log-Out\n6. Quit Application")
      val option = readInt()
      option match{
        case 1 => {
          println("Not yet implemented")
          println()
          Thread.sleep(1000)
        }

        case 2 => {
          println("Not yet implemented")
          println()
          Thread.sleep(1000)
        }

        case 3 => {
          println("Not yet implemented")
          println()
          Thread.sleep(1000)
        }

        case 4 => {
          println("Not yet implemented")
          println()
          Thread.sleep(1000)
        }

        case 5 => {
          println("Logout Successfull")
          println()
          Thread.sleep(1000)
          println("Welcome to Venom Corporation's Hotel Management System")
          manager = readLine("Insert Username: ")
          println()
          println(s"Welcome $manager, please select an option")
          
        }  
        
        case 6 => loop = false
        
      }
    } while(loop) 
    println("Thank you for using Venom Corporation's Hotel Management System")
    Thread.sleep(2000)
  }
  
}