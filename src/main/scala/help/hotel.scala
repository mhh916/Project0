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
import net.liftweb.json._


class Hotel() {
  implicit val formats = DefaultFormats
  var total = 1

  // Uses parameters to create a document and import into the collection provided in parameters(collection)
  def checkIn(room: Int, customerID: Int, bookingDate: String,  nights: Int, bookingCollection: MongoCollection[Document], roomCollection: MongoCollection[Document]): Unit = {
    // Creates document based on input parameters
    val doc: Document = Document(
      "roomId" -> room,
      "customerId" -> customerID,
      "bookingDate" -> bookingDate,
      "nights" -> nights,
      "totalCharge" -> checkInHelper(room, nights, roomCollection)
    )
    
    // Creates observable while inserting document into the collection
    val observable: Observable[Completed] = bookingCollection.insertOne(doc)
        observable.subscribe(new Observer[Completed] {
            override def onNext(result: Completed): Unit = println("Adding Guest")
            override def onError(e: Throwable): Unit = println("Failed")
            override def onComplete(): Unit = println(s"Successfully booked in Room $room \n")
        })
    
    // Update room to be occupied
    roomCollection.updateOne(equal("roomNumber", room), set("occupied", true)).results()

  }

  def checkOut(customerID: Int, bookingCollection: MongoCollection[Document], roomCollection: MongoCollection[Document]): Unit = {
    var roomNum = 0
    var results = bookingCollection.find(equal("customerId", customerID)).projection(fields(include("roomId"),excludeId())).results()
    for(result <- results) {
        val jsonString = result.toJson()
        val jValue = parse(jsonString)
        val resultDoc = jValue.extract[RoomNum]
        roomNum = resultDoc.roomId
    }
    bookingCollection.deleteOne(equal("customerId", customerID)).printHeadResult("Deleted: ")
    roomCollection.updateOne(equal("roomNumber", roomNum), set("occupied", false)).printHeadResult("Updated: ")



  }

  def checkInHelper(room: Int, nights: Int, roomCollection: MongoCollection[Document]): Int = {
    var totalCharge: Int = 0
    var price: Int = 0
    var results = roomCollection.find(equal("roomNumber", room)).projection(fields(include("price"),excludeId())).results()
    
    for(result <- results) {
        val jsonString = result.toJson()
        val jValue = parse(jsonString)
        val resultDoc = jValue.extract[Price]
        price = resultDoc.price.toInt
        totalCharge = nights * price
    }
    totalCharge

  }

  def chargeGuest(charge: Int, customerId: Int, bookingCollection: MongoCollection[Document]): Unit = {
    var current: Int = 0
    var newCharge: Int = 0
    
    var results = bookingCollection.find(equal("customerId", customerId)).projection(fields(include("totalCharge"),excludeId())).results()
    
    for(result <- results) {
        val jsonString = result.toJson()
        println(jsonString)
        val jValue = parse(jsonString)
        val resultDoc = jValue.extract[Charge]
        current = resultDoc.totalCharge.toInt
        newCharge = current + charge
    }
    bookingCollection.updateOne(equal("customerId", customerId), set("totalCharge", newCharge)).results()

  }

  def checkRooms(beds: Int, roomCollection: MongoCollection[Document]): Unit = {
    //var results = roomCollection.find(and(equal("occupied", false),equal("beds", beds))).projection(fields(exclude("occupied"),excludeId())).printResults("Rooms: ")
    var results = roomCollection.find(and(equal("occupied", false),equal("beds", beds))).projection(fields(exclude("occupied"),excludeId())).results()
    for(result <- results) {
        val jsonString = result.toJson()
        val jValue = parse(jsonString)
        val resultDoc = jValue.extract[Rooms]
         println(s"Room Number: ${resultDoc.roomNumber}, Beds: ${resultDoc.beds}, Description: ${resultDoc.description}, Price: ${resultDoc.price}")
       
    }
  }

  def importBookings(bookingCollection: MongoCollection[Document], roomCollection: MongoCollection[Document]): Unit = {
    //println("RoomId, customerId, bookingDate, nights")
    val bs = io.Source.fromFile("C:/Users/M1/Desktop/Work/Rev/Scala Big Data/Scala Code/Project/Project0/src/main/scala/sample.csv")
    for (line <- bs.getLines()) {
        val cols = line.split(",").map(_.trim)
        val roomNum = cols(0)
        val custID = cols(1)
        val date = cols(2)
        val nights = cols(3)

        checkIn(roomNum.toInt,custID.toInt,date,nights.toInt,bookingCollection,roomCollection)
    }
    
    bs.close()

    println("\nGroup registered successfully")
    println()
  }


  def viewGuestList(): Unit = {

  }

  def exportGuestList(): Unit = {
    
  }
}
case class Charge(totalCharge: Int)

case class Price(price: Int)

case class RoomNum(roomId: Int)
case class Rooms(roomNumber: Int, beds: Int, description: String, price: Int)