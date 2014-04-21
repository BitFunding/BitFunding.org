package org.bitfunding.app.oauth

import org.scalatra._
import scalate.ScalateSupport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.googleapis.auth.oauth2.{
  GoogleAuthorizationCodeFlow,
  GoogleCredential,
  GoogleAuthorizationCodeRequestUrl}
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.client.auth.oauth2.{
  Credential,
  TokenResponseException}
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import org.bitfunding.app.init.DatabaseSessionSupport

class GoogleOAuth(authUrl_ : String) extends BitfundingAuth{

  val authUrl = authUrl_

  val oauth = new GoogleAuthorizationCodeFlow.Builder(
    new NetHttpTransport(),
    new JacksonFactory(),
    "985951757831-lu4pht8550ubgn5meq65fbnk4nssm9gi.apps.googleusercontent.com",
    "2TKn6Pe8y82EUP03K_sr2aY3",
    ListBuffer("https://www.googleapis.com/auth/plus.me", "https://www.googleapis.com/auth/plus.profile.emails.read") : java.util.List[String])
    .setApprovalPrompt("force")
    .build()

  val service = "google"

  def authUser(code : String, state : String) = {

    try{
      val token = oauth.newTokenRequest(code).setRedirectUri(this.authUrl + this.service + "/").execute
      val creds = new GoogleCredential().setFromTokenResponse(token)
      val email = creds.getServiceAccountUser()
      Some(getOrCreate(email))
    }catch{
      case ex : TokenResponseException => None
    }
  }

  def redirectUrl() = {

    oauth.newAuthorizationUrl.setState("theState").setRedirectUri(this.authUrl + this.service + "/").build
  }

  def authenticate(auth : String) : Either[Credential, GoogleAuthorizationCodeRequestUrl] = {

    val creds = oauth.loadCredential(auth)

    if(creds != null){

      Left(creds)
    }else{

      Right(oauth.newAuthorizationUrl())
    }
  }

}
