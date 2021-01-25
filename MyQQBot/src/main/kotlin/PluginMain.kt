package org.example.mirai.plugin

import kotlinx.coroutines.CompletableJob
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.message.code.MiraiCode.deserializeMiraiCode
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.PlainText
import java.io.File
import java.net.URL
import java.net.URLEncoder
import kotlin.coroutines.EmptyCoroutineContext

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.example.mirai-example",
        version = "0.1.0"
    )
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
        //配置文件目录 "${dataFolder.absolutePath}/"

        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            val mes = message.contentToString();
            val miraiMes = message.serializeToMiraiCode();
            val groupId : Long = group.id;

            //Help Menu
            if (message.serializeToMiraiCode() == "[mirai:at:1465814436] help") {
                group.sendMessage(
                    "====帮助菜单====\n" +
                        "\"复读[内容]\"     -发送内容\n" +
                        "\"hi(!)\\hello(!)\"     -发送hi(!)\\hello(!)\n" +
                        "\"Wolfram [问题]\"     -接入Wolfram并获得问题的答案\n" +
                        "\"@bot help\"     -发送帮助菜单\n" +
                        "更多功能请等待更新"
                );
            }

            //Wolfram
            if (mes.startsWith("Wolfram ")) {
                var wolfram = mes.replaceFirst("Wolfram ", "");
                wolfram = URLEncoder.encode(wolfram, "utf-8");
                try {
                    group.sendMessage(URL("http://api.wolframalpha.com/v1/result?appid=YV466K-43XHJPRWY7&i=$wolfram&timeout=5").readText());
                } catch (throwable: Throwable) {
                    group.sendMessage("No short answer available");
                }
            }

            //Simple Communication
            if (miraiMes.startsWith("复读")) {
                group.sendMessage(miraiMes.replaceFirst("复读", "").deserializeMiraiCode());
            }
            val welcomeStrings = arrayOf("hi", "Hi", "hi!", "Hi!", "Hello", "hello", "Hello!", "hello!");
            if (welcomeStrings.contains(miraiMes)) {
                group.sendMessage(miraiMes);
            }
            if (miraiMes == "mirai" || miraiMes == "Mirai") {
                group.sendMessage("我在！\n\"@我 help\"以获取支持的指令");
            }

            //Homework

        }
    }
}
