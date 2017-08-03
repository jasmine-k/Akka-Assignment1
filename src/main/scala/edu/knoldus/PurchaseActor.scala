package edu.knoldus

import akka.actor._
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

class PurchaseActorMaster(purchaseActorRef: ActorRef) extends Actor with ActorLogging {

  var router = {
    log.info("Creating routees")
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[PurchaseActor])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def receive: Unit = {

    case customer: Customer => log.info("Sending customer to worker PurchaseActor.")
      router.route(customer, sender())
    case Terminated(purchasedActorTerminate) =>
      log.info("Terminating worker PurchaseActor")
      router = router.removeRoutee(purchasedActorTerminate)
      val purchaseActorRef = context.actorOf(Props[PurchaseActor])
      context watch purchaseActorRef
      router = router.addRoutee(purchaseActorRef)

  }

}

object PurchaseActorMaster {

  def props(purchaseActorRef: ActorRef): Props =
    Props(classOf[PurchaseActorMaster], purchaseActorRef)

}

class PurchaseActor extends Actor with ActorLogging {

  override def receive: Unit = {
    case customer: Customer => log.info("Sending Smartphone to customer.")
      sender() ! SamsungSmartphone
  }

}

object PurchaseActor {

  def props: Props = Props[PurchaseActor]

}
