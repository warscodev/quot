package com.udpr.quot.service.admin;

import com.udpr.quot.domain.admin.Admin;
import com.udpr.quot.domain.admin.repository.AdminRepository;
import com.udpr.quot.web.dto.admin.AdminDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Transactional
    public void save(AdminDto dto) {
        validateDuplicateAdmin(dto);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        dto.setPassword(encoder.encode(dto.getPassword()));
        adminRepository.save(dto.toEntity());
    }

    @Override
    public Admin loadUserByUsername(String account) throws UsernameNotFoundException {
        return adminRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException((account)));
    }

    private void validateDuplicateAdmin(AdminDto admin) {
        Optional<Admin> result = adminRepository.findByAccount(admin.getAccount());
        result.ifPresent(a -> {
            throw new IllegalStateException("이미 존재하는 계정명입니다.");
        });

    }
}
