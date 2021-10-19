package help

import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps
import org.mongodb.scala._
import org.mongodb.scala.model._
import net.liftweb.json._
import scala.io.StdIn._
import help.Helpers._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.Projections._
import scala.concurrent.ExecutionContext.Implicits.global
import org.mongodb.scala.model.Aggregates._
import java.io.File
import java.io.PrintWriter


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
  // Checks out a guest with  their given ID and updates their room to not be occupied
  def checkOut(customerID: Int, bookingCollection: MongoCollection[Document], roomCollection: MongoCollection[Document]): Unit = {
    var roomNum = 0
    // Finds document from bookings collection based on customer ID and projects to show only roomId
    var results = bookingCollection.find(equal("customerId", customerID)).projection(fields(include("roomId"),excludeId())).results()
    // Loops through results grabbing the roomId
    for(result <- results) {
        val jsonString = result.toJson()
        val jValue = parse(jsonString)
        val resultDoc = jValue.extract[RoomNum]
        roomNum = resultDoc.roomId
    }
    // Deletes the booking document based on customer ID
    bookingCollection.deleteOne(equal("customerId", customerID)).results()
    // Sets room taken from booking document and sets occupied to false
    roomCollection.updateOne(equal("roomNumber", roomNum), set("occupied", false)).results()
    println("\nSuccessfully Checked Out\n")
  }
  // Helper function designed to get price of room from room Collection and multiply it by the nights stayed
  def checkInHelper(room: Int, nights: Int, roomCollection: MongoCollection[Document]): Int = {
    var totalCharge: Int = 0
    var price: Int = 0
    // extracts room price from room collection
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
  // adds a charge amount to the property totalCharge in the booking document
  def chargeGuest(charge: Int, customerId: Int, bookingCollection: MongoCollection[Document]): Unit = {
    var current: Int = 0
    var newCharge: Int = 0
    // Gets document with specific customerId and projects only totalCharge property
    var results = bookingCollection.find(equal("customerId", customerId)).projection(fields(include("totalCharge"),excludeId())).results()
    // grabs old totalCharge and adds it to charge amount from input and creates new val newCharge as the sum
    for(result <- results) {
        val jsonString = result.toJson()
        val jValue = parse(jsonString)
        val resultDoc = jValue.extract[Charge]
        current = resultDoc.totalCharge.toInt
        newCharge = current + charge
    }
    // Updates the same booking document with the new total Charge
    bookingCollection.updateOne(equal("customerId", customerId), set("totalCharge", newCharge)).results()
    println("Charged Successfully")
    println()

  }
  // Goes through the room collection and outputs all unoccupied rooms
  def checkRooms(beds: Int, roomCollection: MongoCollection[Document]): Unit = {
    // Gets all rooms that are not occupied with with the input amount of beds
    var results = roomCollection.find(and(equal("occupied", false),equal("beds", beds))).projection(fields(exclude("occupied"),excludeId())).results()
    for(result <- results) {
        val jsonString = result.toJson()
        val jValue = parse(jsonString)
        val resultDoc = jValue.extract[Rooms]
         println(s"Room Number: ${resultDoc.roomNumber}, Beds: ${resultDoc.beds}, Description: ${resultDoc.description}, Price: ${resultDoc.price}")
       
    }
    println()
  }
  // import booking documents from sample.csv 
  def importBookings(bookingCollection: MongoCollection[Document], roomCollection: MongoCollection[Document]): Unit = {
    //println("RoomId, customerId, bookingDate, nights")
    val bs = io.Source.fromFile("C:/Users/M1/Desktop/Work/Rev/Scala Big Data/Scala Code/Project/Project0/src/main/scala/sample.csv")
    for (line <- bs.getLines()) {
        val cols = line.split(",").map(_.trim)
        val roomNum = cols(0)
        val custID = cols(1)
        val date = cols(2)
        val nights = cols(3)
        //passes parsed data to the check in function
        checkIn(roomNum.toInt,custID.toInt,date,nights.toInt,bookingCollection,roomCollection)
    }
    
    bs.close()

    println("\nGroup registered successfully")
    println()
  }

  // Shows current guest who occupy a room
  def viewGuestList(bookingCollection: MongoCollection[Document], customerCollection: MongoCollection[Document]): Unit = {
    var roomNum = 0
    var customerId = 0
    var totalCharge = 0
    // First get roomId, customer ID and total Charge from all bookings 
    var results = bookingCollection.find().projection(fields(include("roomId"), include("customerId"), include("totalCharge"),excludeId())).results()
    println()
    // Loop through results
    for(result <- results) {
        val jsonString = result.toJson()
        val jValue = parse(jsonString)
        
        val resultDoc = jValue.extract[Customer]
        roomNum = resultDoc.roomId
        customerId = resultDoc.customerId
        totalCharge = resultDoc.totalCharge
        // grab documents from custoemr collection using properties obtained from the booking collections
        var results2 = customerCollection.find(equal("customerId", customerId)).projection(fields(include("name"),excludeId())).results()
            for(result2 <- results2){
                val jsonString = result2.toJson()
                val jValue = parse(parseJson(jsonString))
                val resultDoc = jValue.extract[CustomerName]
                // Output the results from both queries
                println(s"Name: ${resultDoc.firstName} ${resultDoc.lastName}\n\t\t\tRoom: $roomNum    CustomerID: $customerId   Total Charge: $totalCharge")
                
            }


    }
    println()
  }
  // Export the current guest list into a new csv file
  def exportGuestList(bookingCollection: MongoCollection[Document], customerCollection: MongoCollection[Document]): Unit = {
    val fileObj = new File("Output.csv")
    val print_Writer = new PrintWriter(fileObj) 
    print_Writer.write("Name, CustomerID, Room, TotalCharge")

    var roomNum = 0
    var customerId = 0
    var totalCharge = 0
    var results = bookingCollection.find().projection(fields(include("roomId"), include("customerId"), include("totalCharge"), excludeId())).results()

    println()
    for(result <- results) {
        val jsonString = result.toJson()
        val jValue = parse(jsonString)
        
        val resultDoc = jValue.extract[Customer]
        roomNum = resultDoc.roomId
        customerId = resultDoc.customerId
        totalCharge = resultDoc.totalCharge
        var results2 = customerCollection.find(equal("customerId", customerId)).projection(fields(include("name"),excludeId())).results()
            for(result2 <- results2){
                val jsonString = result2.toJson()
                val jValue = parse(parseJson(jsonString))
                val resultDoc = jValue.extract[CustomerName]
                print_Writer.append(s"\n ${resultDoc.firstName} ${resultDoc.lastName}, $roomNum, $customerId, $totalCharge")                
            }
    }
     print_Writer.close() 
     println("Exported Successfully")
     println()
  }
  // Helper function for export/view guest list takes input json and outputs embedded document
  def parseJson(j: String): String = {
    val l = j.length()
    j.slice(8, l-1)
  }
}
case class Charge(totalCharge: Int)
case class Price(price: Int)
case class RoomNum(roomId: Int)
case class Rooms(roomNumber: Int, beds: Int, description: String, price: Int)
case class Customer(roomId: Int, customerId: Int, totalCharge: Int)
case class CustomerName(firstName: String, lastName: String)