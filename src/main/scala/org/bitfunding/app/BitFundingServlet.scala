package org.bitfunding.app

import org.scalatra._
import scalate.ScalateSupport
import org.bitfunding.app.oauth.Auth

class BitFundingServlet extends BitfundingStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

  get("/logged", Auth.isUser(request)) {

    <html>
      Hello!
    </html>
  }

  get("/auth/:service/"){

    Auth.authenticate(params("service"), params.get("code"),params.get("state"))
  }
  
}
