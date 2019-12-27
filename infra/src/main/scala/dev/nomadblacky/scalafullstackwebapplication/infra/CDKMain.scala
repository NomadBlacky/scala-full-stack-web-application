package dev.nomadblacky.scalafullstackwebapplication.infra

import software.amazon.awscdk.core.{App, Construct, Stack}
import software.amazon.awscdk.services.ec2.{Vpc, VpcProps}
import software.amazon.awscdk.services.ecs._
import software.amazon.awscdk.services.ecs.patterns.{
  ApplicationLoadBalancedFargateService,
  ApplicationLoadBalancedFargateServiceProps
}

import scala.collection.JavaConverters._

object CDKMain {
  def main(args: Array[String]): Unit = {
    val app = new App()

    new WebAppStack(app, "WebAppStack")

    app.synth()
  }
}

class WebAppStack(scope: Construct, name: String) extends Stack(scope, name) {
  val vpc: Vpc = new Vpc(this, "Vpc", VpcProps.builder().maxAzs(2).build())

  val cluster: Cluster = new Cluster(this, "Cluster", ClusterProps.builder().vpc(vpc).build())

  val taskDefinition: FargateTaskDefinition = {
    val props   = FargateTaskDefinitionProps.builder().cpu(256).memoryLimitMiB(1024).build()
    val taskDef = new FargateTaskDefinition(this, "TaskDefinition", props)

    val container = {
      val opts = ContainerDefinitionOptions
        .builder()
        .image(ContainerImage.fromAsset("./server/target/docker/stage"))
        .environment(Map("APPLICATION_SECRET" -> sys.env("APPLICATION_SECRET")).asJava)
        .logging(LogDriver.awsLogs(AwsLogDriverProps.builder().streamPrefix("webserver").build()))
        .build()
      taskDef.addContainer("WebServerContainer", opts)
    }
    container.addPortMappings(PortMapping.builder().containerPort(9000).build())

    taskDef
  }

  val service: ApplicationLoadBalancedFargateService = {
    val props = ApplicationLoadBalancedFargateServiceProps
      .builder()
      .cluster(cluster)
      .taskDefinition(taskDefinition)
      .publicLoadBalancer(true)
      .desiredCount(1)
      .enableEcsManagedTags(true)
      .propagateTags(PropagatedTagSource.SERVICE)
      .build()
    new ApplicationLoadBalancedFargateService(this, "Service", props)
  }
}
