package com.oj_cpp.auth.domain.service;

import java.util.List;

public interface TokenService {
    String generateToken(String username, List<String> roles);
    String generateRefreshToken(String username);
}
