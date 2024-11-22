package com.inventory.LogiStack.services;

import java.time.LocalDateTime;
import java.util.List;

public interface IUtility {
    List<String> getUserRoles();
    boolean checkAdminRoleFromLoggedInUser();
    String getLoggedInUserName();
    String generateUniqueString();
    LocalDateTime convertToDate(String date);
}
