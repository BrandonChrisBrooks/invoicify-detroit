package com.ally.invoicify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ally.invoicify.models.Invoice;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByCompanyId(Long companyId);
}
