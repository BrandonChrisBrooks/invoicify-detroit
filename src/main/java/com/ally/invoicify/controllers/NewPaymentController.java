package com.ally.invoicify.controllers;

import java.util.List;
import java.sql.Date;
import java.util.Calendar;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ally.invoicify.exceptions.UnknownException;

import com.ally.invoicify.models.Invoice; 
import com.ally.invoicify.models.NewPayment;
import com.ally.invoicify.repositories.NewPaymentRepository;
import com.ally.invoicify.repositories.InvoiceRepository;;

@RestController
@RequestMapping("/api/new-payment")
public class NewPaymentController {
	
	@Autowired
	NewPaymentRepository newPaymentRepo;
	@Autowired
	InvoiceRepository invoiceRepo;
	
	@GetMapping
	public List<NewPayment> getAll(){
		return newPaymentRepo.findAll();
	}
	
	@GetMapping("{id}")
	public NewPayment getOne(@PathVariable Long id){
		return newPaymentRepo.findOne(id);
	}
	
	@PostMapping("{invoiceId}")
	public Object create(@RequestBody NewPayment newPayment, @PathVariable Long invoiceId){
		long nowish = Calendar.getInstance().getTimeInMillis();
		Date now = new Date(nowish);
		Invoice invoice = invoiceRepo.getOne(invoiceId);

	    invoice.setCurrentBalance(invoice.getCurrentBalance() - newPayment.getAmount());

		if(invoice.getCurrentBalance() == 0) {
			invoice.setPaidOn(now);

			
		}
	if (invoice.getCurrentBalance() < 0){
		throw new UnknownException("Balance Error");

	}

	return newPaymentRepo.save(newPayment);
		
	}

	@DeleteMapping("{id}")
	public NewPayment delete(@PathVariable long id){
		NewPayment original = newPaymentRepo.findOne(id);
		Invoice invoice = invoiceRepo.findOne(original.getInvoiceId());
		invoice.setCurrentBalance(invoice.getCurrentBalance() + original.getAmount());
		invoiceRepo.save(invoice);
		newPaymentRepo.delete(original);
		return original;
	}

}
