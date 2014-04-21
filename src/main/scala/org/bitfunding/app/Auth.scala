package org.bitfunding.app.oauth

import org.scalatra._
import javax.servlet.http.HttpServletRequest
import org.bitfunding.app.core.User
import org.bitfunding.app.core.Library._
import org.squeryl.PrimitiveTypeMode._

trait BitfundingAuth{
  
  val authUrl : String

  def authUser(code : String, state : String) : Option[User]

  val service : String

  def redirectUrl() : String 

  def getOrCreate(email : String) : User = {

    from(users)(s => where(s.email === email) select(s)).headOption match{

      case Some(user) => user
      case None => 
        var user = new User(email, email)
        users.insert(List(user))
        user
    }
  }
}

class Auth(
  authUrl : String
){

  val googleOAuth = new GoogleOAuth(authUrl)
  val usernameArg = "user:username"

  def loginServices() = Seq(

    ("Google", this.googleOAuth.redirectUrl())
  )

  def getUser(cookies : SweetCookies) : Option[User] = {

    for{

      username <- cookies.get(this.usernameArg)
      user <- from(users)(u => where(u.username === username) select u).headOption
    } yield user
  }

  def isUser(cookies : SweetCookies) : Boolean = {
    this.getUser(cookies).nonEmpty
  }

  def authenticate(
    service : String, 
    code_ : Option[String], 
    state_ : Option[String],
    cookies : SweetCookies) = {

    for{

      code <- code_
      state <- state_
      user <- service match{

        case this.googleOAuth.service => this.googleOAuth.authUser(code, state)
      }
      res <- Some(cookies.set(this.usernameArg, user.username))
    } yield user
  }

}
