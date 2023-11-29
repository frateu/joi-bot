package br.frateu.joi.economia;

import br.frateu.joi.JoiConstantes;
import br.frateu.joi.update.UpdateJoi;
import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class ConversaoMoeda {
    private BigDecimal valorConverter = BigDecimal.ZERO;
    private BigDecimal valorConvertido = BigDecimal.ZERO;
    private BigDecimal valorConversaoMoeda = BigDecimal.ZERO;
    private String statusConversao = "";
    private String moedaOriginal = "";
    private String moedaDestino = "";

    public ConversaoMoeda() {
        setStatusConversao(JoiConstantes.CONST_CONVERSAO_MOEDA_STATUS_INICIAR);
    }

    public BigDecimal getValorConverter() {
        return valorConverter;
    }

    public void setValorConverter(@NotNull Update update) {
        try {
            this.valorConverter = new BigDecimal(update.message().text());
            this.statusConversao = JoiConstantes.CONST_CONVERSAO_MOEDA_STATUS_VALOR;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BigDecimal getValorConvertido() {
        return valorConvertido;
    }

    public void setValorConvertido(BigDecimal valorConvertido) {
        this.valorConvertido = valorConvertido;
    }

    public String getStatusConversao() {
        return statusConversao;
    }

    public void setStatusConversao(String statusConversao) {
        this.statusConversao = statusConversao;
    }

    public String getMoedaOriginal() {
        return moedaOriginal;
    }

    public void setMoedaOriginal(@NotNull Update update) {
        this.moedaOriginal = update.message().text();
        this.statusConversao = JoiConstantes.CONST_CONVERSAO_MOEDA_STATUS_MOEDA_ORIGEM_INSERIDA;
    }

    public String getMoedaDestino() {
        return moedaDestino;
    }

    public void setMoedaDestino(@NotNull Update update) {
        this.moedaDestino = update.message().text();
        this.statusConversao = JoiConstantes.CONST_CONVERSAO_MOEDA_STATUS_MOEDA_DESTINO_INSERIDA;
    }

    public BigDecimal getValorConversaoMoeda() {
        return valorConversaoMoeda;
    }

    public void setValorConversaoMoeda(BigDecimal valorConversaoMoeda) {
        this.valorConversaoMoeda = valorConversaoMoeda;
    }

    private void moedaOrigem(@NotNull TelegramBot bot, @NotNull Update update) {
        // Envio de "Escrevendo" antes de enviar a resposta
        bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

        // Envio da mensagem de resposta
        bot.execute(new SendMessage(update.message().chat().id(),"Insira a moeda de origem."));

        setStatusConversao(JoiConstantes.CONST_CONVERSAO_MOEDA_STATUS_MOEDA_ORIGEM);
    }

    private void moedaDestino(@NotNull TelegramBot bot, @NotNull Update update) {
        // Envio de "Escrevendo" antes de enviar a resposta
        bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

        // Envio da mensagem de resposta
        bot.execute(new SendMessage(update.message().chat().id(),"Insira a moeda de destino."));

        setStatusConversao(JoiConstantes.CONST_CONVERSAO_MOEDA_STATUS_MOEDA_DESTINO);
    }

    private void valorConverter(@NotNull TelegramBot bot, @NotNull Update update) {
        // Envio de "Escrevendo" antes de enviar a resposta
        bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

        // Envio da mensagem de resposta
        bot.execute(new SendMessage(update.message().chat().id(),"Insira a valor a ser convertido."));

        setStatusConversao(JoiConstantes.CONST_CONVERSAO_MOEDA_STATUS_VALOR);
    }

    private void valorConvertido(@NotNull TelegramBot bot, @NotNull Update update) {
        // Envio de "Escrevendo" antes de enviar a resposta
        bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

        // Envio da mensagem de resposta
        bot.execute(new SendMessage(update.message().chat().id(),"O valor convertido de " + getMoedaOriginal() + " para " + getMoedaDestino() + " é de: " + getValorConvertido()));

        UpdateJoi.statusConversao = new HashMap<>();
    }

    private void valorConversao(@NotNull Update update) {
        Gson gson = new Gson();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/" + getMoedaOriginal() + "/" + getMoedaDestino() + ".json"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;

        try {
            setValorConverter(update);

            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            Object jsonMoeda = gson.fromJson(response.body(), Object.class);

            // Remova os caracteres '{' e '}' da string
            String cleanInput = jsonMoeda.toString().substring(1, jsonMoeda.toString().length() - 1);

            // Divida a string em pares chave-valor
            HashMap<String, String> hashMapValorMoeda = getStringHashMap(cleanInput);

            setValorConversaoMoeda(new BigDecimal(hashMapValorMoeda.get(getMoedaDestino())));

            setValorConvertido(getValorConverter().multiply(getValorConversaoMoeda()));

            setStatusConversao(JoiConstantes.CONST_CONVERSAO_MOEDA_STATUS_VALOR_CONVERTIDO);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private static HashMap<String, String> getStringHashMap(String cleanInput) {
        String[] keyValuePairs = cleanInput.split(", ");

        // Crie um HashMap para armazenar os valores
        HashMap<String, String> hashMapValorMoeda = new HashMap<>();

        // Itere sobre os pares chave-valor e adicione ao HashMap
        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            String key = entry[0];
            String value = entry[1];
            hashMapValorMoeda.put(key, value);
        }
        return hashMapValorMoeda;
    }

    public void run(@NotNull TelegramBot bot, @NotNull Update update) {
        switch(getStatusConversao()){
            case JoiConstantes.CONST_CONVERSAO_MOEDA_STATUS_INICIAR:
                moedaOrigem(bot, update);
                break;
            case JoiConstantes.CONST_CONVERSAO_MOEDA_STATUS_MOEDA_ORIGEM:
                setMoedaOriginal(update);
                moedaDestino(bot, update);
                break;
            case JoiConstantes.CONST_CONVERSAO_MOEDA_STATUS_MOEDA_DESTINO:
                setMoedaDestino(update);
                valorConverter(bot, update);
                break;
            case JoiConstantes.CONST_CONVERSAO_MOEDA_STATUS_VALOR:
                valorConversao(update);
                valorConvertido(bot, update);
                break;
        }
    }
}
