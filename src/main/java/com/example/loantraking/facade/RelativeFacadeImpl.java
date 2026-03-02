package com.example.loantraking.facade;

import com.example.loantraking.Entity.Customer;
import com.example.loantraking.Entity.Relative;
import com.example.loantraking.dto.RelativeDTO;
import com.example.loantraking.service.CustomerService;
import com.example.loantraking.service.RelativeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RelativeFacadeImpl implements RelativeFacade {

    private final RelativeService relativeService;
    private final CustomerService customerService;

    @Override
    public RelativeDTO createRelative(RelativeDTO relativeDTO) {
        Customer customer = customerService.getCustomerById(relativeDTO.getCustomerId());
        Relative relative = convertToEntity(relativeDTO);
        relative.setCustomer(customer);
        Relative savedRelative = relativeService.saveRelative(relative);
        return convertToDTO(savedRelative);
    }

    @Override
    public RelativeDTO updateRelative(String id, RelativeDTO relativeDTO) {
        Relative existingRelative = relativeService.getRelativeById(id);
        Relative relativeToUpdate = convertToEntity(relativeDTO);
        relativeToUpdate.setRelativeId(id);
        relativeToUpdate.setCustomer(existingRelative.getCustomer()); // Preserve customer
        Relative updatedRelative = relativeService.updateRelative(relativeToUpdate);
        return convertToDTO(updatedRelative);
    }

    @Override
    public void deleteRelative(String id) {
        relativeService.deleteRelative(id);
    }

    @Override
    public RelativeDTO getRelative(String id) {
        return convertToDTO(relativeService.getRelativeById(id));
    }

    @Override
    public List<RelativeDTO> getRelativesByCustomer(String customerId) {
        return relativeService.getRelativesByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Relative convertToEntity(RelativeDTO dto) {
        return Relative.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .mobileNumber(dto.getMobileNumber())
                .nationalId(dto.getNationalId())
                .gender(dto.getGender())
                .dateOfBirth(dto.getDateOfBirth())
                .relationship(dto.getRelationship())
                .build();
    }

    private RelativeDTO convertToDTO(Relative entity) {
        return RelativeDTO.builder()
                .relativeId(entity.getRelativeId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .mobileNumber(entity.getMobileNumber())
                .nationalId(entity.getNationalId())
                .gender(entity.getGender())
                .dateOfBirth(entity.getDateOfBirth())
                .relationship(entity.getRelationship())
                .customerId(entity.getCustomer() != null ? entity.getCustomer().getCustomerId() : null)
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .createdDate(entity.getCreatedDate())
                .updatedDate(entity.getUpdatedDate())
                .build();
    }
}
