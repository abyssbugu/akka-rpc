package com.abyss.akka.spark

trait RemoteMessage  extends Serializable{

}

//worker向master发送注册信息，由于不在同一进程中，需要实现序列化
case class RegisterMessage(workerId:String, memory:Int, cores:Int) extends RemoteMessage
//master反馈注册成功信息给worker,由于不在同一进程中，也需要实现序列化
case class RegisteredMessage(message:String) extends RemoteMessage
//worker向worker发送心跳 由于在同一进程中，不需要实现序列化
case object HeartBeat
//worker向master发送心跳，由于不在同一进程中，需要实现序列化
case class SendHeartBeat(workerId:String) extends RemoteMessage
//master自己向自己发送消息，由于在同一进程中，不需要实现序列化
case object CheckOutTime
