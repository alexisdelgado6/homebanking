package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			Client clientMelba = new Client("Melba", "Styles", "melba69@hotmail.com", passwordEncoder.encode("1234"));
			Account accountMelba = new Account("VIN001", LocalDateTime.now(), 5000.00, clientMelba ,AccountType.CURRENT);
			Account accountMelba2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.00, clientMelba ,AccountType.CURRENT);
			Account accountMelba3 = new Account("VIN003", LocalDateTime.now(), 400.00, clientMelba ,AccountType.CURRENT);


			Client clientAlexis = new Client("Alexis","Delgado", "alexis@hotmail.com", passwordEncoder.encode("1234"));
			Account accountAlexis = new Account("VIN004", LocalDateTime.now(), 10000.00, clientAlexis ,AccountType.CURRENT);


			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 700.00, "holis, te giro lo de la ", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 400.00, "haloja ", LocalDateTime.now());
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 500.00, "buenasssssss ", LocalDateTime.now());

			Loan loanMortgage = new Loan("Mortgage", 500000.00, Arrays.asList(12, 24, 36, 48, 60));
			Loan loanPersonal = new Loan("Personal", 100000.00, Arrays.asList(6, 12, 24));
			Loan loanCar = new Loan("Car", 300000.00, Arrays.asList(6, 12, 24, 36));

			ClientLoan clientloan1 = new ClientLoan(400000.00, loanMortgage.getPayments().get(4), clientMelba, loanMortgage);
			ClientLoan clientloan2 = new ClientLoan(50000.00, loanPersonal.getPayments().get(2), clientMelba, loanPersonal);
			ClientLoan clientloan3 = new ClientLoan(100000.00, loanPersonal.getPayments().get(2), clientAlexis, loanPersonal);
			ClientLoan clientloan4 = new ClientLoan(200000.00, loanCar.getPayments().get(3), clientAlexis, loanCar);

			Card cardGold = new Card(clientMelba, clientMelba.getFirstName()+" "+clientMelba.getLastName() ,CardType.DEBIT, CardColor.GOLD, "1111 2222 3333 4444", 123, LocalDate.now(), LocalDate.now(), true, accountMelba);
			Card cardTitatium = new Card(clientMelba, clientMelba.getFirstName()+" "+clientMelba.getLastName() ,CardType.CREDIT, CardColor.TITANIUM, "2222 3333 4444 5555",777,LocalDate.now().plusYears(5), LocalDate.now(), true,accountMelba);
            Card cardSilver = new Card(clientAlexis, clientAlexis.getFirstName()+" "+clientAlexis.getLastName() ,CardType.CREDIT, CardColor.SILVER, "7840 8756 8934 9859", 209, LocalDate.now().plusYears(5), LocalDate.now(), true, accountAlexis);

			//clientMelba.addAccount(accountMelba3);
			//clientAlexis.addAccount(accountAlexis);
			//clientMelba.addAccount(accountMelba2);
			accountMelba.addTransaction(transaction1);
			accountMelba2.addTransaction(transaction2);
			accountAlexis.addTransaction(transaction3);

			clientMelba.addCard(cardGold);
			clientMelba.addCard(cardTitatium);
            clientAlexis.addCard(cardSilver);


			clientRepository.save(clientMelba);
			clientRepository.save(clientAlexis);
			accountRepository.save(accountMelba);
			accountRepository.save(accountMelba3);
			accountRepository.save(accountMelba2);
			accountRepository.save(accountAlexis);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);

			loanRepository.save(loanMortgage);
			loanRepository.save(loanPersonal);
			loanRepository.save(loanCar);

			clientLoanRepository.save(clientloan1);
			clientLoanRepository.save(clientloan2);
			clientLoanRepository.save(clientloan3);
			clientLoanRepository.save(clientloan4);

			cardRepository.save(cardGold);
			cardRepository.save(cardTitatium);
            cardRepository.save(cardSilver);

		};
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
}
