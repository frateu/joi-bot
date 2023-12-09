package br.frateu.joi;

import br.frateu.joi.update.UpdateJoi;

import java.util.Timer;

public class MainJoi {
    public static void main(String[] args) {
        new Timer().schedule(new UpdateJoi(), 0, 1000);
    }
}
