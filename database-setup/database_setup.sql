CREATE DATABASE Perfios_Bank;

USE Perfios_Bank;

CREATE TABLE `Users` (
  `Username` varchar(20) NOT NULL,
  `Password` varchar(64) NOT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Accounts` (
  `Account_Number` varchar(14) NOT NULL,
  `Username` varchar(20) NOT NULL,
  `Photo` mediumblob,
  `First_Name` varchar(1000) NOT NULL,
  `Last_Name` varchar(1000) NOT NULL,
  `Age` int NOT NULL,
  `Aadhaar` bigint NOT NULL,
  `Aadhaar_Proof` mediumblob,
  `PAN` varchar(10) NOT NULL,
  `PAN_Proof` mediumblob,
  `Address` varchar(1000) NOT NULL,
  `Phone_Number` bigint NOT NULL,
  `Amount` decimal(7,2) NOT NULL,
  `Status` varchar(8) NOT NULL,
  `Is_Frozen` tinyint DEFAULT '1',
  PRIMARY KEY (`Account_Number`),
  UNIQUE KEY `Aadhaar` (`Aadhaar`),
  UNIQUE KEY `PAN` (`PAN`),
  UNIQUE KEY `Phone_Number` (`Phone_Number`),
  KEY `Username` (`Username`),
  CONSTRAINT `Accounts_ibfk_1` FOREIGN KEY (`Username`) REFERENCES `Users` (`Username`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Transactions` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(20) NOT NULL,
  `Date_and_Time` datetime NOT NULL,
  `Type` char(1) NOT NULL,
  `Amount` decimal(12,2) NOT NULL,
  `Balance` decimal(12,2) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `Username` (`Username`),
  CONSTRAINT `Transactions_ibfk_1` FOREIGN KEY (`Username`) REFERENCES `Users` (`Username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=700 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Fixed_Deposits` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(20) NOT NULL,
  `Principal` decimal(8,2) NOT NULL,
  `End_Date` date NOT NULL,
  `Interest_Rate` decimal(4,2) NOT NULL,
  `Maturity_Amount` decimal(8,2) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `Username` (`Username`),
  CONSTRAINT `Fixed_Deposits_ibfk_1` FOREIGN KEY (`Username`) REFERENCES `Users` (`Username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Car_Loans` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(20) NOT NULL,
  `Loan_Amount` decimal(12,2) NOT NULL,
  `Days` int NOT NULL,
  `Cibil_Score` int NOT NULL,
  `Cibil_Report` mediumblob,
  `Identity_Proof` mediumblob,
  `Address_Proof` mediumblob,
  `Income_Proof` mediumblob,
  `Interest_Rate` decimal(4,2) NOT NULL,
  `Due_Amount` decimal(12,2) NOT NULL,
  `Status` varchar(8) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `Username` (`Username`),
  CONSTRAINT `Car_Loans_ibfk_1` FOREIGN KEY (`Username`) REFERENCES `Users` (`Username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=344 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Car_Loan_Repayments` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Loan_ID` int NOT NULL,
  `Username` varchar(20) NOT NULL,
  `Start_Date` date NOT NULL,
  `Has_Started` tinyint NOT NULL,
  `End_Date` date NOT NULL,
  `Has_Ended` tinyint NOT NULL,
  `EMI` decimal(12,2) NOT NULL,
  `Misses` tinyint NOT NULL,
  `Penalty` decimal(12,2) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `Username` (`Username`),
  KEY `Loan_ID` (`Loan_ID`),
  CONSTRAINT `Car_Loan_Repayments_ibfk_1` FOREIGN KEY (`Loan_ID`) REFERENCES `Car_Loans` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=275 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Admins` (
  `Username` varchar(20) NOT NULL,
  `Password` varchar(64) NOT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO Admins VALUES('admin', 123);

INSERT INTO Users VALUES('TestServices', 'WjUxvQSDS4h0oAw7UImt6AewvPmf7Q4iCIVRBid7u7LJoejH4/VC0oEfgJ9QNl8R');
INSERT INTO Users VALUES('PB', 'dwC7jRzX6w1RWJBPpsyeAZee4DlK9zFQedw5cqCBRAtBrpykV8Extg5xiwm8AxAE');
INSERT INTO Users VALUES('TestFrozenAccount', '9IWiaPyUNLVJVGlx8QUTm2FXt8mQK6Kcvb8d45Y+2pNm790BNW5WYxVOwSvCtft4');

INSERT INTO Accounts(Account_Number, Username, First_Name, Last_Name, Age, Aadhaar, PAN, Address, Phone_Number, Amount, Status, Is_Frozen) VALUES('PBIN1000000001', 'TestServices', 'Test', 'Services', 22, 951623004784, 'CEGPS3512G', 'Bengaluru', 9658321047, 3003.00, 'Approved', 0);
INSERT INTO Accounts(Account_Number, Username, First_Name, Last_Name, Age, Aadhaar, PAN, Address, Phone_Number, Amount, Status, Is_Frozen) VALUES('PBIN1000000002', 'PB', 'Perfios', 'Bank', 35, 123456789132, 'CEGP24815J', 'Bengaluru', 9234568791, 2002.00, 'Approved', 0);
INSERT INTO Accounts(Account_Number, Username, First_Name, Last_Name, Age, Aadhaar, PAN, Address, Phone_Number, Amount, Status, Is_Frozen) VALUES('PBIN1000000003', 'TestFrozenAccount', 'Test', 'Frozen Account', 32, 976154323806, 'CEGPF4815A', 'Mumbai', 9672253314, 4004.00, 'Approved', 1);

INSERT INTO Transactions(Username, Date_and_Time, Type, Amount, Balance) VALUES('TestServices', '2022-10-08 17:16:00', 'D', 3003.00, 3003.00);
INSERT INTO Transactions(Username, Date_and_Time, Type, Amount, Balance) VALUES('TestServices', '2022-10-08 17:17:00', 'D', 100000.00, 103003.00);
INSERT INTO Transactions(Username, Date_and_Time, Type, Amount, Balance) VALUES('PB', '2022-09-24 22:27:12', 'D', 2002.00, 2002.00);
INSERT INTO Transactions(Username, Date_and_Time, Type, Amount, Balance) VALUES('TestFrozenAccount', '2016-10-08 20:36:53', 'D', 4004.00, 4004.00);
INSERT INTO Transactions(Username, Date_and_Time, Type, Amount, Balance) VALUES('TestFrozenAccount', '2016-10-10 14:42:12', 'D', 953420.00, 957424.00);
INSERT INTO Transactions(Username, Date_and_Time, Type, Amount, Balance) VALUES('TestFrozenAccount', '2016-12-01 10:24:47', 'W', 22360.39, 931059.61);

INSERT INTO Fixed_Deposits(Username, Principal, End_Date, Interest_Rate, Maturity_Amount) VALUES('TestServices', 7642.17, '2023-07-14', 5.50, 8071.95);

INSERT INTO Car_Loans(Username, Loan_Amount, Days, Cibil_Score, Interest_Rate, Due_Amount, Status) VALUES('TestFrozenAccount', 953420, 2000, 763, 8.30, 1475805.84 ,'Approved');

INSERT INTO Car_Loan_Repayments(Loan_ID, Username, Start_Date, Has_Started, End_Date, Has_Ended, EMI, Misses, Penalty) VALUES(<add the corresponding ID from Car_Loans table here>, 'TestFrozenAccount', '2016-12-01', 1, '2022-05-01', 0, 22360.39, 3, 447.21);
