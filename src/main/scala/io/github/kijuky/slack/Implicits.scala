package io.github.kijuky.slack

import com.google.gson.{FieldNamingPolicy, GsonBuilder}
import com.slack.api.Slack
import com.slack.api.model.Attachments.{asAttachments, attachment}
import com.slack.api.model.Field
import com.slack.api.model.block.Blocks.asBlocks
import com.slack.api.model.block.LayoutBlock
import com.slack.api.webhook.{Payload, WebhookResponse}
import okhttp3.Response

import java.io.IOException
import scala.collection.JavaConverters._

object Implicits {
  private lazy val gsonBuilder =
    new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
  private lazy val gson = gsonBuilder.create()
  private lazy val prettyGson = gsonBuilder.setPrettyPrinting().create()

  implicit class RichSlack(slack: Slack) {
    def send(optWebhookUrl: Option[String], payload: Payload): Unit = {
      val payloadWithLinkNames = payload.withLinkNames
      optWebhookUrl match {
        // for debug
        case None =>
          val payloadJson = prettyGson.toJson(payloadWithLinkNames)
          println(payloadJson)
        case Some(webhookUrl) =>
          val payloadJson = gson.toJson(payloadWithLinkNames)
          val response = slack.send(webhookUrl, payloadJson)
          if (response.getCode != 200) {
            throw new IOException(response.getMessage)
          }
      }
    }
  }

  implicit class RichPayload(payload: Payload) {

    /** attachments にある `@user-id` 文字列を、メンションとして機能させます。
      *
      * blocks は `Payload` のままで `@user-id` 文字列がメンションとして機能します。そのため、 blocks
      * だけを使う場合は、この変換は不要です。
      */
    def withLinkNames = new SlackWebhookPayloadWithLinkNames(payload)
  }

  implicit class RichResponse(response: Response) {
    def toWebhookResponse: WebhookResponse =
      WebhookResponse
        .builder()
        .code(response.code())
        .message(response.message())
        .body(response.body().string())
        .build()
  }

  implicit class RichPayloadBuilder(payloadBuilder: Payload.PayloadBuilder) {
    def attachmentFields(fields: Seq[Field]): Payload.PayloadBuilder = {
      val attachments = asAttachments(attachment(_.fields(fields.asJava)))
      payloadBuilder.attachments(attachments)
    }

    def blockSections(sections: Seq[LayoutBlock]): Payload.PayloadBuilder = {
      val blocks = asBlocks(sections: _*)
      payloadBuilder.blocks(blocks)
    }
  }

  def field(title: String, value: String, valueShortEnough: Boolean = false) =
    new Field(title, value, valueShortEnough)
}
