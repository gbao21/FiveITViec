package com.DATN.FiveITViec.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}