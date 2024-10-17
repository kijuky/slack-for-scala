package io.github.kijuky.zio.slack

import com.slack.api.webhook.Payload
import zio.*

trait SlackNotifier:
  def notify(payload: Payload): Task[Unit]

object SlackNotifier:
  def notify(payload: Payload): RIO[SlackNotifier, Unit] =
    ZIO.serviceWithZIO(_.notify(payload))
