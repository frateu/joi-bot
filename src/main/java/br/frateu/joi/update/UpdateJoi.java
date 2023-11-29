package br.frateu.joi.update;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;
import java.util.TimerTask;

public class UpdateJoi extends TimerTask {
    Dotenv dotenvConfig = Dotenv.configure().load();
    String token = dotenvConfig.get("TOKEN");
    TelegramBot bot = new TelegramBot(token);
    int offSetValue = 0;

    @Override
    public void run() {
        // Objeto responsável por receber as mensagens
        GetUpdatesResponse updatesResponse;

        // Executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
        updatesResponse = bot.execute(new GetUpdates().limit(100).offset(offSetValue));

        // Lista de mensagens
        List<Update> updates = updatesResponse.updates();

        // Análise de cada ação da mensagem
        for (Update update : updates) {
            // Atualização do off-set
            offSetValue = update.updateId() + 1;

            if (update.message().text().equals("/teste")) {
                // Envio de "Escrevendo" antes de enviar a resposta
                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                // Envio da mensagem de resposta
                bot.execute(new SendMessage(update.message().chat().id(),"Teste!"));
            } else {
                // Envio de "Escrevendo" antes de enviar a resposta
                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                // Envio da mensagem de resposta
                bot.execute(new SendMessage(update.message().chat().id(),"Sei lá!!"));
            }
        }
    }
}
