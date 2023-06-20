CREATE TABLE dependencies (
  id INT PRIMARY KEY AUTO_INCREMENT,
  dependent_name VARCHAR(255) NOT NULL,
  amount NUMERIC(10,2) NOT NULL,
  date DATE NOT NULL,
  deadline_to_receive DATE NOT NULL,
  receipt MEDIUMBLOB NULL,
  expense_id INT NOT NULL,
  installments INT NULL,
  FOREIGN KEY (expense_id) REFERENCES expenses (id)
);
