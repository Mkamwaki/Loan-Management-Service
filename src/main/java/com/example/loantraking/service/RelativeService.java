package com.example.loantraking.service;

import com.example.loantraking.Entity.Relative;
import java.util.List;

public interface RelativeService {
    Relative saveRelative(Relative relative);
    Relative updateRelative(Relative relative);
    void deleteRelative(String id);
    Relative getRelativeById(String id);
    List<Relative> getRelativesByCustomerId(String customerId);
}
