package org.bitfunding.app.oauth

import org.scalatra._
import scalate.ScalateSupport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.googleapis.auth.oauth2.{
  GoogleAuthorizationCodeFlow,
  GoogleCredential,
  GoogleAuthorizationCodeRequestUrl}
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.client.auth.oauth2.Credential
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

object GoogleOAuth extends BitfundingAuth{

  val oauth = new GoogleAuthorizationCodeFlow.Builder(
    new NetHttpTransport(),
    new JacksonFactory(),
    "985951757831.apps.googleusercontent.com",
    "AIzaSyCa81GfnMN5JDYR3tq9dSqMowrbRM20bkE",
    ListBuffer("https://www.googleapis.com/auth/plus.me") : java.util.List[String])
    .setApprovalPrompt("force")
    .build()

  val service = "google"

  def authUser(code : String, state : String) = {

    val token = oauth.newTokenRequest(code).execute()
    val creds = new GoogleCredential().setFromTokenResponse(token);
    val email = creds.getServiceAccountUser()
    
    Some(getOrCreate(email))

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
