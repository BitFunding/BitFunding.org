package org.bitfunding.app.oauth

import javax.servlet.http.HttpServletRequest
import org.bitfunding.app.core.User

trait BitfundingAuth{

  
  def authUser(code : String, state : String) : Option[User]

  def getOrCreate(email : String) : User = {

    null

  }
}

object Auth{

  def isUser(request : HttpServletRequest) : Boolean = {

    true
  }

  def authenticate(service : String, code_ : Option[String], state_ : Option[String]) = {

    for{

      code <- code_
      state <- state_
      user <- service match{

        case GoogleOAuth.service => GoogleOAuth.authUser(code, state)
      }
    } yield user
  }

}
