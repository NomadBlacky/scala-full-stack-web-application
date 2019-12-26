package dev.nomadblacky.scalafullstackwebapplication

import dev.nomadblacky.scalafullstackwebapplication.shared.SharedMessages
import org.scalajs.dom

object ScalaJSExample {

  def main(args: Array[String]): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
  }
}
