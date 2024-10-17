package io.github.kijuky.zio.slack

import zio.*

object SlackService:
  def layer(webhookUrl: String = ""): TaskLayer[SlackClient] =
    ZLayer.scoped:
      ZIO.acquireRelease(for {
        optWebhookUrl <- System.env("SLACK_WEBHOOK_URL")
        webhookUrl <- ZIO.succeed(optWebhookUrl.getOrElse(webhookUrl))
      } yield SlackClient(webhookUrl)) { client =>
        ZIO.succeed(client.close())
      }
