package dev.nomadblacky.scalafullstackwebapplication.infra

import software.amazon.awscdk.core.{App, Construct, Stack}
import software.amazon.awscdk.services.ec2.{Vpc, VpcProps}
import software.amazon.awscdk.services.ecs.{Cluster, ClusterProps}

object CDKMain {
  def main(args: Array[String]): Unit = {
    val app = new App()

    new WebAppStack(app, "WebAppStack")

    app.synth()
  }
}

class WebAppStack(scope: Construct, name: String) extends Stack(scope, name) {
  val vpc = new Vpc(this, "Vpc", VpcProps.builder().maxAzs(1).build())

  val cluster = new Cluster(this, "Cluster", ClusterProps.builder().vpc(vpc).build())
}
