package com.ziemniak.webserv;

class RegisterResponse {
    private final String nick;
    private final boolean accepted;
    private final String message;

    RegisterResponse(String nick, boolean accepted, String message) {
        this.nick = nick;
        this.accepted = accepted;
        this.message = message;
    }

    public String getNick() {
        return nick;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getMessage() {
        return message;
    }
}
