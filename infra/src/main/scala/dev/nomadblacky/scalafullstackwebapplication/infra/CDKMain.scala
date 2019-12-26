package dev.nomadblacky.scalafullstackwebapplication.infra

import software.amazon.awscdk.core.{App, Construct, Stack}

object CDKMain {
  def main(args: Array[String]): Unit = {
    val app = new App()

    new WebAppStack(app, "WebAppStack")

    app.synth()
  }
}

class WebAppStack(scope: Construct, name: String) extends Stack(scope, name) {
  // TODO
}
