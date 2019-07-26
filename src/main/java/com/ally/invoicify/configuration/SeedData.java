package com.ally.invoicify.configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ally.invoicify.models.Company;
import com.ally.invoicify.models.FlatFeeBillingRecord;
import com.ally.invoicify.models.RateBasedBillingRecord;
import com.ally.invoicify.models.User;
import com.ally.invoicify.models.InvoiceView;
import com.ally.invoicify.repositories.BillingRecordRepository;
import com.ally.invoicify.repositories.CompanyRepository;
import com.ally.invoicify.repositories.InvoiceLineItemRepository;
import com.ally.invoicify.repositories.InvoiceRepository;
import com.ally.invoicify.repositories.UserRepository;
import com.ally.invoicify.models.*;
import java.util.*;
import java.sql.Date;
@Configuration
public class SeedData {
    public SeedData(BillingRecordRepository recordRepository, CompanyRepository companyRepository,
            UserRepository userRepository, InvoiceLineItemRepository invoiceLineItemRepository , InvoiceRepository invoiceRepository , PasswordEncoder encoder) {
        User admin = userRepository.save(new User("admin", encoder.encode("admin")));
        Company ajax = companyRepository.save(new Company("AJAX Ltd."));
        Company lomax = companyRepository.save(new Company("Lomax Brothers, LLC"));
        recordRepository.save(new FlatFeeBillingRecord(300, "Faxes", ajax, admin));
        BillingRecord billingRecord = recordRepository.save(new FlatFeeBillingRecord(1.75, "Socks", ajax, admin));
        recordRepository.save(new FlatFeeBillingRecord(500, "Paper", lomax, admin));
        recordRepository.save(new FlatFeeBillingRecord(72.33, "Stockings", lomax, admin));
        recordRepository.save(new FlatFeeBillingRecord(142.99, "Paint", lomax, admin));
        recordRepository.save(new RateBasedBillingRecord(500, 3.5, "Legal services", ajax, admin));
        recordRepository.save(new RateBasedBillingRecord(150, 2.5, "Painting", ajax, admin));
        recordRepository.save(new RateBasedBillingRecord(100, 4.25, "House cleaning", ajax, admin));
        recordRepository.save(new RateBasedBillingRecord(700, 8, "Palm reading", lomax, admin));
        recordRepository.save(new RateBasedBillingRecord(1.57, 25, "Show shining", lomax, admin));
		
		// long[] arrays = new long[]{13};
        // InvoiceView view = new InvoiceView("Test!@#",arrays);
        // invoiceRepository.save(view,ajax.getId());
		
		for (int i=0; i <= 10; i++){
			Invoice invoice = invoiceRepository.save(new Invoice());
			String numToString = Integer.toString(i);
			String desc = "Test"+numToString;
			double currBal = 1.75 + i;
			double initialBal = 1.85 + i;
			Random  rnd;
			long    ms;
			
			// Get a new random instance, seeded from the clock
			rnd = new Random();
			ms = 1451606400L + (Math.abs(rnd.nextLong()) % (3L * 365 * 24 * 60 * 60 * 1000));
			System.out.println("ms value ===>"+ms);
			long nowish = ms;
			invoiceSeed(invoiceLineItemRepository, invoiceRepository, admin, ajax, billingRecord, invoice, desc, currBal,
					initialBal, nowish);
		}



			//---------------------------------------------------
		
		// String desc = "Test2";
		// double currBal = 1.75;
		// double initialBal = 1.85;

		// Random  rnd;
		// // Date    dt;
		// long    ms;
		
		// // Get a new random instance, seeded from the clock
		// rnd = new Random();
		
		// // Get an Epoch value roughly between 1940 and 2010
		// // -946771200000L = January 1, 1940
		// // Add up to 70 years to it (using modulus on the next long)
		// ms = 1451606400L + (Math.abs(rnd.nextLong()) % (3L * 365 * 24 * 60 * 60 * 1000));
		// System.out.println("ms value ===>"+ms);
		
		// // Construct a date
		// // dt = new Date(ms);


		// // long nowish = Calendar.getInstance().getTimeInMillis();
		// long nowish = ms;



        // invoiceSeed(invoiceLineItemRepository, invoiceRepository, admin, ajax, billingRecord, invoice, desc, currBal,
		// 		initialBal, nowish);
    }

	private void invoiceSeed(InvoiceLineItemRepository invoiceLineItemRepository, InvoiceRepository invoiceRepository,
			User admin, Company ajax, BillingRecord billingRecord, Invoice invoice, String desc, double currBal,
			double initialBal, long nowish) {
		invoice.setCreatedBy(admin);
        invoice.setInvoiceDescription(desc);
        InvoiceLineItem lineItem = new InvoiceLineItem();
        List<InvoiceLineItem> lineItems = new ArrayList<>();
        
        Date now = new Date(nowish);
        lineItem.setBillingRecord(billingRecord);
        lineItem.setCreatedBy(admin);
        lineItem.setCreatedOn(now);
        lineItem.setInvoice(invoice);
        InvoiceLineItem temp = invoiceLineItemRepository.save(lineItem);
        lineItems.add(temp);
        invoice.setLineItems(lineItems);
        invoice.setCompany(ajax);
        invoice.setCreatedOn(now);
        invoice.setCurrentBalance(currBal);
        invoice.setInitialBalance(initialBal);
        invoiceRepository.save(invoice);
	}
}