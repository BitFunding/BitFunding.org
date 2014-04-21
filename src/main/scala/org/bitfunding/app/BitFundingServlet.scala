package org.bitfunding.app

import org.scalatra._
import scalate.ScalateSupport
import org.bitfunding.app.oauth.Auth
import org.bitfunding.app.init.DatabaseSessionSupport

class BitFundingServlet extends BitfundingStack
    with UrlGeneratorSupport
    with DatabaseSessionSupport
{

  lazy val baseUrl = "http://" + this.serverHost + ":" + this.serverPort

  val authPath = "/auth/"

  lazy val auth = new Auth(this.baseUrl + this.authPath)

  get("/") {
    contentType="text/html"
    ssp("/index", "logins" -> this.auth.loginServices(), "title" -> "q type safe pue...")
  }

  val login = get("/logged", this.auth.isUser(cookies)) {

    <html>
      Hello!
    </html>
  }

  val authenticator = get(this.authPath + ":service/"){

    this.auth.authenticate(
      params("service"), 
      params.get("code"),
      params.get("state"),
      cookies) match{
      
      case Some(user) => this.redirect(this.url(this.login))
      case None =>  this.redirect("/") // Change to an error page
    }
  }

}
