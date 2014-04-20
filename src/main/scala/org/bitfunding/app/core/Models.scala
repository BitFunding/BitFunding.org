package org.bitfunding.app.core

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema
import org.squeryl.annotations.Column
import java.util.Date
import java.sql.Timestamp
 
class User(

  val id: Long,
  val username: String,
  val email: String,
  val password: String) {

  def this() = this(0,"","","")
}

object Library extends Schema{

  val users = table[User]("user")
}
