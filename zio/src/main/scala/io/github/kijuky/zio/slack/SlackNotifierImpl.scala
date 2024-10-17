package io.github.kijuky.zio.slack

import com.google.gson.{FieldNamingPolicy, GsonBuilder}
import com.slack.api.webhook.Payload
import io.github.kijuky.zio.slack.Extensions.*
import zio.*

import java.io.IOException

case class SlackNotifierImpl(slack: SlackClient) extends SlackNotifier:
  private lazy val gsonBuilder =
    GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
  private lazy val gson = gsonBuilder.create()
  private lazy val prettyGson = gsonBuilder.setPrettyPrinting().create()

  override def notify(payload: Payload): Task[Unit] =
    val payloadWithLinkNames = payload.withLinkNames
    for {
      _ <-
        if (slack.webhookUrl.isEmpty)
          val payloadJson = prettyGson.toJson(payloadWithLinkNames)
          Console.printLine(payloadJson)
        else
          val payloadJson = gson.toJson(payloadWithLinkNames)
          ZIO.whenCaseZIO(slack.send(payloadJson)) {
            case response if response.getCode != 200 =>
              ZIO.fail(IOException(response.getMessage))
            case _ =>
              ZIO.unit
          }
    } yield ()

object SlackNotifierImpl {
  def layer: URLayer[SlackClient, SlackNotifierImpl] =
    ZLayer(
      for slack <- ZIO.service[SlackClient]
      yield SlackNotifierImpl(slack)
    )
}
