package com.example.loantraking.facade;

import com.example.loantraking.dto.RelativeDTO;
import java.util.List;

public interface RelativeFacade {
    RelativeDTO createRelative(RelativeDTO relativeDTO);
    RelativeDTO updateRelative(String id, RelativeDTO relativeDTO);
    void deleteRelative(String id);
    RelativeDTO getRelative(String id);
    List<RelativeDTO> getRelativesByCustomer(String customerId);
}
