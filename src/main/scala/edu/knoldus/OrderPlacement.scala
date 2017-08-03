package edu.knoldus

import akka.pattern._
import akka.actor.ActorSystem
import akka.util.Timeout
import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global

object OrderPlacement extends App {

  val system = ActorSystem("PurchaseHandler")

  val purchaseActorRef = system.actorOf(PurchaseActor.props)

  val purchaseActorMasterRef = system.actorOf(PurchaseActorMaster.props(purchaseActorRef))

  val validationActorRef = system.actorOf(ValidationActor.props(purchaseActorMasterRef))

  val purchaseRequestHandlerRef = system.actorOf(PurchaseRequestHandler.props(validationActorRef))

  val customer = new Customer("Jasmine", "New Delhi", 123456789123456L, 1234567890)

  implicit val timeout = Timeout(100 seconds)

  val purchaseResult = purchaseRequestHandlerRef ? customer

  purchaseResult.foreach(println(_))

}
