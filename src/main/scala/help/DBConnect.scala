package help

import scala.io.StdIn._
import org.mongodb.scala._
import help.Helpers._

class DBConnect() {
  val client: MongoClient = MongoClient()
  val database: MongoDatabase = client.getDatabase("test")
  val rooms: MongoCollection[Document] = database.getCollection("room")
  val bookings: MongoCollection[Document] = database.getCollection("bookings")
  val customers: MongoCollection[Document] = database.getCollection("customers")
  
  def getRooms(): MongoCollection[Document] = {
    rooms
  }

  def getBookings(): MongoCollection[Document] = {
    bookings
  }

  def getCustomers(): MongoCollection[Document] = {
    customers
  }

  def getDatabase(): MongoDatabase = {
    database
  }

  def getClient(): MongoClient = {
    client
  }



}