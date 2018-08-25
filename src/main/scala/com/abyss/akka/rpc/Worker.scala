package com.abyss.akka.rpc

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * Created by Abyss on 2018/8/25.
  * description:利用akka的actor模型实现2个进程间的通信-----Worker端
  */

class Worker extends Actor {
  //构造代码块先被执行
  println("worker constructor invoked")

  //prestart方法会在构造代码块执行后被调用，并且只被调用一次
  override def preStart(): Unit = {
    println("preStart method invoked")
    //获取master actor的引用
    //ActorContext全局变量，可以通过在已经存在的actor中，寻找目标actor
    //调用对应actorSelection方法，
    // 方法需要一个path路径：1、通信协议、2、master的IP地址、3、master的端口 4、创建master actor老大 5、actor层级
    val master: ActorSelection = context.actorSelection("akka.tcp://masterActorSystem@192.168.31.127:8888/user/masterActor")

    //向master发送消息
    master ! "connect"
  }

  //receive方法会在prestart方法执行后被调用，表示不断的接受消息
  override def receive: Receive = {
    case "connect" => {
      println("a client connected")
    }
    case "success" =>{
      println("注册成功")
    }
  }
}

object Worker {
  def main(args: Array[String]): Unit = {
    //master的ip地址
    val host = args(0)
    //master的port端口
    val port = args(1)

    //准备配置文件信息
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
      """.stripMargin

    //配置config对象 利用ConfigFactory解析配置文件，获取配置信息
    val config = ConfigFactory.parseString(configStr)

    // 1、创建ActorSystem,它是整个进程中老大，它负责创建和监督actor，它是单例对象
    val workerActorSystem = ActorSystem("workerActorSystem", config)
    // 2、通过ActorSystem来创建master actor
    val workerActor: ActorRef = workerActorSystem.actorOf(Props(new Worker), "workerActor")
    // 3、向worker actor发送消息
        workerActor ! "connect"
  }
}

