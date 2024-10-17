package io.github.kijuky.zio.slack

import com.slack.api.Slack
import com.slack.api.webhook.{Payload, WebhookResponse}
import zio.*

import scala.concurrent.Future

final class SlackClient(val webhookUrl: String, slack: Slack)
    extends AutoCloseable:
  def send(payload: String): Task[WebhookResponse] =
    ZIO.fromFuture(implicit ec => Future(slack.send(webhookUrl, payload)))
  def send(payload: Payload): Task[WebhookResponse] =
    ZIO.fromFuture(implicit ec => Future(slack.send(webhookUrl, payload)))
  def close(): Unit =
    slack.close()

object SlackClient:
  def apply(webhookUrl: String): SlackClient =
    new SlackClient(webhookUrl, Slack.getInstance())
