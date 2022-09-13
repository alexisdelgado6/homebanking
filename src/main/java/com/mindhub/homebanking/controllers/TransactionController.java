package com.mindhub.homebanking.controllers;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.Service.AccountService;
import com.mindhub.homebanking.Service.CardService;
import com.mindhub.homebanking.Service.ClientService;
import com.mindhub.homebanking.Service.TransactionsService;
import com.mindhub.homebanking.dtos.PaymentsDTO;
import com.mindhub.homebanking.dtos.PdfDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class TransactionController {

    @Autowired
    private TransactionsService transactionsService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;


    @Transactional
    @PostMapping(path = "/api/transactions")
    public ResponseEntity<Object> makeTransaction(
            @RequestParam double amount, @RequestParam String description,
            @RequestParam String numberAccountOrigin, @RequestParam String numberAccountReceive,
            Authentication authentication
            ){
        Account accountOrigin = accountService.findAccountByNumber(numberAccountOrigin);
        Account accountReceive = accountService.findAccountByNumber(numberAccountReceive);
        if (amount < 0){
            return new ResponseEntity<>("You can't send a negative number.", HttpStatus.FORBIDDEN);
        }
        if (amount <= 0 || description.isEmpty() || numberAccountOrigin.isEmpty() || numberAccountReceive.isEmpty()){
            return new ResponseEntity<>("Missing data.", HttpStatus.FORBIDDEN);
        }
        if (numberAccountOrigin.equals(numberAccountReceive)){
            return new ResponseEntity<>("Accounts can't be the same.", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin == null){
            return new ResponseEntity<>("Account doesn't exist.", HttpStatus.FORBIDDEN);
        }
        if (!clientService.findByEmail(authentication.getName()).getAccounts().contains(accountOrigin)){
            return new ResponseEntity<>("The account isn't yours.", HttpStatus.FORBIDDEN);
        }
        if (accountReceive == null){
            return new ResponseEntity<>("The account you are trying to send money doesn't exist.", HttpStatus.FORBIDDEN);
        }
        if (amount > accountOrigin.getBalance()){
            return new ResponseEntity<>("You don't have enough money.", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(accountOrigin,TransactionType.DEBIT,(amount) * -1,description, LocalDateTime.now());
        transactionsService.saveTransaction(transactionDebit);
        Transaction transactionCredit = new Transaction(accountReceive, TransactionType.CREDIT,amount,description,LocalDateTime.now());
        transactionsService.saveTransaction(transactionCredit);

        accountOrigin.setBalance(accountOrigin.getBalance() - amount);
        accountReceive.setBalance(accountReceive.getBalance() + amount);
        accountService.saveAccount(accountOrigin);
        accountService.saveAccount(accountReceive);
        return new ResponseEntity<>("The transaction was sucesfull", HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/transactions/filter")
        public ResponseEntity<Object>transactionsPDF(@RequestBody PdfDTO pdfDTO, Authentication authentication) throws DocumentException, FileNotFoundException {
        Account account = accountService.findAccountByNumber(pdfDTO.getAccount());
        Client client = clientService.findByEmail(authentication.getName());
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("The account isn't yous.", HttpStatus.FORBIDDEN);
        }
        if (pdfDTO.getFromDate() == null || pdfDTO.getToDate() == null || account == null){
            return new ResponseEntity<>("Missing data.", HttpStatus.FORBIDDEN);
        }
        Set<Transaction> transactionssearch = transactionsService.findByDateBetween(pdfDTO.getFromDate(), pdfDTO.getToDate(), account);
        createPDF(transactionssearch, account);
        return new ResponseEntity<>("PDF successfully generated, check out your desktop.", HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/api/clients/current/transactions/payments")
    public ResponseEntity<Object> newPayments(Authentication authentication, @RequestBody PaymentsDTO paymentsDTO){
        Client client = clientService.findByEmail(authentication.getName());
        Card card = cardService.findByCardNumber(paymentsDTO.getNumber());
        Account accountOrigin = accountService.findAccountByNumber(paymentsDTO.getAccountNumber());
        Card cardNumber = cardService.findByCardNumber(paymentsDTO.getNumber());
        if (client == null){
            return new ResponseEntity<>("Client does not exist", HttpStatus.FORBIDDEN);
        }
        if (card == null){
            return new ResponseEntity<>("Card does not exist", HttpStatus.FORBIDDEN);
        }
        if(card.isCardStatus() == false){
            return new ResponseEntity<>("Disabled card", HttpStatus.FORBIDDEN);
        }
        if (!card.getCvv().equals(paymentsDTO.getCvv())){
            return new ResponseEntity<>("Invalid Cvv", HttpStatus.FORBIDDEN);
        }
        if(cardNumber == null){
            return new ResponseEntity<>("Card number does not exist", HttpStatus.FORBIDDEN);
        }
        if (paymentsDTO.getAmount() <= 0 ){
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
        if(accountOrigin == null){
            return new ResponseEntity<>("Account does not exist", HttpStatus.FORBIDDEN);
        }
        if (paymentsDTO.getAmount() > accountOrigin.getBalance()){
            return new ResponseEntity<>("Amount is invalid", HttpStatus.FORBIDDEN);
        }

        transactionsService.saveTransaction(new Transaction(accountOrigin,TransactionType.DEBIT,- paymentsDTO.getAmount(), paymentsDTO.getDescription(),LocalDateTime.now()));
        accountOrigin.setBalance(accountOrigin.getBalance() - paymentsDTO.getAmount());
        return new ResponseEntity<>("Payment sucessful",HttpStatus.CREATED);
    }


//    public static void createPDF(Set<Transaction> transactions) throws DocumentException, FileNotFoundException {
//
//        var doc = new Document();
//            String route = System.getProperty("user.home");
//            PdfWriter.getInstance(doc, new FileOutputStream("Your-transactions.pdf"));
//            PdfWriter.getInstance(doc, new FileOutputStream(route + "/Desktop/TransactionInfo.pdf"));
//
//            doc.open();
//
//        var bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
//        var paragraph = new Paragraph("Your transactions.", bold);
//
//        var table = new PdfPTable(4);
//            Stream.of("Amount","Description","Date", "Type").forEach(table::addCell);
//
//            transactions.forEach(transaction -> {
//            table.addCell("$"+transaction.getAmount());
//            table.addCell(transaction.getDescription());
//            table.addCell(transaction.getDate().toString());
//            table.addCell(transaction.getType().toString());
//        });
//
//            paragraph.add(table);
//            doc.add(paragraph);
//            doc.close();
//    }

    public void createPDF(Set<Transaction> transactions, Account account) throws FileNotFoundException, DocumentException {

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18,
                Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 14,
                Font.BOLD, BaseColor.WHITE);

        Font subFont = new Font(Font.FontFamily.HELVETICA, 12,
                Font.NORMAL);
        try {
            Document document = new Document(PageSize.A4);
            String route = System.getProperty("user.home");
            PdfWriter.getInstance(document, new FileOutputStream(route + "/Desktop/Your-transaction.pdf"));


            document.open();
            document.setMargins(2,2,2,2);



            /*TITLES*/
            Paragraph title = new Paragraph("FineLine Bank - Your selected transactions", titleFont);
            title.setSpacingAfter(3);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(-2);

            Paragraph subTitle = new Paragraph("Account number: " + account.getNumber(), subFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);
            subTitle.setSpacingAfter(1);

            Paragraph date = new Paragraph("Current date: " + LocalDate.now(), subFont);
            date.setSpacingAfter(6);
            date.setAlignment(Element.ALIGN_CENTER);




            /*LOGO*/
            Image img = Image.getInstance("./src/main/resources/static/web/assets/img/logo1.png");
            img.scaleAbsoluteWidth(150);
            img.scaleAbsoluteHeight(100);
            img.setAlignment(Element.ALIGN_CENTER);

            /*HEADERS*/
            PdfPTable pdfPTable = new PdfPTable(4);
            PdfPCell pdfPCell1 = new PdfPCell(new Paragraph("Description", headerFont));
            PdfPCell pdfPCell2 = new PdfPCell(new Paragraph("Date", headerFont));
            PdfPCell pdfPCell3 = new PdfPCell(new Paragraph("Type", headerFont));
            PdfPCell pdfPCell4 = new PdfPCell(new Paragraph("Amount", headerFont));
            pdfPCell1.setBackgroundColor(new BaseColor(75, 0, 224));
            pdfPCell2.setBackgroundColor(new BaseColor(75, 0, 224));
            pdfPCell3.setBackgroundColor(new BaseColor(75, 0, 224));
            pdfPCell4.setBackgroundColor(new BaseColor(75, 0, 224));
            pdfPCell1.setBorder(0);
            pdfPCell2.setBorder(0);
            pdfPCell3.setBorder(0);
            pdfPCell4.setBorder(0);
            pdfPCell1.setPadding(5);
            pdfPCell2.setPadding(5);
            pdfPCell3.setPadding(5);
            pdfPCell4.setPadding(5);
            pdfPTable.addCell(pdfPCell1);
            pdfPTable.addCell(pdfPCell2);
            pdfPTable.addCell(pdfPCell3);
            pdfPTable.addCell(pdfPCell4);

            /*TABLE OF TRANSACTIONS*/
            transactions.forEach(transaction -> {

                PdfPCell pdfPCell5 = new PdfPCell(new Paragraph(transaction.getDescription(), subFont));
                PdfPCell pdfPCell6 = new PdfPCell(new Paragraph(transaction.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), subFont));
                PdfPCell pdfPCell7 = new PdfPCell(new Paragraph(String.valueOf(transaction.getType()), subFont));
                PdfPCell pdfPCell8 = new PdfPCell(new Paragraph("$" + String.valueOf(transaction.getAmount()), subFont));
                pdfPCell5.setBorder(1);
                pdfPCell6.setBorder(1);
                pdfPCell7.setBorder(1);
                pdfPCell8.setBorder(1);

                pdfPTable.addCell(pdfPCell5);
                pdfPTable.addCell(pdfPCell6);
                pdfPTable.addCell(pdfPCell7);
                pdfPTable.addCell(pdfPCell8);
            });

            document.add(img);
            document.add(title);
            document.add(subTitle);
            document.add(date);
            document.add(pdfPTable);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
