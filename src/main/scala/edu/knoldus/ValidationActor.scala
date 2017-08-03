package edu.knoldus

import akka.actor.{ActorLogging, ActorRef, Props, Actor}

class ValidationActor(purchaseActorMasterRef: ActorRef) extends Actor with ActorLogging{

  var numberOfItems = 1000

  override def receive: Unit = {

    case customer: Customer =>

      if(numberOfItems > 0){
        log.info("Smartphone is available. Forwarding request to PurchaseActorMaster.")
        numberOfItems = numberOfItems -1
        purchaseActorMasterRef.forward(customer)
      }

      else{
        log.info("Smartphone is out of stock! Sending out of box message back to customer.")
        sender() ! "Out Of Stock!"

      }

    case msg =>
      log.error("Invalid information received for validation.")
      sender() ! "Invalid information received for validation"
  }

}

object ValidationActor {

  def props(purchaseActorMasterRef: ActorRef): Props =
    Props(classOf[ValidationActor], purchaseActorMasterRef)
}
