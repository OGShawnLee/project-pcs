package org.example.business.auth;

import org.example.business.dto.AccountDTO;
import org.mindrot.jbcrypt.BCrypt;

public class AuthClient {
  private AccountDTO currentUser;
  private static AuthClient instance;

  private AuthClient() {
    this.currentUser = null;
  }

  public static AuthClient getInstance() {
    if (instance == null) {
      instance = new AuthClient();
    }

    return instance;
  }

  public AccountDTO getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(AccountDTO currentUser) {
    this.currentUser = currentUser;
  }
}
