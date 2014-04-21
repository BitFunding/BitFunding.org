package org.bitfunding.app.core

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.{Schema, KeyedEntity}
import org.squeryl.internals.PrimaryKey
import org.squeryl.annotations.Column
import java.util.Date
import java.sql.Timestamp
 
class User( 
  val username: String,
  val email: String,
  val password: String) extends KeyedEntity[PrimaryKey]{

  def this(username : String, email : String) = this(username, email, "NotAHash")
  def id = PrimaryKey()
}

object Library extends Schema{

  val users = table[User]("user")

  on(users)(s => declare(
    s.username is(unique),
    s.email is(unique)))
}
