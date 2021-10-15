package help

import scala.io.StdIn._
import org.mongodb.scala._
import help.Helpers._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.Projections._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps
import org.mongodb.scala.model.Aggregates._


class Hotel() {
  var total = 1

  // Uses parameters to create a document and import into the collection provided in parameters(collection)
  def checkIn(room: Int, customerID: Int, bookingDate: String,  nights: Int, bookingCollection: MongoCollection[Document], roomCollection: MongoCollection[Document]): Unit = {
    // Creates document based on input parameters
    val doc: Document = Document(
      "roomId" -> room,
      "customerId" -> customerID,
      "bookingDate" -> bookingDate,
      "totalCharge" -> checkInHelper(room, nights, roomCollection)
    )
    
    // Creates observable while inserting document into the collection
    val observable: Observable[Completed] = bookingCollection.insertOne(doc)
        observable.subscribe(new Observer[Completed] {
            override def onNext(result: Completed): Unit = println("Adding Guest")
            override def onError(e: Throwable): Unit = println("Failed")
            override def onComplete(): Unit = println(s"Successfully booked in Room $room ")
        })
    
    // Update room to be occupied
    roomCollection.updateOne(equal("roomNumber", room), set("occupied", true))
 
  }

  def checkOut(): Unit = {
    
  }

  def checkInHelper(room: Int, nights: Int, roomCollection: MongoCollection[Document]): Int = {
    room match {
        case room if room > 250 => 300 * nights
        case room if room > 150 => 200 * nights
        case room if room > 100 => 100 * nights
    }
   
     
  }

  def chargeGuest(charge: Double): Unit = {
    //total += charge
  }

  def checkRooms(roomCollection: MongoCollection[Document]): Unit = {

  }

  def importBookings(bookingCollection: MongoCollection[Document], roomCollection: MongoCollection[Document]): Unit = {
    //println("RoomId, customerId, bookingDate")
    val bs = io.Source.fromFile("C:/Users/M1/Desktop/Work/Rev/Scala Big Data/Scala Code/Project/Project0/src/main/scala/sample.csv")
    for (line <- bs.getLines()) {
        val cols = line.split(",").map(_.trim)
        var roomNum = cols(0)
        println(roomNum)
        //println(s"${cols(0)}|${cols(1)}|${cols(2)}")
        val doc: Document = Document(
          "roomId" -> cols(0),
          "customerId" -> cols(1),
          "bookingDate" -> cols(2)
        )
        //add booking to database
        val observable: Observable[Completed] = bookingCollection.insertOne(doc)
        observable.subscribe(new Observer[Completed] {
          override def onNext(result: Completed): Unit = println("Adding Guests")
          override def onError(e: Throwable): Unit = println("Failed")
          override def onComplete(): Unit = println(s"Successfully imported online bookings")
        })
        
        // Update room to be occupied
        //roomCollection.find(equal("roomNumber",roomNum.toInt)).printResults()
        roomCollection.updateOne(equal("roomNumber", roomNum.toInt), set("occupied", true)).printHeadResult("Updating Room to Occupied\n")
    
      
    }
    
    bs.close()

    println("Group registered successfully")
    println()
  }
}