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
import scala.concurrent.duration._
import scala.concurrent.Await
import dispatch.{Http => DispatchHttp, _}
import java.util.concurrent.TimeoutException
import scala.util.parsing.json.JSON.parseFull
import Defaults._
import org.bitfunding.app.init.OAuthSupport

class GoogleOAuth(authUrlGen : String => String) extends BitfundingAuth 
    with OAuthSupport{

  val service = "google"
  val authUrl = authUrlGen(this.service)

  val dataUrl = "https://www.googleapis.com/oauth2/v1/userinfo"

  val oauth = new GoogleAuthorizationCodeFlow.Builder(
    new NetHttpTransport(),
    new JacksonFactory(),
    this.oAuthConf.google_client,
    this.oAuthConf.google_secret,
    ListBuffer(
      "https://www.googleapis.com/auth/plus.login",
      "https://www.googleapis.com/auth/userinfo.email") : java.util.List[String])
    .setApprovalPrompt("force")
    .build()

  def authUser(code : String, state : String) = {

    if(this.takeState(state).nonEmpty){
      try{
        this.states -= state
        val token = oauth.newTokenRequest(code)
          .setRedirectUri(this.authUrl)
          .execute
        val req = DispatchHttp(url(this.dataUrl).GET.setQueryParameters(Map(
          "alt" -> Seq("json"),
          "access_token" -> Seq(token.get("access_token").asInstanceOf[String])
        )))
        val res = Await.result(req, 20 seconds)
        for{
          creds <- parseFull(res.getResponseBody)
          email <- creds.asInstanceOf[Map[String,String]].get("email")
          user <- Some(this.getOrCreate(email))
        } yield user
      }catch{
        case ex : TokenResponseException => None
        case ex : TimeoutException => None
      }

    }else{
      None
    }
  }

  def redirectUrl() = {

    val state = this.addState()
    oauth.newAuthorizationUrl.setState(state)
      .setRedirectUri(this.authUrl).build
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
