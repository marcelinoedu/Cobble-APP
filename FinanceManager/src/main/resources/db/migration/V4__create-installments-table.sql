CREATE TABLE installments(
  installment_id INT PRIMARY KEY AUTO_INCREMENT,
  amount NUMERIC(10,2) NOT NULL,
  deadline DATE NOT NULL,
  expense_id INT NOT NULL,
  paid BOOLEAN DEFAULT FALSE,
  paid_at TIMESTAMP NULL,
  FOREIGN KEY (expense_id) REFERENCES expenses (id)
);

