package edu.knoldus

import akka.actor._

class PurchaseRequestHandler(validationActorRef: ActorRef) extends Actor with ActorLogging {

  override def receive: Unit = {

    case customer: Customer =>
      log.info("Customer information received in PurchaseRequestHandler..Forwarding your request for validation")
      validationActorRef.forward(customer)

    case _ =>
      log.error("Invalid customer information")
      sender() ! "Invalid customer information"
  }

}

object PurchaseRequestHandler {

  def props(validationActorRef: ActorRef): Props =
    Props(classOf[PurchaseRequestHandler], validationActorRef)

}
