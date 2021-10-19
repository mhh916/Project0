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
    Thread.sleep(100)
    println()
    println("Welcome to Venom Corporation's Hotel Management System")
    println()

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
    def disconnect(): Unit = {
        println("Closing database connection...\n ")
        client.close()
    }



}