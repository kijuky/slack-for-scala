package io.github.kijuky.zio.slack

import com.slack.api.model.Attachments.*
import com.slack.api.model.Field
import com.slack.api.model.block.Blocks.*
import com.slack.api.model.block.LayoutBlock
import com.slack.api.webhook.Payload

import scala.jdk.CollectionConverters.*

object Extensions:
  extension (payload: Payload)
    /** attachments にある `@user-id` 文字列を、メンションとして機能させます。
      *
      * blocks は `Payload` のままで `@user-id` 文字列がメンションとして機能します。そのため、 blocks
      * だけを使う場合は、この変換は不要です。
      */
    def withLinkNames = SlackWebhookPayloadWithLinkNames(payload)

  extension (payloadBuilder: Payload.PayloadBuilder)
    def attachmentFields(fields: Seq[Field]): Payload.PayloadBuilder =
      val attachments = asAttachments(attachment(_.fields(fields.asJava)))
      payloadBuilder.attachments(attachments)

    def blockSections(sections: Seq[LayoutBlock]): Payload.PayloadBuilder =
      val blocks = asBlocks(sections: _*)
      payloadBuilder.blocks(blocks)

  def field(title: String, value: String, valueShortEnough: Boolean = false) =
    Field(title, value, valueShortEnough)
