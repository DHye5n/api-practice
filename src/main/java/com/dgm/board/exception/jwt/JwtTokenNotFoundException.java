package com.dgm.board.exception.jwt;

import io.jsonwebtoken.JwtException;

public class JwtTokenNotFoundException extends JwtException {

    public JwtTokenNotFoundException() {
        super("JWT not found");
    }
}
