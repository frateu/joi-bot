package br.frateu.joi.economia;

import br.frateu.joi.ConstantesJoi;
import br.frateu.joi.update.UpdateJoi;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.ChatMemberUpdated;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ControladorEconomia {
    private String statusEconomia = "";

    public String getStatusEconomia() {
        return statusEconomia;
    }

    public void setStatusEconomia(String statusEconomia) {
        this.statusEconomia = statusEconomia;
    }

    public ControladorEconomia() {
        setStatusEconomia(ConstantesJoi.CONST_ECONOMIA_STATUS_ESCOLHER);
    }

    public static HashMap<ChatMemberUpdated, ConversaoMoeda> statusConversao = new HashMap<>();

    public void run(@NotNull TelegramBot bot, @NotNull Update update) {
        // Verifica se a opção selecionada foi a de economia e está válido
        if (UpdateJoi.statusEconomia.containsKey(update.chatMember())) {
            ControladorEconomia controladorEconomia = UpdateJoi.statusEconomia.get(update.chatMember());

            // Verificar qual o status da economia
            if (controladorEconomia.statusEconomia.equals(ConstantesJoi.CONST_ECONOMIA_STATUS_ESCOLHER)) {
                // Envio de "Escrevendo" antes de enviar a resposta
                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                // Envio da mensagem de opções de conversão
                bot.execute(new SendMessage(update.message().chat().id(),"Encolha entre:\n\n" +
                                                                                "/ienereal\n" +
                                                                                "/dolarreal\n" +
                                                                                "/realiene\n" +
                                                                                "/realdolar"));

                setStatusEconomia(ConstantesJoi.CONST_ECONOMIA_STATUS_ESCOLHIDO);
            } else if (controladorEconomia.statusEconomia.equals(ConstantesJoi.CONST_ECONOMIA_STATUS_ESCOLHIDO)) { // Verifica qual foi a opção selecionada
                // Conversão de Iene para Real
                if (update.message().text().equals("/ienereal") || (statusConversao.containsKey(update.chatMember()) && !update.message().text().contains("/"))) {
                    if (statusConversao.containsKey(update.chatMember())) {
                        ConversaoMoeda conversaoMoeda = statusConversao.get(update.chatMember());
                        conversaoMoeda.run(bot, update);
                    } else {
                        statusConversao.put(update.chatMember(), new ConversaoMoeda(ConstantesJoi.CONST_MOEDA_IENE, ConstantesJoi.CONST_MOEDA_REAL));
                        ConversaoMoeda conversaoMoeda = statusConversao.get(update.chatMember());
                        conversaoMoeda.run(bot, update);
                    }
                // Conversão de Dolar para Real
                } else if (update.message().text().equals("/dolarreal") || (statusConversao.containsKey(update.chatMember()) && !update.message().text().contains("/"))) {
                    if (statusConversao.containsKey(update.chatMember())) {
                        ConversaoMoeda conversaoMoeda = statusConversao.get(update.chatMember());
                        conversaoMoeda.run(bot, update);
                    } else {
                        statusConversao.put(update.chatMember(), new ConversaoMoeda(ConstantesJoi.CONST_MOEDA_DOLAR, ConstantesJoi.CONST_MOEDA_REAL));
                        ConversaoMoeda conversaoMoeda = statusConversao.get(update.chatMember());
                        conversaoMoeda.run(bot, update);
                    }
                // Conversão de Real para Iene
                } else if (update.message().text().equals("/realiene") || (statusConversao.containsKey(update.chatMember()) && !update.message().text().contains("/"))) {
                    if (statusConversao.containsKey(update.chatMember())) {
                        ConversaoMoeda conversaoMoeda = statusConversao.get(update.chatMember());
                        conversaoMoeda.run(bot, update);
                    } else {
                        statusConversao.put(update.chatMember(), new ConversaoMoeda(ConstantesJoi.CONST_MOEDA_REAL, ConstantesJoi.CONST_MOEDA_IENE));
                        ConversaoMoeda conversaoMoeda = statusConversao.get(update.chatMember());
                        conversaoMoeda.run(bot, update);
                    }
                // Conversão de Real para Dolar
                } else if (update.message().text().equals("/realdolar") || (statusConversao.containsKey(update.chatMember()) && !update.message().text().contains("/"))) {
                    if (statusConversao.containsKey(update.chatMember())) {
                        ConversaoMoeda conversaoMoeda = statusConversao.get(update.chatMember());
                        conversaoMoeda.run(bot, update);
                    } else {
                        statusConversao.put(update.chatMember(), new ConversaoMoeda(ConstantesJoi.CONST_MOEDA_REAL, ConstantesJoi.CONST_MOEDA_DOLAR));
                        ConversaoMoeda conversaoMoeda = statusConversao.get(update.chatMember());
                        conversaoMoeda.run(bot, update);
                    }
                }
            }
        }
    }
}
