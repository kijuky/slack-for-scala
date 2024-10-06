package io.github.kijuky.slack

import com.slack.api.model.Attachment
import com.slack.api.model.block.LayoutBlock
import com.slack.api.webhook.Payload

/** このクラスを SlackUtils 内に定義すると Malformed class name エラーが発生するため、SlackUtils
  * の外側に定義している。
  *
  * @param payload
  *   元となるペイロードインスタンス
  * @see
  *   [[https://stackoverflow.com/questions/68553844 Scala (Shell): java.lang.InternalError: Malformed class name]]
  */
@annotation.nowarn("cat=deprecation")
class SlackWebhookPayloadWithLinkNames(payload: Payload) {
  val threadTs: String = payload.getThreadTs
  val text: String = payload.getText
  val channel: String = payload.getChannel
  val username: String = payload.getUsername
  val iconUrl: String = payload.getIconUrl
  val iconEmoji: String = payload.getIconEmoji
  val blocks: java.util.List[LayoutBlock] = payload.getBlocks
  val attachments: java.util.List[Attachment] = payload.getAttachments
  val linkNames = true
}
