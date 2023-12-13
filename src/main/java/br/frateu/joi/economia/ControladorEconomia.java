package br.frateu.joi.economia;

import br.frateu.joi.ConstantesJoi;
import br.frateu.joi.update.UpdateJoi;
import com.pengrad.telegrambot.TelegramBot;
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

    public static HashMap<String, ConversaoMoeda> statusConversao = new HashMap<>();
    public static HashMap<String, GastoFixo> statusGastoFixo = new HashMap<>();

    public void run(@NotNull TelegramBot bot, @NotNull Update update) {
        // Verifica se a opção selecionada foi a de economia e está válido
        if (UpdateJoi.statusEconomia.containsKey(update.message().from().username())) {
            ControladorEconomia controladorEconomia = UpdateJoi.statusEconomia.get(update.message().from().username());

            // Verificar qual o status da economia
            switch (controladorEconomia.statusEconomia) {
                case ConstantesJoi.CONST_ECONOMIA_STATUS_ESCOLHER -> {
                    // Envio de "Escrevendo" antes de enviar a resposta
                    bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                    // Envio da mensagem de opções de conversão
                    bot.execute(new SendMessage(update.message().chat().id(), "Encolha entre:\n" + ConstantesJoi.CONST_OPCOES_MENU));
                    setStatusEconomia(ConstantesJoi.CONST_ECONOMIA_STATUS_MENU);
                }
                case ConstantesJoi.CONST_ECONOMIA_STATUS_MENU -> {  // Verifica qual foi a opção selecionada no menu
                    if (update.message().text().equals("/gastofixo")) {
                        // Envio de "Escrevendo" antes de enviar a resposta
                        bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                        // Envio da mensagem de opções de conversão
                        bot.execute(new SendMessage(update.message().chat().id(), "Encolha entre:\n" + ConstantesJoi.CONST_OPCOES_GASTO_FIXO));

                        setStatusEconomia(ConstantesJoi.CONST_ECONOMIA_ESCOLHA_GASTO_FIXO);
                    } else if (update.message().text().equals("/conversaomoeda")) {
                        // Envio de "Escrevendo" antes de enviar a resposta
                        bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                        // Envio da mensagem de opções de conversão
                        bot.execute(new SendMessage(update.message().chat().id(), "Encolha entre:\n" + ConstantesJoi.CONST_OPCOES_CONVERSAO));

                        setStatusEconomia(ConstantesJoi.CONST_ECONOMIA_ESCOLHA_CONVERSAO);
                    }
                }
                case ConstantesJoi.CONST_ECONOMIA_ESCOLHA_GASTO_FIXO -> opcoesGastoFixo(bot, update);
                case ConstantesJoi.CONST_ECONOMIA_ESCOLHA_CONVERSAO -> opcoesConversaoMoeda(bot, update);
            }
        }
    }

    // Opções para a Conversão de Moeda
    private void opcoesConversaoMoeda(@NotNull TelegramBot bot, @NotNull Update update) {
        // Conversão de Iene para Real
        if (update.message().text().equals("/ienereal") || (statusConversao.containsKey(update.message().from().username()) && !update.message().text().contains("/"))) {
            if (statusConversao.containsKey(update.message().from().username())) {
                ConversaoMoeda conversaoMoeda = statusConversao.get(update.message().from().username());
                conversaoMoeda.run(bot, update);
            } else {
                statusConversao.put(update.message().from().username(), new ConversaoMoeda(ConstantesJoi.CONST_MOEDA_IENE, ConstantesJoi.CONST_MOEDA_REAL));
                ConversaoMoeda conversaoMoeda = statusConversao.get(update.message().from().username());
                conversaoMoeda.run(bot, update);
            }
            // Conversão de Dolar para Real
        } else if (update.message().text().equals("/dolarreal") || (statusConversao.containsKey(update.message().from().username()) && !update.message().text().contains("/"))) {
            if (statusConversao.containsKey(update.message().from().username())) {
                ConversaoMoeda conversaoMoeda = statusConversao.get(update.message().from().username());
                conversaoMoeda.run(bot, update);
            } else {
                statusConversao.put(update.message().from().username(), new ConversaoMoeda(ConstantesJoi.CONST_MOEDA_DOLAR, ConstantesJoi.CONST_MOEDA_REAL));
                ConversaoMoeda conversaoMoeda = statusConversao.get(update.message().from().username());
                conversaoMoeda.run(bot, update);
            }
            // Conversão de Real para Iene
        } else if (update.message().text().equals("/realiene") || (statusConversao.containsKey(update.message().from().username()) && !update.message().text().contains("/"))) {
            if (statusConversao.containsKey(update.message().from().username())) {
                ConversaoMoeda conversaoMoeda = statusConversao.get(update.message().from().username());
                conversaoMoeda.run(bot, update);
            } else {
                statusConversao.put(update.message().from().username(), new ConversaoMoeda(ConstantesJoi.CONST_MOEDA_REAL, ConstantesJoi.CONST_MOEDA_IENE));
                ConversaoMoeda conversaoMoeda = statusConversao.get(update.message().from().username());
                conversaoMoeda.run(bot, update);
            }
            // Conversão de Real para Dolar
        } else if (update.message().text().equals("/realdolar") || (statusConversao.containsKey(update.message().from().username()) && !update.message().text().contains("/"))) {
            if (statusConversao.containsKey(update.message().from().username())) {
                ConversaoMoeda conversaoMoeda = statusConversao.get(update.message().from().username());
                conversaoMoeda.run(bot, update);
            } else {
                statusConversao.put(update.message().from().username(), new ConversaoMoeda(ConstantesJoi.CONST_MOEDA_REAL, ConstantesJoi.CONST_MOEDA_DOLAR));
                ConversaoMoeda conversaoMoeda = statusConversao.get(update.message().from().username());
                conversaoMoeda.run(bot, update);
            }
        }
    }

    public void opcoesGastoFixo(@NotNull TelegramBot bot, @NotNull Update update) {
        if (update.message().text().equals("/incluir") || (statusGastoFixo.containsKey(update.message().from().username()) && !update.message().text().contains("/"))) {
            if (statusGastoFixo.containsKey(update.message().from().username())) {
                GastoFixo gastoFixo = statusGastoFixo.get(update.message().from().username());
                gastoFixo.run(bot, update);
            } else {
                statusGastoFixo.put(update.message().from().username(), new GastoFixo(update, ConstantesJoi.CONST_GASTO_FIXO_INCLUIR));
                GastoFixo gastoFixo = statusGastoFixo.get(update.message().from().username());
                gastoFixo.run(bot, update);
            }
        }
    }
}
