package br.frateu.joi.economia;

import br.frateu.joi.ConstantesJoi;
import br.frateu.joi.database.EconomiaDB;
import br.frateu.joi.update.UpdateJoi;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;

public class GastoFixo {
    private String usuario;
    private String descricao;
    private BigDecimal valor;
    private String statusGastoFixo = "";

    public GastoFixo(Update update, String statusGastoFixo) {
        this.usuario = update.message().from().username();
        this.statusGastoFixo = statusGastoFixo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(Update update) {
        this.usuario = update.message().from().username();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(Update update) {
        this.descricao = update.message().text();
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(Update update) {
        this.valor = new BigDecimal(update.message().text());
    }

    public String getStatusGastoFixo() {
        return statusGastoFixo;
    }

    public void setStatusGastoFixo(String statusGastoFixo) {
        this.statusGastoFixo = statusGastoFixo;
    }

    public void inserirDescricao(TelegramBot bot, Update update) {
        // Envio de "Escrevendo" antes de enviar a resposta
        bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

        // Envio da mensagem de resposta
        bot.execute(new SendMessage(update.message().chat().id(),"Insira a descrição do Gasto Fixo."));

        setStatusGastoFixo(ConstantesJoi.CONST_GASTO_FIXO_DESCRICAO);
    }

    public void inserirValor(TelegramBot bot, Update update) {
        // Envio de "Escrevendo" antes de enviar a resposta
        bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

        // Envio da mensagem de resposta
        bot.execute(new SendMessage(update.message().chat().id(),"Insira o valor do Gasto Fixo."));

        setStatusGastoFixo(ConstantesJoi.CONST_GASTO_FIXO_VALOR);
    }

    public void finalizadaInclusao(@NotNull TelegramBot bot, @NotNull Update update) {
        // Envio de "Escrevendo" antes de enviar a resposta
        bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

        try {
            EconomiaDB economiaDB = new EconomiaDB();

            economiaDB.inserirGastoFixo(this);

            ControladorEconomia.statusGastoFixo = new HashMap<>();
            UpdateJoi.statusEconomia = new HashMap<>();

            // Envio da mensagem de resposta
            bot.execute(new SendMessage(update.message().chat().id(),"Inclui, amor."));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // Envio da mensagem de resposta
            bot.execute(new SendMessage(update.message().chat().id(),"Não consegui incluir não, amor."));
        }
    }

    public void run(@NotNull TelegramBot bot, @NotNull Update update) {
        switch(getStatusGastoFixo()){
            case ConstantesJoi.CONST_GASTO_FIXO_INCLUIR:
                inserirDescricao(bot, update);
                break;
            case ConstantesJoi.CONST_GASTO_FIXO_DESCRICAO:
                setDescricao(update);
                inserirValor(bot, update);
                break;
            case ConstantesJoi.CONST_GASTO_FIXO_VALOR:
                setValor(update);
                finalizadaInclusao(bot, update);
                break;
            /*case ConstantesJoi.CONST_GASTO_FIXO_CONSULTAR:
                valorConverter(bot, update);
                break;
            case ConstantesJoi.CONST_GASTO_FIXO_USUARIO:
                valorConversao(update);
                valorConvertido(bot, update);
                break;*/
        }
    }
}
